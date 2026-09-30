package lommie.onlycraftonce;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.*;
import lommie.onlycraftonce.packet.ServerboundRequestCurrentConfigPacket;
import lommie.onlycraftonce.platform.Services;
import lommie.onlycraftonce.yacl.MaxCraftableEntry;
import lommie.onlycraftonce.yacl.MaxCraftableEntryControllerBuilder;
import lommie.onlycraftonce.yacl.YaclState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;

import java.util.Map;

public class ModMenuApiImpl implements ModMenuApi {
    @Override
    public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
        if (!Services.PLATFORM.isModLoaded(Constants.YACL_MODID)) {
            Constants.LOG.warn("YACL is needed to generate config screen");
            return Map.of();
        }

        return Map.of(Constants.MOD_ID, new ConfigScreenFactory<>() {
            @Override
            public Screen create(Screen parent) {
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
                                .name(Component.translatableWithFallback(Constants.MOD_ID+".name",Constants.MOD_NAME))
                                .group(ListOption.<MaxCraftableEntry>createBuilder()
                                        .name(Component.translatableWithFallback(Constants.MOD_ID+".config_group",Constants.CONFIG_OPTION_NAME))
                                        .description(OptionDescription.of(Component.translatableWithFallback(Constants.MOD_ID+".config_group_description",Constants.CONFIG_OPTION_DESCRIPTION)))
                                        .binding(MaxCraftableEntry.toList(YaclState.config),() -> MaxCraftableEntry.toList(YaclState.config), (new_value) -> YaclState.config = MaxCraftableEntry.toMap(new_value))
                                        .controller(MaxCraftableEntryControllerBuilder::create)
                                        .initial(new MaxCraftableEntry(Items.MACE,3))
                                        .build()
                                )
                                .build()
                        )
                        .build().generateScreen(parent);
            }
        });
    }
}
