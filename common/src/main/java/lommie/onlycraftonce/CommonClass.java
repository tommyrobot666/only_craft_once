package lommie.onlycraftonce;

import lommie.onlycraftonce.packet.ModPackets;
import lommie.onlycraftonce.platform.Services;

public class CommonClass {

    public static void init() {
        if (Services.PLATFORM.isModLoaded("only_craft_once")) {
            Constants.LOG.info("YOU WILL NEVER CRAFT AGAIN");
        }

        Config.tryToLoadConfigAndHandleErrors();

        if (Services.PLATFORM.isModLoaded(Constants.YACL_MODID)){
            ModPackets.registerServer();
        }
    }


}
