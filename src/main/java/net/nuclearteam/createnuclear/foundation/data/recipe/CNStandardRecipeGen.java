package net.nuclearteam.createnuclear.foundation.data.recipe;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.recipe.CompatMetals;
import com.simibubi.create.foundation.data.recipe.Mods;
import com.simibubi.create.foundation.mixin.accessor.MappedRegistryAccessor;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import net.createmod.catnip.registry.RegisteredObjectsHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import com.simibubi.create.api.data.recipe.BaseRecipeProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.nuclearteam.createnuclear.CNBlocks;
import net.nuclearteam.createnuclear.CNItems;
import net.nuclearteam.createnuclear.CNTags;
import net.nuclearteam.createnuclear.CNTags.CNItemTags;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.content.equipment.armor.AntiRadiationArmorItem;
import net.nuclearteam.createnuclear.content.equipment.cloth.ClothItem;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.UnaryOperator;

@SuppressWarnings("unused")
public class CNStandardRecipeGen extends BaseRecipeProvider {
    final List<GeneratedRecipe> all = new ArrayList<>();

    private final Marker CRAFTING = enterFolder("crafting");
    GeneratedRecipe
        WHITE_CLOTH_FROM_STRING = create(ClothItem.Cloths.WHITE_CLOTH::getItem)
            .unlockedBy(() -> Items.STRING)
            .viaShaped(b -> b
                .define('#', Items.STRING)
                .pattern("###")
                .pattern("###")
                .showNotification(true)
            ),

        WHITE_CLOTH_FROM_WOOL = create(ClothItem.Cloths.WHITE_CLOTH::getItem)
            .returns(6)
            .unlockedBy(() -> Items.WHITE_WOOL)
            .withSuffix("_wool")
            .viaShaped(b -> b
                .define('#', Blocks.WHITE_WOOL)
                .pattern("###")
                .pattern("###")
                .showNotification(true)
            ),

        ENRICHED_SOUL_SOIL = create(CNBlocks.ENRICHED_SOUL_SOIL).unlockedBy(() -> Items.NETHER_STAR)
            .viaShaped(b -> b
                .define('S', Blocks.SOUL_SOIL)
                .define('O', Blocks.OBSIDIAN)
                .define('N', Items.NETHER_STAR)
                .pattern("SOS")
                .pattern("ONO")
                .pattern("SOS")
                .showNotification(true)
            ),

        ENRICHING_CAMPFIRE = create(CNBlocks.ENRICHING_CAMPFIRE).unlockedBy(CNBlocks.ENRICHED_SOUL_SOIL::get)
            .viaShaped(b -> b
                .define('E', CNBlocks.ENRICHED_SOUL_SOIL)
                .define('L', ItemTags.LOGS)
                .define('S', Tags.Items.RODS_WOODEN)
                .pattern(" S ")
                .pattern("SES")
                .pattern("LLL")
                .showNotification(true)
            ),

        LEAD_COMPACTING = metalCompacting(ImmutableList.of(CNItems.LEAD_NUGGET, CNItems.LEAD_INGOT, CNBlocks.LEAD_BLOCK),
            ImmutableList.of(() -> CNTags.forgeItemTag("nuggets/lead"), () -> CNTags.forgeItemTag("ingots/lead"), () -> CNTags.forgeItemTag("storage_blocks/lead"))),

        STEEL_COMPACTING = metalCompacting(ImmutableList.of(CNItems.STEEL_NUGGET, CNItems.STEEL_INGOT, CNBlocks.STEEL_BLOCK),
            ImmutableList.of(() -> CNTags.forgeItemTag("nuggets/steel"), () -> CNTags.forgeItemTag("ingots/steel"), () -> CNTags.forgeItemTag("storage_blocks/steel"))),

