package me.whizvox.thermonukes.common.util;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public enum GeigerCounterMode {

  CHUNK,
  ENTITY;

  public final Component name;

  GeigerCounterMode() {
    this.name = Component.translatable("thermonukes.geiger_counter.mode." + toString().toLowerCase());
  }

  public static GeigerCounterMode nextMode(GeigerCounterMode mode) {
    int ordinal = (mode.ordinal() + 1) % GeigerCounterMode.values().length;
    return GeigerCounterMode.values()[ordinal];
  }

  @Nullable
  public static GeigerCounterMode fromOrdinal(byte b) {
    return EnumUtils.fromOrdinal(b, GeigerCounterMode.class);
  }

}
