package lommie.onlycraftonce.mixin;

import net.minecraft.world.inventory.RecipeCraftingHolder;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RecipeCraftingHolder.class)
public interface RecipeCraftingHolderMixin {
    /* TODO figure out why this mixin did nothing
    @Inject(method = "awardUsedRecipes",
            at=@At("HEAD"))
    default void addTimesCrafted(Player player, List<ItemStack> itemStacks, CallbackInfo ci){
        if (!(this instanceof ResultContainer)) return;
        ResultContainer thisContainer = (ResultContainer) this;
        if (player.level().isClientSide()) return;

        TimesCraftedSavedData savedData = TimesCraftedSavedData.get((ServerLevel) player.level());
        ItemStack result = thisContainer.getItem(0);
        if (result.isEmpty()) return;

        Item item = result.getItem();
        //TODO I should change this to just item.toString(), but that would break compatibility with v1.0
        String key = "minecraft.crafted:"+item.toString().replace(':','.');
        savedData.map.put(key,savedData.map.getOrDefault(key,0)+1);
        savedData.setDirty();
    }
    */
}