        RAW_LEAD_BLOCK = create(CNBlocks.RAW_LEAD_BLOCK)
            .unlockedByTag(() -> CNTags.forgeItemTag("raw_materials/lead"))
            .viaShaped(b ->
                b.define('R', CNTags.forgeItemTag("raw_materials/lead"))
                .pattern("RRR")
                .pattern("RRR")
                .pattern("RRR")
                .showNotification(true)
            ),

        RAW_URANIUM_BLOCK = create(CNBlocks.RAW_URANIUM_BLOCK)
            .unlockedByTag(() -> CNTags.forgeItemTag("raw_materials/uranium"))
            .viaShaped(b ->
                b.define('R', CNTags.forgeItemTag("raw_materials/uranium"))
                .pattern("RRR")
                .pattern("RRR")
                .pattern("RRR")
                .showNotification(true)
            ),

    REACTOR_BLUEPRINT_ITEM = create(CNItems.REACTOR_BLUEPRINT)
            .unlockedBy(CNBlocks.REACTOR_CONTROLLER::get)
            .viaShaped(b ->
                b.define('S', CNTags.forgeItemTag("ingots/steel"))
                .define('D', AllBlocks.DISPLAY_BOARD)
                .define('P', AllItems.PRECISION_MECHANISM)
                .define('E', AllItems.EMPTY_SCHEMATIC)
                .pattern("SDS")
                .pattern("SPS")
                .pattern("SES")
                .showNotification(true)
            )
        ;


    private final Marker  CRAFTING_REACTOR = enterFolder("crafting/reactor");

    GeneratedRecipe
        REINFORCED_GLASS = create(CNBlocks.REINFORCED_GLASS)
            .unlockedBy(CNBlocks.REACTOR_CASING::get)
            .viaShaped(b -> b
                .define('G', CNTags.forgeItemTag("glass"))
                .define('S', CNTags.forgeItemTag("ingots/lead"))
                .pattern("SGS")
                .pattern("GSG")
                .pattern("SGS")
                .showNotification(true)
            )
    ;

    private final Marker  CRAFTING_ITEMS = enterFolder("crafting/items/armors");

    AntiRadiationArmorItem.DyeRecipeArmorList
        ANTI_RADIATION_HELMET = new AntiRadiationArmorItem.DyeRecipeArmorList(color -> create(CNItems.ANTI_RADIATION_HELMETS.get(color))
            .unlockedByTag(() -> CNItemTags.CLOTH.tag)
            .viaShaped(i -> i
                .define('X', CNTags.forgeItemTag("ingots/lead"))
                .define('Y', ClothItem.Cloths.getByColor(color).get())
                .define('Z', CNBlocks.REINFORCED_GLASS)
                .pattern("YXY")
                .pattern("XZX")
                .showNotification(true)
            )
        ),


        ANTI_RADIATION_CHESTPLATES = new AntiRadiationArmorItem.DyeRecipeArmorList(color -> create(CNItems.ANTI_RADIATION_CHESTPLATES.get(color))
            .unlockedByTag(() -> CNItemTags.CLOTH.tag)
            .viaShaped(i -> i
            .define('X', CNTags.forgeItemTag("ingots/lead"))
            .define('Y', ClothItem.Cloths.getByColor(color).get())
            .define('Z', CNItems.GRAPHITE_ROD)
                .pattern("Y Y")
                .pattern("XXX")
                .pattern("ZXZ")
                .showNotification(true)
            )
        ),

        ANTI_RADIATION_LEGGINGS = new AntiRadiationArmorItem.DyeRecipeArmorList(color -> create(CNItems.ANTI_RADIATION_LEGGINGS.get(color))
            .unlockedByTag(() -> CNItemTags.CLOTH.tag)
            .viaShaped(i -> i
                .define('X', CNTags.forgeItemTag("ingots/lead"))
                .define('Y', ClothItem.Cloths.getByColor(color).get())
                .pattern("YXY")
                .pattern("X X")
                .pattern("Y Y")
                .showNotification(true)
            )
        )
    ;

