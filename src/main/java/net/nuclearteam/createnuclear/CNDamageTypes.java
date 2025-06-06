package net.nuclearteam.createnuclear;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

@SuppressWarnings("unused")
public class CNDamageTypes {
    private static ResourceKey<DamageType> key(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, CreateNuclear.asResource(name));
    }

    public static void bootstrap(BootstrapContext<DamageType> ctx) {

    }
}
