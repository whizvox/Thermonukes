package me.whizvox.thermonukes.common.lib;

import me.whizvox.thermonukes.Thermonukes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ThermonukesSounds {

  private static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Thermonukes.MOD_ID);

  public static void register(IEventBus bus) {
    SOUNDS.register(bus);
  }

  private static RegistryObject<SoundEvent> register(String name) {
    return SOUNDS.register(name, () -> new SoundEvent(new ResourceLocation(Thermonukes.MOD_ID, name)));
  }

  private static RegistryObject<SoundEvent> register(String name, float range) {
    return SOUNDS.register(name, () -> new SoundEvent(new ResourceLocation(Thermonukes.MOD_ID, name), range));
  }

  public static final RegistryObject<SoundEvent>
      ANTIMATTER_EXPLOSIVE_DETONATION = register("antimatter_explosive_detonation", 48),
      BLACK_HOLE_WHOOSHING = register("black_hole.wooshing"),
      BLACK_HOLE_DISAPPEAR = register("black_hole.disappear", 32),
      WATER_SWIRL = register("water_swirl"),
      MACHINE_PUMPING = register("machine_pumping"),
      GEIGER_COUNTER_MODE_CHANGE = register("geiger_counter.mode_change"),
      GEIGER_COUNTER_CLICK = register("geiger_counter.click"),
      GEIGER_COUNTER_BURST = register("geiger_counter.burst"),
      GEIGER_COUNTER_FAST_BURST = register("geiger_counter.fast_burst"),
      GEIGER_COUNTER_EXTREME_BURST = register("geiger_counter.extreme_burst");

}
