package lommie.onlycraftonce;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import lommie.onlycraftonce.platform.Services;
import lommie.onlycraftonce.yacl.YACLScreen;

public class ModMenuApiImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (!Services.PLATFORM.isModLoaded(Constants.YACL_MODID)) {
            Constants.LOG.warn("YACL is needed to generate config screen");
            return ModMenuApi.super.getModConfigScreenFactory();
        }

        return YACLScreen::generateScreen;
    }
}
