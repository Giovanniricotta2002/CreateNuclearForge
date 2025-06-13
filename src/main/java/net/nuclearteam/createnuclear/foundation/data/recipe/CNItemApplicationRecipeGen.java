package net.nuclearteam.createnuclear.foundation.data.recipe;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.api.data.recipe.ItemApplicationRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.nuclearteam.createnuclear.CNBlocks;
import net.nuclearteam.createnuclear.CNTags;
import net.nuclearteam.createnuclear.CreateNuclear;

import java.util.concurrent.CompletableFuture;

public class CNItemApplicationRecipeGen extends ItemApplicationRecipeGen {

    GeneratedRecipe REACTOR_CASING = itemApplication("reactor_casing_from_steel_and_brass_casing",
            Ingredient.of(CNTags.forgeItemTag("ingots/steel")),
            AllBlocks.BRASS_CASING.get(),
            CNBlocks.REACTOR_CASING.get()
    );

    GeneratedRecipe REACTOR_OUTPUT = itemApplication("reactor_output_from_shaft_and_reactor_casing",
            AllBlocks.SHAFT.asItem(),
            CNBlocks.REACTOR_CASING.get(),
            CNBlocks.REACTOR_OUTPUT.get()
    );

    GeneratedRecipe REACTOR_INPUT = itemApplication("reactor_input_from_hopper_and_reactor_casing",
            Items.HOPPER,
            CNBlocks.REACTOR_CASING.get(),
            CNBlocks.REACTOR_INPUT.get()
    );

    protected GeneratedRecipe itemApplication(String name, Ingredient ingredient, ItemLike input, ItemLike output) {
        return create(CreateNuclear.asResource(name), b ->
                b.require(input)
                        .require(ingredient)
                        .output(output)
        );
    }

    protected GeneratedRecipe itemApplication(String name, Item ingredient, ItemLike input, ItemLike output) {
        return create(CreateNuclear.asResource(name), b ->
                b.require(input)
                        .require(ingredient)
                        .output(output)
        );
    }


    public CNItemApplicationRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, CreateNuclear.MOD_ID);
    }
}