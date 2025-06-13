package net.nuclearteam.createnuclear.foundation.events;


import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.nuclearteam.createnuclear.CNEntityType;

@EventBusSubscriber(bus = Bus.MOD, value = Dist.CLIENT)
public class CommentEventClients {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        CNEntityType.registerModelLayer(event);
    }


}