    GeneratedRecipe
        ANTI_RADIATION_BOOTS = create(CNItems.ANTI_RADIATION_BOOTS).unlockedByTag(() -> CNItemTags.CLOTH.tag)
            .viaShaped(b -> b
                .define('X', CNTags.forgeItemTag("ingots/lead"))
                .define('Y', ClothItem.Cloths.WHITE_CLOTH.getItem())
                .pattern("Y Y")
                .pattern("X X")
                .showNotification(true)
            )
        ;

    private final Marker BLAST_FURNACE = enterFolder("blast_furnace");
    GeneratedRecipe
        URANIUM_ORE_TO_URANIUM_POWDER = blastFurnaceRecipeTags(() -> CNItems.RAW_URANIUM::get, () -> CNItemTags.URANIUM_ORES.tag, "_for_uranium_ore", 4),
        RAW_LEAD_ORES = blastFurnaceRecipeTags(() -> CNItems.LEAD_INGOT::get, () -> CNItemTags.LEAD_ORES.tag, "_for_lead_ore", 1),
        RAW_LEAD = blastFurnaceRecipeTags(CNItems.LEAD_INGOT::get, () -> CNTags.forgeItemTag("raw_materials/lead"), "_for_raw_lead", 1),
        CRUSHED_RAW_LEAD_TO_LEAD_BLAST_FURNACE = blastFurnaceRecipe(CNItems.LEAD_INGOT::get, AllItems.CRUSHED_LEAD::get, "_for_lead", 1)
    ;



    static class Marker {
    }

    String currentFolder = "";

    Marker enterFolder(String folder) {
        currentFolder = folder;
        return new Marker();
    }

    GeneratedRecipeBuilder create(Supplier<ItemLike> result) {
        return new GeneratedRecipeBuilder(currentFolder, result);
    }

    GeneratedRecipeBuilder create(ResourceLocation result) {
        return new GeneratedRecipeBuilder(currentFolder, result);
    }

    GeneratedRecipeBuilder create(ItemProviderEntry<? extends ItemLike, ? extends ItemLike> result) {
        return create(result::get);
    }

    GeneratedRecipe createSpecial(Function<CraftingBookCategory, Recipe<?>> builder, String recipeType,
                                  String path) {
        ResourceLocation location = Create.asResource(recipeType + "/" + currentFolder + "/" + path);
        return register(consumer -> {
            SpecialRecipeBuilder b = SpecialRecipeBuilder.special(builder);
            b.save(consumer, location.toString());
        });
    }

    GeneratedRecipe blastCrushedMetal(Supplier<? extends ItemLike> result, Supplier<? extends ItemLike> ingredient) {
        return create(result::get).withSuffix("_from_crushed")
                .viaCooking(ingredient)
                .rewardXP(.1f)
                .inBlastFurnace();
    }

    GeneratedRecipe blastModdedCrushedMetal(ItemEntry<? extends Item> ingredient, CompatMetals metal) {
        for (Mods mod : metal.getMods()) {
            String metalName = metal.getName(mod);
            ResourceLocation ingot = mod.ingotOf(metalName);
            String modId = mod.getId();
            create(ingot).withSuffix("_compat_" + modId)
                    .whenModLoaded(modId)
                    .viaCooking(ingredient::get)
                    .rewardXP(.1f)
                    .inBlastFurnace();
        }
        return null;
    }

    GeneratedRecipe recycleGlass(BlockEntry<? extends Block> ingredient) {
        return create(() -> Blocks.GLASS).withSuffix("_from_" + ingredient.getId()
                        .getPath())
                .viaCooking(ingredient::get)
                .forDuration(50)
                .inFurnace();
    }

    GeneratedRecipe recycleGlassPane(BlockEntry<? extends Block> ingredient) {
        return create(() -> Blocks.GLASS_PANE).withSuffix("_from_" + ingredient.getId()
                        .getPath())
                .viaCooking(ingredient::get)
                .forDuration(50)
                .inFurnace();
    }

