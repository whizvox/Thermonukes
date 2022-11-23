package me.whizvox.thermonukes.common.lib;

import me.whizvox.thermonukes.Thermonukes;
import me.whizvox.thermonukes.common.fluid.CleansingWaterFluid;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ThermonukesFluids {

  private static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Thermonukes.MOD_ID);

  public static void register(IEventBus bus) {
    FLUIDS.register(bus);
  }

  public static final RegistryObject<FlowingFluid>
      //CLEANSING_WATER_SOURCE = FLUIDS.register("cleansing_water", () -> new ForgeFlowingFluid.Source(ThermonukesFluids.CLEANSING_WATER_PROPERTIES)),
      //CLEANSING_WATER_FLOWING = FLUIDS.register("flowing_cleansing_water", () -> new ForgeFlowingFluid.Flowing(ThermonukesFluids.CLEANSING_WATER_PROPERTIES));
      CLEANSING_WATER_SOURCE = FLUIDS.register("cleansing_water", () -> new CleansingWaterFluid(ThermonukesFluids.CLEANSING_WATER_PROPERTIES, true)),
      CLEANSING_WATER_FLOWING = FLUIDS.register("flowing_cleansing_water", () -> new CleansingWaterFluid(ThermonukesFluids.CLEANSING_WATER_PROPERTIES, false));

  public static final ForgeFlowingFluid.Properties
      CLEANSING_WATER_PROPERTIES = new ForgeFlowingFluid.Properties(ThermonukesFluidTypes.CLEANSING_WATER, CLEANSING_WATER_SOURCE, CLEANSING_WATER_FLOWING)
          .bucket(ThermonukesItems.CLEANSING_WATER_BUCKET)
          .block(ThermonukesBlocks.CLEANSING_WATER);

}
