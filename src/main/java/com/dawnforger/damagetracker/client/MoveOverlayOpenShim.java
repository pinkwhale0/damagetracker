package com.dawnforger.damagetracker.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Opens the existing OverlayMoveScreen when the "Move/Edit Overlay" keybind is pressed.
 * This uses consumeClick() on client tick and ensures the mouse is released so dragging works.
 *
 * Drop-in: safe alongside existing handlers. If your existing handler was using isDown(), this fixes it.
 */
@Mod.EventBusSubscriber(modid = "damagetracker", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class MoveOverlayOpenShim {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;
        final Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        if (Keybinds.MOVE_OVERLAY != null && Keybinds.MOVE_OVERLAY.consumeClick()) {
            // Make sure cursor is visible while the Screen is open.
            mc.mouseHandler.releaseMouse();
            mc.setScreen(new OverlayMoveScreen());
        }
    }
}