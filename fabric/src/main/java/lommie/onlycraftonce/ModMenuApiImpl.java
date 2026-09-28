package lommie.onlycraftonce;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.*;
import lommie.onlycraftonce.platform.Services;
import lommie.onlycraftonce.yacl.MaxCraftableEntry;
import lommie.onlycraftonce.yacl.MaxCraftableEntryControllerBuilder;
import lommie.onlycraftonce.yacl.YaclState;
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
                return YetAnotherConfigLib.createBuilder()
                        .title(Component.literal(Constants.MOD_ID))
                        .category(ConfigCategory.createBuilder()
                                .name(Component.translatableWithFallback(Constants.MOD_ID+".name",Constants.MOD_NAME))
                                .group(ListOption.<MaxCraftableEntry>createBuilder()
                                        .name(Component.translatableWithFallback(Constants.MOD_ID+".config_group",Constants.CONFIG_OPTION_NAME))
                                        .description(OptionDescription.of(Component.translatableWithFallback(Constants.MOD_ID+".config_group_description",Constants.CONFIG_OPTION_DESCRIPTION)))
                                        .binding(MaxCraftableEntry.toList(YaclState.config),() -> MaxCraftableEntry.toList(YaclState.config), (new_value) -> YaclState.config = MaxCraftableEntry.toMap(new_value))
                                        .controller(MaxCraftableEntryControllerBuilder::create)
                                        .initial(new MaxCraftableEntry(Items.AIR,3))
                                        .build()
                                )
                                .build()
                        )
                        .build().generateScreen(parent);
            }
        });
    }
}
