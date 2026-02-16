package lommie.onlycraftonce.platform;

import lommie.onlycraftonce.Constants;
import lommie.onlycraftonce.platform.services.IPlatformHelper;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;

import java.nio.file.Path;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.getCurrent().isProduction();
    }

    @Override
    public Path configFile() {
        return FMLLoader.getCurrent().getGameDir().resolve("config").resolve(Constants.MOD_ID+".json");
    }
}
