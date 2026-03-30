package g_mungus.zpl.client.screen;

import g_mungus.zpl.block.advanced_gryo.AdvancedGyroscopeControllerBlockEntity;
import g_mungus.zpl.network.ModPackets;
import g_mungus.zpl.network.SetGyroMappingPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;

public class AdvancedGyroscopeControllerConfigScreen extends Screen {
    // Panel dimensions
    private static final int PANEL_WIDTH = 260;
    private static final int PANEL_HEIGHT = 220;

    // Layout constants (relative to panel top-left)
    private static final int TITLE_Y = 8;
    private static final int CONTENT_TOP = 24;
    private static final int CONTENT_HEIGHT = 186; // 24 to 210

    private static final int FUNC_BOX_X = 80;
    private static final int FUNC_BOX_WIDTH = 100;
    private static final int FUNC_BOX_HEIGHT = 16;
    private static final int FUNC_SPACING = 23;

    private static final int DOT_SIZE = 12;
    private static final int DOT_HIT_RADIUS = 8;
    private static final int LEFT_DOT_X = 15;
    private static final int RIGHT_DOT_X = 233;
    private static final int DOT_SPACING = 46;

    private static final int CLOSE_BUTTON_SIZE = 16;
    private static final int CLOSE_BUTTON_MARGIN = 4;
    private static final int CLOSE_ICON_SIZE = 10;

    // Input indices: 0-3 left (G/B/R/P), 4-7 right (G/B/R/P)
    private static final String[] INPUT_LABELS = {
            "L:Green", "L:Blue", "L:Red", "L:Purple",
            "R:Green", "R:Blue", "R:Red", "R:Purple"
    };

    // Function indices: 0-7
    private static final String[] FUNCTION_NAMES = {
            "Rot X+", "Rot X-", "Rot Y+", "Rot Y-",
            "Rot Z+", "Rot Z-", "Stabilize", "Bypass"
    };

    // Dot colors: Green, Blue, Red, Purple (ARGB)
    private static final int[] DOT_COLORS = {
            0xFF22BB44, // Green
            0xFF2255EE, // Blue
            0xFFDD2222, // Red
            0xFF9922CC  // Purple
    };

    // Panel colors
    private static final int COLOR_PANEL_BG      = 0xFF2B2B2B;
    private static final int COLOR_PANEL_BORDER   = 0xFF555555;
    private static final int COLOR_FUNC_BOX_BG    = 0xFF3A3A3A;
    private static final int COLOR_FUNC_BOX_HOVER = 0xFF4A4A4A;
    private static final int COLOR_FUNC_BORDER    = 0xFF666666;
    private static final int COLOR_TEXT           = 0xFFE0E0E0;
    private static final int COLOR_TITLE          = 0xFFFFFFFF;
    private static final int COLOR_DRAG_LINE      = 0xFFFFFFFF;

    private final AdvancedGyroscopeControllerScreen parent;
    private final int[] mapping = new int[]{-1, -1, -1, -1, -1, -1, -1, -1};

    // Drag state
    private int draggingInput = -1;
    private double dragX;
    private double dragY;

