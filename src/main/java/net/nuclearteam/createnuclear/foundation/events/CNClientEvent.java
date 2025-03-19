package net.nuclearteam.createnuclear.foundation.events;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.nuclearteam.createnuclear.content.equipment.armor.HelmetOverlayRenderer;

@EventBusSubscriber(Dist.CLIENT)
public class CNClientEvent {
    @EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
    public static class ModBusEvents {
        @SubscribeEvent
        public static void regusterGuiOverlays(RegisterGuiOverlaysEvent event) {
            event.registerAbove(VanillaGuiOverlay.HELMET.id(), "helmet_vision", HelmetOverlayRenderer.OVERLAY);
            event.registerAbove(VanillaGuiOverlay.HELMET.id(), "irradiated_vision", IrradiatedOverlayRendererVision.OVERLAY);
        }
    }
}
