package lommie.onlycraftonce.mixin;

import lommie.onlycraftonce.CommonClass;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={CraftingMenu.class, InventoryMenu.class})
public abstract class AbstractCraftingMenuMixin {
    @Shadow(remap = false) public abstract Slot getResultSlot();

    @Inject(method = "quickMoveStack", at = @At("HEAD"), cancellable = true)
    void preventQuickMoveBug(Player player, int index, CallbackInfoReturnable<ItemStack> cir) {
        if (getResultSlot().getContainerSlot() == index &&
                CommonClass.maxTimesCrafted.keySet().stream().anyMatch(item -> getResultSlot().getItem().is(item))) {
            player.displayClientMessage(
                    Component.translatableWithFallback("only_craft_once.chat.quick_move","Can't quick move limited items! (causes dupe bug)")
                            .withStyle(ChatFormatting.DARK_RED),false);
            cir.setReturnValue(ItemStack.EMPTY);
            cir.cancel();
        }
    }
}
