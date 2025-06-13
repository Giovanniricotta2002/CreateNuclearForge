package net.nuclearteam.createnuclear.foundation.events;


import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.foundation.events.overlay.IrradiatedOverlayRendererVision;

@EventBusSubscriber(modid = CreateNuclear.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CNClientEvent {
    private static final HudRenderer HUD_RENDERER = new HudRenderer();

    @SubscribeEvent
    public static void onRegisterGui(RegisterGuiLayersEvent event) {
        HUD_RENDERER.onHudRender(event);
        event.registerAbove(VanillaGuiLayers.CAMERA_OVERLAYS, CreateNuclear.asResource("irradiated_vision"), IrradiatedOverlayRendererVision::renderOverlay);
    }


}
