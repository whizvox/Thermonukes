package me.whizvox.thermonukes.common.lib;

import me.whizvox.thermonukes.Thermonukes;
import me.whizvox.thermonukes.common.entity.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ThermonukesEntities {

  private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Thermonukes.MOD_ID);

  public static void register(IEventBus bus) {
    ENTITIES.register(bus);
  }

  private static <T extends Entity> RegistryObject<EntityType<T>> register(String name, EntityType.Builder<T> builder) {
    return ENTITIES.register(name, () -> builder.build(name));
  }

  private static <T extends AbstractPrimedExplosive> RegistryObject<EntityType<T>> registerPrimedExplosive(String name, EntityType.EntityFactory<T> factory) {
    return register(name, EntityType.Builder.of(factory, MobCategory.MISC).sized(0.98F, 0.98F).clientTrackingRange(10).updateInterval(2));
  }

  public static final RegistryObject<EntityType<PrimedAntimatterExplosive>> PRIMED_ANTIMATTER_EXPLOSIVE =
      registerPrimedExplosive("primed_antimatter_explosive", PrimedAntimatterExplosive::new);
  public static final RegistryObject<EntityType<PrimedRedMatterExplosive>> PRIMED_RED_MATTER_EXPLOSIVE =
      registerPrimedExplosive("primed_red_matter_explosive", PrimedRedMatterExplosive::new);
  public static final RegistryObject<EntityType<PrimedNuke>> PRIMED_NUKE =
      registerPrimedExplosive("primed_nuclear_explosive", PrimedNuke::new);

  public static final RegistryObject<EntityType<BlackHole>> BLACK_HOLE =
      register("black_hole", EntityType.Builder.<BlackHole>of(BlackHole::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(10).updateInterval(5));

}