    GeneratedRecipe blastFurnaceRecipe(Supplier<? extends ItemLike> result, Supplier<? extends ItemLike> ingredient, String suffix, int count) {
        return create(result::get).withSuffix(suffix)
                .returns(count)
                .viaCooking(ingredient)
                .rewardXP(.1f)
                .inBlastFurnace();
    }

    GeneratedRecipe blastFurnaceRecipeTags(Supplier<? extends ItemLike> result, Supplier<TagKey<Item>> ingredient, String suffix, int count) {
        return create(result::get).withSuffix(suffix)
            .returns(count)
            .viaCookingTag(ingredient)
            .rewardXP(.1f)
            .inBlastFurnace();
    }

    GeneratedRecipe metalCompacting(List<ItemProviderEntry<? extends ItemLike, ? extends ItemLike>> variants,
                                    List<Supplier<TagKey<Item>>> ingredients) {
        GeneratedRecipe result = null;
        for (int i = 0; i + 1 < variants.size(); i++) {
            ItemProviderEntry<? extends ItemLike, ? extends ItemLike> currentEntry = variants.get(i);
            ItemProviderEntry<? extends ItemLike, ? extends ItemLike> nextEntry = variants.get(i + 1);
            Supplier<TagKey<Item>> currentIngredient = ingredients.get(i);
            Supplier<TagKey<Item>> nextIngredient = ingredients.get(i + 1);

            result = create(nextEntry).withSuffix("_from_compacting")
                    .unlockedBy(currentEntry::get)
                    .viaShaped(b -> b.pattern("###")
                            .pattern("###")
                            .pattern("###")
                            .define('#', currentIngredient.get()));

            result = create(currentEntry).returns(9)
                    .withSuffix("_from_decompacting")
                    .unlockedBy(nextEntry::get)
                    .viaShapeless(b -> b.requires(nextIngredient.get()));
        }
        return result;
    }

    GeneratedRecipe conversionCycle(List<ItemProviderEntry<? extends ItemLike, ? extends ItemLike>> cycle) {
        GeneratedRecipe result = null;
        for (int i = 0; i < cycle.size(); i++) {
            ItemProviderEntry<? extends ItemLike, ? extends ItemLike> currentEntry = cycle.get(i);
            ItemProviderEntry<? extends ItemLike, ? extends ItemLike> nextEntry = cycle.get((i + 1) % cycle.size());
            result = create(nextEntry).withSuffix("_from_conversion")
                    .unlockedBy(currentEntry::get)
                    .viaShapeless(b -> b.requires(currentEntry.get()));
        }
        return result;
    }

    GeneratedRecipe clearData(ItemProviderEntry<? extends ItemLike, ? extends ItemLike> item) {
        return create(item).withSuffix("_clear")
                .unlockedBy(item::get)
                .viaShapeless(b -> b.requires(item.get()));
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        all.forEach(c -> c.register(output));
        Create.LOGGER.info("{} registered {} recipe{}", getName(), all.size(), all.size() == 1 ? "" : "s");
    }

    protected GeneratedRecipe register(GeneratedRecipe recipe) {
        all.add(recipe);
        return recipe;
    }

    class GeneratedRecipeBuilder {

        private final String path;
        private String suffix;
        private Supplier<? extends ItemLike> result;
        private ResourceLocation compatDatagenOutput;
        List<ICondition> recipeConditions;

        private Supplier<ItemPredicate> unlockedBy;
        private int amount;

        private GeneratedRecipeBuilder(String path) {
            this.path = path;
            this.recipeConditions = new ArrayList<>();
            this.suffix = "";
            this.amount = 1;
        }

        public GeneratedRecipeBuilder(String path, Supplier<? extends ItemLike> result) {
            this(path);
            this.result = result;
        }

        public GeneratedRecipeBuilder(String path, ResourceLocation result) {
            this(path);
            this.compatDatagenOutput = result;
        }

