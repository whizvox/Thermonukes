package me.whizvox.thermonukes.data.client;

import me.whizvox.thermonukes.Thermonukes;
import me.whizvox.thermonukes.common.item.GeigerCounterItem;
import me.whizvox.thermonukes.common.lib.ThermonukesItems;
import me.whizvox.thermonukes.common.util.GeigerCounterLevel;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ThermonukesItemModelProvider extends ItemModelProvider {

  public ThermonukesItemModelProvider(DataGenerator gen, ExistingFileHelper existingFileHelper) {
    super(gen, Thermonukes.MOD_ID, existingFileHelper);
  }

  private ModelFile modModel(String name) {
    return new ModelFile.UncheckedModelFile(modLoc(name));
  }

  private ModelFile existingModel(ResourceLocation name) {
    return new ModelFile.ExistingModelFile(name, existingFileHelper);
  }

  private ModelFile generatedModel() {
    return existingModel(mcLoc("item/generated"));
  }

  private ModelFile handheldModel() {
    return existingModel(mcLoc("item/handheld"));
  }

  private void basicItem(Supplier<? extends Item> itemSup, String parent) {
    ResourceLocation name = ForgeRegistries.ITEMS.getKey(itemSup.get());
    getBuilder(name.toString())
        .parent(new ModelFile.UncheckedModelFile(parent))
        .texture("layer0", new ResourceLocation(name.getNamespace(), "item/" + name.getPath()));
  }

  private void simple(ResourceLocation location) {
    getBuilder(location.toString())
        .parent(generatedModel())
        .texture("layer0", location);
  }

  private void basicItem(Supplier<? extends Item> itemOp) {
    basicItem(itemOp, "item/generated");
  }

  private void basicHandheldItem(Supplier<? extends Item> itemOp) {
    basicItem(itemOp, "item/handheld");
  }

  private void geigerCounter() {
    for (GeigerCounterLevel value : GeigerCounterLevel.values()) {
      simple(modLoc("item/geiger_counter/" + value.toString().toLowerCase()));
    }

    GeigerCounterItem item = ThermonukesItems.GEIGER_COUNTER.get();
    ResourceLocation name = ForgeRegistries.ITEMS.getKey(item);
    ResourceLocation reading = modLoc("reading");
    ItemModelBuilder builder = getBuilder(name.toString())
        .parent(generatedModel())
        .texture("layer0", modLoc("item/geiger_counter/none"));
    for (GeigerCounterLevel value : GeigerCounterLevel.values()) {
      builder.override()
          .predicate(reading, value.propValue)
          .model(modModel("item/geiger_counter/" + value.toString().toLowerCase()))
          .end();
    }
  }

  @Override
  protected void registerModels() {
    geigerCounter();
    basicItem(ThermonukesItems.SALTY_MIXTURE_TUBE);
    basicItem(ThermonukesItems.CLEANSING_SALT);
    basicItem(ThermonukesItems.HAZMAT_SUIT_HEADPIECE);
    basicItem(ThermonukesItems.HAZMAT_SUIT_COAT);
    basicItem(ThermonukesItems.HAZMAT_SUIT_LEGGINGS);
    basicItem(ThermonukesItems.RUBBER_BOOTS);
    basicItem(ThermonukesItems.GAS_MASK);
    //basicItem(ThermonukesItems.ANTI_RADIATIVE_FABRIC_SHEET);
    //basicItem(ThermonukesItems.RUBBER_SHEET);
    getBuilder("item/cleansing_water_bucket")
        .parent(generatedModel())
        .texture("layer0", mcLoc("item/bucket"))
        .texture("layer1", "item/cleansing_water_bucket_fluid");
  }

}
