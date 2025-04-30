package net.nuclearteam.createnuclear.foundation.data.recipe;

import com.ibm.icu.impl.TextTrieMap;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.item.TagDependentIngredientItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.nuclearteam.createnuclear.CNItems;

import java.util.function.Supplier;

public class CNWashingRecipeGen extends CNProcessingRecipeGen {

    GeneratedRecipe

            CRUSHED_LEAD = crushedOre(AllItems.CRUSHED_LEAD, () -> CNItems.LEAD_NUGGET, () -> AllItems.EXP_NUGGET, .5f)
            ;

    public GeneratedRecipe crushedOre(ItemEntry<TagDependentIngredientItem> crushed, Supplier<ItemLike> nugget, Supplier<ItemLike> secondary,
                                      float secondaryChance) {
        return create(crushed.getId(), b -> b
                .withItemIngredients(Ingredient.of(crushed.get())) // Ajout explicite de l'ingrédient
                .output(nugget.get(), 9)
                .output(secondaryChance, secondary.get(), 1));
    }

    public CNWashingRecipeGen(PackOutput output) {
        super(output);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.SPLASHING;
    }

}
