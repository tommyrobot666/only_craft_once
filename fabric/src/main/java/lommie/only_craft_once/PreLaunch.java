package lommie.only_craft_once;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;

public class PreLaunch implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        try {
            ClassLoader classLoader = FabricLauncherBase.getLauncher().getTargetClassLoader();
            Class<?> knotLoader = Class.forName("net.fabricmc.loader.impl.launch.knot.KnotClassLoader", false, classLoader);
            if (knotLoader.isInstance(classLoader)){
                Constants.LOG.info("found KnotClassLoader");
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
