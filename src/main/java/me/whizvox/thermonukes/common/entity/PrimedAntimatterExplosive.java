package me.whizvox.thermonukes.common.entity;

import me.whizvox.thermonukes.common.lib.ThermonukesDamageSources;
import me.whizvox.thermonukes.common.lib.ThermonukesEntities;
import me.whizvox.thermonukes.common.lib.ThermonukesSounds;
import me.whizvox.thermonukes.common.util.Vec2i;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

public class PrimedAntimatterExplosive extends AbstractPrimedExplosive {

  public PrimedAntimatterExplosive(EntityType<?> type, Level level) {
    super(type, level);
  }

  public PrimedAntimatterExplosive(Level level, BlockPos pos) {
    this(ThermonukesEntities.PRIMED_ANTIMATTER_EXPLOSIVE.get(), level);
    moveTo(pos);
    setPower(60);
  }

  @Override
  public void detonate() {
    level.playSound(null, position().x, position().y, position().z, ThermonukesSounds.ANTIMATTER_EXPLOSIVE_DETONATION.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
    float radius = getPower();
    float radiusSqr = radius * radius;
    int iRadius = (int) radius;
    BlockPos centerPos = blockPosition();
    Vec2i centerPos2d = new Vec2i(centerPos);
    List<Vec2i> blocks = new ArrayList<>();
    for (int x = -iRadius; x <= iRadius; x++) {
      for (int z = -iRadius; z <= iRadius; z++) {
        Vec2i pos2d = new Vec2i(centerPos.offset(x, 0, z));
        if (pos2d.distSqr(centerPos2d) <= radiusSqr) {
          blocks.add(pos2d);
        }
      }
    }
    for (int y = level.getMinBuildHeight(); y < level.getMaxBuildHeight(); y++) {
      for (Vec2i horzPos : blocks) {
        BlockPos pos = new BlockPos(horzPos.x(), y, horzPos.z());
        BlockState state = level.getBlockState(pos);
        if (state.getDestroySpeed(level, pos) > 0) {
          level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
        }
      }
    }
    List<Entity> entities = level.getEntities((Entity) null, new AABB(centerPos.offset(-radius, level.getMinBuildHeight(), -radius), centerPos.offset(radius, level.getMaxBuildHeight(), radius)), entity -> true);
    entities.forEach(entity -> {
      if ((!(entity instanceof Player player) || !player.isCreative()) && distSqrIgnoreY(centerPos, entity.position()) <= radiusSqr) {
        if (entity instanceof LivingEntity livEntity) {
          livEntity.hurt(ThermonukesDamageSources.ANTIMATTER_EXPLOSION, livEntity.getHealth());
        } else {
          entity.kill();
        }
      }
    });
  }

  private static double distSqrIgnoreY(BlockPos pos1, Position pos2) {
    double dx = pos2.x() - pos1.getX() + 0.5;
    double dz = pos2.z() - pos1.getZ() + 0.5;
    return dx * dx + dz * dz;
  }

}
