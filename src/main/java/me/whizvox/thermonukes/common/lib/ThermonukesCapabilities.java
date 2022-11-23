package me.whizvox.thermonukes.common.lib;

import me.whizvox.thermonukes.Thermonukes;
import me.whizvox.thermonukes.common.capability.EntityRadiation;
import me.whizvox.thermonukes.common.capability.WorldRadiation;
import me.whizvox.thermonukes.common.capability.provider.EntityRadiationCapabilityProvider;
import me.whizvox.thermonukes.common.capability.provider.WorldRadiationCapabilityProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class ThermonukesCapabilities {

  public static final Capability<WorldRadiation> WORLD_RADIATION = CapabilityManager.get(new CapabilityToken<>() {});
  public static final Capability<EntityRadiation> ENTITY_RADIATION = CapabilityManager.get(new CapabilityToken<>() {});

  private static void onRegister(final RegisterCapabilitiesEvent event) {
    event.register(WorldRadiation.class);
    event.register(EntityRadiation.class);
  }

  private static void onAttachToLevel(final AttachCapabilitiesEvent<Level> event) {
    event.addCapability(new ResourceLocation(Thermonukes.MOD_ID, "world_radiation"), new WorldRadiationCapabilityProvider());
  }

  private static void onAttachToLivingEntity(final AttachCapabilitiesEvent<Entity> event) {
    if (event.getObject() instanceof LivingEntity) {
      event.addCapability(new ResourceLocation(Thermonukes.MOD_ID, "entity_radiation"), new EntityRadiationCapabilityProvider());
    }
  }

  public static void register(IEventBus modBus, IEventBus forgeBus) {
    modBus.addListener(ThermonukesCapabilities::onRegister);
    forgeBus.addGenericListener(Level.class, ThermonukesCapabilities::onAttachToLevel);
    forgeBus.addGenericListener(Entity.class, ThermonukesCapabilities::onAttachToLivingEntity);
  }

}
