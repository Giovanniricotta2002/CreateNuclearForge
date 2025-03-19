package net.nuclearteam.createnuclear.foundation.events;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GameType;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.nuclearteam.createnuclear.CNEffects;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.foundation.utility.RenderTextureOverlay;

public class IrradiatedOverlayRendererVision {
    public static final IGuiOverlay OVERLAY = IrradiatedOverlayRendererVision::renderOverlay;

    public static final ResourceLocation IRRADIATED_VISION = CreateNuclear.asResource("textures/misc/irradiated_vision/effect_radioactive.png");

    public static void renderOverlay(ForgeGui gui, GuiGraphics graphics, float partialTicks, int width, int height) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui || mc.gameMode.getPlayerMode() == GameType.SPECTATOR) return;

        LocalPlayer localPlayer = mc.player;
        RenderSystem.enableBlend();

        if (localPlayer.hasEffect(CNEffects.RADIATION.get())) {
            RenderTextureOverlay.renderTextureOverlay(graphics, IrradiatedOverlayRendererVision.IRRADIATED_VISION, 1f);
        }
    }
}
