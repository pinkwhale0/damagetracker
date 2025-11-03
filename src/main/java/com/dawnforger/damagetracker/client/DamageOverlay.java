package com.dawnforger.damagetracker.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

import java.util.List;

public final class DamageOverlay {

    private DamageOverlay() {}

    public static void renderHud(GuiGraphics g, float partialTick) {
        if (!ClientConfig.OVERLAY_ENABLED_DEFAULT.get()) return;

        int x = ClientConfig.OVERLAY_X.get();
        int y = ClientConfig.OVERLAY_Y.get();
        renderAt(g, x, y, false);
    }

    public static void renderAt(GuiGraphics g, int x, int y, boolean showFrame) {
        int windowMs = ClientConfig.TIME_WINDOW_MS.get();
        int topN = ClientConfig.TOP_N_SOURCES.get();
        List<ClientDamageStore.Row> rows = ClientDamageStore.topSkillsForDisplay(topN);
        double total = ClientDamageStore.totalForDisplay();
        double dps = ClientDamageStore.dpsForDisplay();

        var font = Minecraft.getInstance().font;
        int padding = ClientConfig.OVERLAY_BG_PADDING.get();
        int rowH = Math.max(ClientConfig.ROW_HEIGHT.get(), font.lineHeight);
        int rowGap = ClientConfig.ROW_GAP.get();
        int valueGap = ClientConfig.ROW_VALUE_GAP.get();

        String header = String.format("Damage: %s   DPS: %s", fmt(total), fmt(dps));

        int labelColWidth = font.width(header);
        int valuesColWidth = 0;
        for (ClientDamageStore.Row r : rows) {
            labelColWidth = Math.max(labelColWidth, font.width(r.label));
            String values = valueText(r, total);
            valuesColWidth = Math.max(valuesColWidth, font.width(values));
        }
        int boxW = padding + labelColWidth + valueGap + valuesColWidth + padding;
        int boxH = padding + font.lineHeight + rowGap + rows.size() * (rowH + rowGap) + padding;

        if (ClientConfig.OVERLAY_BG_ENABLED.get()) {
            int argb = ClientConfig.OVERLAY_BG_COLOR.get();
            int a = Mth.clamp(ClientConfig.OVERLAY_BG_ALPHA.get(), 0, 255);
            int bg = (a << 24) | (argb & 0x00FFFFFF);
            g.fill(x, y, x + boxW, y + boxH, bg);
        }

        if (showFrame) {
            g.fill(x, y, x + boxW, y + 1, 0xFFFFFFFF);
            g.fill(x, y + boxH - 1, x + boxW, y + boxH, 0xFFFFFFFF);
            g.fill(x, y, x + 1, y + boxH, 0xFFFFFFFF);
            g.fill(x + boxW - 1, y, x + boxW, y + boxH, 0xFFFFFFFF);
        }

        int ty = y + padding;
        g.drawString(font, header, x + padding, ty, 0xFF000000, false);
        ty += font.lineHeight + rowGap;

        int labelX = x + padding;
        int valuesRightX = x + boxW - padding;
        int innerLeft = x + padding;
        int innerRight = x + boxW - padding;
        int barWidthMax = innerRight - innerLeft;

        for (ClientDamageStore.Row r : rows) {
            double pct = (total > 0.0) ? (r.total / total) : 0.0;

            int base = ElementColors.colorFor(r.element);
            int alpha = Mth.clamp(ClientConfig.ROW_BAR_ALPHA.get(), 0, 255);
            int barColor = (alpha << 24) | (base & 0x00FFFFFF);

            int bh = rowH;
            int bw = (int)Math.round(barWidthMax * pct);
            int by = ty;
            g.fill(innerLeft, by, innerLeft + bw, by + bh, barColor);

            g.drawString(font, r.label, labelX, by + (bh - font.lineHeight) / 2, 0xFF000000, false);

            String values = valueText(r, total);
            int valuesW = font.width(values);
            g.drawString(font, values, valuesRightX - valuesW, by + (bh - font.lineHeight) / 2, 0xFF000000, false);

            ty += bh + rowGap;
        }
    }

    private static String valueText(ClientDamageStore.Row r, double grandTotal) {
        double pct = (grandTotal > 0.0) ? (r.total / grandTotal * 100.0) : 0.0;
        return fmt(r.total) + "   " + fmt(r.dps) + "/s   (" + String.format("%.1f", pct) + "%)";
    }

    public static OverlayMoveScreen.Bounds currentBounds(int x, int y) {
        var font = Minecraft.getInstance().font;
        int padding = ClientConfig.OVERLAY_BG_PADDING.get();
        int rowH = Math.max(ClientConfig.ROW_HEIGHT.get(), font.lineHeight);
        int rowGap = ClientConfig.ROW_GAP.get();
        int valueGap = ClientConfig.ROW_VALUE_GAP.get();

        int labelColWidth = font.width("Some Very Long Ability Name");
        int valuesColWidth = font.width("999.9k   99.9k/s   (100.0%)");
        int boxW = padding + labelColWidth + valueGap + valuesColWidth + padding;

        int topN = ClientConfig.TOP_N_SOURCES.get();
        int boxH = padding + font.lineHeight + rowGap + topN * (rowH + rowGap) + padding;

        return new OverlayMoveScreen.Bounds(x, y, boxW, boxH);
    }

    private static String fmt(double v) {
        if (v >= 1_000_000) return String.format("%.1fM", v / 1_000_000.0);
        if (v >= 1_000) return String.format("%.1fk", v / 1_000.0);
        if (v >= 100) return String.format("%.0f", v);
        if (v >= 10) return String.format("%.1f", v);
        return String.format("%.2f", v);
    }
    public static boolean isEnabled() {
        return ClientConfig.OVERLAY_ENABLED_DEFAULT.get();
    }

    public static void setEnabled(boolean v) {
        ClientConfig.OVERLAY_ENABLED_DEFAULT.set(v);
    }
}
