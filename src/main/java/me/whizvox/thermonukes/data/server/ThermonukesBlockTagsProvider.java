package me.whizvox.thermonukes.data.server;

import me.whizvox.thermonukes.Thermonukes;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ThermonukesBlockTagsProvider extends BlockTagsProvider {

  public ThermonukesBlockTagsProvider(DataGenerator gen, @Nullable ExistingFileHelper existingFileHelper) {
    super(gen, Thermonukes.MOD_ID, existingFileHelper);
  }

  @Override
  protected void addTags() {

  }

}
