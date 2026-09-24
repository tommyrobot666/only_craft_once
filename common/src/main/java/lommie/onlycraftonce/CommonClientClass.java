package lommie.onlycraftonce;

import lommie.onlycraftonce.packet.ModPackets;
import lommie.onlycraftonce.platform.Services;

public class CommonClientClass {
    public static void init() {
        if (Services.PLATFORM.isModLoaded(Constants.YACL_MODID)){
            ModPackets.registerClient();
        }
    }
}
