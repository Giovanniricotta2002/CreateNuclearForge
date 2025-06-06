package net.nuclearteam.createnuclear.foundation.events;


import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber
public class CommentEvents {
    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {

    }
}
