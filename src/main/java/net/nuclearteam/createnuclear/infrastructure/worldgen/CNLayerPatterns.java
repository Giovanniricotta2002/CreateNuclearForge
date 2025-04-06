package net.nuclearteam.createnuclear.infrastructure.worldgen;

import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import com.simibubi.create.infrastructure.worldgen.LayerPattern;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.world.level.block.Blocks;
import net.nuclearteam.createnuclear.content.decoration.palettes.CNPaletteStoneTypes;

public class CNLayerPatterns {
    public static final NonNullSupplier<LayerPattern>
        AUTUNITE = () -> LayerPattern.builder()
            .layer(l -> l.weight(2)
                .block(AllPaletteStoneTypes.LIMESTONE.getBaseBlock())
                .size(2, 4))
            .layer(l -> l.weight(1)
                .blocks(Blocks.CALCITE, Blocks.SANDSTONE)
                .size(2, 3))
            .layer(l -> l.weight(1)
                .block(Blocks.STONE)
                .size(1, 2))
            .layer(l -> l.weight(1)
                .blocks(Blocks.GLOWSTONE, Blocks.QUARTZ_BLOCK)
                .size(1, 1))
            .layer(l -> l.weight(6)
                .block(CNPaletteStoneTypes.AUTUNITE.getBaseBlock())
                .size(1, 6))
            .build();
}
