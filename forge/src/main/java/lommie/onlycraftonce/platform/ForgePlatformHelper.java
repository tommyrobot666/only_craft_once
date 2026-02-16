package lommie.onlycraftonce.platform;

import lommie.onlycraftonce.Constants;
import lommie.onlycraftonce.platform.services.IPlatformHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

import java.nio.file.Path;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public Path configFile() {
        return FMLLoader.getGamePath().resolve("config").resolve(Constants.MOD_ID+".json");
    }
}
