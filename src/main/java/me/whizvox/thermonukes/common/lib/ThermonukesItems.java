package me.whizvox.thermonukes.common.lib;

import me.whizvox.thermonukes.Thermonukes;
import me.whizvox.thermonukes.common.ThermonukesCreativeTab;
import me.whizvox.thermonukes.common.item.CleansingSaltItem;
import me.whizvox.thermonukes.common.item.GeigerCounterItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;

public class ThermonukesItems {

  private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Thermonukes.MOD_ID);

  public static void register(IEventBus bus) {
    ITEMS.register(bus);
  }

  public static Item.Properties defaultProperties() {
    return new Item.Properties().tab(ThermonukesCreativeTab.INSTANCE);
  }

  public static Item.Properties unstackableProperties() {
    return defaultProperties().stacksTo(1);
  }

  private static RegistryObject<Item> registerSimple(String name, Function<Item.Properties, Item.Properties> builder) {
    return ITEMS.register(name, () -> new Item(builder.apply(defaultProperties())));
  }

  private static RegistryObject<Item> registerMaterial(String name) {
    return registerSimple(name, props -> props);
  }

  private static RegistryObject<BlockItem> registerBlockItem(RegistryObject<? extends Block> block) {
    return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties().tab(ThermonukesCreativeTab.INSTANCE)));
  }

  public static final RegistryObject<Item>
      SALTY_MIXTURE_TUBE = registerMaterial("salty_mixture_tube"),
      ANTI_RADIATIVE_FABRIC_SHEET = registerMaterial("anti_radiative_fabric_sheet"),
      RUBBER_SHEET = registerMaterial("rubber_sheet"),
      CLEANSING_SALT = ITEMS.register("cleansing_salt", CleansingSaltItem::new);
  public static final RegistryObject<BucketItem>
      CLEANSING_WATER_BUCKET = ITEMS.register("cleansing_water_bucket", () -> new BucketItem(ThermonukesFluids.CLEANSING_WATER_SOURCE, defaultProperties().stacksTo(1)));

  public static final RegistryObject<ArmorItem>
      HAZMAT_SUIT_HEADPIECE = ITEMS.register("hazmat_headpiece", () -> new ArmorItem(ThermonukesArmor.HAZMAT_SUIT, EquipmentSlot.HEAD, unstackableProperties())),
      HAZMAT_SUIT_COAT = ITEMS.register("hazmat_coat", () -> new ArmorItem(ThermonukesArmor.HAZMAT_SUIT, EquipmentSlot.CHEST, unstackableProperties())),
      HAZMAT_SUIT_LEGGINGS = ITEMS.register("hazmat_leggings", () -> new ArmorItem(ThermonukesArmor.HAZMAT_SUIT, EquipmentSlot.LEGS, unstackableProperties())),
      RUBBER_BOOTS = ITEMS.register("rubber_boots", () -> new ArmorItem(ThermonukesArmor.HAZMAT_SUIT, EquipmentSlot.FEET, unstackableProperties())),
      GAS_MASK = ITEMS.register("gas_mask", () -> new ArmorItem(ThermonukesArmor.GAS_MASK, EquipmentSlot.HEAD, unstackableProperties()));

  public static final RegistryObject<GeigerCounterItem> GEIGER_COUNTER = ITEMS.register("geiger_counter", GeigerCounterItem::new);

  public static final RegistryObject<BlockItem>
      ANTIMATTER_EXPLOSIVE = registerBlockItem(ThermonukesBlocks.ANTIMATTER_EXPLOSIVE),
      RED_MATTER_EXPLOSIVE = registerBlockItem(ThermonukesBlocks.RED_MATTER_EXPLOSIVE),
      NUKE = registerBlockItem(ThermonukesBlocks.NUKE),
      AIR_SCRUBBER = registerBlockItem(ThermonukesBlocks.AIR_SCRUBBER);

}
