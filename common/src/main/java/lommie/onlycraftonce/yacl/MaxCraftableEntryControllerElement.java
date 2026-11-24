package lommie.onlycraftonce.yacl;

import com.mojang.blaze3d.platform.InputConstants;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.ControllerWidget;
import dev.isxander.yacl3.gui.utils.GuiUtils;
import dev.isxander.yacl3.gui.utils.ItemRegistryHelper;
import dev.isxander.yacl3.gui.utils.UndoRedoHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class MaxCraftableEntryControllerElement extends ControllerWidget<MaxCraftableEntryController> {

    private static final int MAX_INPUT_FIELD_WIDTH = 20;
    private Item currentItem;
    private int currentMax;

    TextInputBox itemInputField = new TextInputBox();
    TextInputBox maxInputField = new TextInputBox();

    public MaxCraftableEntryControllerElement(MaxCraftableEntryController control, YACLScreen screen, Dimension<Integer> dim) {
        super(control, screen, dim);
        currentItem = control.option.pendingValue().item();
        itemInputField.inputField = BuiltInRegistries.ITEM.getKey(currentItem).toString();
        itemInputField.getCurrentValue = () -> Component.literal(BuiltInRegistries.ITEM.getKey(currentItem).toString());
        itemInputField.inputFieldFocusedEvent = () -> {
            if (itemInputField.inputFieldFocused) maxInputField.inputFieldFocused = false;
        };
        currentMax = control.option.pendingValue().max();
        maxInputField.inputField = String.valueOf(currentMax);
        maxInputField.getCurrentValue = () -> Component.literal(String.valueOf(currentMax));
        maxInputField.inputFieldFocusedEvent = () -> {
            if (maxInputField.inputFieldFocused) itemInputField.inputFieldFocused = false;
        };
    }

    @Override
    protected int getHoveredControlWidth() {
        return itemInputField.getHoveredControlWidth()+16+maxInputField.getHoveredControlWidth();
    }

    @Override
    protected void drawValueText(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        itemInputField.extractValueText(graphics,mouseX,mouseY,delta,this);
        maxInputField.extractValueText(graphics,mouseX,mouseY,delta,this);
        if (currentItem != null) {
            extractFakeItem(
                    graphics,
                    currentItem,
                    getDimension().xLimit() - getXPadding() + 2 -MAX_INPUT_FIELD_WIDTH -16,
                    getDimension().y() + 2
            );
        }

    }


    //from ItemControllerElement
    private void extractFakeItem(GuiGraphics graphics, Item item, int x, int y) {
        ItemStack stack = null;
        try {
            stack = new ItemStack(item);
        } catch (NullPointerException ignored) {
            // ItemStacks no longer exist until dynamic registries have been loaded,
            // which is either loading into a level or opening the create world screen.
            // This means we cannot do anything that involves ItemStacks until then.
        }
        if (stack == null) return;
        graphics.renderFakeItem(stack, x, y);
    }
    void updateCurrentItem() {
        currentItem = ItemRegistryHelper.getItemFromName(itemInputField.inputField, currentItem);
    }

    void updateCurrentMax() {
        try {
            currentMax = Integer.parseInt(maxInputField.inputField);
        } catch (NumberFormatException e) {

        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean b1 = itemInputField.mouseClicked(mouseX,mouseY,button,this);
        boolean b2 = maxInputField.mouseClicked(mouseX,mouseY,button,this);
        if (!(b1 || b2)){
            itemInputField.inputFieldFocused = false;
            maxInputField.inputFieldFocused = false;
            unfocus();
            return false;
        }
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean b1 = itemInputField.keyPressed(keyCode,scanCode,modifiers,this);
        boolean b2 = maxInputField.keyPressed(keyCode,scanCode,modifiers,this);
        return b1 || b2;
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        boolean b1 = itemInputField.charTyped(codePoint,modifiers);
        boolean b2 = maxInputField.charTyped(codePoint,modifiers);
        return b1 || b2;
    }

    @Override
    public void setFocused(boolean focused) {
        super.setFocused(focused);
        if (!focused) {
            itemInputField.setFocused(false, this);
            maxInputField.setFocused(false, this);
        }
    }

    @Override
    public void setDimension(Dimension<Integer> dim) {
        super.setDimension(dim);
        itemInputField.setDimension(dim.withWidth(dim.width()-MAX_INPUT_FIELD_WIDTH).moved(-16,0));
        maxInputField.setDimension(dim.moved(dim.width()-MAX_INPUT_FIELD_WIDTH,0).withWidth(MAX_INPUT_FIELD_WIDTH));
    }

    @Override
    protected int getUnhoveredControlWidth() {
        return itemInputField.getUnhoveredControlWidth(this)+16+maxInputField.getUnhoveredControlWidth(this);
    }

    class TextInputBox {
        Supplier<Component> getValue = () -> Component.literal(this.inputField);
        Supplier<Component> getCurrentValue;

        protected final boolean instantApply = true;

        protected String inputField;
        protected Dimension<Integer> inputFieldBounds;
        protected boolean inputFieldFocused;

        protected int caretPos;
        protected int previousCaretPos;
        protected int selectionLength;
        protected int renderOffset;

        protected UndoRedoHelper undoRedoHelper;

        protected float ticks;
        protected float caretTicks;

        private final Component emptyText = Component.literal("emptyyy");
        private Runnable inputFieldFocusedEvent;

        protected void extractValueText (GuiGraphics graphics,int mouseX, int mouseY, float a, ControllerWidget w){
//            Component valueText = getCurrentValue.get();
            Component valueText = getValue.get();
            if (valueText.getString().isEmpty()) valueText = emptyText;

            if (!isHovered(w))
                valueText = Component.literal(GuiUtils.shortenString(valueText.getString(), textRenderer, getMaxUnwrapLength(), "...")).setStyle(valueText.getStyle());

            int textX = getDimension().xLimit() - textRenderer.width(valueText) + renderOffset - getXPadding();
            graphics.enableScissor(inputFieldBounds.x(), inputFieldBounds.y() - 2, inputFieldBounds.xLimit() + 1, inputFieldBounds.yLimit() + 4);
            graphics.drawString(textRenderer, valueText, textX, getTextY(), getValueColor(), true);

            if (isHovered(w)) {
                ticks += a;

                String text = getValue.get().getString();

                graphics.fill(inputFieldBounds.x(), inputFieldBounds.yLimit(), inputFieldBounds.xLimit(), inputFieldBounds.yLimit() + 1, -1);
                graphics.fill(inputFieldBounds.x() + 1, inputFieldBounds.yLimit() + 1, inputFieldBounds.xLimit() + 1, inputFieldBounds.yLimit() + 2, 0xFF404040);

                if (inputFieldFocused) {
                    if (caretPos > text.length())
                        caretPos = text.length();

                    int caretX = textX + textRenderer.width(text.substring(0, caretPos));
                    if (text.isEmpty())
                        caretX = inputFieldBounds.x() + inputFieldBounds.width() / 2;

                    if (selectionLength != 0) {
                        int selectionX = textX + textRenderer.width(text.substring(0, caretPos + selectionLength));
                        graphics.fill(caretX, inputFieldBounds.y() - 2, selectionX, inputFieldBounds.yLimit() - 1, 0x803030FF);
                    }

                    if (caretPos != previousCaretPos) {
                        previousCaretPos = caretPos;
                        caretTicks = 0;
                    }

                    if ((caretTicks += a) % 20 <= 10)
                        graphics.fill(caretX, inputFieldBounds.y() - 2, caretX + 1, inputFieldBounds.yLimit() - 1, -1);
                }
            }
            graphics.disableScissor();

        }

        private boolean isHoveredInputField ( double mouseX, double mouseY){
            return inputFieldBounds.isPointInside((int) mouseX, (int) mouseY);
        }

        public boolean mouseClicked(double mouseX, double mouseY, int button, ControllerWidget w) {
            if (isAvailable() && getDimension().isPointInside((int) mouseX, (int) mouseY)) {
                inputFieldFocused = true;

                if (!inputFieldBounds.isPointInside((int) mouseX, (int) mouseY)) {
                    caretPos = getDefaultCaretPos();
                } else {
                    // gets the appropriate caret position for where you click
                    int textX = (int) mouseX - (inputFieldBounds.xLimit() - textRenderer.width(getValueText()));
                    int pos = -1;
                    int currentWidth = 0;
                    for (char ch : inputField.toCharArray()) {
                        pos++;
                        int charLength = textRenderer.width(String.valueOf(ch));
                        if (currentWidth + charLength / 2 > textX) { // if more than halfway past the characters select in front of that char
                            caretPos = pos;
                            break;
                        } else if (pos == inputField.length() - 1) {
                            // if we have reached the end and no matches, it must be the second half of the char so the last position
                            caretPos = pos + 1;
                        }
                        currentWidth += charLength;
                    }

                    selectionLength = 0;
                }
//            if (undoRedoHelper == null) {
//                undoRedoHelper = new UndoRedoHelper(inputField, caretPos, selectionLength);
//            }

                return true;
            } else {
                unfocus(w);
            }

            return false;
        }

        private void setInputFieldFocused(boolean b) {
            inputFieldFocused = b;
            inputFieldFocusedEvent.run();
        }

        protected int getDefaultCaretPos () {
            return inputField.length();
        }

        public boolean keyPressed(int keyCode, int scanCode, int modifiers, ControllerWidget w) {
            if (!inputFieldFocused)
                return false;

            switch (keyCode) {
                case InputConstants.KEY_ESCAPE, InputConstants.KEY_RETURN -> {
                    unfocus(w);
                    return true;
                }
                case InputConstants.KEY_LEFT -> {
                    if (Screen.hasShiftDown()) {
                        if (Screen.hasControlDown()) {
                            int spaceChar = findSpaceIndex(true);
                            selectionLength += caretPos - spaceChar;
                            caretPos = spaceChar;
                        } else if (caretPos > 0) {
                            caretPos--;
                            selectionLength += 1;
                        }
                        checkRenderOffset();
                    } else {
                        if (caretPos > 0) {
                            if (Screen.hasControlDown()) {
                                caretPos = findSpaceIndex(true);
                            } else {
                                if (selectionLength != 0) {
                                    caretPos += Math.min(selectionLength, 0);
                                } else caretPos--;
                            }
                        }
                        checkRenderOffset();
                        selectionLength = 0;
                    }

                    return true;
                }
                case InputConstants.KEY_RIGHT -> {
                    if (Screen.hasShiftDown()) {
                        if (Screen.hasControlDown()) {
                            int spaceChar = findSpaceIndex(false);
                            selectionLength -= spaceChar - caretPos;
                            caretPos = spaceChar;
                        } else if (caretPos < inputField.length()) {
                            caretPos++;
                            selectionLength -= 1;
                        }
                        checkRenderOffset();
                    } else {
                        if (caretPos < inputField.length()) {
                            if (Screen.hasControlDown()) {
                                caretPos = findSpaceIndex(false);
                            } else {
                                if (selectionLength != 0) {
                                    caretPos += Math.max(selectionLength, 0);
                                } else caretPos++;
                            }
                            checkRenderOffset();
                        }
                        selectionLength = 0;
                    }

                    return true;
                }
                case InputConstants.KEY_BACKSPACE -> {
                    doBackspace();
                    return true;
                }
                case InputConstants.KEY_DELETE -> {
                    doDelete();
                    return true;
                }
                case InputConstants.KEY_END -> {
                    if (Screen.hasShiftDown()) {
                        selectionLength -= inputField.length() - caretPos;
                    } else selectionLength = 0;
                    caretPos = inputField.length();
                    checkRenderOffset();
                    return true;
                }
                case InputConstants.KEY_HOME -> {
                    if (Screen.hasShiftDown()) {
                        selectionLength += caretPos;
                        caretPos = 0;
                    } else {
                        caretPos = 0;
                        selectionLength = 0;
                    }
                    checkRenderOffset();
                    return true;
                }
//            case InputConstants.KEY_Z -> {
//                if (Screen.hasControlDown()) {
//                    UndoRedoHelper.FieldState updated = Screen.hasShiftDown() ? undoRedoHelper.redo() : undoRedoHelper.undo();
//                    if (updated != null) {
//                        System.out.println("Updated: " + updated);
//                        if (modifyInput(builder -> builder.replace(0, inputField.length(), updated.text()))) {
//                            caretPos = updated.cursorPos();
//                            selectionLength = updated.selectionLength();
//                            checkRenderOffset();
//                        }
//                    }
//                    return true;
//                }
//            }
            }

            if (Screen.isPaste(keyCode)) {
                return doPaste();
            } else if (Screen.isCopy(keyCode)) {
                return doCopy();
            } else if (Screen.isCut(keyCode)) {
                return doCut();
            } else if (Screen.isSelectAll(keyCode)) {
                return doSelectAll();
            }

            return false;
        }

        protected boolean doPaste () {
            this.write(client.keyboardHandler.getClipboard());
            updateUndoHistory();
            return true;
        }

        protected boolean doCopy () {
            if (selectionLength != 0) {
                client.keyboardHandler.setClipboard(getSelection());
                return true;
            }
            return false;
        }

        protected boolean doCut () {
            if (selectionLength != 0) {
                client.keyboardHandler.setClipboard(getSelection());
                this.write("");
                updateUndoHistory();
                return true;
            }
            return false;
        }

        protected boolean doSelectAll () {
            caretPos = inputField.length();
            checkRenderOffset();
            selectionLength = -caretPos;
            return true;
        }

        protected void checkRenderOffset () {
            if (textRenderer.width(inputField) < getUnshiftedLength()) {
                renderOffset = 0;
                return;
            }

            int textX = getDimension().xLimit() - textRenderer.width(inputField) - getXPadding();
            int caretX = textX + textRenderer.width(inputField.substring(0, caretPos));

            int minX = getDimension().xLimit() - getXPadding() - getUnshiftedLength();
            int maxX = minX + getUnshiftedLength();

            if (caretX + renderOffset < minX) {
                renderOffset = minX - caretX;
            } else if (caretX + renderOffset > maxX) {
                renderOffset = maxX - caretX;
            }
        }

        public boolean charTyped(char chr, int modifiers) {
            if (!inputFieldFocused)
                return false;

            if (!Screen.hasControlDown()) {
                write(Character.toString(chr));
                updateUndoHistory();
                return true;
            }

            return false;
        }

        protected void doBackspace () {
            if (selectionLength != 0) {
                write("");
            } else if (caretPos > 0) {
                if (modifyInput(builder -> builder.deleteCharAt(caretPos - 1))) {
                    caretPos--;
                    checkRenderOffset();
                }
            }
            updateUndoHistory();
        }

        protected void doDelete () {
            if (selectionLength != 0) {
                write("");
            } else if (caretPos < inputField.length()) {
                modifyInput(builder -> builder.deleteCharAt(caretPos));
            }
            updateUndoHistory();
        }

        public void write (String string){
            if (selectionLength == 0) {
                if (modifyInput(builder -> builder.insert(caretPos, string))) {
                    caretPos += string.length();
                    checkRenderOffset();
                }
            } else {
                int start = getSelectionStart();
                int end = getSelectionEnd();

                if (modifyInput(builder -> builder.replace(start, end, string))) {
                    caretPos = start + string.length();
                    selectionLength = 0;
                    checkRenderOffset();
                }
            }
        }

        public boolean modifyInput (Consumer < StringBuilder > consumer) {
            StringBuilder temp = new StringBuilder(inputField);
            consumer.accept(temp);
            inputField = temp.toString();
            if (instantApply)
                updateControl();
            return true;
        }

        protected void updateUndoHistory () {
    //        undoRedoHelper.save(inputField, caretPos, selectionLength);
        }

        public int getUnshiftedLength () {
            if (optionNameString.isEmpty())
                return getDimension().width() - getXPadding() * 2;
            return getDimension().width() / 8 * 5;
        }

        public int getMaxUnwrapLength () {
            if (optionNameString.isEmpty())
                return getDimension().width() - getXPadding() * 2;
            return getDimension().width() / 2;
        }

        public int getSelectionStart () {
            return Math.min(caretPos, caretPos + selectionLength);
        }

        public int getSelectionEnd () {
            return Math.max(caretPos, caretPos + selectionLength);
        }

        protected String getSelection () {
            return inputField.substring(getSelectionStart(), getSelectionEnd());
        }

        protected int findSpaceIndex ( boolean reverse){
            int i;
            int fromIndex = caretPos;
            if (reverse) {
                if (caretPos > 0)
                    fromIndex -= 2;
                i = this.inputField.lastIndexOf(" ", fromIndex) + 1;
            } else {
                if (caretPos < inputField.length())
                    fromIndex += 1;
                i = this.inputField.indexOf(" ", fromIndex) + 1;

                if (i == 0) i = inputField.length();
            }

            return i;
        }

        public void setFocused ( boolean focused, ControllerWidget w){
            setInputFieldFocused(focused);
            updateTextInputFocus(focused,w);
        }

        public void unfocus (ControllerWidget w) {
            setInputFieldFocused(false);
            renderOffset = 0;
            if (!instantApply) updateControl();
            updateTextInputFocus(false,w);
        }

        private void updateTextInputFocus ( boolean focused, ControllerWidget w){
//            //? if >=26.3 {
//            Minecraft.getInstance().onTextInputFocusChange(w, focused);
//            //?}
        }

        public void setDimension (Dimension < Integer > dim) {
            int width = Math.max(6, Math.min(textRenderer.width(getValue.get()), getUnshiftedLength()));
            inputFieldBounds = Dimension.ofInt(dim.xLimit() - getXPadding() - width, dim.centerY() - textRenderer.lineHeight / 2, width, textRenderer.lineHeight);
        }

        public boolean isHovered (ControllerWidget w) {
            return MaxCraftableEntryControllerElement.super.isHovered() || inputFieldFocused;
        }

        protected int getUnhoveredControlWidth (ControllerWidget w) {
            return !isHovered(w) ? Math.min(getHoveredControlWidth(), getMaxUnwrapLength()) : getHoveredControlWidth();
        }

        protected int getHoveredControlWidth () {
            return Math.min(textRenderer.width(getValue.get()), getUnshiftedLength());
        }
    }

    protected void updateControl() {
        updateCurrentItem();
        updateCurrentMax();
        control.option.requestSet(new MaxCraftableEntry(currentItem, currentMax));
    }

}
