package com.dawnforger.damagetracker.client;

public final class ElementColors {
    private ElementColors() {}

    /**
     * Returns ARGB color for an element name.
     * Accepts synonyms across M&S versions and UI strings.
     * - Chaos == Shadow
     * - Lightning == Thunder
     * - Cold == Ice
     * - Nature == Poison
     */
    public static int colorFor(String elementName) {
        if (elementName == null) return ClientConfig.COLOR_MULTI.get();
        String e = elementName.toLowerCase();

        // Normalize well-known synonyms
        if (e.contains("thunder")) e = "lightning";
        if (e.contains("ice"))     e = "cold";
        if (e.contains("poison"))  e = "nature";
        if (e.contains("shadow"))  e = "chaos";   // <— unify Shadow -> Chaos

        // Map
        if (e.contains("lightning")) return ClientConfig.COLOR_LIGHTNING.get();
        if (e.contains("fire"))      return ClientConfig.COLOR_FIRE.get();
        if (e.contains("cold"))      return ClientConfig.COLOR_COLD.get();
        if (e.contains("nature"))    return ClientConfig.COLOR_NATURE.get();
        if (e.contains("chaos"))     return ClientConfig.COLOR_CHAOS.get(); // Chaos/Shadow
        if (e.contains("holy") || e.contains("radiant")) return ClientConfig.COLOR_HOLY.get();
        if (e.contains("phys"))      return ClientConfig.COLOR_PHYSICAL.get();
        if (e.contains("multi"))     return ClientConfig.COLOR_MULTI.get();

        return ClientConfig.COLOR_MULTI.get();
    }
}
