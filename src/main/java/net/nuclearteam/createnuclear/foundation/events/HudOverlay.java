package net.nuclearteam.createnuclear.foundation.events;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

/**
 * Base interface for all HUD overlays.
 */
public interface HudOverlay {
    /**
     * Returns the ResourceLocation of the vanilla (or custom) overlay
     * after which this overlay should be rendered.
     *
     * @return the anchor overlay ID
     */
    ResourceLocation getAfterOverlay();

    /**
     * Determines whether this overlay should be rendered.
     * @return true if the overlay is active, false otherwise.
     */
    boolean isActive();

    /**
     * Renders the overlay.
     * @param graphics the GUI graphics context
     * @param partialTicks frame interpolation value
     */
    void render(GuiGraphics graphics, float partialTicks);

    /**
     * Computes the dynamic rendering priority of the overlay.
     * Lower values render behind higher values.
     * @return integer priority value
     */
    int getPriority();
}