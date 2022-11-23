package me.whizvox.thermonukes.common.block;

import me.whizvox.thermonukes.common.ExplosiveProperties;
import me.whizvox.thermonukes.common.entity.AbstractPrimedExplosive;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;

public class ExplosiveBlock extends Block {

  protected final ExplosiveProperties explosiveProps;
  private final IPrimedExplosiveCreator primedExplosiveCreator;

  public ExplosiveBlock(Properties props, ExplosiveProperties explosiveProps, IPrimedExplosiveCreator primedExplosiveCreator) {
    super(props.sound(SoundType.GRASS));
    this.explosiveProps = explosiveProps;
    this.primedExplosiveCreator = primedExplosiveCreator;
  }

  public ExplosiveBlock(ExplosiveProperties explosiveProps, IPrimedExplosiveCreator primedExplosiveCreator) {
    this(BlockBehaviour.Properties.of(Material.EXPLOSIVE), explosiveProps, primedExplosiveCreator);
  }

  protected void spawnPrimedExplosive(Level level, BlockPos pos, boolean exploded) {
    level.removeBlock(pos, false);
    AbstractPrimedExplosive explosive = primedExplosiveCreator.create(level, pos);
    if (exploded) {
      final int fuse = explosiveProps.fuseLength();
      explosive.setFuse(level.random.nextInt(fuse / 4) + fuse / 8);
    } else {
      explosive.setFuse(explosiveProps.fuseLength());
    }
    level.addFreshEntity(explosive);
    level.playSound(null, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
  }

  @Override
  public boolean canDropFromExplosion(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
    return !explosiveProps.litByExplosive();
  }

  @Override
  public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
    if (explosiveProps.litByPlayer()) {
      ItemStack stack = player.getItemInHand(hand);
      if (stack.is(Items.FLINT_AND_STEEL) || stack.is(Items.FIRE_CHARGE)) {
        if (!level.isClientSide) {
          spawnPrimedExplosive(level, pos, false);
        }
        if (!player.isCreative()) {
          if (stack.is(Items.FLINT_AND_STEEL)) {
            stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
          } else {
            stack.shrink(1);
          }
        }
        player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
        return InteractionResult.sidedSuccess(level.isClientSide);
      }
    }
    return super.use(state, level, pos, player, hand, hit);
  }

  @Override
  public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos oldPos, boolean isMoving) {
    if (explosiveProps.litByRedstone() &&
        !level.isClientSide &&
        level.hasNeighborSignal(pos)) {
      spawnPrimedExplosive(level, pos, false);
    }
  }

  @Override
  public void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
    if (explosiveProps.litByProjectile() && !level.isClientSide) {
      BlockPos pos = hit.getBlockPos();
      if (projectile.isOnFire() && projectile.mayInteract(level, pos)) {
        spawnPrimedExplosive(level, pos, false);
      }
    }
  }

  @Override
  public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
    if (explosiveProps.litByRedstone() &&
        !level.isClientSide &&
        !oldState.is(state.getBlock()) &&
        level.hasNeighborSignal(pos)) {
      spawnPrimedExplosive(level, pos, false);
    }
  }

  @Override
  public void wasExploded(Level level, BlockPos pos, Explosion explosion) {
    if (explosiveProps.litByExplosive() &&
        !level.isClientSide) {
      spawnPrimedExplosive(level, pos, true);
    }
  }

}
