package net.nuclearteam.createnuclear;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.nuclearteam.createnuclear.content.effects.RadiationEffect;

public class CNEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, CreateNuclear.MOD_ID);

    public static final Holder<MobEffect> RADIATION = EFFECTS.register("radiation",
            () -> new RadiationEffect()
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED,
                            CreateNuclear.asResource("radiation"), -0.25f,
                            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }}
