package lommie.onlycraftonce.packet;

import lommie.onlycraftonce.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import org.jspecify.annotations.NonNull;

public record ServerboundRequestCurrentConfigPacket() implements CustomPacketPayload {
    public static final Type<ServerboundRequestCurrentConfigPacket> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Constants.MOD_ID,"request_config"));
    public static final StreamCodec<FriendlyByteBuf,ServerboundRequestCurrentConfigPacket> CODEC = StreamCodec.unit(new ServerboundRequestCurrentConfigPacket());

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ServerboundRequestCurrentConfigPacket packet, ServerPlayer player){
        // does player have permission "modid.view_config"?
        // send ClientboundCurrentConfigPacket
    }
}
