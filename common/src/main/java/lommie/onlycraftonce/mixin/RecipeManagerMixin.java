package lommie.onlycraftonce.mixin;

import lommie.onlycraftonce.Config;
import lommie.onlycraftonce.saveddata.TimesCraftedSavedData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin {
    @Inject(
            method = "getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/item/crafting/RecipeInput;Lnet/minecraft/world/level/Level;)Ljava/util/Optional;",
            at = @At("RETURN"),
            cancellable = true)
    <I extends RecipeInput, T extends Recipe<@NotNull I>>
    void checkIfCraftedBefore(RecipeType<T> recipeType, I input, Level level, CallbackInfoReturnable<Optional<RecipeHolder<T>>> cir){
        if (cir.getReturnValue().isEmpty()) return;
        var recipeHolder = cir.getReturnValue().get();
        ItemStack result = recipeHolder.value().assemble(input,level.registryAccess());
        Item item = result.getItem();
        TimesCraftedSavedData savedData = TimesCraftedSavedData.get(((ServerLevel) level));
        //TODO I should change this to just item.toString(), but that would break compatibility with v1.0
        String key = "minecraft.crafted:"+item.toString().replace(':','.');
        if (!savedData.map.containsKey(key)) return;
        if (savedData.map.get(key) + result.getCount() > Config.maxTimesCrafted.get(item)) {
            cir.setReturnValue(Optional.empty());
        }
    }

    @Inject(
            method = "Lnet/minecraft/world/item/crafting/RecipeManager;getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/item/crafting/RecipeInput;Lnet/minecraft/world/level/Level;Lnet/minecraft/resources/ResourceLocation;)Ljava/util/Optional;",
            at = @At("RETURN"),
            cancellable = true)
    <I extends RecipeInput, T extends Recipe<@NotNull I>>
    void checkIfCraftedBefore2(RecipeType<T> recipeType, I input, Level level, ResourceLocation lastRecipe, CallbackInfoReturnable<Optional<RecipeHolder<T>>> cir){
        checkIfCraftedBefore(recipeType,input,level,cir);
    }

    @Inject(
            method = "getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/item/crafting/RecipeInput;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/crafting/RecipeHolder;)Ljava/util/Optional;",
            at = @At("RETURN"),
            cancellable = true)
    <I extends RecipeInput, T extends Recipe<@NotNull I>>
    void checkIfCraftedBefore2(RecipeType<T> recipeType, I input, Level level, @Nullable RecipeHolder<T> lastRecipe, CallbackInfoReturnable<Optional<RecipeHolder<T>>> cir){
        checkIfCraftedBefore(recipeType,input,level,cir);
    }
}
