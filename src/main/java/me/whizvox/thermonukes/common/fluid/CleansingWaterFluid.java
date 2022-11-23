package me.whizvox.thermonukes.common.fluid;

import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class CleansingWaterFluid extends ForgeFlowingFluid {

  public static final int MAX_POTENCY = 4;
  public static final IntegerProperty POTENCY = IntegerProperty.create("potency", 0, MAX_POTENCY);

  private final boolean isSource;

  public CleansingWaterFluid(Properties props, boolean isSource) {
    super(props);
    this.isSource = isSource;
    FluidState defaultState = getStateDefinition().any()
        .setValue(POTENCY, 0);
    if (!isSource) {
      defaultState = defaultState.setValue(FlowingFluid.LEVEL, 7);
    }
    registerDefaultState(defaultState);
  }

  @Override
  protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
    super.createFluidStateDefinition(builder);
    if (!isSource) {
      builder.add(FlowingFluid.LEVEL);
    }
    builder.add(POTENCY);
  }

  @Override
  public boolean isSource(FluidState state) {
    return isSource;
  }

  @Override
  public int getAmount(FluidState state) {
    return isSource ? 8 : state.getValue(FlowingFluid.LEVEL);
  }



}
