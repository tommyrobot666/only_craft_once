package lommie.onlycraftonce.packet;

import lommie.onlycraftonce.platform.Services;

public class ModPackets {
    public static void registerServer(){
        Services.NETWORKING.registerServerbound(ServerboundUpdateConfigPacket.TYPE,ServerboundUpdateConfigPacket.CODEC,ServerboundUpdateConfigPacket::handle);
        Services.NETWORKING.registerServerbound(ServerboundRequestCurrentConfigPacket.TYPE,ServerboundRequestCurrentConfigPacket.CODEC,ServerboundRequestCurrentConfigPacket::handle);
    }

    public static void registerClient(){
        Services.NETWORKING.registerClientbound(ClientboundCurrentConfigPacket.TYPE,ClientboundCurrentConfigPacket.CODEC,ClientboundCurrentConfigPacket::handle);
    }
}
