package g_mungus.zpl.client.screen;

import g_mungus.zpl.block.advanced_gryo.AdvancedGyroscopeControllerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AdvancedGyroscopeControllerScreen extends AbstractContainerScreen<AdvancedGyroscopeControllerMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("zpl", "textures/gui/container/advanced_gyroscope_controller.png");

    public AdvancedGyroscopeControllerScreen(AdvancedGyroscopeControllerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int left = (width - imageWidth) / 2;
        int top = (height - imageHeight) / 2;
        guiGraphics.blit(TEXTURE, left, top, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int energy = menu.getEnergyStored();
        int maxEnergy = menu.getMaxEnergyStored();
        String text = "Energy: " + energy + " / " + maxEnergy + " FE";
        guiGraphics.drawString(font, text, 8, 6, 0x404040, false);

        int usage = menu.getEnergyUsage();
        String usageText = "Usage: " + usage + " FE/t";
        guiGraphics.drawString(font, usageText, 8, 16, 0x404040, false);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
