package me.whizvox.thermonukes.common.capability.provider;

import me.whizvox.thermonukes.common.capability.EntityRadiation;
import me.whizvox.thermonukes.common.lib.ThermonukesCapabilities;
import net.minecraft.nbt.FloatTag;

public class EntityRadiationCapabilityProvider extends SimpleCapabilityProvider<EntityRadiation, FloatTag> {

  public EntityRadiationCapabilityProvider() {
    super(ThermonukesCapabilities.ENTITY_RADIATION, new EntityRadiation());
  }

}
