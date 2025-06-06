package net.nuclearteam.createnuclear.foundation.events.overlay;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

/**
 * Base interface for all HUD overlays.
 */
public interface HudOverlay {
    ResourceLocation getAfterOverlay();

    ResourceLocation getOverlayId();

    boolean isActive();

    int getPriority();

    LayeredDraw.Layer getOverlay();

    void render(GuiGraphics gui, GuiGraphics graphics, float partialTicks, int width, int height);

    default void register(RegisterGuiLayersEvent event) {
        event.registerAbove(
                getAfterOverlay(),
                getOverlayId(),
                getOverlay()
        );
    }
}
