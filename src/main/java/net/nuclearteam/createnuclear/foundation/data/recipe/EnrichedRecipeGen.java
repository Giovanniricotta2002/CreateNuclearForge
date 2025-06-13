package net.nuclearteam.createnuclear.foundation.data.recipe;

import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.api.data.recipe.StandardProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.createmod.catnip.registry.RegisteredObjectsHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.nuclearteam.createnuclear.CNRecipeTypes;
import net.nuclearteam.createnuclear.content.kinetics.fan.processing.EnrichedRecipe;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * The base class for Haunting recipe generation.
 * Addons should extend this and use the {@link ProcessingRecipeGen#create} methods
 * or the helper methods contained in this class to make recipes.
 * For an example of how you might do this, see Create's implementation: {@link net.nuclearteam.createnuclear.foundation.data.recipe.CNEnrichedRecipeGen}.
 * Needs to be added to a registered recipe provider to do anything, see {@link net.nuclearteam.createnuclear.foundation.data.recipe.CNRecipeProvider}
 */
public abstract class EnrichedRecipeGen extends StandardProcessingRecipeGen<EnrichedRecipe> {
    public GeneratedRecipe convert(ItemLike input, ItemLike result) {
        return convert(() -> Ingredient.of(input), () -> result);
    }

    public GeneratedRecipe convert(Supplier<Ingredient> input, Supplier<ItemLike> result) {
        return create(asResource(RegisteredObjectsHelper.getKeyOrThrow(result.get().asItem())
                        .getPath()),
                p -> p.withItemIngredients(input.get())
                        .output(result.get()));
    }

    public EnrichedRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String defaultNamespace) {
        super(output, registries, defaultNamespace);
    }

    @Override
    protected CNRecipeTypes getRecipeType() {
        return CNRecipeTypes.ENRICHED;
    }
}
