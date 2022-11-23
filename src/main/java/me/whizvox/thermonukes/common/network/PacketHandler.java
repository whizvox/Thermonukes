package me.whizvox.thermonukes.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public interface PacketHandler<T> {

  Class<T> getType();

  void encode(T obj, FriendlyByteBuf buf);

  T decode(FriendlyByteBuf buf);

  void handle(T obj, NetworkEvent.Context ctx);

}
