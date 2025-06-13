package net.nuclearteam.createnuclear.foundation.events;


import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.nuclearteam.createnuclear.CNPotions;
import net.nuclearteam.createnuclear.CreateNuclear;

@EventBusSubscriber(modid = CreateNuclear.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class CommentEvents {
    @SubscribeEvent
    public static void onBrewingRecipeRegister(RegisterBrewingRecipesEvent event) {
        CNPotions.registerPotionsRecipes(event);
    }
}
