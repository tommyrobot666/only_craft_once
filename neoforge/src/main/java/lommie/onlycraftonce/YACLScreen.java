package lommie.onlycraftonce;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.IntegerFieldControllerBuilder;
import dev.isxander.yacl3.api.controller.ItemControllerBuilder;
import lommie.onlycraftonce.packet.ServerboundRequestCurrentConfigPacket;
import lommie.onlycraftonce.packet.ServerboundUpdateConfigPacket;
import lommie.onlycraftonce.platform.Services;
import lommie.onlycraftonce.yacl.YaclState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.fml.ModContainer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class YACLScreen {
    static boolean firstTry = true;

    public static Screen generateScreen(ModContainer modContainer, Screen parent) {
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
            firstTry = true;
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
        return YetAnotherConfigLib.createBuilder()
                .title(Component.literal(Constants.MOD_ID))
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatableWithFallback(Constants.MOD_ID+".name",Constants.MOD_NAME).append(" Experimental"))
                        .group(ListOption.<Item>createBuilder()
                                .name(Component.translatableWithFallback(Constants.MOD_ID+".config_group_item",Constants.CONFIG_OPTION_ITEM_NAME))
                                .description(OptionDescription.of(Component.translatableWithFallback(Constants.MOD_ID+".config_group_item_list_description",Constants.CONFIG_OPTION_ITEM_LIST_DESCRIPTION)))
                                .binding(setInitialItemsList(),YACLScreen::getItemsList,YACLScreen::setItemsList)
                                .controller(ItemControllerBuilder::create)
                                .initial(Items.MACE)
                                .build())
                        .group(ListOption.<Integer>createBuilder()
                                .name(Component.translatableWithFallback(Constants.MOD_ID+".config_group",Constants.CONFIG_OPTION_NAME))
                                .description(OptionDescription.of(Component.translatableWithFallback(Constants.MOD_ID+".config_group_description",Constants.CONFIG_OPTION_DESCRIPTION)))
                                .binding(setInitialMaxsList(),YACLScreen::getMaxsList, YACLScreen::setMaxsList)
                                .controller(IntegerFieldControllerBuilder::create)
                                .initial(3)
                                .build())
                        .build())
                .save(
                        () -> {
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
                )
                .build().generateScreen(parent);
    }

    static List<Integer> maxs;

    private static void setMaxsList(@NotNull List<Integer> integers) {
        maxs = integers;
    }

    private static @NotNull List<Integer> getMaxsList() {
        return maxs;
    }

    private static @NotNull List<Integer> setInitialMaxsList() {
        maxs = YaclState.config.values().stream().toList();
        return maxs;
    }

    static List<Item> items;

    private static void setItemsList(@NotNull List<Item> n) {
        items = n;
    }

    private static @NotNull List<Item> getItemsList() {
        return items;
    }

    private static @NotNull List<Item> setInitialItemsList() {
        items = YaclState.config.keySet().stream().toList();
        return items;
    }
}
