package me.whizvox.thermonukes.common.lib;

import me.whizvox.thermonukes.Thermonukes;
import me.whizvox.thermonukes.common.fluid.ThermonukesBaseFluidType;
import me.whizvox.thermonukes.common.util.FluidClientProperties;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ThermonukesFluidTypes {

  private static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, Thermonukes.MOD_ID);

  public static void register(IEventBus bus) {
    FLUID_TYPES.register(bus);
  }

  private static RegistryObject<FluidType> register(String name, FluidType.Properties props, FluidClientProperties.Builder builder) {
    return FLUID_TYPES.register(name, () -> new ThermonukesBaseFluidType(props, builder.build()));
  }

  public static final RegistryObject<FluidType> CLEANSING_WATER = register(
      "cleansing_water",
      FluidType.Properties.create()
          .supportsBoating(true)
          .canHydrate(true)
          .temperature(500),
      FluidClientProperties.builder()
          .tintColor(0xA053B1F7)
  );

}
