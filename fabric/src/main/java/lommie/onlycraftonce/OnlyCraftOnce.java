package lommie.onlycraftonce;

import lommie.onlycraftonce.packet.ModPackets;
import lommie.onlycraftonce.platform.Services;
import net.fabricmc.api.ModInitializer;

public class OnlyCraftOnce implements ModInitializer {

    @Override
    public void onInitialize() {
        CommonClass.init();

        if (Services.PLATFORM.isModLoaded(CommonClass.YACL_MODID)){
            ModPackets.registerServer();
        }
    }
}
