package net.nuclearteam.createnuclear.infrastructure.worldgen;

import com.simibubi.create.infrastructure.worldgen.AllPlacedFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers.AddFeaturesBiomeModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.nuclearteam.createnuclear.CreateNuclear;

public class CNBiomeModifiers {
    public static final ResourceKey<BiomeModifier>
        URANIUM_ORE = key("uranium_ore"),
        LEAD_ORE = key("lead_ore"),
        STRIATED_ORES_OVERWORLD = key("striated_ores_overworld")
    ;

    private static ResourceKey<BiomeModifier> key(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, CreateNuclear.asResource(name));
    }

    public static void bootstrap(BootstrapContext<BiomeModifier> ctx) {
        HolderGetter<Biome> biomeLookup = ctx.lookup(Registries.BIOME);
        HolderSet<Biome> isOverworld = biomeLookup.getOrThrow(BiomeTags.IS_OVERWORLD);

        HolderGetter<PlacedFeature> featureLookup = ctx.lookup(Registries.PLACED_FEATURE);
        Holder<PlacedFeature> uraniumOre = featureLookup.getOrThrow(CNPlacedFeatures.URANIUM_ORE);
        Holder<PlacedFeature> leadOre = featureLookup.getOrThrow(CNPlacedFeatures.LEAD_ORE);
        Holder<PlacedFeature> striatedOresOverworld = featureLookup.getOrThrow(CNPlacedFeatures.STRIATED_ORES_OVERWORLD);

        ctx.register(URANIUM_ORE, addOre(isOverworld, uraniumOre));
        ctx.register(LEAD_ORE, addOre(isOverworld, leadOre));
        ctx.register(STRIATED_ORES_OVERWORLD, addOre(isOverworld, striatedOresOverworld));
    }

    private static AddFeaturesBiomeModifier addOre(HolderSet<Biome> biomes, Holder<PlacedFeature> feature) {
        return new AddFeaturesBiomeModifier(biomes, HolderSet.direct(feature), GenerationStep.Decoration.UNDERGROUND_ORES);
    }
}
