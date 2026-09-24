package lommie.onlycraftonce;

import lommie.onlycraftonce.platform.NeoForgeNetworkPacketRegister;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.function.BiConsumer;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class RegisterEvents {
    @SubscribeEvent
    public static <P extends CustomPacketPayload> void register(RegisterPayloadHandlersEvent event){
        final PayloadRegistrar registrar = event.registrar("1");
        NeoForgeNetworkPacketRegister.deferredServerboundRegistrations.forEach((packet) ->{
            registerPacket(registrar,packet.type(),packet.codec(),packet.handler());
        });


    }

    private static <T extends CustomPacketPayload, Y extends CustomPacketPayload.Type<T>, U extends StreamCodec<? super RegistryFriendlyByteBuf, T>> void registerPacket(PayloadRegistrar registrar, CustomPacketPayload.Type<? extends CustomPacketPayload> type, StreamCodec<RegistryFriendlyByteBuf,? extends CustomPacketPayload> codec, BiConsumer<? extends CustomPacketPayload, ServerPlayer> handler) {
        registrar.playToServer(((Y) (Object) type), ((U) (Object) codec),
                (p,c) -> {
                    ((BiConsumer<T,ServerPlayer>) (Object) handler).accept((T) p,((ServerPlayer) c.player()));
                });
    }
}
