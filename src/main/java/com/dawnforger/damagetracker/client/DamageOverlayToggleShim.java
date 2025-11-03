package com.dawnforger.damagetracker.client;

public final class DamageOverlayToggleShim {
    private DamageOverlayToggleShim() {}

    public static boolean isEnabled() {
        return ClientConfig.OVERLAY_ENABLED_DEFAULT.get();
    }
    public static void setEnabled(boolean v) {
        ClientConfig.OVERLAY_ENABLED_DEFAULT.set(v);
    }
}
