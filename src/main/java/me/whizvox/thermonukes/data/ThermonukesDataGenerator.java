package me.whizvox.thermonukes.data;

import me.whizvox.thermonukes.data.client.ThermonukesBlockStateProvider;
import me.whizvox.thermonukes.data.client.ThermonukesItemModelProvider;
import me.whizvox.thermonukes.data.client.ThermonukesLanguageProvider;
import me.whizvox.thermonukes.data.client.ThermonukesSoundProvider;
import me.whizvox.thermonukes.data.server.ThermonukesLootTableProvider;
import me.whizvox.thermonukes.data.server.ThermonukesRecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class ThermonukesDataGenerator {

  public static void register(IEventBus bus) {
    bus.addListener(ThermonukesDataGenerator::onGatherData);
  }

  private static void onGatherData(final GatherDataEvent event) {
    DataGenerator gen = event.getGenerator();
    ExistingFileHelper fileHelper = event.getExistingFileHelper();
    boolean includeClient = event.includeClient();
    boolean includeServer = event.includeServer();

    gen.addProvider(includeClient, new ThermonukesSoundProvider(gen, fileHelper));
    gen.addProvider(includeClient, new ThermonukesBlockStateProvider(gen, fileHelper));
    gen.addProvider(includeClient, new ThermonukesItemModelProvider(gen, fileHelper));
    gen.addProvider(includeClient, new ThermonukesLanguageProvider(gen));
    gen.addProvider(includeServer, new ThermonukesRecipeProvider(gen));
    gen.addProvider(includeServer, new ThermonukesLootTableProvider(gen));
  }

}
