package me.whizvox.thermonukes.server.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class ThermonukesCommands {

  public static void register(IEventBus bus) {
    bus.addListener((RegisterCommandsEvent event) -> {
      CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
      ThermonukesCommand.register(dispatcher);
    });
  }

}
