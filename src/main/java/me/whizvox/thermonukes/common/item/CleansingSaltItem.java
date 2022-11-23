package me.whizvox.thermonukes.common.item;

import me.whizvox.thermonukes.common.block.CleansingWaterBlock;
import me.whizvox.thermonukes.common.lib.ThermonukesBlocks;
import me.whizvox.thermonukes.common.lib.ThermonukesItems;
import me.whizvox.thermonukes.common.lib.ThermonukesSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class CleansingSaltItem extends Item {

  public CleansingSaltItem() {
    super(ThermonukesItems.defaultItemProperties());
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    ItemStack stack = player.getItemInHand(hand);
    BlockHitResult hit = level.clip(new ClipContext(player.getEyePosition(), player.getEyePosition().add(player.getLookAngle().scale(5.0)), ClipContext.Block.COLLIDER, ClipContext.Fluid.SOURCE_ONLY, player));
    if (hit.getType() == HitResult.Type.BLOCK) {
      BlockPos pos = hit.getBlockPos();
      BlockState state = level.getBlockState(pos);
      boolean success = false;
      if (state.is(ThermonukesBlocks.CLEANSING_WATER.get())) {
        int potency = state.getValue(CleansingWaterBlock.POTENCY);
        if (potency < CleansingWaterBlock.MAX_POTENCY) {
          level.setBlock(pos, ThermonukesBlocks.CLEANSING_WATER.get().defaultBlockState().setValue(CleansingWaterBlock.POTENCY, potency + 1), Block.UPDATE_ALL);
          success = true;
        }
      } else if (state.is(Blocks.WATER)) {
        level.setBlock(pos, ThermonukesBlocks.CLEANSING_WATER.get().defaultBlockState(), Block.UPDATE_ALL);
        success = true;
      }
      if (success) {
        if (!player.isCreative()) {
          stack.shrink(1);
        }
        for (int i = player.getRandom().nextInt(3); i < 6; i++) {
          level.addParticle(
              ParticleTypes.SPLASH,
              pos.getX() + 0.2 + player.getRandom().nextDouble() * 0.6,
              pos.getY() + 1.0,
              pos.getZ() + 0.2 + player.getRandom().nextDouble() * 0.6,
              player.getRandom().nextDouble() * 0.2 - 0.1,
              player.getRandom().nextDouble() * 0.2 + 0.1,
              player.getRandom().nextDouble() * 0.2 - 0.1
          );
        }
        level.playSound(null, pos, ThermonukesSounds.WATER_SWIRL.get(), SoundSource.BLOCKS, 1.0F, 0.8F + player.getRandom().nextFloat() * 0.4F);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
      }
    }
    return InteractionResultHolder.pass(stack);
  }

}
