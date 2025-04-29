package net.nuclearteam.createnuclear.foundation.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Central registry for all HudOverlay instances.
 * Overlays auto-register themselves via a static block in their class.
 */
public class HudOverlayRegistry {
    private static final List<HudOverlay> OVERLAYS = new ArrayList<>();

    /**
     * Registers a HudOverlay instance.
     *
     * @param overlay the HudOverlay to register
     */
    public static void register(HudOverlay overlay) {
        OVERLAYS.add(overlay);
    }

    /**
     * Returns an unmodifiable list of all registered overlays.
     *
     * @return list of registered overlays
     */
    public static List<HudOverlay> getAll() {
        return Collections.unmodifiableList(OVERLAYS);
    }
}
