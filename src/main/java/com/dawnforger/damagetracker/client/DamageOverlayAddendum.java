package com.dawnforger.damagetracker.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public final class DamageOverlayAddendum {

    private static boolean firstClampDone = false;

    private DamageOverlayAddendum() {}

    /** Call from your DamageOverlay.renderHud(...) at the beginning. */
    public static boolean shouldRenderAndClampPosition(GuiGraphics g) {
        if (!ClientConfig.OVERLAY_ENABLED_DEFAULT.get()) return false;

        if (!firstClampDone) {
            var window = Minecraft.getInstance().getWindow();
            int sw = window.getGuiScaledWidth();
            int sh = window.getGuiScaledHeight();
            int x = ClientConfig.OVERLAY_X.get();
            int y = ClientConfig.OVERLAY_Y.get();
            int safeMargin = 10;

            if (x > sw - safeMargin) x = Math.max(10, sw - 200);
            if (y > sh - safeMargin) y = Math.max(10, sh - 120);

            ClientConfig.OVERLAY_X.set(x);
            ClientConfig.OVERLAY_Y.set(y);
            firstClampDone = true;
        }
        return true;
    }
}
