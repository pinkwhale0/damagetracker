package com.dawnforger.damagetracker.client;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "damagetracker", bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ClientConfig {

    public static final ForgeConfigSpec SPEC;

    // Overlay / behavior
    public static final ForgeConfigSpec.IntValue TIME_WINDOW_MS;
    public static final ForgeConfigSpec.BooleanValue ROLLING_WINDOW_ENABLED;
    public static final ForgeConfigSpec.IntValue TOP_N_SOURCES;
    public static final ForgeConfigSpec.IntValue OVERLAY_X;
    public static final ForgeConfigSpec.IntValue OVERLAY_Y;
    public static final ForgeConfigSpec.IntValue OVERLAY_MAX_WIDTH;
    public static final ForgeConfigSpec.IntValue OVERLAY_MAX_HEIGHT;
    public static final ForgeConfigSpec.BooleanValue OVERLAY_ENABLED_DEFAULT;

    // Background style
    public static final ForgeConfigSpec.BooleanValue OVERLAY_BG_ENABLED;
    public static final ForgeConfigSpec.IntValue OVERLAY_BG_COLOR;   // ARGB
    public static final ForgeConfigSpec.IntValue OVERLAY_BG_ALPHA;   // 0-255
    public static final ForgeConfigSpec.IntValue OVERLAY_BG_PADDING; // px

    // Row/bar style
    public static final ForgeConfigSpec.IntValue ROW_BAR_ALPHA;      // 0-255
    public static final ForgeConfigSpec.IntValue ROW_HEIGHT;         // px
    public static final ForgeConfigSpec.IntValue ROW_GAP;            // px
    public static final ForgeConfigSpec.IntValue ROW_VALUE_GAP;      // px

    // Capture
    public static final ForgeConfigSpec.BooleanValue REQUIRE_SELF_ONLY;
    public static final ForgeConfigSpec.IntValue DEDUPE_MS;
    public static final ForgeConfigSpec.BooleanValue PREFER_DEALT_OVER_APPLIED;
    public static final ForgeConfigSpec.IntValue IDLE_FREEZE_MS;

    // Element Colors
    public static final ForgeConfigSpec.IntValue COLOR_PHYSICAL;     // Orange
    public static final ForgeConfigSpec.IntValue COLOR_FIRE;         // Red
    public static final ForgeConfigSpec.IntValue COLOR_LIGHTNING;    // Yellow
    public static final ForgeConfigSpec.IntValue COLOR_COLD;         // Light Blue
    public static final ForgeConfigSpec.IntValue COLOR_NATURE;       // (unchanged)
    public static final ForgeConfigSpec.IntValue COLOR_SHADOW;       // (unchanged)
    public static final ForgeConfigSpec.IntValue COLOR_HOLY;         // (unchanged)
    public static final ForgeConfigSpec.IntValue COLOR_MULTI;        // Violet
    public static final ForgeConfigSpec.IntValue COLOR_CHAOS;        // Purple

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();

        b.push("overlay");
        TIME_WINDOW_MS = b.defineInRange("timeWindowMs", 10_000, 1_000, 300_000);
        ROLLING_WINDOW_ENABLED = b.define("rollingWindowEnabled", true);
        TOP_N_SOURCES = b.defineInRange("topNSources", 10, 1, 15);
        OVERLAY_X = b.defineInRange("x", 10, 0, 10_000);
        OVERLAY_Y = b.defineInRange("y", 10, 0, 10_000);
        OVERLAY_MAX_WIDTH = b.defineInRange("maxWidth", 300, 120, 1_000);
        OVERLAY_MAX_HEIGHT = b.defineInRange("maxHeight", 400, 80, 2_000);
        OVERLAY_ENABLED_DEFAULT = b.define("enabledByDefault", true);
        b.pop();

        b.push("overlayBg");
        OVERLAY_BG_ENABLED = b.define("enabled", true);
        // Flat green, zero transparency (opaque). ARGB is kept, alpha below also set to 255.
        OVERLAY_BG_COLOR = b.defineInRange("colorARGB", 0xFF666666, Integer.MIN_VALUE, Integer.MAX_VALUE);
        OVERLAY_BG_ALPHA = b.defineInRange("alpha", 255, 0, 255);
        OVERLAY_BG_PADDING = b.defineInRange("padding", 6, 0, 32);
        b.pop();

        b.push("rowStyle");
        ROW_BAR_ALPHA = b.defineInRange("barAlpha", 200, 0, 255);
        ROW_HEIGHT = b.defineInRange("height", 14, 10, 40);
        ROW_GAP = b.defineInRange("gap", 2, 0, 12);
        ROW_VALUE_GAP = b.defineInRange("valueGap", 8, 2, 40);
        b.pop();

        b.push("capture");
        REQUIRE_SELF_ONLY = b.define("requireSelfOnly", true);
        DEDUPE_MS = b.defineInRange("dedupeMs", 150, 0, 1000);
        PREFER_DEALT_OVER_APPLIED = b.define("preferDealtOverApplied", true);
        IDLE_FREEZE_MS = b.defineInRange("idleFreezeMs", 1500, 0, 60_000);
        b.pop();

        b.push("colors");
        final int MIN = Integer.MIN_VALUE, MAX = Integer.MAX_VALUE;
        // Updated defaults per request
        COLOR_PHYSICAL  = b.defineInRange("physical",  0xFFFFA500, MIN, MAX); // Orange
        COLOR_FIRE      = b.defineInRange("fire",      0xFFFF0000, MIN, MAX); // Red
        COLOR_LIGHTNING = b.defineInRange("lightning", 0xFFFFFF00, MIN, MAX); // Yellow
        COLOR_COLD      = b.defineInRange("cold",      0xFF66CCFF, MIN, MAX); // Light Blue
        COLOR_NATURE    = b.defineInRange("nature",    0xFF6EDC6E, MIN, MAX); // keep
        COLOR_SHADOW    = b.defineInRange("shadow",    0xFFB266FF, MIN, MAX); // keep
        COLOR_HOLY      = b.defineInRange("holy",      0xFFFFF3B0, MIN, MAX); // keep
        COLOR_MULTI     = b.defineInRange("multi",     0xFF8A2BE2, MIN, MAX); // Violet
        COLOR_CHAOS     = b.defineInRange("chaos",     0xFF800080, MIN, MAX); // Purple
        b.pop();

        SPEC = b.build();
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, SPEC, "damagetracker-client.toml");
    }

    private ClientConfig() {}
}
