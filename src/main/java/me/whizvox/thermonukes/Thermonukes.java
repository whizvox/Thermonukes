package me.whizvox.thermonukes;

import com.mojang.logging.LogUtils;
import me.whizvox.thermonukes.client.ThermonukesClientEvents;
import me.whizvox.thermonukes.common.event.ThermonukesWorldEvents;
import me.whizvox.thermonukes.common.lib.*;
import me.whizvox.thermonukes.common.network.ThermonukesNetwork;
import me.whizvox.thermonukes.data.ThermonukesDataGenerator;
import me.whizvox.thermonukes.server.command.ThermonukesCommands;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Thermonukes.MOD_ID)
public class Thermonukes {

  public static final String MOD_ID = "thermonukes";

  private static final Logger LOGGER = LogUtils.getLogger();

  public Thermonukes() {
    final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
    final IEventBus forgeBus = MinecraftForge.EVENT_BUS;

    ThermonukesNetwork.register();
    ThermonukesBlocks.register(modBus);
    ThermonukesBlockEntities.register(modBus);
    ThermonukesItems.register(modBus);
    ThermonukesEntities.register(modBus);
    ThermonukesFluids.register(modBus);
    ThermonukesFluidTypes.register(modBus);
    ThermonukesClientEvents.register(modBus);
    ThermonukesSounds.register(modBus);
    ThermonukesCapabilities.register(modBus, forgeBus);
    ThermonukesDataGenerator.register(modBus);
    ThermonukesMenus.register(modBus);

    ThermonukesCommands.register(forgeBus);
    ThermonukesWorldEvents.register(forgeBus);
  }

  public static ResourceLocation modLoc(String path) {
    return new ResourceLocation(MOD_ID, path);
  }

}
