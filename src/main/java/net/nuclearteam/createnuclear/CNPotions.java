package net.nuclearteam.createnuclear;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;


public class CNPotions {

    public static final DeferredRegister<Potion> CN_POTIONS = DeferredRegister.create(BuiltInRegistries.POTION, CreateNuclear.MOD_ID);

    public static final Holder<Potion> POTION_1 = CN_POTIONS.register("potion_of_radiation_1",
        () -> new Potion(new MobEffectInstance(CNEffects.RADIATION.getDelegate(), 900)));
    public static final Holder<Potion> POTION_AUGMENT_1 = CN_POTIONS.register("potion_of_radiation_augment_1",
        () -> new Potion(new MobEffectInstance(CNEffects.RADIATION.getDelegate(), 1800)));
    public static final Holder<Potion> POTION_2 = CN_POTIONS.register("potion_of_radiation_2",
        () -> new Potion(new MobEffectInstance(CNEffects.RADIATION.getDelegate(), 410, 1)));

    public static void register(IEventBus eventBus) {
        CN_POTIONS.register(eventBus);
    }

    public static void registerPotionsRecipes() {
        PotionBrewing.addMix(Potions.AWKWARD, CNItems.ENRICHED_YELLOWCAKE.get(), POTION_1.get());
        PotionBrewing.addMix(POTION_1.get(), Items.REDSTONE, POTION_AUGMENT_1.get());
        PotionBrewing.addMix(POTION_1.get(), Items.GLOWSTONE_DUST, POTION_2.get());
    }
}