        GeneratedRecipeBuilder returns(int amount) {
            this.amount = amount;
            return this;
        }

        GeneratedRecipeBuilder unlockedBy(Supplier<? extends ItemLike> item) {
            this.unlockedBy = () -> ItemPredicate.Builder.item()
                    .of(item.get())
                    .build();
            return this;
        }

        GeneratedRecipeBuilder unlockedByTag(Supplier<TagKey<Item>> tag) {
            this.unlockedBy = () -> ItemPredicate.Builder.item()
                    .of(tag.get())
                    .build();
            return this;
        }

        GeneratedRecipeBuilder whenModLoaded(String mod) {
            return withCondition(new ModLoadedCondition(mod));
        }

        GeneratedRecipeBuilder whenModMissing(String mod) {
            return withCondition(new NotCondition(new ModLoadedCondition(mod)));
        }

        GeneratedRecipeBuilder withCondition(ICondition condition) {
            recipeConditions.add(condition);
            return this;
        }

        GeneratedRecipeBuilder withSuffix(String suffix) {
            this.suffix = suffix;
            return this;
        }

        // FIXME 5.1 refactor - recipe categories as markers instead of sections?
        GeneratedRecipe viaShaped(UnaryOperator<ShapedRecipeBuilder> builder) {
            return register(consumer -> {
                ShapedRecipeBuilder b =
                        builder.apply(ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result.get(), amount));
                if (unlockedBy != null)
                    b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));
                b.save(consumer, createLocation("crafting"));
            });
        }

        GeneratedRecipe viaShapeless(UnaryOperator<ShapelessRecipeBuilder> builder) {
            return register(recipeOutput -> {
                ShapelessRecipeBuilder b =
                        builder.apply(ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result.get(), amount));
                if (unlockedBy != null)
                    b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));

                RecipeOutput conditionalOutput = recipeOutput.withConditions(recipeConditions.toArray(new ICondition[0]));

                b.save(recipeOutput, createLocation("crafting"));
            });
        }

        GeneratedRecipe viaNetheriteSmithing(Supplier<? extends Item> base, Supplier<Ingredient> upgradeMaterial) {
            return register(consumer -> {
                SmithingTransformRecipeBuilder b =
                        SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                                Ingredient.of(base.get()), upgradeMaterial.get(), RecipeCategory.COMBAT, result.get()
                                        .asItem());
                b.unlocks("has_item", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(base.get())
                        .build()));
                b.save(consumer, createLocation("crafting"));
            });
        }

        private ResourceLocation createSimpleLocation(String recipeType) {
            return Create.asResource(recipeType + "/" + getRegistryName().getPath() + suffix);
        }

        private ResourceLocation createLocation(String recipeType) {
            return Create.asResource(recipeType + "/" + path + "/" + getRegistryName().getPath() + suffix);
        }

        private ResourceLocation getRegistryName() {
            return compatDatagenOutput == null ? RegisteredObjectsHelper.getKeyOrThrow(result.get()
                    .asItem()) : compatDatagenOutput;
        }

        GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder viaCooking(Supplier<? extends ItemLike> item) {
            return unlockedBy(item).viaCookingIngredient(() -> Ingredient.of(item.get()));
        }

        GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder viaCookingTag(Supplier<TagKey<Item>> tag) {
            return unlockedByTag(tag).viaCookingIngredient(() -> Ingredient.of(tag.get()));
        }

        GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder viaCookingIngredient(Supplier<Ingredient> ingredient) {
            return new GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder(ingredient);
        }

        class GeneratedCookingRecipeBuilder {

            private Supplier<Ingredient> ingredient;
            private float exp;
            private int cookingTime;

            GeneratedCookingRecipeBuilder(Supplier<Ingredient> ingredient) {
                this.ingredient = ingredient;
                cookingTime = 200;
                exp = 0;
            }

            GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder forDuration(int duration) {
                cookingTime = duration;
                return this;
            }

            GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder rewardXP(float xp) {
                exp = xp;
                return this;
            }

            GeneratedRecipe inFurnace() {
                return inFurnace(b -> b);
            }

            GeneratedRecipe inFurnace(UnaryOperator<SimpleCookingRecipeBuilder> builder) {
                return create(RecipeSerializer.SMELTING_RECIPE, builder, SmeltingRecipe::new, 1);
            }

            GeneratedRecipe inSmoker() {
                return inSmoker(b -> b);
            }

            GeneratedRecipe inSmoker(UnaryOperator<SimpleCookingRecipeBuilder> builder) {
                create(RecipeSerializer.SMELTING_RECIPE, builder, SmeltingRecipe::new, 1);
                create(RecipeSerializer.CAMPFIRE_COOKING_RECIPE, builder, CampfireCookingRecipe::new, 3);
                return create(RecipeSerializer.SMOKING_RECIPE, builder, SmokingRecipe::new, .5f);
            }

            GeneratedRecipe inBlastFurnace() {
                return inBlastFurnace(b -> b);
            }

            GeneratedRecipe inBlastFurnace(UnaryOperator<SimpleCookingRecipeBuilder> builder) {
                create(RecipeSerializer.SMELTING_RECIPE, builder, SmeltingRecipe::new, 1);
                return create(RecipeSerializer.BLASTING_RECIPE, builder, BlastingRecipe::new, .5f);
            }

            private <T extends AbstractCookingRecipe> GeneratedRecipe create(RecipeSerializer<T> serializer,
                                                                             UnaryOperator<SimpleCookingRecipeBuilder> builder, AbstractCookingRecipe.Factory<T> factory, float cookingTimeModifier) {
                return register(recipeOutput -> {
                    boolean isOtherMod = compatDatagenOutput != null;

                    SimpleCookingRecipeBuilder b = builder.apply(SimpleCookingRecipeBuilder.generic(ingredient.get(),
                            RecipeCategory.MISC, isOtherMod ? Items.DIRT : result.get(), exp,
                            (int) (cookingTime * cookingTimeModifier), serializer, factory));
                    if (unlockedBy != null)
                        b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));

                    RecipeOutput conditionalOutput = recipeOutput.withConditions(recipeConditions.toArray(new ICondition[0]));

                    b.save(
                            isOtherMod ? new ModdedCookingRecipeOutput(conditionalOutput, compatDatagenOutput) : conditionalOutput,
                            createSimpleLocation(RegisteredObjectsHelper.getKeyOrThrow(serializer).getPath())
                    );
                });
            }
        }
    }



    public CNStandardRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, CreateNuclear.MOD_ID);
    }

    @ParametersAreNonnullByDefault
    @MethodsReturnNonnullByDefault
    private static class ModdedCookingRecipeOutputShim implements Recipe<RecipeInput> {

        private static final Map<RecipeType<?>, ModdedCookingRecipeOutputShim.Serializer> serializers = new ConcurrentHashMap<>();

        private final Recipe<?> wrapped;
        private final ResourceLocation overrideID;

        private ModdedCookingRecipeOutputShim(Recipe<?> wrapped, ResourceLocation overrideID) {
            this.wrapped = wrapped;
            this.overrideID = overrideID;
        }

        @Override
        public boolean matches(RecipeInput recipeInput, Level level) {
            throw new AssertionError("Only for datagen output");
        }

        @Override
        public ItemStack assemble(RecipeInput input, HolderLookup.Provider registries) {
            throw new AssertionError("Only for datagen output");
        }

        @Override
        public boolean canCraftInDimensions(int pWidth, int pHeight) {
            throw new AssertionError("Only for datagen output");
        }

        @Override
        public ItemStack getResultItem(HolderLookup.Provider registries) {
            throw new AssertionError("Only for datagen output");
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return serializers.computeIfAbsent(
                    getType(),
                    t -> ModdedCookingRecipeOutputShim.Serializer.create(wrapped)
            );
        }

        @Override
        public RecipeType<?> getType() {
            return wrapped.getType();
        }

        private record Serializer(MapCodec<Recipe<?>> wrappedCodec) implements RecipeSerializer<ModdedCookingRecipeOutputShim> {
            private static ModdedCookingRecipeOutputShim.Serializer create(Recipe<?> wrapped) {
                RecipeSerializer<?> wrappedSerializer = wrapped.getSerializer();
                @SuppressWarnings("unchecked")
                ModdedCookingRecipeOutputShim.Serializer serializer = new ModdedCookingRecipeOutputShim.Serializer((MapCodec<Recipe<?>>) wrappedSerializer.codec());

                // Need to do some registry injection to get the Recipe/Registry#byNameCodec to encode the right type for this
                // getResourceKey and getId
                // byValue and toId
                // Holder.Reference: key
                if (BuiltInRegistries.RECIPE_SERIALIZER instanceof com.simibubi.create.foundation.mixin.accessor.MappedRegistryAccessor<?> mra) {
                    @SuppressWarnings("unchecked")
                    com.simibubi.create.foundation.mixin.accessor.MappedRegistryAccessor<RecipeSerializer<?>> mra$ = (MappedRegistryAccessor<RecipeSerializer<?>>) mra;

                    int wrappedId = mra$.getToId().getOrDefault(wrappedSerializer, -1);
                    ResourceKey<RecipeSerializer<?>> wrappedKey = mra$.getByValue().get(wrappedSerializer).key();

                    mra$.getToId().put(serializer, wrappedId);
                    //noinspection DataFlowIssue - it is ok to pass null as the owner, because this is only being used for serialization
                    mra$.getByValue().put(serializer, Holder.Reference.createStandAlone(null, wrappedKey));
                } else {
                    throw new AssertionError("ModdedCookingRecipeOutputShim will not be able to" +
                            " serialize without injecting into a registry. Expected" +
                            " BuiltInRegistries.RECIPE_SERIALIZER to be of class MappedRegistry, is of class " +
                            BuiltInRegistries.RECIPE_SERIALIZER.getClass()
                    );
                }
                return serializer;
            }

            @Override
            public MapCodec<ModdedCookingRecipeOutputShim> codec() {
                return RecordCodecBuilder.mapCodec(instance -> instance.group(
                        wrappedCodec.forGetter(i -> i.wrapped),
                        ModdedCookingRecipeOutputShim.FakeItemStack.CODEC.fieldOf("result").forGetter(i -> new ModdedCookingRecipeOutputShim.FakeItemStack(i.overrideID))
                ).apply(instance, (wrappedRecipe, fakeItemStack) -> {
                    throw new AssertionError("Only for datagen output");
                }));
            }

            @Override
            public StreamCodec<RegistryFriendlyByteBuf, ModdedCookingRecipeOutputShim> streamCodec() {
                throw new AssertionError("Only for datagen output");
            }
        }

        private record FakeItemStack(ResourceLocation id) {
            public static Codec<ModdedCookingRecipeOutputShim.FakeItemStack> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    ResourceLocation.CODEC.fieldOf("id").forGetter(ModdedCookingRecipeOutputShim.FakeItemStack::id)
            ).apply(instance, ModdedCookingRecipeOutputShim.FakeItemStack::new));
        }
    }

    @ParametersAreNonnullByDefault
    @MethodsReturnNonnullByDefault
    private record ModdedCookingRecipeOutput(RecipeOutput wrapped, ResourceLocation outputOverride) implements RecipeOutput {

        @Override
        public Advancement.Builder advancement() {
            return wrapped.advancement();
        }

        @Override
        public void accept(ResourceLocation id, Recipe<?> recipe, @Nullable AdvancementHolder advancement, ICondition... conditions) {
            wrapped.accept(id, new ModdedCookingRecipeOutputShim(recipe, outputOverride), advancement, conditions);
        }
    }
}