package me.whizvox.thermonukes.client;

import me.whizvox.thermonukes.Thermonukes;
import me.whizvox.thermonukes.client.render.entity.PrimedExplosiveRenderer;
import me.whizvox.thermonukes.client.screen.container.AirScrubberContainerScreen;
import me.whizvox.thermonukes.common.block.ExplosiveBlock;
import me.whizvox.thermonukes.common.entity.AbstractPrimedExplosive;
import me.whizvox.thermonukes.common.item.GeigerCounterItem;
import me.whizvox.thermonukes.common.lib.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ThermonukesClientEvents {

  private static <T extends AbstractPrimedExplosive> void registerPrimedExplosiveRenderer(RegistryObject<? extends ExplosiveBlock> block, RegistryObject<EntityType<T>> entityType) {
    EntityRenderers.register(entityType.get(), context -> new PrimedExplosiveRenderer(context, block.get().defaultBlockState()));
  }

  public static void register(IEventBus bus) {
    bus.addListener(ThermonukesClientEvents::onClientSetup);
  }

  private static void registerItemProperty(Supplier<? extends Item> itemSup, String name, ItemPropertyFunction func) {
    ItemProperties.register(itemSup.get(), new ResourceLocation(Thermonukes.MOD_ID, name), func);
  }

  private static void onClientSetup(final FMLClientSetupEvent event) {
    registerPrimedExplosiveRenderer(ThermonukesBlocks.ANTIMATTER_EXPLOSIVE, ThermonukesEntities.PRIMED_ANTIMATTER_EXPLOSIVE);
    registerPrimedExplosiveRenderer(ThermonukesBlocks.RED_MATTER_EXPLOSIVE, ThermonukesEntities.PRIMED_RED_MATTER_EXPLOSIVE);
    registerPrimedExplosiveRenderer(ThermonukesBlocks.NUKE, ThermonukesEntities.PRIMED_NUKE);

    EntityRenderers.register(ThermonukesEntities.BLACK_HOLE.get(), NoopRenderer::new);

    ItemBlockRenderTypes.setRenderLayer(ThermonukesFluids.CLEANSING_WATER_SOURCE.get(), RenderType.translucent());
    ItemBlockRenderTypes.setRenderLayer(ThermonukesFluids.CLEANSING_WATER_FLOWING.get(), RenderType.translucent());

    registerItemProperty(ThermonukesItems.GEIGER_COUNTER, "reading", (stack, level, entity, seed) ->
        GeigerCounterItem.readLevel(stack).propValue
    );

    event.enqueueWork(() -> {
      MenuScreens.register(ThermonukesMenus.AIR_SCRUBBER.get(), AirScrubberContainerScreen::new);
    });
  }

}
