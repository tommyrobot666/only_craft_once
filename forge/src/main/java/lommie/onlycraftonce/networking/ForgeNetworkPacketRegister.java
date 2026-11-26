package lommie.onlycraftonce.networking;

import lommie.onlycraftonce.platform.services.INetworkPacketRegister;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ForgeNetworkPacketRegister implements INetworkPacketRegister {
    @Override
    public <P extends CustomPacketPayload> void registerServerbound(CustomPacketPayload.Type<P> type, StreamCodec<RegistryFriendlyByteBuf, P> codec, BiConsumer<P, ServerPlayer> handler, Class<P> pClass) {
        ModChannel.SERVERBOUND.messageBuilder(pClass).consumerNetworkThread((p,ctx) -> {
            handler.accept(p,ctx.getSender());
        });
    }

    @Override
    public <P extends CustomPacketPayload> void registerClientbound(CustomPacketPayload.Type<P> type, StreamCodec<RegistryFriendlyByteBuf, P> codec, Consumer<P> handler, Class<P> pClass) {
        ModChannel.CLIENTBOUND.messageBuilder(pClass).consumerNetworkThread((p,ctx) -> {
            handler.accept(p);
        });
    }

    @Override
    public void sendClientbound(CustomPacketPayload packetPayload, ServerPlayer player) {
        PacketDistributor.PLAYER.with(player).send(new ClientboundCustomPayloadPacket(packetPayload));
    }

    @Override
    public void sendServerbound(CustomPacketPayload packetPayload) {
        PacketDistributor.SERVER.with(null).send(new ServerboundCustomPayloadPacket(packetPayload));
    }
}
