package lommie.onlycraftonce;

import lommie.onlycraftonce.platform.NeoForgeNetworkPacketRegister;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class RegisterEvents {
    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event){
        // fix neoforge dedicated server not recognizing clientbound packets (solution credit at: https://forums.minecraftforge.net/topic/89019-1161solved-sending-packets-to-client-from-dedicated-server/ comment: first comment by Novârch)
        // https://neoforged.net/news/20.4networking-rework/
        final PayloadRegistrar registrar = event.registrar("1").optional();
        NeoForgeNetworkPacketRegister.deferredServerboundRegistrations.forEach((packet) ->{
            registerPacket(registrar,packet.type(),packet.codec(),packet.handler());
        });
        NeoForgeNetworkPacketRegister.deferredClientboundRegistrations.forEach((packet) -> {
            registerClientPacket(registrar, packet.type(), packet.codec());
        });
    }

    private static <T extends CustomPacketPayload, Y extends CustomPacketPayload.Type<T>, U extends StreamCodec<? super RegistryFriendlyByteBuf, T>> void registerPacket(PayloadRegistrar registrar, CustomPacketPayload.Type<? extends CustomPacketPayload> type, StreamCodec<RegistryFriendlyByteBuf,? extends CustomPacketPayload> codec, BiConsumer<? extends CustomPacketPayload, ServerPlayer> handler) {
        registrar.playToServer(((Y) type), ((U) codec),
                (p,c) -> {
                    ((BiConsumer<T,ServerPlayer>) handler).accept((T) p,((ServerPlayer) c.player()));
                });
    }

    private static <T extends CustomPacketPayload, Y extends CustomPacketPayload.Type<T>, U extends StreamCodec<? super RegistryFriendlyByteBuf, T>> void registerClientPacket(PayloadRegistrar registrar, CustomPacketPayload.Type<? extends CustomPacketPayload> type, StreamCodec<RegistryFriendlyByteBuf,? extends CustomPacketPayload> codec) {
        registrar.playToClient(((Y) type), ((U) codec));
    }
}
