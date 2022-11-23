package me.whizvox.thermonukes.data.server;

import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ThermonukesLootTableProvider extends LootTableProvider {

  public ThermonukesLootTableProvider(DataGenerator pGenerator) {
    super(pGenerator);
  }

  @Override
  protected void validate(Map<ResourceLocation, LootTable> tables, ValidationContext ctx) {
    tables.forEach((name, table) -> LootTables.validate(ctx, name, table));
  }

  @Override
  protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
    return List.of(
        Pair.of(BlockLoot::new, LootContextParamSets.EMPTY)
    );
  }

}
