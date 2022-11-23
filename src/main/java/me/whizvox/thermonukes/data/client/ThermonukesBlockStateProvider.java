package me.whizvox.thermonukes.data.client;

import me.whizvox.thermonukes.Thermonukes;
import me.whizvox.thermonukes.common.block.AirScrubberBlock;
import me.whizvox.thermonukes.common.lib.ThermonukesBlocks;
import me.whizvox.thermonukes.common.lib.ThermonukesItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class ThermonukesBlockStateProvider extends BlockStateProvider {

  public ThermonukesBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
    super(gen, Thermonukes.MOD_ID, exFileHelper);
  }

  protected String name(Block block) {
    return ForgeRegistries.BLOCKS.getKey(block).getPath();
  }

  protected void cubeAll(Block block, ResourceLocation texture) {
    simpleBlockAndItem(block, models().cubeAll(name(block), texture));
  }

  protected void cubeColumn(Block block, ResourceLocation side, ResourceLocation end) {
    simpleBlockAndItem(block, models().cubeColumn(name(block), side, end));
  }

  protected void simpleBlockAndItem(Block block, ConfiguredModel model) {
    simpleBlock(block, model);
    itemModels().getBuilder(ForgeRegistries.BLOCKS.getKey(block).getPath()).parent(model.model);
  }

  protected void simpleBlockAndItem(Block block, ModelFile model) {
    simpleBlockAndItem(block, new ConfiguredModel(model));
  }

  private void registerAirScrubber() {
    ConfiguredModel offModel = new ConfiguredModel(models().getBuilder("block/air_scrubber_off")
        .parent(models().getExistingFile(modLoc("block/air_scrubber_base")))
        .texture("side", "block/air_scrubber_side_off")
        .texture("top", "block/air_scrubber_top_off"));
    ConfiguredModel onModel = new ConfiguredModel(models().getBuilder("block/air_scrubber_on")
        .parent(models().getExistingFile(modLoc("block/air_scrubber_base")))
        .texture("side", "block/air_scrubber_side_on")
        .texture("top", "block/air_scrubber_top_on"));
    getVariantBuilder(ThermonukesBlocks.AIR_SCRUBBER.get())
        .partialState().with(AirScrubberBlock.RUNNING, false).setModels(offModel)
        .partialState().with(AirScrubberBlock.RUNNING, true).setModels(onModel);
    itemModels().getBuilder(ForgeRegistries.ITEMS.getKey(ThermonukesItems.AIR_SCRUBBER.get()).getPath())
        .parent(offModel.model);
  }

  @Override
  protected void registerStatesAndModels() {
    cubeAll(ThermonukesBlocks.RED_MATTER_EXPLOSIVE.get(), modLoc("block/red_matter_explosive"));
    cubeColumn(ThermonukesBlocks.ANTIMATTER_EXPLOSIVE.get(), modLoc("block/antimatter_explosive_side"), modLoc("block/antimatter_explosive_end"));
    cubeColumn(ThermonukesBlocks.NUKE.get(), modLoc("block/nuke_side"), modLoc("block/nuke_end"));
    registerAirScrubber();

    getVariantBuilder(ThermonukesBlocks.CLEANSING_WATER.get())
        .partialState()
        .setModels(new ConfiguredModel(models().getBuilder("block/cleansing_water").texture("particle", mcLoc("block/water_still"))));
  }

}
