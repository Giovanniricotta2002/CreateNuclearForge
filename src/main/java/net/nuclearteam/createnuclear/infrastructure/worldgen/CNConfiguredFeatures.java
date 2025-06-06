package net.nuclearteam.createnuclear.infrastructure.worldgen;

import com.simibubi.create.infrastructure.worldgen.AllFeatures;
import com.simibubi.create.infrastructure.worldgen.LayerPattern;
import com.simibubi.create.infrastructure.worldgen.LayeredOreConfiguration;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration.TargetBlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.nuclearteam.createnuclear.CNBlocks;
import net.nuclearteam.createnuclear.CreateNuclear;
import static net.minecraft.data.worldgen.features.FeatureUtils.register;

import java.util.List;

public class CNConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>>
        URANIUM_ORE = key("uranium_ore"),
        LEAD_ORE = key("lead_ore"),
        STRIATED_ORES_OVERWORLD = key("striated_ores_overworld")
    ;

    private static ResourceKey<ConfiguredFeature<?, ?>> key(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, CreateNuclear.asResource(name));
    }

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> ctx) {
        RuleTest stoneOreReplaceable = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateOreReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        List<TargetBlockState> uraniumTargetStates = List.of(
                OreConfiguration.target(stoneOreReplaceable, CNBlocks.URANIUM_ORE.getDefaultState()),
                OreConfiguration.target(deepslateOreReplaceables, CNBlocks.DEEPSLATE_URANIUM_ORE.getDefaultState())
        );

        register(ctx, URANIUM_ORE, Feature.ORE, new OreConfiguration(uraniumTargetStates, 7));

        List<TargetBlockState> leadTargetStates = List.of(
                OreConfiguration.target(stoneOreReplaceable, CNBlocks.LEAD_ORE.getDefaultState()),
                OreConfiguration.target(deepslateOreReplaceables, CNBlocks.DEEPSLATE_LEAD_ORE.getDefaultState())
        );

        register(ctx, LEAD_ORE, Feature.ORE, new OreConfiguration(leadTargetStates, 7));

        List<LayerPattern> overworldLayerPatterns = List.of(
                CNLayerPatterns.AUTUNITE.get()
        );

        register(ctx, STRIATED_ORES_OVERWORLD, AllFeatures.LAYERED_ORE.get(), new LayeredOreConfiguration(overworldLayerPatterns, 32, 0));
    }
}
