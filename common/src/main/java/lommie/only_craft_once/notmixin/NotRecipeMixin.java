package lommie.only_craft_once.notmixin;

import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;

public interface NotRecipeMixin<T extends RecipeInput> {
    default boolean matches(T input, Level level){
        return false;
    }
}
