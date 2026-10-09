package lommie.onlycraftonce.yacl;

import dev.isxander.yacl3.api.Controller;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.ControllerBuilder;

public class MaxCraftableEntryControllerBuilderImpl implements ControllerBuilder<MaxCraftableEntry> {
    Option<MaxCraftableEntry> option;

    public MaxCraftableEntryControllerBuilderImpl(Option<MaxCraftableEntry> option) {
        this.option = option;
    }

    @Override
    public Controller<MaxCraftableEntry> build() {
        return new MaxCraftableEntryController(option);
    }
}
