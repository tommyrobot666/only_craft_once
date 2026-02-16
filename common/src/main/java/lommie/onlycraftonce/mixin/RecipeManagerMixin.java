package lommie.onlycraftonce.mixin;

import lommie.onlycraftonce.CommonClass;
import lommie.onlycraftonce.saveddata.TimesCraftedSavedData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin {
    @Shadow @Nullable protected abstract <T extends Recipe<?>> RecipeHolder<T> byKeyTyped(RecipeType<T> p_332930_, ResourceLocation p_335282_);

    @Inject(
            method = "getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/item/crafting/RecipeInput;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/crafting/RecipeHolder;)Ljava/util/Optional;",
            at = @At("HEAD"),
            cancellable = true)
    <I extends RecipeInput, T extends Recipe<@NotNull I>>
    void checkIfCraftedBefore(RecipeType<@NotNull T> recipeType, I input, Level level, @Nullable RecipeHolder<@NotNull T> lastRecipe, CallbackInfoReturnable<Optional<RecipeHolder<@NotNull T>>> cir){
        if (lastRecipe == null || level.isClientSide()) return;
        ItemStack result = lastRecipe.value().assemble(input,level.registryAccess());
        TimesCraftedSavedData savedData = TimesCraftedSavedData.getFromLevel(((ServerLevel) level));
        for (Item item : CommonClass.maxTimesCrafted.keySet()) {
            if (result.is(item)){
                if (result.getCount() + savedData.map.getOrDefault("minecraft.crafted:"+item.toString().replace(':','.'),0) > CommonClass.maxTimesCrafted.get(item)){
                    cir.setReturnValue(Optional.empty());
                    cir.cancel();
                    return;
                }
            }
        }
    }

    @Inject(
            method = "getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/item/crafting/RecipeInput;Lnet/minecraft/world/level/Level;Lnet/minecraft/resources/ResourceLocation;)Ljava/util/Optional;",
            at = @At("HEAD"),
            cancellable = true)
    <I extends RecipeInput, T extends Recipe<@NotNull I>>
    void checkIfCraftedBefore2(RecipeType<T> recipeType, I input, Level level, ResourceLocation lastRecipe, CallbackInfoReturnable<Optional<RecipeHolder<T>>> cir){
        RecipeHolder<T> holder = lastRecipe!=null?this.byKeyTyped(recipeType,lastRecipe):null;
        checkIfCraftedBefore(recipeType,input,level,holder,cir);
    }

    @Inject(
            method = "getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/item/crafting/RecipeInput;Lnet/minecraft/world/level/Level;)Ljava/util/Optional;",
            at = @At("HEAD"),
            cancellable = true)
    <I extends RecipeInput, T extends Recipe<@NotNull I>>
    void checkIfCraftedBefore2(RecipeType<T> recipeType, I input, Level level, CallbackInfoReturnable<Optional<RecipeHolder<T>>> cir){
        checkIfCraftedBefore(recipeType,input,level,null,cir);
    }
}
