package lommie.onlycraftonce.client;

import lommie.onlycraftonce.Constants;
import lommie.onlycraftonce.platform.NeoForgeNetworkPacketRegister;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;

import java.util.function.Consumer;

@EventBusSubscriber(value = Dist.CLIENT, modid = Constants.MOD_ID)
public class ClientRegisterEvents {
    @SubscribeEvent
    public static void register(RegisterClientPayloadHandlersEvent event){
        NeoForgeNetworkPacketRegister.deferredClientboundRegistrations.forEach(
                (packet) -> registerPacket(event,packet.type(),packet.handler())
        );
    }

    static <T> void registerPacket(RegisterClientPayloadHandlersEvent event, CustomPacketPayload.Type<? extends CustomPacketPayload> type, Consumer<? extends CustomPacketPayload> handler) {
        event.register(type, (p,c) -> ((Consumer<T>) handler).accept(((T) p)));
    }
}
