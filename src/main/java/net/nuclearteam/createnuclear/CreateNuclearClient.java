package net.nuclearteam.createnuclear;

import net.createmod.ponder.foundation.PonderIndex;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.nuclearteam.createnuclear.foundation.ponder.CreateNuclearPonderPlugin;

@Mod(value = CreateNuclear.MOD_ID, dist = Dist.CLIENT)
@SuppressWarnings("unused")
public class CreateNuclearClient {

    public CreateNuclearClient(IEventBus modEventBus) {
        onCtorClient(modEventBus);
    }

    public static void onCtorClient(IEventBus modEventBus) {
        IEventBus neoEventBus = NeoForge.EVENT_BUS;

        modEventBus.addListener(CreateNuclearClient::clientInit);

    }

    public static void clientInit(final FMLClientSetupEvent event) {
        PonderIndex.addPlugin(new CreateNuclearPonderPlugin());

    }
}
