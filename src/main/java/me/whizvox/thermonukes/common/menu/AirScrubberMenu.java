package me.whizvox.thermonukes.common.menu;

import me.whizvox.thermonukes.common.block.entity.AirScrubberBlockEntity;
import me.whizvox.thermonukes.common.lib.ThermonukesItems;
import me.whizvox.thermonukes.common.lib.ThermonukesMenus;
import me.whizvox.thermonukes.common.util.MenuUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.function.Predicate;

public class AirScrubberMenu extends AbstractContainerMenu {

  public static AirScrubberMenu createForServer(int containerId, Inventory playerInv, AirScrubberBlockEntity airScrubber) {
    return new AirScrubberMenu(containerId, playerInv, airScrubber.getInventory(), MenuUtils.stillValid(airScrubber));
  }

  public static AirScrubberMenu createForClient(int containerId, Inventory playerInv, FriendlyByteBuf extraData) {
    return new AirScrubberMenu(containerId, playerInv, new ItemStackHandler(AirScrubberBlockEntity.SLOTS), MenuUtils.stillValid(extraData));
  }

  private final Predicate<Player> stillValidPredicate;

  protected AirScrubberMenu(int containerId, Inventory playerInv, IItemHandler airScrubberInv, Predicate<Player> stillValidPredicate) {
    super(ThermonukesMenus.AIR_SCRUBBER.get(), containerId);
    this.stillValidPredicate = stillValidPredicate;

    for (int x = 0; x < 3; x++) {
      addSlot(RestrictedSlot.forItem(airScrubberInv, x, 62 + x * 18, 35, ThermonukesItems.CLEANSING_SALT.get()));
    }
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 9; j++) {
        addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
      }
    }
    for (int i = 0; i < 9; i++) {
      addSlot(new Slot(playerInv, i, 8 + i * 18, 142));
    }
  }

  //  0 -  2: cleansing salt slots
  //  3 - 29: player inventory
  // 30 - 38: player hotbar
  @Override
  public ItemStack quickMoveStack(Player player, int quickMovedSlotIndex) {
    // The quick moved slot stack
    ItemStack quickMovedStack = ItemStack.EMPTY;
    // The quick moved slot
    Slot quickMovedSlot = this.slots.get(quickMovedSlotIndex);

    // If the slot is in the valid range and the slot is not empty
    if (quickMovedSlot != null && quickMovedSlot.hasItem()) {
      // Get the raw stack to move
      ItemStack rawStack = quickMovedSlot.getItem();
      // Set the slot stack to a copy of the raw stack
      quickMovedStack = rawStack.copy();

      /*
      The following quick move logic can be simplified to if in data inventory,
      try to move to player inventory/hotbar and vice versa for containers
      that cannot transform data (e.g. chests).
      */

      // If the quick move was performed on the data inventory result slot
      if (quickMovedSlotIndex == 0) {
        // Try to move the result slot into the player inventory/hotbar
        if (!this.moveItemStackTo(rawStack, 3, 39, true)) {
          // If cannot move, no longer quick move
          return ItemStack.EMPTY;
        }

        // Perform logic on result slot quick move
        quickMovedSlot.onQuickCraft(rawStack, quickMovedStack);
      }
      // Else if the quick move was performed on the player inventory or hotbar slot
      else if (quickMovedSlotIndex >= 3 && quickMovedSlotIndex < 39) {
        // Try to move the inventory/hotbar slot into the data inventory input slots
        if (!this.moveItemStackTo(rawStack, 0, 3, false)) {
          // If cannot move and in player inventory slot, try to move to hotbar
          if (quickMovedSlotIndex < 30) {
            if (!this.moveItemStackTo(rawStack, 30, 39, false)) {
              // If cannot move, no longer quick move
              return ItemStack.EMPTY;
            }
          }
          // Else try to move hotbar into player inventory slot
          else if (!this.moveItemStackTo(rawStack, 3, 30, false)) {
            // If cannot move, no longer quick move
            return ItemStack.EMPTY;
          }
        }
      }
      // Else if the quick move was performed on the data inventory input slots, try to move to player inventory/hotbar
      else if (!this.moveItemStackTo(rawStack, 3, 39, false)) {
        // If cannot move, no longer quick move
        return ItemStack.EMPTY;
      }

      if (rawStack.isEmpty()) {
        // If the raw stack has completely moved out of the slot, set the slot to the empty stack
        quickMovedSlot.set(ItemStack.EMPTY);
      } else {
        // Otherwise, notify the slot that that the stack count has changed
        quickMovedSlot.setChanged();
      }

    /*
    The following if statement and Slot#onTake call can be removed if the
    menu does not represent a container that can transform stacks (e.g.
    chests).
    */
      if (rawStack.getCount() == quickMovedStack.getCount()) {
        // If the raw stack was not able to be moved to another slot, no longer quick move
        return ItemStack.EMPTY;
      }
      // Execute logic on what to do post move with the remaining stack
      quickMovedSlot.onTake(player, rawStack);
    }

    return quickMovedStack; // Return the slot stack
  }

  @Override
  public boolean stillValid(Player player) {
    return stillValidPredicate.test(player);
  }

}
