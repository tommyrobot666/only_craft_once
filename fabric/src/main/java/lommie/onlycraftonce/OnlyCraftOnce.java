package lommie.onlycraftonce;

import lommie.onlycraftonce.yacl.YaclState;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class OnlyCraftOnce implements ModInitializer {

    @Override
    public void onInitialize() {
        CommonClass.init();

        ClientPlayConnectionEvents.JOIN.register((a,b,c) -> YaclState.changed = false);
    }
}
