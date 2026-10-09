package lommie.onlycraftonce.yacl;

import dev.isxander.yacl3.api.Controller;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.AbstractWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import net.minecraft.network.chat.Component;

public class MaxCraftableEntryController implements Controller<MaxCraftableEntry> {
    Option<MaxCraftableEntry> option;

    public MaxCraftableEntryController(Option<MaxCraftableEntry> option) {
        this.option = option;
    }

    @Override
    public Option<MaxCraftableEntry> option() {
        return option;
    }

    @Override
    public Component formatValue() {
        return Component.literal("idk");
    }

    @Override
    public AbstractWidget provideWidget(YACLScreen screen, Dimension<Integer> dim) {
        return new MaxCraftableEntryControllerElement(this,screen,dim);
    }
}
