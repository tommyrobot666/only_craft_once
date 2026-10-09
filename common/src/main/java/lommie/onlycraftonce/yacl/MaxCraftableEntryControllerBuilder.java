package lommie.onlycraftonce.yacl;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.ControllerBuilder;

public interface MaxCraftableEntryControllerBuilder extends ControllerBuilder<MaxCraftableEntry> {
    static ControllerBuilder<MaxCraftableEntry> create(Option<MaxCraftableEntry> option) {
        return new MaxCraftableEntryControllerBuilderImpl(option);
    }
}
