package me.whizvox.thermonukes.common.item;

import me.whizvox.thermonukes.common.lib.ThermonukesCapabilities;
import me.whizvox.thermonukes.common.lib.ThermonukesItems;
import me.whizvox.thermonukes.common.lib.ThermonukesLang;
import me.whizvox.thermonukes.common.lib.ThermonukesSounds;
import me.whizvox.thermonukes.common.util.GeigerCounterLevel;
import me.whizvox.thermonukes.common.util.GeigerCounterMode;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GeigerCounterItem extends Item {

  public GeigerCounterItem() {
    super(ThermonukesItems.defaultItemProperties().stacksTo(1).defaultDurability(4));
  }

  @Override
  public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
    Player player = (Player) entity;
    if (level.getGameTime() % 2 == 0 && !level.isClientSide) {
      level.getCapability(ThermonukesCapabilities.WORLD_RADIATION).ifPresent(worldRadiation -> {
        float dosage = worldRadiation.getDosage(player.chunkPosition());
        if (isSelected) {
          if (dosage > 10000) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), ThermonukesSounds.GEIGER_COUNTER_EXTREME_BURST.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
          } else if (dosage > 1000) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), ThermonukesSounds.GEIGER_COUNTER_FAST_BURST.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
          } else if (dosage > 100) {
            // attempt to play a burst
            // 100 -> 0%, 1000 -> 100%
            if (player.getRandom().nextFloat() < (1.0F / 900.0F) * (dosage - 100.0F)) {
              level.playSound(null, player.getX(), player.getY(), player.getZ(), ThermonukesSounds.GEIGER_COUNTER_BURST.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            } else { // will attempt to play a click if failed
              // 100 -> 10%, 1000 -> 100%
              //if (player.getRandom().nextFloat() < (9.0F / 9000.0F) * dosage) {
              level.playSound(null, player.getX(), player.getY(), player.getZ(), ThermonukesSounds.GEIGER_COUNTER_CLICK.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
              //}
            }
          } else {
            // only play clicks at <100
            // 100% chance at 100, 1% chance at 1
            if (player.getRandom().nextFloat() < (1.0F / 100.0F) * dosage) {
              level.playSound(null, player.getX(), player.getY(), player.getZ(), ThermonukesSounds.GEIGER_COUNTER_CLICK.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            }
          }
        }
        GeigerCounterLevel actualLevel = GeigerCounterLevel.getLevel(dosage);
        GeigerCounterLevel itemLevel = readLevel(stack);
        if (actualLevel != itemLevel) {
          writeLevel(stack, actualLevel);
        }
      });
    }
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    ItemStack stack = player.getItemInHand(hand);
    if (!level.isClientSide) {
      if (player.isShiftKeyDown()) {
        GeigerCounterMode newMode = goToNextMode(stack);
        player.displayClientMessage(ThermonukesLang.geigerCounterNewMode(newMode), true);
        player.playNotifySound(ThermonukesSounds.GEIGER_COUNTER_MODE_CHANGE.get(), SoundSource.MASTER, 1.0F, 1.0F);
      } else {
        if (readMode(stack) == GeigerCounterMode.CHUNK) {
          level.getCapability(ThermonukesCapabilities.WORLD_RADIATION).ifPresent(worldRadiation -> {
            float dosage = worldRadiation.getDosage(player.chunkPosition());
            player.displayClientMessage(ThermonukesLang.geigerCounterChunkReading(dosage), true);
          });
        } else {
          player.getCapability(ThermonukesCapabilities.ENTITY_RADIATION).ifPresent(entityRadiation -> {
            float dosage = entityRadiation.getDosage();
            player.displayClientMessage(ThermonukesLang.geigerCounterSelfReading(dosage), true);
          });
        }
      }
    }
    return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
  }

  @Override
  public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
    if (!player.level.isClientSide) {
      target.getCapability(ThermonukesCapabilities.ENTITY_RADIATION).ifPresent(entityRadiation -> {
        float dosage = entityRadiation.getDosage();
        player.displayClientMessage(ThermonukesLang.geigerCounterEntityReading(target, dosage), true);
      });
    }
    return InteractionResult.sidedSuccess(player.level.isClientSide);
  }

  private static final String
      TAG_LEVEL = "GeigerCounterLevel",
      TAG_MODE = "GeigerCounterMode";

  public static GeigerCounterLevel readLevel(ItemStack stack) {
    if (stack.hasTag()) {
      CompoundTag tag = stack.getTag();
      if (tag.contains(TAG_LEVEL)) {
        return GeigerCounterLevel.fromOrdinal(tag.getByte(TAG_LEVEL));
      }
    }
    return GeigerCounterLevel.NONE;
  }

  public static void writeLevel(ItemStack stack, GeigerCounterLevel level) {
    stack.getOrCreateTag().putByte(TAG_LEVEL, (byte) level.ordinal());
  }

  public static GeigerCounterMode readMode(ItemStack stack) {
    if (stack.hasTag()) {
      CompoundTag tag = stack.getTag();
      if (tag.contains(TAG_MODE, Tag.TAG_BYTE)) {
        return GeigerCounterMode.fromOrdinal(tag.getByte(TAG_MODE));
      }
    }
    writeMode(stack, GeigerCounterMode.CHUNK);
    return GeigerCounterMode.CHUNK;
  }

  public static void writeMode(ItemStack stack, GeigerCounterMode mode) {
    stack.getOrCreateTag().putByte(TAG_MODE, (byte) mode.ordinal());
  }

  public static GeigerCounterMode goToNextMode(ItemStack stack) {
    GeigerCounterMode mode = GeigerCounterMode.nextMode(readMode(stack));
    writeMode(stack, mode);
    return mode;
  }

}
