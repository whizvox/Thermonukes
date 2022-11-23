package me.whizvox.thermonukes.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Predicate;

public class MenuUtils {

  public static Predicate<Player> stillValid(BlockPos pos) {
    return player -> pos.distToCenterSqr(player.position()) <= 64.0;
  }

  public static Predicate<Player> stillValid(FriendlyByteBuf extraData) {
    if (extraData == null) {
      return player -> true;
    }
    return stillValid(extraData.readBlockPos());
  }

  public static Predicate<Player> stillValid(BlockEntity blockEntity) {
    return stillValid(blockEntity.getBlockPos());
  }

}
