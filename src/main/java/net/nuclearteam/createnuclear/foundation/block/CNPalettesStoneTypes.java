package net.nuclearteam.createnuclear.foundation.block;

import com.simibubi.create.AllTags;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.createmod.catnip.lang.Lang;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.ForgeRegistries;
import net.nuclearteam.createnuclear.CNTags;
import net.nuclearteam.createnuclear.CreateNuclear;

import java.util.function.Function;

public enum CNPalettesStoneTypes {
    AUTUNITE(CNPaletteBlockPatten.STANDARD_RANGE, r -> r.paletteStoneBlock("asurine", () -> Blocks.DEEPSLATE, true, true)
            .properties(p -> p.destroyTime(1.25f)
                    .mapColor(MapColor.COLOR_GREEN))
            .register()),
    ;


    private Function<CreateRegistrate, NonNullSupplier<Block>> factory;
    private CNPalettesVariantEntry variants;

    public NonNullSupplier<Block> baseBlock;
    public final CNPaletteBlockPatten[] variantTypes;
    public TagKey<Item> materialTag;

    private CNPalettesStoneTypes(CNPaletteBlockPatten[] variantTypes,
                                 Function<CreateRegistrate, NonNullSupplier<Block>> factory) {
        this.factory = factory;
        this.variantTypes = variantTypes;
    }

    public NonNullSupplier<Block> getBaseBlock() {
        return baseBlock;
    }

    public CNPalettesVariantEntry getVariants() {
        return variants;
    }

    public static void register(CreateRegistrate registrate) {
        for (CNPalettesStoneTypes paletteStoneVariants : values()) {
            NonNullSupplier<Block> baseBlock = paletteStoneVariants.factory.apply(registrate);
            paletteStoneVariants.baseBlock = baseBlock;
            String id = Lang.asId(paletteStoneVariants.name());
            paletteStoneVariants.materialTag =
                    CNTags.optionalTag(ForgeRegistries.ITEMS, CreateNuclear.asResource("stone_types/" + id));
            paletteStoneVariants.variants = new CNPalettesVariantEntry(id, paletteStoneVariants);
        }
    }


}
