package lommie.onlycraftonce.mixin;

import lommie.onlycraftonce.CommonClass;
import lommie.onlycraftonce.Constants;
import lommie.onlycraftonce.saveddata.TimesCraftedSavedData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    @Shadow private RecipeMap recipes;

    @Inject(
            method = "getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/item/crafting/RecipeInput;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/crafting/RecipeHolder;)Ljava/util/Optional;",
            at = @At("HEAD"),
            cancellable = true)
    <I extends RecipeInput, T extends Recipe<@NotNull I>>
    void checkIfCraftedBefore(RecipeType<@NotNull T> recipeType, I input, Level level, @Nullable RecipeHolder<@NotNull T> lastRecipe, CallbackInfoReturnable<Optional<RecipeHolder<@NotNull T>>> cir){
        if (lastRecipe == null || level.isClientSide()) return;
        ItemStack result = lastRecipe.value().assemble(input,level.registryAccess());
        TimesCraftedSavedData savedData = ((ServerLevel) level).getDataStorage().computeIfAbsent(TimesCraftedSavedData.TYPE);
        for (Item item : CommonClass.maxTimesCrafted.keySet()) {
            Constants.LOG.error(item.toString());
            if (result.is(item)){
                Constants.LOG.error(String.valueOf(result.getCount() + savedData.map.getOrDefault("minecraft.crafted:"+item.toString().replace(':','.'),0)));
                Constants.LOG.error(String.valueOf(result.getCount() + savedData.map.getOrDefault("minecraft.crafted:"+item.toString().replace(':','.'),0) > CommonClass.maxTimesCrafted.get(item)));
                Constants.LOG.error(String.valueOf(savedData.map.getOrDefault("minecraft.crafted:"+item.toString().replace(':','.'),0)));
                if (result.getCount() + savedData.map.getOrDefault("minecraft.crafted:"+item.toString().replace(':','.'),0) > CommonClass.maxTimesCrafted.get(item)){
                    cir.setReturnValue(Optional.empty());
                    cir.cancel();
                    return;
                }
            }
        }
    }

    @Inject(
            method = "getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/item/crafting/RecipeInput;Lnet/minecraft/world/level/Level;Lnet/minecraft/resources/ResourceKey;)Ljava/util/Optional;",
            at = @At("HEAD"),
            cancellable = true)
    <I extends RecipeInput, T extends Recipe<@NotNull I>>
    void checkIfCraftedBefore2(RecipeType<T> recipeType, I input, Level level, @Nullable ResourceKey<Recipe<?>> recipe, CallbackInfoReturnable<Optional<RecipeHolder<T>>> cir){
//        checkIfCraftedBefore(recipeType,input,level,recipe==null?null:new RecipeHolder<>(recipe, (T) level.registryAccess().getOrThrow(recipe).value()),cir);
        checkIfCraftedBefore2(recipeType,input,level,cir);
    }

    @Inject(
            method = "getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/item/crafting/RecipeInput;Lnet/minecraft/world/level/Level;)Ljava/util/Optional;",
            at = @At("HEAD"),
            cancellable = true)
    <I extends RecipeInput, T extends Recipe<@NotNull I>>
    void checkIfCraftedBefore2(RecipeType<T> recipeType, I input, Level level, CallbackInfoReturnable<Optional<RecipeHolder<T>>> cir){
        Optional<RecipeHolder<T>> recipeOpt = this.recipes.getRecipesFor(recipeType,input,level).findFirst();
        if (recipeOpt.isEmpty()){
            cir.setReturnValue(Optional.empty());
            cir.cancel();
        }

        checkIfCraftedBefore(recipeType,input,level,recipeOpt.orElseThrow(),cir);
    }
}
