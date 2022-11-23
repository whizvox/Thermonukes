package me.whizvox.thermonukes.common.util;

public enum GeigerCounterLevel {

  NONE(0.0F),
  LOW(0.25F),
  WARM(0.5F),
  HOT(0.75F),
  EXTREME(1.0F);

  public final float propValue;

  GeigerCounterLevel(float propValue) {
    this.propValue = propValue;
  }

  public static GeigerCounterLevel getLevel(float dosage) {
    GeigerCounterLevel level;
    if (dosage < 1) {
      level = GeigerCounterLevel.NONE;
    } else if (dosage < 100) {
      level = GeigerCounterLevel.LOW;
    } else if (dosage < 1000) {
      level = GeigerCounterLevel.WARM;
    } else if (dosage < 10000) {
      level = GeigerCounterLevel.HOT;
    } else {
      level = GeigerCounterLevel.EXTREME;
    }
    return level;
  }

  public static GeigerCounterLevel fromOrdinal(byte b) {
    return EnumUtils.fromOrdinal(b, GeigerCounterLevel.class);
  }

}
