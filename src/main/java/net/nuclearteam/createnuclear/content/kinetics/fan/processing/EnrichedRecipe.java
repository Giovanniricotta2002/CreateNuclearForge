package net.nuclearteam.createnuclear.content.kinetics.fan.processing;

import javax.annotation.ParametersAreNonnullByDefault;


import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.CNRecipeTypes;

@ParametersAreNonnullByDefault
public class EnrichedRecipe extends StandardProcessingRecipe<SingleRecipeInput> {

    public EnrichedRecipe(ProcessingRecipeParams params) {
        super(CNRecipeTypes.ENRICHED, params);
    }

    @Override
    public boolean matches(SingleRecipeInput inv, Level worldIn) {
        if (inv.isEmpty())
            return false;
        return ingredients.get(0)
                .test(inv.getItem(0));
    }

    @Override
    protected int getMaxInputCount() {
        return 1;
    }

    @Override
    protected int getMaxOutputCount() {
        return 12;
    }

}
