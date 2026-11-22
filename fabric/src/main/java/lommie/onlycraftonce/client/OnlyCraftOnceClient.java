package lommie.onlycraftonce.client;

import lommie.onlycraftonce.CommonClientClass;
import lommie.onlycraftonce.yacl.YACLScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class OnlyCraftOnceClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CommonClientClass.init();
        ClientPlayConnectionEvents.JOIN.register((a, b, c) -> YACLScreen.onNewServer());
    }
}
