package me.whizvox.thermonukes.common.block;

import me.whizvox.thermonukes.common.block.entity.AirScrubberBlockEntity;
import me.whizvox.thermonukes.common.util.TickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class AirScrubberBlock extends BaseEntityBlock {

  public static final BooleanProperty RUNNING = BooleanProperty.create("running");

  private static final VoxelShape SHAPE = Shapes.or(
      Block.box(0.0, 0.0, 0.0, 16.0, 13.0, 16.0),
      Block.box(5.0, 13.0, 5.0, 11.0, 16.0, 11.0)
  );

  public AirScrubberBlock() {
    super(BlockBehaviour.Properties.of(Material.METAL).strength(5.0F, 6.0F));
    registerDefaultState(stateDefinition.any().setValue(RUNNING, false));
  }

  @Override
  public RenderShape getRenderShape(BlockState state) {
    return RenderShape.MODEL;
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
    return SHAPE;
  }

  @Override
  public boolean useShapeForLightOcclusion(BlockState pState) {
    return true;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(RUNNING);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new AirScrubberBlockEntity(pos, state);
  }

  @Override
  public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
    if (state.getValue(RUNNING)) {
      for (int i = 0; i < 3; i++) {
        level.addParticle(ParticleTypes.EFFECT,
            pos.getX() + 0.375F + rand.nextFloat() * 0.25F,
            pos.getY() + 1.0F,
            pos.getZ() + 0.375F + rand.nextFloat() * 0.25F,
            rand.nextFloat() * 0.1F - 0.05F,
            rand.nextFloat() * 0.3F + 0.2F,
            rand.nextFloat() * 0.1F - 0.05F
        );
      }
    }
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
    return TickableBlockEntity.createTicker();
  }

  @Nullable
  @Override
  public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
    return (AirScrubberBlockEntity) level.getBlockEntity(pos);
  }

  @Override
  public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
    if (!level.isClientSide) {
      NetworkHooks.openScreen((ServerPlayer) player, getMenuProvider(state, level, pos), pos);
    }
    return InteractionResult.sidedSuccess(level.isClientSide);
  }

}
