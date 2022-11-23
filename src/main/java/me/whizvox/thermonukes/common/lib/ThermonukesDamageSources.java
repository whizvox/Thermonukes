package me.whizvox.thermonukes.common.lib;

import me.whizvox.thermonukes.Thermonukes;
import net.minecraft.world.damagesource.DamageSource;

import java.util.function.Function;

public class ThermonukesDamageSources {

  private static <T extends DamageSource> T create(Function<String, T> constructor, String id) {
    return constructor.apply(Thermonukes.MOD_ID + "." + id);
  }

  private static DamageSource simple(String id) {
    return create(DamageSource::new, id);
  }

  public static final DamageSource
      ANTIMATTER_EXPLOSION = simple("antimatter_explosion").bypassArmor(),
      BLACK_HOLE = simple("black_hole").bypassArmor(),
      RADIATION = simple("radiation").bypassArmor();

}
