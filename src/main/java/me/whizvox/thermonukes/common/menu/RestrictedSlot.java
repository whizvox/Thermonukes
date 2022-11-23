package me.whizvox.thermonukes.common.menu;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class RestrictedSlot extends SlotItemHandler {

  private final Predicate<ItemStack> mayPlaceFunction;

  public RestrictedSlot(IItemHandler inventory, int index, int x, int y, Predicate<ItemStack> mayPlacePredicate) {
    super(inventory, index, x, y);
    this.mayPlaceFunction = mayPlacePredicate;
  }

  @Override
  public boolean mayPlace(@NotNull ItemStack stack) {
    return mayPlaceFunction.test(stack) && super.mayPlace(stack);
  }

  public static RestrictedSlot forItem(IItemHandler inventory, int index, int x, int y, Item item) {
    return new RestrictedSlot(inventory, index, x, y, stack -> stack.is(item));
  }

}
