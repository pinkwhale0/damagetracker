package com.dawnforger.damagetracker.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

// Use the real MODID constant to avoid annotation mismatch.
import com.dawnforger.damagetracker.DamageTracker;

@Mod.EventBusSubscriber(modid = DamageTracker.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class Keybinds {

    private static final Logger LOG = LogManager.getLogger("DamageTracker/Keybinds");

    public static KeyMapping TOGGLE_OVERLAY;
    public static KeyMapping MOVE_OVERLAY;
    public static KeyMapping CLEAR_WINDOW;
    public static KeyMapping OPEN_REPORT;

    private Keybinds() {}

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent e) {
        final String CAT = "key.categories.damagetracker";

        TOGGLE_OVERLAY = new KeyMapping(
                "key.damagetracker.toggle",
                KeyConflictContext.IN_GAME,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_SLASH, // '/'
                CAT
        );

        MOVE_OVERLAY = new KeyMapping(
                "key.damagetracker.move",
                KeyConflictContext.IN_GAME,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_MINUS, // '-'
                CAT
        );

        CLEAR_WINDOW = new KeyMapping(
                "key.damagetracker.clear",
                KeyConflictContext.IN_GAME,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_KP_MULTIPLY, // '*'
                CAT
        );

        OPEN_REPORT = new KeyMapping(
                "key.damagetracker.report",
                KeyConflictContext.IN_GAME,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN, // unbound
                CAT
        );

        e.register(TOGGLE_OVERLAY);
        e.register(MOVE_OVERLAY);
        e.register(CLEAR_WINDOW);
        e.register(OPEN_REPORT);
        LOG.info("[DT] Keybinds registered (/, -, numpad *, report=unbound).");
    }

    @Mod.EventBusSubscriber(modid = DamageTracker.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static final class ClientHandler {

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent evt) {
            if (evt.phase != TickEvent.Phase.END) return;
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return;
            if (mc.screen != null) return;

            boolean any = false;

            if (TOGGLE_OVERLAY != null && TOGGLE_OVERLAY.consumeClick()) {
                any = true;
                boolean now = !DamageOverlay.isEnabled();
                DamageOverlay.setEnabled(now);
                LOG.info("[DT] Toggle Overlay -> {}", now);
            }
            if (MOVE_OVERLAY != null && MOVE_OVERLAY.consumeClick()) {
                any = true;
                LOG.info("[DT] Move/Edit Overlay pressed. Opening move screen.");
                try {
                    mc.setScreen(new OverlayMoveScreen());
                } catch (Throwable t) {
                    LOG.warn("[DT] Failed to open OverlayMoveScreen", t);
                }
            }
            if (CLEAR_WINDOW != null && CLEAR_WINDOW.consumeClick()) {
                any = true;
                LOG.info("[DT] Clear Window pressed. Clearing client store.");
                try {
                    ClientDamageStore.clear();
                } catch (Throwable t) {
                    LOG.warn("[DT] Failed to clear ClientDamageStore", t);
                }
            }
            if (OPEN_REPORT != null && OPEN_REPORT.consumeClick()) {
                any = true;
                LOG.info("[DT] Open Report pressed.");
                try {
                    mc.setScreen(new DamageReportScreen());
                } catch (Throwable t) {
                    LOG.warn("[DT] Report screen not available.", t);
                }
            }

            // Light heartbeat to confirm tick firing if nothing else
            if (!any && (mc.level != null) && (mc.level.getGameTime() % 200 == 0)) {
                LOG.debug("[DT] Keybind tick heartbeat ok.");
            }
        }
    }
}
