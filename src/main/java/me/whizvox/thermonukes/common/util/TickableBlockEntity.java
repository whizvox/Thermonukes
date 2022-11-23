package me.whizvox.thermonukes.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

public interface TickableBlockEntity {

  void tick(Level level, BlockPos pos, BlockState state);

  static <T extends BlockEntity> BlockEntityTicker<T> createTicker() {
    return (level, pos, state, blockEntity) -> ((TickableBlockEntity) blockEntity).tick(level, pos, state);
  }

}
