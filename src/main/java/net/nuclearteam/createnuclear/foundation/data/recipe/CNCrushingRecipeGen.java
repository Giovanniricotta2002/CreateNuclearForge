package net.nuclearteam.createnuclear.foundation.data.recipe;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.api.data.recipe.CrushingRecipeGen;
import com.simibubi.create.foundation.data.recipe.CompatMetals;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.neoforged.neoforge.common.conditions.TagEmptyCondition;
import net.nuclearteam.createnuclear.CNBlocks;
import net.nuclearteam.createnuclear.CNItems;
import net.nuclearteam.createnuclear.CreateNuclear;

import static com.simibubi.create.foundation.data.recipe.CompatMetals.URANIUM;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class CNCrushingRecipeGen extends CrushingRecipeGen {

    GeneratedRecipe
        COAL_DUST = create("coal", b -> b
            .duration(250)
            .require(ItemTags.COALS)
            .output(.50f, CNItems.COAL_DUST)
        ),

        GRANITE_URANIUM_POWDER = create(() -> Items.GRANITE, b -> b.duration(250)
            .output(.5f, CNItems.URANIUM_POWDER)
            .output(1f, Blocks.RED_SAND)
        ),

        FIX_RAW_URANIUM_FOR_FORGE = moddedOreCustom(URANIUM, CNItems.URANIUM_POWDER::get,9),


        RAW_URANIUM_BLOCK = create(() -> CNBlocks.RAW_URANIUM_BLOCK, b -> b
            .duration(250)
            .output(1, CNItems.URANIUM_POWDER,81)
        ),

        RAW_ZINC = create(() -> AllItems.RAW_ZINC, b -> b.duration(250)
            .output(1, AllItems.CRUSHED_ZINC, 1)
            .output(.75f, AllItems.EXP_NUGGET, 1)
            .output(.25f, CNItems.LEAD_NUGGET,1)
        ),


        RAW_COPPER = create(() -> Items.RAW_COPPER, b -> b.duration(250)
            .output(1, AllItems.CRUSHED_COPPER, 1)
            .output(.75f, AllItems.EXP_NUGGET, 1)
            .output(.15f, CNItems.LEAD_NUGGET,1)
        )
    ;

    protected GeneratedRecipe moddedOreCustom(CompatMetals metal, Supplier<ItemLike> result, int amount) {
        String name = metal.getName();
        return create(name + "_ore", b -> {
            String prefix = "ores/";
            return b.duration(400)
                    .withCondition(new NotCondition(new TagEmptyCondition("c", prefix + name)))
                    .require(AllTags.commonItemTag(prefix + name))
                    .output(result.get(), 1)
                    .output(.75f, result.get(), 1)
                    .output(.75f, AllItems.EXP_NUGGET.get());
        });
    }

    public CNCrushingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, CreateNuclear.MOD_ID);
    }




}
