package net.nuclearteam.createnuclear.foundation.utility;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class RenderTextureOverlay {
    public static void renderTextureOverlay(GuiGraphics guiGraphics, ResourceLocation texture, float alpha) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        guiGraphics.setColor(1f, 1f, 1f, alpha);
        guiGraphics.blit(texture, 0, 0, -90, 0.0f, 0.0f, guiGraphics.guiWidth(), guiGraphics.guiHeight(), guiGraphics.guiWidth(), guiGraphics.guiHeight());
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
