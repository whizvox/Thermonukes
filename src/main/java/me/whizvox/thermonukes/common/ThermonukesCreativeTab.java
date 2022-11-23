package me.whizvox.thermonukes.common;

import me.whizvox.thermonukes.Thermonukes;
import me.whizvox.thermonukes.common.lib.ThermonukesItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ThermonukesCreativeTab extends CreativeModeTab {

  public ThermonukesCreativeTab() {
    super(Thermonukes.MOD_ID + ".main");
  }

  @Override
  public ItemStack makeIcon() {
    return new ItemStack(ThermonukesItems.ANTIMATTER_EXPLOSIVE.get());
  }

  public static final ThermonukesCreativeTab INSTANCE = new ThermonukesCreativeTab();

}
