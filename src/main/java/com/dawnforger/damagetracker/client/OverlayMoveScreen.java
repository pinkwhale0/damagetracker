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

        int x = ClientConfig.OVERLAY_X.get();
        int y = ClientConfig.OVERLAY_Y.get();
        Bounds b = DamageOverlay.currentBounds(x, y);

        if (b.contains((int)mouseX, (int)mouseY)) {
            dragging = true;
            dragOffX = (int)mouseX - x;
            dragOffY = (int)mouseY - y;
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        if (!dragging || button != 0) return super.mouseDragged(mouseX, mouseY, button, dx, dy);
        int newX = Math.max(0, (int)mouseX - dragOffX);
        int newY = Math.max(0, (int)mouseY - dragOffY);
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

        g.drawString(Minecraft.getInstance().font, "Drag the overlay to reposition. Esc to save & close.", 10, 10, 0xFFFFFFFF, false);

        DamageOverlay.renderAt(g, x, y, true);
        super.render(g, mouseX, mouseY, partialTick);
    }

    public static final class Bounds {
        public final int x, y, w, h;
        public Bounds(int x, int y, int w, int h) { this.x=x; this.y=y; this.w=w; this.h=h; }
        public boolean contains(int mx, int my) {
            return mx >= x && my >= y && mx < x + w && my < y + h;
        }
    }
}
