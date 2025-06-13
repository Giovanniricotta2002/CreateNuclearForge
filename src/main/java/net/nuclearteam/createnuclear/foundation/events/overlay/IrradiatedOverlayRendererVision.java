package net.nuclearteam.createnuclear.foundation.events.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GameType;
import net.nuclearteam.createnuclear.CNEffects;
import net.nuclearteam.createnuclear.CreateNuclear;

/**
 * Handles the client-side rendering of a radiation vision overlay
 * when radiation affects the player.
 * <p>
 * This class provides a {@link LayeredDraw.Layer} to be injected into GUI overlays,
 * and manages the fade-in/fade-out alpha blending of the overlay.
 */
public class IrradiatedOverlayRendererVision {

    /**
     * The current transparency level of the irradiated vision overlay.
     * This should ideally be tracked per-player but is currently static.
     */
    private static float irradiatedVisionAlpha = 0.0f;

    /**
     * The texture resource location for the irradiated vision overlay.
     */
    public static final ResourceLocation IRRADIATED_VISION =
            CreateNuclear.asResource("textures/misc/irradiated_vision/irradiated_vision.png");

    /**
     * Returns a {@link LayeredDraw.Layer} implementation that renders the irradiated vision overlay.
     * This method is used to register the overlay with the game's layered renderer system.
     *
     * @return a Layer that renders the overlay when the radiation effect is active.
     */
    public static LayeredDraw.Layer getOverlay() {
        return IrradiatedOverlayRendererVision::renderOverlay;
    }

    /**
     * Renders the overlay if radiation affects the player.
     * This method increases or decreases the alpha level based on the presence of the radiation effect.
     * It is invoked each frame by the layered GUI system.
     *
     * @param guiGraphics the GUI graphics context
     * @param deltaTracker a tracker for frame delta/time
     */
    public static void renderOverlay(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.options.hideGui || mc.gameMode.getPlayerMode() == GameType.SPECTATOR)
            return;

        LocalPlayer localPlayer = mc.player;
        RenderSystem.enableBlend();

        // Adjust the overlay alpha depending on whether the radiation effect is active
        if (localPlayer.hasEffect(CNEffects.RADIATION.getDelegate())) {
            irradiatedVisionAlpha = Math.min(1.0f, irradiatedVisionAlpha + 0.01f);
        } else {
            irradiatedVisionAlpha = Math.max(0.0f, irradiatedVisionAlpha - 0.01f);
        }

        // Render the overlay if the alpha is greater than 0
        if (irradiatedVisionAlpha > 0.0f) {
            renderTextureOverlay(guiGraphics, IRRADIATED_VISION, irradiatedVisionAlpha, true);
        }
    }

    /**
     * Renders a full-screen texture overlay with the specified alpha and rendering conditions.
     *
     * @param guiGraphics      the GUI graphics context
     * @param texture          the texture to render
     * @param alpha            the transparency level (0.0 to 1.0)
     * @param onlyFirstPerson  whether the overlay should only render in first-person view
     */
    public static void renderTextureOverlay(GuiGraphics guiGraphics, ResourceLocation texture, float alpha, boolean onlyFirstPerson) {
        boolean isFirstPerson = Minecraft.getInstance().options.getCameraType().isFirstPerson();

        // Skip rendering if restricted to first-person and not in first-person view
        if (onlyFirstPerson && !isFirstPerson)
            return;

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);

        guiGraphics.setColor(1f, 1f, 1f, alpha);
        guiGraphics.blit(
                texture,
                0, 0,
                -90,
                0.0f, 0.0f,
                guiGraphics.guiWidth(), guiGraphics.guiHeight(),
                guiGraphics.guiWidth(), guiGraphics.guiHeight()
        );

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
