package lommie.onlycraftonce.yacl;

import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.AbstractWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class MaxCraftableEntryControllerElement extends AbstractWidget {
    boolean focused;

    public MaxCraftableEntryControllerElement(MaxCraftableEntryController control, YACLScreen screen, Dimension<Integer> dim) {
        super(dim);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {

    }

    @Override
    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    @Override
    public boolean isFocused() {
        return focused;
    }
}
