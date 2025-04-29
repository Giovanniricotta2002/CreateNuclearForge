package net.nuclearteam.createnuclear.foundation.events;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.nuclearteam.createnuclear.CNEntityType;
import net.nuclearteam.createnuclear.content.equipment.armor.HelmetOverlayRenderer;

import java.util.Comparator;

@EventBusSubscriber(Dist.CLIENT)
public class CNClientEvent {
    /**
     * Called after the vanilla helmet overlay is rendered.
     * Dispatches all registered HudOverlay instances.
     *
     * @param event the RenderGuiOverlayEvent.Post event
     */
    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiOverlayEvent.Post event) {
        // Only proceed if we're after the vanilla helmet overlay
        if (!event.getOverlay().id().equals(VanillaGuiOverlay.HELMET.id())) {
            return;
        }

        GuiGraphics gfx = event.getGuiGraphics();
        float partialTicks = event.getPartialTick();

        // Render all active overlays sorted by priority (highest first)
        HudOverlayRegistry.getAll().stream()
                .filter(HudOverlay::isActive)
                .filter(o -> o.getAfterOverlay().equals(VanillaGuiOverlay.HELMET.id()))
                .sorted(Comparator.comparingInt(HudOverlay::getPriority).reversed())
                .forEach(o -> o.render(gfx, partialTicks));
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        CNEntityType.registerModelLayer(event);
    }
}
