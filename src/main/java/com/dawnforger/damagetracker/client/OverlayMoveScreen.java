package com.dawnforger.damagetracker.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class OverlayMoveScreen extends Screen {

    private boolean dragging = false;
    private int dragOffX = 0;
    private int dragOffY = 0;

    public OverlayMoveScreen() {
        super(Component.literal("Damage Tracker - Move Overlay"));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return super.mouseClicked(mouseX, mouseY, button);

        // Start dragging regardless of precise hit; easier to grab.
        int x = ClientConfig.OVERLAY_X.get();
        int y = ClientConfig.OVERLAY_Y.get();
        dragging = true;
        dragOffX = (int)mouseX - x;
        dragOffY = (int)mouseY - y;
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        if (!dragging || button != 0) return super.mouseDragged(mouseX, mouseY, button, dx, dy);

        var window = Minecraft.getInstance().getWindow();
        int sw = window.getGuiScaledWidth();
        int sh = window.getGuiScaledHeight();

        int newX = Math.max(0, Math.min(sw - 10, (int)mouseX - dragOffX));
        int newY = Math.max(0, Math.min(sh - 10, (int)mouseY - dragOffY));
        ClientConfig.OVERLAY_X.set(newX);
        ClientConfig.OVERLAY_Y.set(newY);
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 && dragging) {
            dragging = false;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(g);

        int x = ClientConfig.OVERLAY_X.get();
        int y = ClientConfig.OVERLAY_Y.get();

        // Instruction banner
        g.fill(6, 6, this.width - 6, 28, 0xAA000000);
        g.drawString(Minecraft.getInstance().font, "Drag anywhere to move overlay. Esc to save & close.", 10, 12, 0xFFFFFFFF, false);

        // Anchor/preview rectangle (helps grabbing even if overlay preview fails)
        // Use DamageOverlay.currentBounds(x, y) if present to get size
        int w = 220, h = 140;
        try {
            Bounds b = DamageOverlay.currentBounds(x, y);
            if (b != null) { w = b.w; h = b.h; }
        } catch (Throwable ignored) {}

        g.fill(x - 4, y - 4, x + w + 4, y + h + 4, 0x33000000);
        g.drawString(Minecraft.getInstance().font, "Overlay anchor", x + 4, y - 12, 0xFFFFFFFF, false);

        try {
            DamageOverlay.renderAt(g, x, y, true);
        } catch (Throwable t) {
            // ignore
        }

        super.render(g, mouseX, mouseY, partialTick);
    }

    /** Keep this class for API compatibility with DamageOverlay.currentBounds(...) */
    public static final class Bounds {
        public final int x, y, w, h;
        public Bounds(int x, int y, int w, int h) { this.x=x; this.y=y; this.w=w; this.h=h; }
        public boolean contains(int mx, int my) {
            return mx >= x && my >= y && mx < x + w && my < y + h;
        }
    }
}
