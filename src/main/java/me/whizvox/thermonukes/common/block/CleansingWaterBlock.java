package me.whizvox.thermonukes.common.block;

import me.whizvox.thermonukes.common.fluid.CleansingWaterFluid;
import me.whizvox.thermonukes.common.lib.ThermonukesFluids;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;

public class CleansingWaterBlock extends LiquidBlock {

  public static final int MAX_POTENCY = CleansingWaterFluid.MAX_POTENCY;
  public static final IntegerProperty POTENCY = CleansingWaterFluid.POTENCY;

  public CleansingWaterBlock() {
    super(ThermonukesFluids.CLEANSING_WATER_SOURCE, BlockBehaviour.Properties.of(Material.WATER));
    registerDefaultState(getStateDefinition().any().setValue(POTENCY, 0));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(POTENCY);
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return super.getFluidState(state).setValue(CleansingWaterFluid.POTENCY, state.getValue(POTENCY));
  }

}
