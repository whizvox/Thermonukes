package me.whizvox.thermonukes.common.item;

import me.whizvox.thermonukes.Thermonukes;
import me.whizvox.thermonukes.common.ThermonukesCreativeTab;
import me.whizvox.thermonukes.common.lib.ThermonukesItems;
import me.whizvox.thermonukes.common.util.ArmorMaterialAdapter;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

public class HazmatSuitArmorItem extends ArmorItem {

  public HazmatSuitArmorItem(EquipmentSlot slot) {
    super(MATERIAL, slot, new Properties().stacksTo(1).tab(ThermonukesCreativeTab.INSTANCE));
  }

  public static final ArmorMaterial MATERIAL = ArmorMaterialAdapter.builder()
      .durability(1500)
      .defense(1, 3, 2, 1)
      .enchantmentValue(8)
      .repairIngredient(() -> Ingredient.EMPTY)
      .name(Thermonukes.modLoc("hazmat_suit"))
      .build();

}
