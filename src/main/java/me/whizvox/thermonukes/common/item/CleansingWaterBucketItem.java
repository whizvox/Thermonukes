package me.whizvox.thermonukes.common.item;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

public class CleansingWaterBucketItem extends BucketItem {

  public CleansingWaterBucketItem(Supplier<? extends Fluid> supplier, Properties builder) {
    super(supplier, builder);
  }

}
