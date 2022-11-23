package me.whizvox.thermonukes.common.network;

import me.whizvox.thermonukes.Thermonukes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class ThermonukesNetwork {

  private static final String PROTOCOL_VERSION = "1";

  private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
      new ResourceLocation(Thermonukes.MOD_ID, "main"),
      () -> PROTOCOL_VERSION,
      PROTOCOL_VERSION::equals,
      PROTOCOL_VERSION::equals
  );

  private static <T> void register(int id, PacketHandler<T> handler, NetworkDirection direction) {
    CHANNEL.registerMessage(id, handler.getType(), handler::encode, handler::decode, (obj, ctxSupplier) -> {
      NetworkEvent.Context ctx = ctxSupplier.get();
      ctx.enqueueWork(() -> handler.handle(obj, ctx));
      ctx.setPacketHandled(true);
    }, Optional.ofNullable(direction));
  }

  public static void register() {
    int id = 1;
  }

  public static void broadcastToClients(Object msg) {
    CHANNEL.send(PacketDistributor.ALL.noArg(), msg);
  }

  public static void sendToClient(Player player, Object msg) {
    CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), msg);
  }

  public static void sendToServer(Object msg) {
    CHANNEL.sendToServer(msg);
  }

}
