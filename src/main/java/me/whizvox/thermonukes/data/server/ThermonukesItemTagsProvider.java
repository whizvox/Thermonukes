package me.whizvox.thermonukes.data.server;

import me.whizvox.thermonukes.Thermonukes;
import me.whizvox.thermonukes.common.lib.ThermonukesItems;
import me.whizvox.thermonukes.common.lib.ThermonukesTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ThermonukesItemTagsProvider extends ItemTagsProvider {

  public ThermonukesItemTagsProvider(DataGenerator gen, BlockTagsProvider blockTagsProv, @Nullable ExistingFileHelper existingFileHelper) {
    super(gen, blockTagsProv, Thermonukes.MOD_ID, existingFileHelper);
  }

  @Override
  protected void addTags() {
    tag(ThermonukesTags.Items.HAZMAT_SUIT).add(
        ThermonukesItems.HAZMAT_SUIT_HEADPIECE.get(),
        ThermonukesItems.HAZMAT_SUIT_COAT.get(),
        ThermonukesItems.HAZMAT_SUIT_LEGGINGS.get(),
        ThermonukesItems.RUBBER_BOOTS.get()
    );
  }

}
