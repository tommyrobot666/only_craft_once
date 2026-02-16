package lommie.onlycraftonce;

import lommie.onlycraftonce.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Map;

public class CommonClass {
    public static Map<Item,Integer> maxTimesCrafted = Map.of(
            Items.MACE, 3,
            Items.IRON_NUGGET, 18
    );

    public static void init() {
        if (Services.PLATFORM.isModLoaded("only_craft_once")) {
            Constants.LOG.info("YOU WILL NEVER CRAFT AGAIN");
        }
    }
}
