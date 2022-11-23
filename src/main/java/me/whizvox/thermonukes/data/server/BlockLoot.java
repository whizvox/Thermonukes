package me.whizvox.thermonukes.data.server;

import me.whizvox.thermonukes.common.lib.ThermonukesBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/*
 * Took heavy "inspiration" from Immersive Engineering:
 * https://github.com/BluSunrize/ImmersiveEngineering/blob/1.18.2/src/datagen/java/blusunrize/immersiveengineering/data/loot/BlockLoot.java
 */
class BlockLoot implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> {

  private final Set<ResourceLocation> generatedTables;
  private BiConsumer<ResourceLocation, LootTable.Builder> out;

  public BlockLoot() {
    generatedTables = new HashSet<>();
    out = null;
  }

  private ResourceLocation tableName(ResourceLocation loc) {
    return new ResourceLocation(loc.getNamespace(), "blocks/" + loc.getPath());
  }

  private LootPool.Builder singleItem(Supplier<? extends ItemLike> item) {
    return LootPool.lootPool()
        .setRolls(ConstantValue.exactly(1.0F))
        .add(LootItem.lootTableItem(item.get()));
  }

  private void register(ResourceLocation name, LootTable.Builder builder) {
    ResourceLocation tableName = tableName(name);
    if (!generatedTables.add(tableName)) {
      throw new IllegalStateException("Duplicate block loot table: " + tableName);
    }
    out.accept(tableName, builder.setParamSet(LootContextParamSets.BLOCK));
  }

  private void register(Supplier<? extends Block> block, LootPool.Builder... pools) {
    LootTable.Builder builder = new LootTable.Builder();
    for (LootPool.Builder pool : pools) {
      builder.withPool(pool);
    }
    register(ForgeRegistries.BLOCKS.getKey(block.get()), builder);
  }

  private void registerSelfDrop(Supplier<? extends Block> block, LootPool.Builder... pools) {
    LootPool.Builder[] newPools = new LootPool.Builder[pools.length + 1];
    System.arraycopy(pools, 0, newPools, 0, pools.length);
    newPools[pools.length] = singleItem(block);
    register(block, newPools);
  }

  @Override
  public void accept(BiConsumer<ResourceLocation, LootTable.Builder> out) {
    this.out = out;
    registerSelfDrop(ThermonukesBlocks.ANTIMATTER_EXPLOSIVE);
    registerSelfDrop(ThermonukesBlocks.RED_MATTER_EXPLOSIVE);
    registerSelfDrop(ThermonukesBlocks.NUKE);
    registerSelfDrop(ThermonukesBlocks.AIR_SCRUBBER);
  }

}
