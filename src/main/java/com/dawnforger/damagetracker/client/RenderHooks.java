package com.dawnforger.damagetracker.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Ensures our HUD overlay actually renders each frame.
 */
@Mod.EventBusSubscriber(modid = "damagetracker", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class RenderHooks {

    private RenderHooks() {}

    @SubscribeEvent
    public static void onRenderGui(final net.minecraftforge.client.event.RenderGuiEvent.Post e) {
        DamageOverlay.renderHud(e.getGuiGraphics(), e.getPartialTick());
    }
}
