package lommie.onlycraftonce;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import lommie.onlycraftonce.platform.Services;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.ModContainer;

import java.util.List;

public class YACLScreen {
    public static Screen generateScreen(ModContainer modContainer, Screen parent) {
        if (!Services.PLATFORM.isModLoaded(Constants.YACL_MODID)) {
            Constants.LOG.warn("YACL is needed to generate config screen");
            return parent;
        }

        return YetAnotherConfigLib.createBuilder()
                .title(Component.literal(Constants.MOD_ID))
                .category(ConfigCategory.createBuilder()
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
}
