package me.whizvox.thermonukes.common.capability;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.longs.Long2FloatArrayMap;
import it.unimi.dsi.fastutil.longs.Long2FloatMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.function.BiConsumer;

public class WorldRadiation implements INBTSerializable<CompoundTag> {

  private static final float
      MAX_NATURAL_DECAY_AMOUNT = 0.3F,
      DOSAGE_DROPOFF_MODIFIER = -0.025F;
  private static final double MINIMUM_DOSAGE_AMOUNT = 50.0;

  private static final String
      SIZE_TAG = "Size",
      CHUNKS_TAG = "Chunks",
      DOSAGES_TAG = "Dosages";

  private final Long2FloatMap irradiatedChunks;

  public WorldRadiation() {
    irradiatedChunks = new Long2FloatArrayMap();
  }

  public float getDosage(ChunkPos pos) {
    return irradiatedChunks.getOrDefault(pos.toLong(), 0.0F);
  }

  public void forEach(BiConsumer<ChunkPos, Float> consumer) {
    irradiatedChunks.forEach((packedChunkPos, dosage) -> consumer.accept(new ChunkPos(packedChunkPos), dosage));
  }

  public void tickDecay(RandomSource rand) {
    Long2FloatMap modifiedChunks = new Long2FloatArrayMap();
    irradiatedChunks.forEach((packedChunkPos, dosage) -> {
      if (rand.nextInt(10) == 0) {
        modifiedChunks.put((long) packedChunkPos, dosage - rand.nextFloat() * MAX_NATURAL_DECAY_AMOUNT);
      }
    });
    modifiedChunks.forEach((packedChunkPos, dosage) -> {
      if (dosage <= 0.0F) {
        irradiatedChunks.remove((long) packedChunkPos);
      } else {
        irradiatedChunks.put((long) packedChunkPos, (float) dosage);
      }
    });
  }

  public void setRadiationDosage(ChunkPos pos, float dosage) {
    if (dosage <= 0.0F) {
      irradiatedChunks.remove(pos.toLong());
    } else {
      irradiatedChunks.put(pos.toLong(), dosage);
    }
  }

  public void clearRadiation(ChunkPos pos) {
    setRadiationDosage(pos, 0.0F);
  }

  public void irradiateArea(BlockPos centerPos, float centerDosage) {
    /*
    Here's how this works:

    Radiation is spread according to an exponential decay model. The further from the epicenter, the amount of
    radiation in that chunk will be significantly lower. With default values, here's what the spread looks like:

    centerDosage = 50000 rads
    0 blocks away = 50000 rads
    16 blocks away = 33516 rads
    32 blocks away = 22466 rads
    64 blocks away = 10094 rads
    ...
    276 blocks away = 50 rads

    The dosage of radiation for an individual chunk is calculated using the following formula:
      r = d * e^(R * x)
    where
    r: local dosage given to the chunk
    d: epicenter dosage
    e: Euler's number
    R: a constant (DOSAGE_DROPOFF_MODIFIER)
    x: distance to epicenter (in blocks)

    However, because this is exponential decay, it's not a good idea to spread to *every* possible chunk until a dosage
    of zero is calculated. That would spread to literally millions of chunks, and 99.9% of that would decay in seconds.
    Instead, only chunks that have a dosage of MINIMUM_DOSAGE_AMOUNT or higher will be calculated. The number of chunks
    to affect is calculated using the formula:
      c = ln(M / x) / R
    where
    c: radius of affect (in blocks)
    M: the minimum dosage that we care to calculate. ideally shouldn't be an amount that decays in less than 5
      seconds, nor longer than 1 minute.
    x: epicenter dosage
    R: a constant (DOSAGE_DROPOFF_MODIFIER)

    The result of ceil(c / 16) is used to determine the chunk radius of however many chunks to affect, and only chunks
    within that radius are affected. This is important, as all chunks in a square, consisting of (chunkRadius * 2 + 1)
    chunks are checked, but not all of them are actually affected. Doing it this way means a lot of chunks are
    skipped, but it's super cheap to calculate a chunk position and check if it's within the acceptable radius.
     */
    ChunkPos centerChunkPos = new ChunkPos(centerPos);
    float radius = (float) (Math.log(MINIMUM_DOSAGE_AMOUNT / centerDosage) / DOSAGE_DROPOFF_MODIFIER);
    float radiusSqr = radius * radius;
    int chunkRadius = Mth.ceil(radius / 16.0F);
    for (int xOff = -chunkRadius; xOff <= chunkRadius; xOff++) {
      for (int zOff = -chunkRadius; zOff <= chunkRadius; zOff++) {
        ChunkPos chunkPos = new ChunkPos(centerChunkPos.x + xOff, centerChunkPos.z + zOff);
        BlockPos chunkCenter = chunkPos.getMiddleBlockPosition(centerPos.getY());
        double distSqr = chunkCenter.distSqr(centerPos);
        if (distSqr <= radiusSqr) {
          float newDosage = (float) (centerDosage * Math.exp(DOSAGE_DROPOFF_MODIFIER * Math.sqrt(distSqr)));
          if (newDosage < 0.0F) {
            // TODO Debug print statement. Remove later.
            System.out.printf("Should not be negative! dist=%.1f, centerDosage=%.1f, localDosage=%.1f%n", Math.sqrt(distSqr), centerDosage, newDosage);
          } else {
            long packedChunkPos = chunkPos.toLong();
            float oldDosage = irradiatedChunks.getOrDefault(packedChunkPos, 0.0F);
            irradiatedChunks.put(packedChunkPos, oldDosage + newDosage);
          }
        }
      }
    }
  }

  public void clearAllRadiation() {
    irradiatedChunks.clear();
  }

  @Override
  public CompoundTag serializeNBT() {
    CompoundTag tag = new CompoundTag();
    int size = irradiatedChunks.size();
    tag.putInt(SIZE_TAG, size);
    LongList packedChunkPositions = new LongArrayList(size);
    IntList dosages = new IntArrayList(size);
    irradiatedChunks.forEach((packedChunkPos, dosage) -> {
      packedChunkPositions.add((long) packedChunkPos);
      dosages.add(Float.floatToIntBits(dosage));
    });
    tag.put(CHUNKS_TAG, new LongArrayTag(packedChunkPositions));
    tag.put(DOSAGES_TAG, new IntArrayTag(dosages));
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundTag tag) {
    irradiatedChunks.clear();
    int size = tag.getInt(SIZE_TAG);
    long[] packedChunkPositions = tag.getLongArray(CHUNKS_TAG);
    int[] dosages = tag.getIntArray(DOSAGES_TAG);
    for (int i = 0; i < size; i++) {
      irradiatedChunks.put(packedChunkPositions[i], Float.intBitsToFloat(dosages[i]));
    }
  }

}
