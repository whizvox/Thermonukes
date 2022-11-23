package me.whizvox.thermonukes.common.lib;

import me.whizvox.thermonukes.Thermonukes;
import me.whizvox.thermonukes.common.block.entity.AirScrubberBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.function.Supplier;

public class ThermonukesBlockEntities {

  private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Thermonukes.MOD_ID);

  public static void register(IEventBus bus) {
    BLOCK_ENTITIES.register(bus);
  }

  @SafeVarargs
  private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> supplier, Supplier<? extends Block>... blocks) {
    return BLOCK_ENTITIES.register(name, () -> BlockEntityType.Builder.of(supplier, Arrays.stream(blocks).map(Supplier::get).toArray(Block[]::new)).build(null));
  }

  public static final RegistryObject<BlockEntityType<AirScrubberBlockEntity>>
      AIR_SCRUBBER = register("air_scrubber", AirScrubberBlockEntity::new, ThermonukesBlocks.AIR_SCRUBBER);

}
