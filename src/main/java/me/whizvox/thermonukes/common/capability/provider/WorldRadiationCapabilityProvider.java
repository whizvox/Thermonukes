package me.whizvox.thermonukes.common.capability.provider;

import me.whizvox.thermonukes.common.capability.WorldRadiation;
import me.whizvox.thermonukes.common.lib.ThermonukesCapabilities;
import net.minecraft.nbt.CompoundTag;

public class WorldRadiationCapabilityProvider extends SimpleCapabilityProvider<WorldRadiation, CompoundTag> {

  public WorldRadiationCapabilityProvider() {
    super(ThermonukesCapabilities.WORLD_RADIATION, new WorldRadiation());
  }

}
