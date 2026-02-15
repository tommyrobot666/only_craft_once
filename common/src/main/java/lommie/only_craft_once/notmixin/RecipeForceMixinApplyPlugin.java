package lommie.only_craft_once.notmixin;

import lommie.only_craft_once.Constants;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.objectweb.asm.tree.ClassNode;

public class RecipeForceMixinApplyPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String s) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String s, String s1) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> set, Set<String> set1) {

    }

    @Override
    public List<String> getMixins() {
        List<String> mixins = getRecipeExtenders().map((c) -> "lommie.only_craft_once.mixin.RecipeMixin$"+c.getName()).toList();
        Constants.LOG.error(mixins.toString());
        return mixins;
    }

    private static Stream<? extends Class<?>> getRecipeExtenders() {
        Class<?> recipeClass;
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try {
            recipeClass = classLoader.loadClass("net.minecraft.world.item.crafting.Recipe");
        } catch (ClassNotFoundException e) {
            try {
                recipeClass = classLoader.loadClass("net.minecraft.class_1860");
            } catch (ClassNotFoundException ex) {
                try {
                    recipeClass = classLoader.loadClass("net.minecraft.recipe.Recipe");
                } catch (ClassNotFoundException exc) {
                    try {
                        recipeClass = classLoader.loadClass("dqs");
                    } catch (ClassNotFoundException classNotFoundException) {
                        throw new RuntimeException(classNotFoundException);
                    }
                }
            }
        }
        final Class<?> finalRecipeClass = recipeClass;
//        classLoader.getResourceAsStream("")

        return classLoader.resources("").filter(u -> {
            Constants.LOG.error(u.getFile());
            Constants.LOG.error(u.toString());
            return u.getFile().endsWith("Recipe.class");
        }).map((u) -> {
            try {
                return classLoader.loadClass(u.getFile());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });//.filter(c -> c.isInstance(finalRecipeClass));
    }

    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }

    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {
//        if (classNode.interfaces.contains("net.minecraft.world.item.crafting.Recipe"))
//        classNode.module.
    }
}
