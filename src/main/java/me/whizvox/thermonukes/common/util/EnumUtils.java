package me.whizvox.thermonukes.common.util;

import org.jetbrains.annotations.Nullable;

public class EnumUtils {

  @Nullable
  public static <T extends Enum<T>> T fromOrdinal(long ordinal, Class<T> cls) {
    T[] values = cls.getEnumConstants();
    if (ordinal < 0 || ordinal >= values.length) {
      return null;
    }
    return values[(int) ordinal];
  }

}
