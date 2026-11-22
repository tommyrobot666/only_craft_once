package lommie.onlycraftonce;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import lommie.onlycraftonce.platform.Services;
import lommie.onlycraftonce.yacl.YACLScreen;

import java.util.Map;

public class ModMenuApiImpl implements ModMenuApi {
    @Override
    public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
        if (!Services.PLATFORM.isModLoaded(Constants.YACL_MODID)) {
            Constants.LOG.warn("YACL is needed to generate config screen");
            return Map.of();
        }

        return Map.of(Constants.MOD_ID, YACLScreen::generateScreen);
    }

}
