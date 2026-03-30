package g_mungus.zpl.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class IconButton extends Button {
    @FunctionalInterface
    public interface IconRenderer {
        void render(GuiGraphics guiGraphics, int x, int y, int size, int color);
    }

    private final IconRenderer iconRenderer;
    private final Component narration;
    private final int iconSize;

    public IconButton(int x, int y, int size, int iconSize, Component narration, OnPress onPress, IconRenderer iconRenderer) {
        super(x, y, size, size, Component.empty(), onPress, DEFAULT_NARRATION);
        this.iconRenderer = iconRenderer;
        this.narration = narration;
        this.iconSize = iconSize;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
        int color = active ? (isHoveredOrFocused() ? 0xFFFFFFFF : 0xFFE0E0E0) : 0xFF7F7F7F;
        int iconX = getX() + (width - iconSize) / 2;
        int iconY = getY() + (height - iconSize) / 2;
        iconRenderer.render(guiGraphics, iconX, iconY, iconSize, color);
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.TITLE, narration);
    }

    public static void renderGearIcon(GuiGraphics guiGraphics, int x, int y, int size, int color) {
        int center = size / 2;
        guiGraphics.fill(x + center - 2, y + center - 2, x + center + 2, y + center + 2, color);
        guiGraphics.fill(x + center - 1, y + 1, x + center + 1, y + 3, color);
        guiGraphics.fill(x + center - 1, y + size - 3, x + center + 1, y + size - 1, color);
        guiGraphics.fill(x + 1, y + center - 1, x + 3, y + center + 1, color);
        guiGraphics.fill(x + size - 3, y + center - 1, x + size - 1, y + center + 1, color);
        guiGraphics.fill(x + 2, y + 2, x + 3, y + 3, color);
        guiGraphics.fill(x + size - 3, y + 2, x + size - 2, y + 3, color);
        guiGraphics.fill(x + 2, y + size - 3, x + 3, y + size - 2, color);
        guiGraphics.fill(x + size - 3, y + size - 3, x + size - 2, y + size - 2, color);
    }

    public static void renderCloseIcon(GuiGraphics guiGraphics, int x, int y, int size, int color) {
        for (int i = 0; i < size; i++) {
            guiGraphics.fill(x + i, y + i, x + i + 1, y + i + 1, color);
            guiGraphics.fill(x + size - 1 - i, y + i, x + size - i, y + i + 1, color);
        }
    }
}
