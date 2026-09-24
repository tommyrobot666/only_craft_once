package lommie.onlycraftonce.packet;

import lommie.onlycraftonce.CommonClass;
import lommie.onlycraftonce.Config;
import lommie.onlycraftonce.Constants;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;

public record ServerboundUpdateConfigPacket(HashMap<Item,Integer> changed_entries) implements CustomPacketPayload {
    public static final Type<ServerboundUpdateConfigPacket> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Constants.MOD_ID,"update_config"));
    public static final StreamCodec<RegistryFriendlyByteBuf,ServerboundUpdateConfigPacket> CODEC = StreamCodec.composite(
            Config.CONFIG_STREAM_CODEC,
            ServerboundUpdateConfigPacket::changed_entries,
            ServerboundUpdateConfigPacket::new);

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ServerboundUpdateConfigPacket packet, ServerPlayer player){
        // does player have permission "modid.change_config"?
        // update entries
    }
}
