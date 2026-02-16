package lommie.onlycraftonce.platform;

import lommie.onlycraftonce.Constants;
import lommie.onlycraftonce.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public Path configFile() {
        return FabricLoader.getInstance().getConfigDir().resolve(Constants.MOD_ID+".json");
    }
}
