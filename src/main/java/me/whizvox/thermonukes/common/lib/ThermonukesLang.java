package me.whizvox.thermonukes.common.lib;

import me.whizvox.thermonukes.Thermonukes;
import me.whizvox.thermonukes.common.util.GeigerCounterMode;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.Collection;

public class ThermonukesLang {

  public static class Keys {
    private static final String BASE = Thermonukes.MOD_ID + ".";
    public static final String
        COMMAND_GENERIC_MUST_BE_ENTITY = BASE + "command.generic.must_be_entity",
        COMMAND_GENERIC_NOT_LOOKING_AT_ENTITY = BASE + "command.generic.not_looking_at_entity",
        COMMAND_RADIATION_SET_CHUNK = BASE + "command.radiation.set.chunk",
        COMMAND_RADIATION_SET_ENTITY = BASE + "command.radiation.set.entity",
        COMMAND_RADIATION_SET_ENTITIES = BASE + "command.radiation.set.entities",
        COMMAND_RADIATION_CLEAR_ENTITY = BASE + "command.radiation.clear.entity",
        COMMAND_RADIATION_CLEAR_ENTITIES = BASE + "command.radiation.clear.entities",
        COMMAND_RADIATION_CLEAR_CHUNK = BASE + "command.radiation.clear.chunk",
        COMMAND_RADIATION_CLEAR_LEVEL = BASE + "command.radiation.clear.level",
        COMMAND_RADIATION_CLEAR_ALL = BASE + "command.radiation.clear.all",
        GEIGER_COUNTER_CHUNK_READING = BASE + "geiger_counter.chunk_reading",
        GEIGER_COUNTER_ENTITY_READING = BASE + "geiger_counter.entity_reading",
        GEIGER_COUNTER_SELF_READING = BASE + "geiger_counter.self_reading",
        GEIGER_COUNTER_NEW_MODE = BASE + "geiger_counter.new_mode",
        AIR_SCRUBBER_CONTAINER = BASE + "container.air_scrubber";
  }

  public static final Component
      COMMAND_RADIATION_CLEAR_ALL = Component.translatable(Keys.COMMAND_RADIATION_CLEAR_ALL),
      AIR_SCRUBBER_CONTAINER = Component.translatable(Keys.AIR_SCRUBBER_CONTAINER);

  public static MutableComponent literal(Object obj) {
    return Component.literal(String.valueOf(obj));
  }

  public static MutableComponent mut(Object obj) {
    if (obj instanceof MutableComponent mutComp) {
      return mutComp;
    } if (obj instanceof Component comp) {
      return comp.copy();
    }
    return literal(obj);
  }

  public static MutableComponent rads(float dosage) {
    return Component.literal(String.format("%.1f rads", dosage));
  }

  public static MutableComponent formattedRads(float dosage) {
    return rads(dosage).withStyle(ChatFormatting.RED);
  }

  public static MutableComponent chunkPos(ChunkPos pos) {
    return Component.literal("[" + pos.x + ", " + pos.z + "]");
  }

  public static MutableComponent formattedChunkPos(ChunkPos pos) {
    return chunkPos(pos).withStyle(ChatFormatting.YELLOW);
  }

  public static MutableComponent asSuccess(Object obj) {
    return mut(obj).withStyle(ChatFormatting.GREEN);
  }

  public static MutableComponent asInfo(Object obj) {
    return mut(obj).withStyle(ChatFormatting.AQUA);
  }

  public static MutableComponent asWarning(Object obj) {
    return mut(obj).withStyle(ChatFormatting.YELLOW);
  }

  public static MutableComponent asError(Object obj) {
    return mut(obj).withStyle(ChatFormatting.RED);
  }

  public static MutableComponent commandRadiationSetChunk(float dosage, ChunkPos pos, ResourceKey<Level> dimension) {
    return Component.translatable(
        Keys.COMMAND_RADIATION_SET_CHUNK,
        formattedRads(dosage),
        formattedChunkPos(pos),
        asInfo(dimension.location())
    );
  }

  public static MutableComponent commandRadiationSetEntities(float dosage, Collection<? extends Entity> entities) {
    if (entities.size() != 1) {
      return Component.translatable(
          Keys.COMMAND_RADIATION_SET_ENTITIES,
          formattedRads(dosage),
          entities.size()
      );
    }
    return Component.translatable(
        Keys.COMMAND_RADIATION_SET_ENTITY,
        formattedRads(dosage),
        entities.stream().findAny().get().getDisplayName()
    );
  }

  public static MutableComponent commandRadiationClearEntity(Collection<? extends Entity> entities) {
    if (entities.size() != 1) {
      return Component.translatable(
          Keys.COMMAND_RADIATION_CLEAR_ENTITIES,
          entities.size()
      );
    }
    return Component.translatable(
        Keys.COMMAND_RADIATION_CLEAR_ENTITY,
        entities.stream().findAny().get().getDisplayName()
    );
  }

  public static MutableComponent commandRadiationClearChunk(ChunkPos pos, ResourceKey<Level> dimension) {
    return Component.translatable(
        Keys.COMMAND_RADIATION_CLEAR_CHUNK,
        formattedChunkPos(pos),
        asInfo(dimension.location())
    );
  }

  public static MutableComponent commandRadiationClearLevel(ResourceKey<Level> dimension) {
    return Component.translatable(
        Keys.COMMAND_RADIATION_CLEAR_LEVEL,
        asInfo(dimension.location())
    );
  }

  public static MutableComponent geigerCounterChunkReading(float dosage) {
    return Component.translatable(
        Keys.GEIGER_COUNTER_CHUNK_READING,
        formattedRads(dosage)
    );
  }

  public static MutableComponent geigerCounterEntityReading(LivingEntity entity, float dosage) {
    return Component.translatable(
        Keys.GEIGER_COUNTER_ENTITY_READING,
        entity.getDisplayName(),
        formattedRads(dosage)
    );
  }

  public static MutableComponent geigerCounterSelfReading(float dosage) {
    return Component.translatable(
        Keys.GEIGER_COUNTER_SELF_READING,
        formattedRads(dosage)
    );
  }

  public static MutableComponent geigerCounterNewMode(GeigerCounterMode mode) {
    return Component.translatable(
        Keys.GEIGER_COUNTER_NEW_MODE,
        asSuccess(mode.name)
    );
  }

}
