package lommie.onlycraftonce.platform.services;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface INetworkPacketRegister {
    <P extends CustomPacketPayload> void registerServerbound(CustomPacketPayload.Type<P> type, StreamCodec<RegistryFriendlyByteBuf,P> codec, BiConsumer<P, ServerPlayer> handler);
    <P extends CustomPacketPayload> void registerClientbound(CustomPacketPayload.Type<P> type, StreamCodec<RegistryFriendlyByteBuf,P> codec, Consumer<P> handler);
}