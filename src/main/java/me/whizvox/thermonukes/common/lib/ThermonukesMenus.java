package me.whizvox.thermonukes.common.lib;

import me.whizvox.thermonukes.Thermonukes;
import me.whizvox.thermonukes.common.menu.AirScrubberMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ThermonukesMenus {

  private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Thermonukes.MOD_ID);

  public static void register(IEventBus bus) {
    MENUS.register(bus);
  }

  public static final RegistryObject<MenuType<AirScrubberMenu>> AIR_SCRUBBER = MENUS.register("air_scrubber", () -> IForgeMenuType.create(AirScrubberMenu::createForClient));

}
