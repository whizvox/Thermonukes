package me.whizvox.thermonukes.common.lib;

import me.whizvox.thermonukes.Thermonukes;
import me.whizvox.thermonukes.common.util.ArmorMaterialAdapter;
import net.minecraft.world.item.ArmorMaterial;

public class ThermonukesArmor {

  public static final ArmorMaterial HAZMAT_SUIT = ArmorMaterialAdapter.builder()
      .durability(1500)
      .defense(1, 3, 2, 1)
      .enchantmentValue(8)
      .name(Thermonukes.modLoc("hazmat_suit"))
      .build();

  public static final ArmorMaterial GAS_MASK = ArmorMaterialAdapter.builder()
      .durability(1000)
      .defense(2, 0, 0, 0)
      .enchantmentValue(3)
      .name(Thermonukes.modLoc("gas_mask"))
      .build();


}
