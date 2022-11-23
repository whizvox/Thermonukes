package me.whizvox.thermonukes.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public record Vec2i(int x, int z) {

  public Vec2i(BlockPos pos) {
    this(pos.getX(), pos.getZ());
  }

  public double distSqr(Vec2i other) {
    double dx = x - other.x;
    double dy = z - other.z;
    return dx * dx + dy * dy;
  }

  public Vec2i relative(Direction direction) {
    return new Vec2i(x + direction.getStepX(), z + direction.getStepZ());
  }

}
