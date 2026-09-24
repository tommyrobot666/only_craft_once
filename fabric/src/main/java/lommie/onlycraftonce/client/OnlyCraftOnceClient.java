package lommie.onlycraftonce.client;

import lommie.onlycraftonce.CommonClientClass;
import net.fabricmc.api.ClientModInitializer;

public class OnlyCraftOnceClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CommonClientClass.init();
    }
}