    public AdvancedGyroscopeControllerConfigScreen(AdvancedGyroscopeControllerScreen parent) {
        super(Component.translatable("screen.zpl.advanced_gyroscope_controller_config"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        // Load current mapping from block entity (client-side copy)
        BlockEntity be = parent.getMenu().getBlockEntity();
        if (be instanceof AdvancedGyroscopeControllerBlockEntity controller) {
            int[] loaded = controller.getInputFunctionMapping();
            System.arraycopy(loaded, 0, mapping, 0, 8);
        }

        int left = (width - PANEL_WIDTH) / 2;
        int top = (height - PANEL_HEIGHT) / 2;
        addRenderableWidget(new IconButton(
                left + PANEL_WIDTH - CLOSE_BUTTON_SIZE - CLOSE_BUTTON_MARGIN,
                top + CLOSE_BUTTON_MARGIN,
                CLOSE_BUTTON_SIZE,
                CLOSE_ICON_SIZE,
                Component.translatable("button.zpl.advanced_gyroscope_controller_close"),
                button -> onClose(),
                IconButton::renderCloseIcon
        ));
    }

    // ── Geometry helpers ────────────────────────────────────────────────────

    /** Top-left y of a function box (relative to panel). */
    private int funcBoxY(int funcIndex) {
        return CONTENT_TOP + funcIndex * FUNC_SPACING + 3;
    }

    /** Center y of a function box (relative to panel). */
    private int funcCenterY(int funcIndex) {
        return funcBoxY(funcIndex) + FUNC_BOX_HEIGHT / 2;
    }

    /** Top-left y of a dot (relative to panel). */
    private int dotY(int dotIndex) {
        return CONTENT_TOP + dotIndex * DOT_SPACING + 17;
    }

    /** Center y of a dot (relative to panel). */
    private int dotCenterY(int dotIndex) {
        return dotY(dotIndex) + DOT_SIZE / 2;
    }

    /** Left-edge x of a dot (relative to panel). Left inputs: 0-3, right: 4-7. */
    private int dotPanelX(int inputIndex) {
        return inputIndex < 4 ? LEFT_DOT_X : RIGHT_DOT_X;
    }

    /** Center x of a dot (relative to panel). */
    private int dotCenterX(int inputIndex) {
        return dotPanelX(inputIndex) + DOT_SIZE / 2;
    }

    /** Screen-space center x of a dot. */
    private int dotScreenCX(int inputIndex, int panelLeft) {
        return panelLeft + dotCenterX(inputIndex);
    }

    /** Screen-space center y of a dot. */
    private int dotScreenCY(int inputIndex, int panelTop) {
        int dotSlot = inputIndex % 4; // 0-3 within its side
        return panelTop + dotCenterY(dotSlot);
    }

    /** Connection anchor x on the dot side (the edge facing inward). */
    private int dotAnchorX(int inputIndex, int panelLeft) {
        if (inputIndex < 4) {
            // left dot: right edge
            return panelLeft + LEFT_DOT_X + DOT_SIZE;
        } else {
            // right dot: left edge
            return panelLeft + RIGHT_DOT_X;
        }
    }

    /** Connection anchor x on the function box side. Left inputs attach to left edge, right to right edge. */
    private int funcAnchorX(int inputIndex, int panelLeft) {
        if (inputIndex < 4) {
            return panelLeft + FUNC_BOX_X;
        } else {
            return panelLeft + FUNC_BOX_X + FUNC_BOX_WIDTH;
        }
    }

    // ── Hit testing ──────────────────────────────────────────────────────────

    private int hitTestDot(double mouseX, double mouseY, int panelLeft, int panelTop) {
        for (int i = 0; i < 8; i++) {
            int cx = dotScreenCX(i, panelLeft);
            int cy = dotScreenCY(i, panelTop);
            double dx = mouseX - cx;
            double dy = mouseY - cy;
            if (dx * dx + dy * dy <= DOT_HIT_RADIUS * DOT_HIT_RADIUS) {
                return i;
            }
        }
        return -1;
    }

    private int hitTestFuncBox(double mouseX, double mouseY, int panelLeft, int panelTop) {
        int expand = 4;
        for (int i = 0; i < 8; i++) {
            int bx = panelLeft + FUNC_BOX_X - expand;
            int by = panelTop  + funcBoxY(i) - expand;
            int bw = FUNC_BOX_WIDTH + expand * 2;
            int bh = FUNC_BOX_HEIGHT + expand * 2;
            if (mouseX >= bx && mouseX < bx + bw && mouseY >= by && mouseY < by + bh) {
                return i;
            }
        }
        return -1;
    }

    // ── Input ────────────────────────────────────────────────────────────────

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int panelLeft = (width - PANEL_WIDTH) / 2;
        int panelTop  = (height - PANEL_HEIGHT) / 2;
        int dotHit = hitTestDot(mouseX, mouseY, panelLeft, panelTop);
        if (dotHit >= 0) {
            if (button == 1) {
                // Right-click: remove connection
                mapping[dotHit] = -1;
                sendMapping();
                return true;
            } else if (button == 0) {
                // Left-click: start drag
                draggingInput = dotHit;
                dragX = mouseX;
                dragY = mouseY;
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragDeltaX, double dragDeltaY) {
        if (draggingInput >= 0) {
            dragX = mouseX;
            dragY = mouseY;
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragDeltaX, dragDeltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (draggingInput >= 0 && button == 0) {
            int panelLeft = (width - PANEL_WIDTH) / 2;
            int panelTop  = (height - PANEL_HEIGHT) / 2;
            int funcHit = hitTestFuncBox(mouseX, mouseY, panelLeft, panelTop);
            mapping[draggingInput] = funcHit; // -1 if not over any box = disconnect
            sendMapping();
            draggingInput = -1;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private void sendMapping() {
        BlockEntity be = parent.getMenu().getBlockEntity();
        if (be == null) return;
        BlockPos pos = be.getBlockPos();
        ModPackets.CHANNEL.sendToServer(new SetGyroMappingPacket(pos, mapping));
    }

    // ── Rendering ────────────────────────────────────────────────────────────

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        renderBackground(g);

        int panelLeft = (width - PANEL_WIDTH) / 2;
        int panelTop  = (height - PANEL_HEIGHT) / 2;

        // 1. Panel background + border
        g.fill(panelLeft,     panelTop,     panelLeft + PANEL_WIDTH,     panelTop + PANEL_HEIGHT,     COLOR_PANEL_BORDER);
        g.fill(panelLeft + 1, panelTop + 1, panelLeft + PANEL_WIDTH - 1, panelTop + PANEL_HEIGHT - 1, COLOR_PANEL_BG);

        // Title
        g.drawString(font, title, panelLeft + 8, panelTop + TITLE_Y, COLOR_TITLE, false);

        // 2. Existing connections (drawn before boxes/dots so they are behind)
        for (int inputIdx = 0; inputIdx < 8; inputIdx++) {
            int funcIdx = mapping[inputIdx];
            if (funcIdx < 0) continue;
            int color = DOT_COLORS[inputIdx % 4];
            int ax = dotAnchorX(inputIdx, panelLeft);
            int ay = panelTop + dotCenterY(inputIdx % 4);
            int bx = funcAnchorX(inputIdx, panelLeft);
            int by = panelTop + funcCenterY(funcIdx);
            drawConnection(g, ax, ay, bx, by, inputIdx < 4, color);
        }

        // 3. Function boxes
        for (int i = 0; i < 8; i++) {
            int bx = panelLeft + FUNC_BOX_X;
            int by = panelTop + funcBoxY(i);
            boolean hovered = mouseX >= bx && mouseX < bx + FUNC_BOX_WIDTH
                    && mouseY >= by && mouseY < by + FUNC_BOX_HEIGHT;
            g.fill(bx,     by,     bx + FUNC_BOX_WIDTH,     by + FUNC_BOX_HEIGHT,     COLOR_FUNC_BORDER);
            g.fill(bx + 1, by + 1, bx + FUNC_BOX_WIDTH - 1, by + FUNC_BOX_HEIGHT - 1, hovered ? COLOR_FUNC_BOX_HOVER : COLOR_FUNC_BOX_BG);
            int textX = bx + (FUNC_BOX_WIDTH - font.width(FUNCTION_NAMES[i])) / 2;
            int textY = by + (FUNC_BOX_HEIGHT - 8) / 2;
            g.drawString(font, FUNCTION_NAMES[i], textX, textY, COLOR_TEXT, false);
        }

        // 4. Input dots
        for (int i = 0; i < 8; i++) {
            int cx = dotScreenCX(i, panelLeft);
            int cy = dotScreenCY(i, panelTop);
            int color = DOT_COLORS[i % 4];
            drawDot(g, cx, cy, DOT_SIZE / 2, color);
            // Small label beside dot
            String label = INPUT_LABELS[i];
            if (i < 4) {
                g.drawString(font, label, cx + DOT_SIZE / 2 + 2, cy - 4, color, false);
            } else {
                int lw = font.width(label);
                g.drawString(font, label, cx - DOT_SIZE / 2 - 2 - lw, cy - 4, color, false);
            }
        }

        // 5. Active drag line (on top of dots and boxes)
        if (draggingInput >= 0) {
            int ax = dotAnchorX(draggingInput, panelLeft);
            int ay = panelTop + dotCenterY(draggingInput % 4);
            drawConnection(g, ax, ay, (int) dragX, (int) dragY, draggingInput < 4, COLOR_DRAG_LINE);
        }

        // 6. Widgets (close button)
        super.render(g, mouseX, mouseY, partialTick);
    }

    /** Draw a filled circle approximation using overlapping rectangles. */
    private void drawDot(GuiGraphics g, int cx, int cy, int radius, int color) {
        for (int dy = -radius; dy <= radius; dy++) {
            int halfWidth = (int) Math.sqrt(radius * radius - dy * dy);
            g.fill(cx - halfWidth, cy + dy, cx + halfWidth + 1, cy + dy + 1, color);
        }
    }

    /** Draw a cubic Bézier connection curve as a series of small filled squares. */
    private void drawConnection(GuiGraphics g, int x0, int y0, int x1, int y1, boolean leftToRight, int color) {
        int bend = Math.max(20, Math.abs(x1 - x0) / 2);
        float cx0, cy0, cx1, cy1;
        if (leftToRight) {
            cx0 = x0 + bend;
            cy0 = y0;
            cx1 = x1 - bend;
            cy1 = y1;
        } else {
            cx0 = x0 - bend;
            cy0 = y0;
            cx1 = x1 + bend;
            cy1 = y1;
        }
        int steps = 40;
        float prevX = x0, prevY = y0;
        for (int step = 1; step <= steps; step++) {
            float t = step / (float) steps;
            float mt = 1 - t;
            float nx = mt * mt * mt * x0 + 3 * mt * mt * t * cx0 + 3 * mt * t * t * cx1 + t * t * t * x1;
            float ny = mt * mt * mt * y0 + 3 * mt * mt * t * cy0 + 3 * mt * t * t * cy1 + t * t * t * y1;
            // Draw a 2×2 square at this point for a thick line feel
            int px = Math.round(nx);
            int py = Math.round(ny);
            g.fill(px, py, px + 2, py + 2, color);
            prevX = nx;
            prevY = ny;
        }
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
