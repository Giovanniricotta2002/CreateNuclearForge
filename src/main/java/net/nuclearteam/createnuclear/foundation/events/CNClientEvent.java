package net.nuclearteam.createnuclear.foundation.events;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.foundation.events.overlay.EventTextOverlay;
import net.nuclearteam.createnuclear.foundation.events.overlay.HelmetOverlay;
import net.nuclearteam.createnuclear.foundation.events.overlay.HudOverlay;
import net.nuclearteam.createnuclear.foundation.events.overlay.RadiationOverlay;

import java.util.Comparator;
import java.util.List;

@EventBusSubscriber(value = Dist.CLIENT)
public class CNClientEvent {
    @EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
    public static class ModBusEvents {
        private static final HudRenderer HUD_RENDERER = new HudRenderer();

        @SubscribeEvent
        public static void onRenderGuiOverlay(RegisterGuiOverlaysEvent event) {
            HUD_RENDERER.onHudRender(event);
        }
    }
}
