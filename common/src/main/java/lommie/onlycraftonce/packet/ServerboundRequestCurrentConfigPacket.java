package lommie.onlycraftonce.packet;

import lommie.onlycraftonce.Config;
import lommie.onlycraftonce.Constants;
import lommie.onlycraftonce.platform.Services;
import lommie.onlycraftonce.server.ModPermissions;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import org.jspecify.annotations.NonNull;

public record ServerboundRequestCurrentConfigPacket() implements CustomPacketPayload {
    public static final Type<ServerboundRequestCurrentConfigPacket> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Constants.MOD_ID,"request_config"));
    public static final StreamCodec<RegistryFriendlyByteBuf,ServerboundRequestCurrentConfigPacket> CODEC = StreamCodec.unit(new ServerboundRequestCurrentConfigPacket());

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ServerboundRequestCurrentConfigPacket packet, ServerPlayer player){
        // does player have permission "modid.view_config"?
        if (!player.permissions().hasPermission(ModPermissions.VIEW_CONFIG)) {
            player.sendSystemMessage(Component.translatableWithFallback(Constants.MOD_ID+".no_permissions.view_config",Constants.NO_PERMISSIONS_VIEW_CONFIG).withColor(TextColor.RED));
            return;
        }
        // send ClientboundCurrentConfigPacket
        Services.NETWORKING.sendClientbound(new ClientboundCurrentConfigPacket(Config.maxTimesCrafted),player);
    }
}
