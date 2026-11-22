package lommie.onlycraftonce.yacl;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.IntegerFieldControllerBuilder;
import dev.isxander.yacl3.api.controller.ItemControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import lommie.onlycraftonce.Constants;
import lommie.onlycraftonce.packet.ServerboundRequestCurrentConfigPacket;
import lommie.onlycraftonce.packet.ServerboundUpdateConfigPacket;
import lommie.onlycraftonce.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class YACLScreen {
    static List<MaxCraftableEntry> maxCraftableEntryList;
    static List<Integer> maxs;
    static List<Item> items;

    static boolean useExperimentalScreen = false;
    static boolean firstTry = true;

    public static Screen generateScreen(Screen parent) {
        if (!Services.PLATFORM.isModLoaded(Constants.YACL_MODID)) {
            Constants.LOG.warn("YACL is needed to generate config screen");
            return parent;
        }

        if (Minecraft.getInstance().getConnection() == null){
            return YetAnotherConfigLib.createBuilder()
                    .title(Component.literal(Constants.MOD_ID))
                    .category(ConfigCategory.createBuilder()
                            .name(Component.translatableWithFallback(Constants.MOD_ID+".error_title",Constants.CONFIG_ERROR))
                            .group(OptionGroup.createBuilder()
                                    .option(LabelOption.create(Component.translatableWithFallback(Constants.MOD_ID+".out_of_game_error",Constants.CONFIG_ERROR_OUT_OF_GAME)))
                                    .build())
                            .build())
                    .build().generateScreen(parent);
        }

        // check changed for when the packet got there, but too late, so user is reopening screen
        if (!YaclState.changed) {
            Services.NETWORKING.sendServerbound(new ServerboundRequestCurrentConfigPacket());

            for (int i = 0; i < Constants.GET_CONFIG_TRYS; i++) {
                try {
                    Thread.sleep(Constants.GET_CONFIG_WAIT);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (YaclState.changed) break;
            }
        }

        // didn't receive packet in time
        if (!YaclState.changed){
            if (firstTry) {
                firstTry = false;
                return YetAnotherConfigLib.createBuilder()
                        .title(Component.literal(Constants.MOD_ID))
                        .category(ConfigCategory.createBuilder()
                                .name(Component.translatableWithFallback(Constants.MOD_ID + ".try_again", Constants.TRY_AGAIN_ERROR))
                                .group(OptionGroup.createBuilder()
                                        .option(LabelOption.create(Component.translatableWithFallback(Constants.MOD_ID + ".try_again", Constants.TRY_AGAIN_ERROR)))
                                        .build())
                                .build())
                        .build().generateScreen(parent);
            }

            return YetAnotherConfigLib.createBuilder()
                    .title(Component.literal(Constants.MOD_ID))
                    .category(ConfigCategory.createBuilder()
                            .name(Component.translatableWithFallback(Constants.MOD_ID+".error_title",Constants.CONFIG_ERROR))
                            .group(OptionGroup.createBuilder()
                                    .option(LabelOption.create(Component.translatableWithFallback(Constants.MOD_ID+".get_config_timeout_error",Constants.CONFIG_ERROR_TIMEOUT)))
                                    .build())
                            .build())
                    .build().generateScreen(parent);
        }

        // reset changed
        YaclState.changed = false;
        firstTry = true;
        maxCraftableEntryList = MaxCraftableEntry.toList(YaclState.config);
        return YetAnotherConfigLib.createBuilder()
                .title(Component.literal(Constants.MOD_ID))
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatableWithFallback(Constants.MOD_ID+".name",Constants.MOD_NAME).append(" Experimental"))
                        .group(ListOption.<Item>createBuilder()
                                .name(Component.translatableWithFallback(Constants.MOD_ID+".config_group_item",Constants.CONFIG_OPTION_ITEM_NAME))
                                .description(OptionDescription.of(Component.translatableWithFallback(Constants.MOD_ID+".config_group_item_list_description",Constants.CONFIG_OPTION_ITEM_LIST_DESCRIPTION)))
                                .binding(setInitialItemsList(),() -> items,(n) -> items=n)
                                .controller(ItemControllerBuilder::create)
                                .initial(Items.MACE)
                                .build())
                        .group(ListOption.<Integer>createBuilder()
                                .name(Component.translatableWithFallback(Constants.MOD_ID+".config_group",Constants.CONFIG_OPTION_NAME))
                                .description(OptionDescription.of(Component.translatableWithFallback(Constants.MOD_ID+".config_group_description",Constants.CONFIG_OPTION_DESCRIPTION)))
                                .binding(setInitialMaxsList(),() -> maxs, (n) -> maxs=n)
                                .controller(IntegerFieldControllerBuilder::create)
                                .initial(3)
                                .build())
                        .build())
                .category(ConfigCategory.createBuilder()
                                .name(Component.translatableWithFallback(Constants.MOD_ID+".name",Constants.MOD_NAME).append(" Experimental"))
                                .group(ListOption.<MaxCraftableEntry>createBuilder()
                                                .name(Component.translatableWithFallback(Constants.MOD_ID+".config_group",Constants.CONFIG_OPTION_NAME))
                                                .description(OptionDescription.of(Component.translatableWithFallback(Constants.MOD_ID+".config_group_description",Constants.CONFIG_OPTION_DESCRIPTION)))
//                                        .binding(MaxCraftableEntry.toList(YaclState.config),() -> MaxCraftableEntry.toList(YaclState.config), (new_value) -> YaclState.config = MaxCraftableEntry.toMap(new_value))
                                                .binding(maxCraftableEntryList,() -> maxCraftableEntryList, (new_value) -> maxCraftableEntryList = new_value)
                                                .controller(MaxCraftableEntryControllerBuilder::create)
                                                .initial(new MaxCraftableEntry(Items.MACE,3))
                                                .build()
                                )
                                .group(OptionGroup.createBuilder()
                                        .name(Component.literal("Send values from experimental screen instead?"))
                                        .option(Option.<Boolean>createBuilder()
                                                .name(Component.literal("Send values from experimental screen instead?"))
                                                .controller(TickBoxControllerBuilder::create)
                                                .binding(useExperimentalScreen,()->useExperimentalScreen,(n)->useExperimentalScreen=n)
                                                .build())
                                        .build())
                                .build()
                )
                .save(
                        () -> {
                            if (useExperimentalScreen){
                                Services.NETWORKING.sendServerbound(new ServerboundUpdateConfigPacket(MaxCraftableEntry.toMap(maxCraftableEntryList)));
                            } else {
                                HashMap<Item,Integer> changed = new HashMap<>();
                                for (int i = 0; i < items.size(); i++) {
                                    if (i >= maxs.size()) break;
                                    changed.put(items.get(i),maxs.get(i));
                                }
                                for (Item item : YaclState.config.keySet()) {
                                    // is changed? check
                                    if (changed.containsKey(item)){
                                        if (Objects.equals(changed.get(item), YaclState.config.get(item))){
                                            changed.remove(item);
                                        }
                                    }
                                    // is removed? check
                                    else {
                                        changed.put(item,-1);
                                    }
                                }

                                Services.NETWORKING.sendServerbound(new ServerboundUpdateConfigPacket(changed));
                            }
                        }
                )
                .build().generateScreen(parent);
    }

    private static @NotNull List<Integer> setInitialMaxsList() {
        maxs = YaclState.config.values().stream().toList();
        return maxs;
    }

    private static @NotNull List<Item> setInitialItemsList() {
        items = YaclState.config.keySet().stream().toList();
        return items;
    }

    public static void onNewServer(){
        firstTry = true;
        YaclState.changed = false;
    }
}
