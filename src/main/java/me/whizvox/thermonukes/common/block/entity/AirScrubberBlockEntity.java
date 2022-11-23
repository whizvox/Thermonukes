package me.whizvox.thermonukes.common.block.entity;

import me.whizvox.thermonukes.client.sound.MachinePumpingSoundInstance;
import me.whizvox.thermonukes.common.block.AirScrubberBlock;
import me.whizvox.thermonukes.common.lib.ThermonukesBlockEntities;
import me.whizvox.thermonukes.common.lib.ThermonukesCapabilities;
import me.whizvox.thermonukes.common.lib.ThermonukesItems;
import me.whizvox.thermonukes.common.lib.ThermonukesLang;
import me.whizvox.thermonukes.common.menu.AirScrubberMenu;
import me.whizvox.thermonukes.common.util.TickableBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class AirScrubberBlockEntity extends BaseContainerBlockEntity implements TickableBlockEntity {

  public static final int SLOTS = 3;

  private static final int CLEANSING_SALT_TIMER = 150;
  private static final float
      MAX_CLEANSE_AMOUNT = 10.0F,
      NEIGHBOR_CLEANSE_MODIFIER = 0.45F;

  private static final String
      TAG_INVENTORY = "Inventory",
      TAG_TIMER = "ScrubberTimer";

  private final ItemStackHandler inventory;
  private final Container invWrapper;
  private int timer;

  private SoundInstance soundInstance;

  public AirScrubberBlockEntity(BlockPos pos, BlockState state) {
    super(ThermonukesBlockEntities.AIR_SCRUBBER.get(), pos, state);
    inventory = new ItemStackHandler(SLOTS);
    invWrapper = new RecipeWrapper(inventory);

    timer = 0;
    soundInstance = null;
  }

  public ItemStackHandler getInventory() {
    return inventory;
  }

  @Override
  protected Component getDefaultName() {
    return ThermonukesLang.AIR_SCRUBBER_CONTAINER;
  }

  @Override
  protected AbstractContainerMenu createMenu(int containerId, Inventory playerInv) {
    return AirScrubberMenu.createForServer(containerId, playerInv, this);
  }

  @Override
  public int getContainerSize() {
    return SLOTS;
  }

  @Override
  public boolean isEmpty() {
    return invWrapper.isEmpty();
  }

  @Override
  public ItemStack getItem(int slot) {
    return invWrapper.getItem(slot);
  }

  @Override
  public ItemStack removeItem(int slot, int amount) {
    return invWrapper.removeItem(slot, amount);
  }

  @Override
  public ItemStack removeItemNoUpdate(int slot) {
    return invWrapper.removeItemNoUpdate(slot);
  }

  @Override
  public void setItem(int pSlot, ItemStack stack) {
    invWrapper.setItem(pSlot, stack);
  }

  @Override
  public boolean stillValid(Player player) {
    return invWrapper.stillValid(player);
  }

  @Override
  public void clearContent() {
    invWrapper.clearContent();
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.put(TAG_INVENTORY, inventory.serializeNBT());
    tag.putShort(TAG_TIMER, (short) timer);
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    inventory.deserializeNBT(tag.getCompound(TAG_INVENTORY));
    timer = tag.getShort(TAG_TIMER);
  }

  @Override
  public void tick(Level level, BlockPos pos, BlockState state) {
    if (!level.isClientSide) {
      level.getCapability(ThermonukesCapabilities.WORLD_RADIATION).ifPresent(radiation -> {
        ChunkPos chunkPos = new ChunkPos(worldPosition);
        float dosage = radiation.getDosage(chunkPos);
        if (dosage > 0.0F) {
          if (timer <= 0) {
            ItemStack stack = ItemStack.EMPTY;
            for (int i = 0; i < inventory.getSlots(); i++) {
              stack = inventory.getStackInSlot(i);
              if (stack.is(ThermonukesItems.CLEANSING_SALT.get())) {
                break;
              }
            }
            if (!stack.isEmpty()) {
              stack.shrink(1);
              setChanged();
              timer = CLEANSING_SALT_TIMER;
            }
          }
          if (timer > 0) {
            timer--;
            if (!state.getValue(AirScrubberBlock.RUNNING)) {
              level.setBlock(pos, state.setValue(AirScrubberBlock.RUNNING, true), Block.UPDATE_ALL);
            }
            setChanged();
            radiation.setRadiationDosage(chunkPos, dosage - level.random.nextFloat() * MAX_CLEANSE_AMOUNT);
            int xOff;
            int zOff;
            // chose random neighbor chunk to cleanse as well
            switch (level.random.nextInt(8)) {
              case 0 -> {
                xOff = -1;
                zOff = -1;
              }
              case 1 -> {
                xOff = 0;
                zOff = -1;
              }
              case 2 -> {
                xOff = 1;
                zOff = -1;
              }
              case 3 -> {
                xOff = -1;
                zOff = 0;
              }
              case 4 -> {
                xOff = 1;
                zOff = 0;
              }
              case 5 -> {
                xOff = -1;
                zOff = 1;
              }
              case 6 -> {
                xOff = 0;
                zOff = 1;
              }
              default -> {
                xOff = 1;
                zOff = 1;
              }
            }
            ChunkPos neighborChunkPos = new ChunkPos(chunkPos.x + xOff, chunkPos.z + zOff);
            float neighborDosage = radiation.getDosage(neighborChunkPos);
            if (neighborDosage > 0.0F) {
              radiation.setRadiationDosage(neighborChunkPos, neighborDosage - level.random.nextFloat() * MAX_CLEANSE_AMOUNT * NEIGHBOR_CLEANSE_MODIFIER);
            }
          } else {
            if (state.getValue(AirScrubberBlock.RUNNING)) {
              level.setBlock(pos, state.setValue(AirScrubberBlock.RUNNING, false), Block.UPDATE_ALL);
            }
          }
        } else {
          if (state.getValue(AirScrubberBlock.RUNNING)) {
            level.setBlock(pos, state.setValue(AirScrubberBlock.RUNNING, false), Block.UPDATE_ALL);
          }
        }
      });
    }
    if (level.isClientSide) {
      updateSound();
    }
  }

  @OnlyIn(Dist.CLIENT)
  public boolean isPlayingSound() {
    return soundInstance != null && Minecraft.getInstance().getSoundManager().isActive(soundInstance);
  }

  @OnlyIn(Dist.CLIENT)
  public boolean shouldPlaySound() {
    Minecraft mc = Minecraft.getInstance();
    return !isRemoved() &&
        getBlockPos().distToCenterSqr(mc.player.position()) < 400.0 &&
        getBlockState().getValue(AirScrubberBlock.RUNNING);
  }

  @OnlyIn(Dist.CLIENT)
  private void updateSound() {
    if (shouldPlaySound() && !isPlayingSound()) {
      Minecraft.getInstance().getSoundManager().play(soundInstance = new MachinePumpingSoundInstance(this));
    }
  }

}
