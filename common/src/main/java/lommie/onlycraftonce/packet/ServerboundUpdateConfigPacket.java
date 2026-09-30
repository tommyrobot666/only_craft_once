package lommie.onlycraftonce.packet;

import lommie.onlycraftonce.Config;
import lommie.onlycraftonce.Constants;
import lommie.onlycraftonce.server.ModPermissions;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
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
        if (!player.permissions().hasPermission(ModPermissions.CHANGE_CONFIG)) {
            player.sendSystemMessage(Component.translatableWithFallback(Constants.MOD_ID+".no_permissions.change_config",Constants.NO_PERMISSIONS_CHANGE_CONFIG).withColor(TextColor.RED));
            return;
        }
        // update entries
        for (Item entry : packet.changed_entries.keySet()){
            if (packet.changed_entries.get(entry) < 0)
                Config.maxTimesCrafted.remove(entry);
            else
                Config.maxTimesCrafted.put(entry,packet.changed_entries.get(entry));
        }

        Config.saveConfig();
    }
}
