package net.nuclearteam.createnuclear.foundation.events.overlay;

import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.nuclearteam.createnuclear.CreateNuclear;

/**
 * HUD overlay for displaying localized text when a specific event occurs.
 */
public class EventTextOverlay implements HudOverlay {
    private static int timer = 0;

    /**
     * Called via a network packet to activate the overlay for a specific duration.
     * @param displayDuration duration in ticks
     */
    public static void triggerEvent(int displayDuration) {
        timer = displayDuration;
    }

    @Override
    public ResourceLocation getAfterOverlay() {
        return VanillaGuiLayers.CAMERA_OVERLAYS;
    }

    @Override
    public ResourceLocation getOverlayId() {
        return ResourceLocation.parse("event_text_overlay");
    }

    @Override
    public boolean isActive() {
        return timer > 0 && false;
    }

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (!isActive()) return;
        if (timer-- <= 0) return;
        CreateNuclear.LOGGER.warn("hum EventTextOverlay: {}", timer);
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        Component text = Component.translatable("overlay.event_message", timer).withStyle(ChatFormatting.RED);
        int widths = guiGraphics.guiWidth();
        int x = (widths - Minecraft.getInstance().font.width(text)) / 2;
        guiGraphics.drawString(Minecraft.getInstance().font, text, x, 10, 0xFFFFFF);
    }

    @Override
    public int getPriority() {
        return 300; // render on top of other overlays
    }

    @Override
    public LayeredDraw.Layer getOverlay() {
        return this::render;
    }
}
