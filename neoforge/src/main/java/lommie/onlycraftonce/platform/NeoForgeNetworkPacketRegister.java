package lommie.onlycraftonce.platform;

import lommie.onlycraftonce.platform.services.INetworkPacketRegister;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.loading.FMLEnvironment;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class NeoForgeNetworkPacketRegister implements INetworkPacketRegister {
    public static final List<DeferredServerbound<? extends CustomPacketPayload>> deferredServerboundRegistrations  = new ArrayList<>();
    public static final List<DeferredClientbound<? extends CustomPacketPayload>> deferredClientboundRegistrations = new ArrayList<>();

    @Override
    public <P extends CustomPacketPayload> void registerServerbound(CustomPacketPayload.Type<P> type, StreamCodec<RegistryFriendlyByteBuf, P> codec, BiConsumer<P, ServerPlayer> handler) {
        deferredServerboundRegistrations.add(new DeferredServerbound<>(type,codec,handler));
    }

    @Override
    public <P extends CustomPacketPayload> void registerClientbound(CustomPacketPayload.Type<P> type, StreamCodec<RegistryFriendlyByteBuf, P> codec, Consumer<P> handler) {
        assert(FMLEnvironment.getDist().isClient());
        deferredClientboundRegistrations.add(new DeferredClientbound<>(type,codec,handler));
    }

    public record DeferredServerbound<P extends CustomPacketPayload>(CustomPacketPayload.Type<P> type, StreamCodec<RegistryFriendlyByteBuf, P> codec, BiConsumer<P, ServerPlayer> handler){

    }

    public record DeferredClientbound<P extends CustomPacketPayload>(CustomPacketPayload.Type<P> type, StreamCodec<RegistryFriendlyByteBuf, P> codec, Consumer<P> handler){

    }
}
