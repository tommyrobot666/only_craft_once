package lommie.onlycraftonce;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import lommie.onlycraftonce.platform.Services;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;
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
                ConfigCategory.Builder firstCategory = ConfigCategory.createBuilder();
//                OptionGroup.Builder rootGroup = firstCategory.rootGroupBuilder();
                return YetAnotherConfigLib.createBuilder()
                        .title(Component.literal(Constants.MOD_ID))
                        .category(firstCategory
                                .name(Component.translatableWithFallback(Constants.MOD_ID+".name",Constants.MOD_NAME))
                                .group(ListOption.<String>createBuilder()
                                        .name(Component.translatableWithFallback(Constants.MOD_ID+".config_group",Constants.CONFIG_OPTION_NAME))
                                        .description(OptionDescription.of(Component.translatableWithFallback(Constants.MOD_ID+".config_group_description",Constants.CONFIG_OPTION_DESCRIPTION)))
                                        .binding(List.of(),() -> List.of(), (new_value) -> {
                                            return;
                                        })
                                        .controller(StringControllerBuilder::create)
                                        .initial("")
                                        .build()
                                )
                                .build()
                        )
                        .build().generateScreen(parent);
            }
        });
    }
}
