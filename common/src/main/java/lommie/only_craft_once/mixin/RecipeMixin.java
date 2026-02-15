package lommie.only_craft_once.mixin;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Recipe.class)
public interface RecipeMixin<T extends RecipeInput>{// extends NotRecipeMixin<T> {// extends Recipe<@NotNull T>{

//    @Redirect(method = "matches",
//    at = @At(value = "INVOKE",
//            target = "Lnet/minecraft/world/item/crafting/Recipe;matches(Lnet/minecraft/world/item/crafting/RecipeInput;Lnet/minecraft/world/level/Level;)Z"))
//    boolean mat(T input, Level level);

//    /**
//     * @author
//     * @reason
//     */
//    @Overwrite
//    boolean matches(RecipeInput input, Level level);

//    {
//        Constants.LOG.error(input.toString());
//        Constants.LOG.error(level.toString());
//        return false;
//    }
//
//    @Redirect(method = "matches", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/Recipe;matches(Lnet/minecraft/world/item/crafting/RecipeInput;Lnet/minecraft/world/level/Level;)Z"))
//    default boolean mat() {
//        return false;
//    }
}
