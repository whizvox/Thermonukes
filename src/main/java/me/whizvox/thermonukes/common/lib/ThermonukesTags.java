package me.whizvox.thermonukes.common.lib;

import me.whizvox.thermonukes.Thermonukes;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ThermonukesTags {

  private static <T> TagKey<T> modTag(ResourceKey<Registry<T>> registry, String name) {
    return TagKey.create(registry, Thermonukes.modLoc(name));
  }

  private static TagKey<Item> modItemTag(String name) {
    return modTag(Registry.ITEM_REGISTRY, name);
  }

  public static final class Items {
    private Items() {}

    public static final TagKey<Item>
        HAZMAT_SUIT = modItemTag("hazmat_suit");
  }

}
