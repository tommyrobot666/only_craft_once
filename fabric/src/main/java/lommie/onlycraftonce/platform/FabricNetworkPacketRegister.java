package lommie.onlycraftonce.platform;

import lommie.onlycraftonce.platform.services.INetworkPacketRegister;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class FabricNetworkPacketRegister implements INetworkPacketRegister {
//    @Environment(EnvType.SERVER)
    @Override
    public <P extends CustomPacketPayload> void registerServerbound(CustomPacketPayload.Type<P> type, StreamCodec<RegistryFriendlyByteBuf, P> codec, BiConsumer<P, ServerPlayer> handler) {
        PayloadTypeRegistry.serverboundPlay().register(type,codec);
        ServerPlayNetworking.registerGlobalReceiver(type, (p,c) -> handler.accept(p,c.player()));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public <P extends CustomPacketPayload> void registerClientbound(CustomPacketPayload.Type<P> type, StreamCodec<RegistryFriendlyByteBuf, P> codec, Consumer<P> handler) {
        PayloadTypeRegistry.clientboundPlay().register(type,codec);
        ClientPlayNetworking.registerGlobalReceiver(type, (p,c) -> handler.accept(p));
    }
}
