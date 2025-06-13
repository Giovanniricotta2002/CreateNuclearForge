package net.nuclearteam.createnuclear.foundation.data.recipe;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.Create;
import com.simibubi.create.api.data.recipe.BaseRecipeProvider.GeneratedRecipe;
import com.simibubi.create.api.data.recipe.MechanicalCraftingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.nuclearteam.createnuclear.*;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class CNMechanicalCraftingRecipeGen extends MechanicalCraftingRecipeGen {

    GeneratedRecipe
        GRAPHITE_ROD = create(CNItems.GRAPHITE_ROD::get)
            .recipe(b -> b
                .key('S', Ingredient.of(CNTags.forgeItemTag("ingots/steel")))
                .key('G', Ingredient.of(CNItems.GRAPHENE))
                .patternLine("SGS")
                .patternLine("SGS")
                .patternLine("SGS")
                .patternLine("SGS")
        ),

        URANIUM_ROD = create(CNItems.URANIUM_ROD::get)
            .recipe(b -> b
                .key('U', Ingredient.of(CNItems.ENRICHED_YELLOWCAKE))
                .patternLine("    U")
                .patternLine("   U ")
                .patternLine("  U  ")
                .patternLine(" U   ")
                .patternLine("U    ")
        ),

        REACTOR_MAIN_FRAME = create(CNBlocks.REACTOR_FRAME::get)
            .recipe(b -> b
                .key('C', Ingredient.of(CNBlocks.REACTOR_CASING))
                .key('G', Ingredient.of(CNBlocks.REINFORCED_GLASS))
                .key('B', Ingredient.of(CNFluids.URANIUM.get().getBucket()))
                .key('S', Ingredient.of(CNTags.forgeItemTag("ingots/steel")))
                .patternLine("CCCCC")
                .patternLine("CSGSC")
                .patternLine("CGBGC")
                .patternLine("CSGSC")
                .patternLine("CCCCC")
        ),

    REACTOR_CONTROLLER = create(CNBlocks.REACTOR_CONTROLLER::get)
        .recipe(b -> b
            .key('C', Ingredient.of(CNBlocks.REACTOR_CASING))
            .key('V', Ingredient.of(AllBlocks.ITEM_VAULT))
            .key('O', Ingredient.of(AllBlocks.SMART_OBSERVER))
            .key('T', Ingredient.of(AllItems.ELECTRON_TUBE))
            .key('N', Ingredient.of(Items.NETHERITE_INGOT))
            .key('X', Ingredient.of(Items.NETHER_STAR))
            .patternLine("CCCCC")
            .patternLine("CNONC")
            .patternLine("CTXTC")
            .patternLine("CNVNC")
            .patternLine("CCCCC")
    ),

    REACTOR_COOLING_FRAME= create(CNBlocks.REACTOR_COOLER::get)
        .recipe(b -> b
            .key('C', Ingredient.of(CNBlocks.REACTOR_CASING))
            .key('I', Ingredient.of(Blocks.BLUE_ICE))
            .key('G', Ingredient.of(CNBlocks.REINFORCED_GLASS))
            .key('S', Ingredient.of(CNTags.forgeItemTag("ingots/steel")))
            .patternLine("CCCCC")
            .patternLine("CSGSC")
            .patternLine("CIGIC")
            .patternLine("CSGSC")
            .patternLine("CCCCC")
    ),

    REACTOR_CORE = create(CNBlocks.REACTOR_CORE::get)
        .recipe(b -> b
            .key('C', Ingredient.of(CNBlocks.REACTOR_CASING))
            .key('P', Ingredient.of(AllItems.PRECISION_MECHANISM))
            .key('B', Ingredient.of(CNFluids.URANIUM.get().getBucket()))
            .key('S', Ingredient.of(CNTags.forgeItemTag("ingots/steel")))
            .patternLine("CCCCC")
            .patternLine("CPSPC")
            .patternLine("CSBSC")
            .patternLine("CPSPC")
            .patternLine("CCCCC")
    );


    public CNMechanicalCraftingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, CreateNuclear.MOD_ID);
    }
}
