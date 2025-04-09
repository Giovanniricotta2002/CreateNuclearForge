package net.nuclearteam.createnuclear.content.equipment.armor;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.GameType;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.nuclearteam.createnuclear.CNTags.CNItemTags;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.foundation.utility.RenderTextureOverlay;

public class HelmetOverlayRenderer {
    public static final IGuiOverlay OVERLAY = HelmetOverlayRenderer::renderOverlay;

    public static ResourceLocation helmetVision = CreateNuclear.asResource("textures/misc/helmet_vision/helmet_vision.png");
    private ResourceLocation helmetVisionDamage1 = CreateNuclear.asResource("textures/misc/helmet_vision/helmet_vision_damage1.png");
    private ResourceLocation helmetVisionDamage2 = CreateNuclear.asResource("textures/misc/helmet_vision/helmet_vision_damage2.png");
    private ResourceLocation helmetVisionDamage3 = CreateNuclear.asResource("textures/misc/helmet_vision/helmet_vision_damage3.png");
    private ResourceLocation helmetVisionBreak = CreateNuclear.asResource("textures/misc/helmet_vision/helmet_vision_break.png");

    public static void renderOverlay(ForgeGui gui, GuiGraphics graphics, float partialTicks, int width, int height) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui || mc.gameMode.getPlayerMode() == GameType.SPECTATOR) return;

        LocalPlayer localPlayer = mc.player;
        RenderSystem.enableBlend();
        Inventory playerInventory = localPlayer.getInventory();

        if (playerInventory.getArmor(3).is(CNItemTags.ANTI_RADIATION_HELMET_FULL_DYE.tag))  {
            RenderTextureOverlay.renderTextureOverlay(graphics, HelmetOverlayRenderer.helmetVision, 1f);
        }
    }
}
