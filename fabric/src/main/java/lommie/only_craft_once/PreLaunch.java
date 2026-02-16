package lommie.only_craft_once;

import com.chocohead.mm.api.ClassTinkerers;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import lommie.only_craft_once.asm.RecipeTransformation;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.fabricmc.loader.impl.launch.FabricLauncher;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;
import net.minecraft.world.item.crafting.Recipe;

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

        ClassGraph classGraph = new ClassGraph()
                .addClassLoader(ClassLoader.getSystemClassLoader())
                .enableClassInfo()
                .ignoreClassVisibility()
                .acceptPackages("*");

        try (ScanResult scan = classGraph.scan(5)) {
            for (ClassInfo classInfo : scan.getClassesImplementing(Recipe.class)) {
                Constants.LOG.error(classInfo.toString());
                ClassTinkerers.addTransformation(classGraph.getClasspath(), RecipeTransformation::transformClass);
            }
        }
    }
}
