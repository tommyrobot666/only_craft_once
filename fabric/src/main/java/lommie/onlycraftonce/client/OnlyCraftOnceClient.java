package lommie.onlycraftonce.client;

import lommie.onlycraftonce.CommonClass;
import lommie.onlycraftonce.packet.ModPackets;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class OnlyCraftOnceClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        if (FabricLoader.getInstance().isModLoaded(CommonClass.YACL_MODID)){
            ModPackets.registerClient();
        }
    }
}
