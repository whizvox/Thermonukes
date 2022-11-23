package me.whizvox.thermonukes.common.capability;

import it.unimi.dsi.fastutil.Pair;
import me.whizvox.thermonukes.common.block.CleansingWaterBlock;
import me.whizvox.thermonukes.common.lib.ThermonukesBlocks;
import me.whizvox.thermonukes.common.lib.ThermonukesFluidTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.FloatTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.INBTSerializable;

public class EntityRadiation implements INBTSerializable<FloatTag> {

  private static final String TAG_DOSAGE = "Dosage";

  private static final float
      DEFAULT_DECAY_AMOUNT = 0.3F,
      DEFAULT_DECAY_CHANCE = 0.1F,
      DEFAULT_CLEANSING_DECAY_AMOUNT = 10.0F,
      DEFAULT_GAIN_AMOUNT = 0.03F,
      DEFAULT_GAIN_CHANCE = 0.2F;

  private float dosage;

  public EntityRadiation() {
    this.dosage = 0;
  }

  public void clearRadiation() {
    dosage = 0.0F;
  }

  public float getDosage() {
    return dosage;
  }

  public void setDosage(float dosage) {
    this.dosage = dosage;
  }

  public void tick(LivingEntity entity, RandomSource rand, float chunkDosage) {
    int maxPotency = -1;
    boolean anyDiluted = false;
    Level level = entity.level;
    if (entity.isInFluidType(ThermonukesFluidTypes.CLEANSING_WATER.get())) {
      for (Pair<BlockPos, BlockState> pair : BlockPos.betweenClosedStream(entity.getBoundingBox())
          .map(pos -> Pair.of(pos.immutable(), level.getBlockState(pos)))
          .filter(pair -> pair.right().is(ThermonukesBlocks.CLEANSING_WATER.get()) && pair.right().getFluidState().isSource())
          .toList()) {
        BlockPos pos = pair.left();
        BlockState state = pair.right();
        int potency = state.getValue(CleansingWaterBlock.POTENCY);
        if (potency > maxPotency) {
          maxPotency = potency;
        }
        // only dilute a maximum of 1 block
        if (!anyDiluted) {
          if (level.random.nextInt(100) == 0) {
            BlockState newBlockState;
            if (potency <= 1) {
              newBlockState = Blocks.WATER.defaultBlockState();
            } else {
              newBlockState = state.setValue(CleansingWaterBlock.POTENCY, potency - 1);
            }
            level.setBlock(pos, newBlockState, Block.UPDATE_ALL);
            anyDiluted = true;
          }
        }
      }
    }
    float delta = 0;
    if (dosage > 0.0F && rand.nextFloat() < DEFAULT_DECAY_CHANCE) {
      delta -= rand.nextFloat() * DEFAULT_DECAY_AMOUNT;
    }
    if (maxPotency >= 0) {
      delta -= rand.nextFloat() * DEFAULT_CLEANSING_DECAY_AMOUNT * (maxPotency + 2);
    }
    if (chunkDosage > 0.0F && rand.nextFloat() < DEFAULT_GAIN_CHANCE) {
      delta += chunkDosage * rand.nextFloat() * DEFAULT_GAIN_AMOUNT;
    }
    dosage = Mth.clamp(dosage + delta, 0.0F, Float.MAX_VALUE);
  }

  @Override
  public FloatTag serializeNBT() {
    return FloatTag.valueOf(dosage);
  }

  @Override
  public void deserializeNBT(FloatTag tag) {
    dosage = tag.getAsFloat();
  }

}
