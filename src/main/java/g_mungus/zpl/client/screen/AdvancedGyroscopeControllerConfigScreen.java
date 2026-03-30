package g_mungus.zpl.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class AdvancedGyroscopeControllerConfigScreen extends Screen {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("zpl", "textures/gui/container/advanced_gyroscope_controller_config.png");
    private static final int PANEL_WIDTH = 176;
    private static final int PANEL_HEIGHT = 166;
    private static final int BUTTON_SIZE = 16;
    private static final int BUTTON_MARGIN = 4;
    private static final int ICON_SIZE = 10;

    private final AdvancedGyroscopeControllerScreen parent;

    public AdvancedGyroscopeControllerConfigScreen(AdvancedGyroscopeControllerScreen parent) {
        super(Component.translatable("screen.zpl.advanced_gyroscope_controller_config"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        int left = (width - PANEL_WIDTH) / 2;
        int top = (height - PANEL_HEIGHT) / 2;
        addRenderableWidget(new IconButton(
                left + PANEL_WIDTH - BUTTON_SIZE - BUTTON_MARGIN,
                top + BUTTON_MARGIN,
                BUTTON_SIZE,
                ICON_SIZE,
                Component.translatable("button.zpl.advanced_gyroscope_controller_close"),
                button -> onClose(),
                IconButton::renderCloseIcon
        ));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics);
        int left = (width - PANEL_WIDTH) / 2;
        int top = (height - PANEL_HEIGHT) / 2;
        guiGraphics.blit(TEXTURE, left, top, 0, 0, PANEL_WIDTH, PANEL_HEIGHT);
        guiGraphics.drawString(font, title, left + 8, top + 6, 0x404040, false);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        minecraft.setScreen(parent);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
