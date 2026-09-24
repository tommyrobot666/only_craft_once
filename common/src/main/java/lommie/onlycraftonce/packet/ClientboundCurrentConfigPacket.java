package lommie.onlycraftonce.packet;


import lommie.onlycraftonce.CommonClass;
import lommie.onlycraftonce.Config;
import lommie.onlycraftonce.Constants;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;

public record ClientboundCurrentConfigPacket(HashMap<Item,Integer> config) implements CustomPacketPayload {
    public static final Type<ClientboundCurrentConfigPacket> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Constants.MOD_ID,"current_config"));
    public static final StreamCodec<RegistryFriendlyByteBuf,ClientboundCurrentConfigPacket> CODEC = StreamCodec.composite(
            Config.CONFIG_STREAM_CODEC,
            ClientboundCurrentConfigPacket::config,
            ClientboundCurrentConfigPacket::new);

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ClientboundCurrentConfigPacket packet){
        // send data to yacl screen
    }
}
