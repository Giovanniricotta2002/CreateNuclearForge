package net.nuclearteam.createnuclear;

import net.createmod.catnip.lang.Lang;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

import java.util.Collections;

import static net.nuclearteam.createnuclear.CNTags.NameSpace.*;

@SuppressWarnings({"unused", "deprecation"})
public class CNTags {
    public static <T> TagKey<T> optionalTag(Registry<T> registry, ResourceLocation id) {
        return TagKey.create(registry.key(), id);
    }

    public static <T> TagKey<T> forgeTag(Registry<T> registry, String path) {
        return optionalTag(registry, ResourceLocation.fromNamespaceAndPath(FORGE.id, path));
    }

    public static TagKey<Block> forgeBlockTag(String path) {
        return forgeTag(BuiltInRegistries.BLOCK, path);
    }

    public static TagKey<Item> forgeItemTag(String path) {
        return forgeTag(BuiltInRegistries.ITEM, path);
    }

    public static TagKey<Fluid> forgeFluidTag(String path) {
        return forgeTag(BuiltInRegistries.FLUID, path);
    }

    public enum NameSpace {
        MOD(CreateNuclear.MOD_ID, false, true),
        CREATE("create"),
        FORGE("forge"),
        MINECRAFT("minecraft")
        ;

        public final String id;
        public final boolean optionalDefault;
        public final boolean alwaysDatagenDefault;

        NameSpace(String id) {
            this(id, true, false);
        }

        NameSpace(String id, boolean optionalDefault, boolean alwaysDatagenDefault) {
            this.id = id;
            this.optionalDefault = optionalDefault;
            this.alwaysDatagenDefault = alwaysDatagenDefault;
        }
    }

    public enum CNBlockTags {
        FAN_PROCESSING_CATALYSTS_ENRICHED(MOD, "fan_processing_catalysts/enriched"),
        ENRICHING_FIRE_BASE_BLOCKS,
        ALL_CAMPFIRES(MINECRAFT, "all/campfires"),
        URANIUM_ORES,
        LEAD_ORES
        ;

        public final TagKey<Block> tag;
        public final boolean alwaysDatagen;

        CNBlockTags() {
            this(MOD);
        }

        CNBlockTags(NameSpace namespace) {
            this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        CNBlockTags(NameSpace nameSpace, String path) {
            this(nameSpace, path, nameSpace.optionalDefault, nameSpace.alwaysDatagenDefault);
        }

        CNBlockTags(NameSpace nameSpace, boolean optional, boolean alwaysDatagenDefault) {
            this(nameSpace, null, optional, alwaysDatagenDefault);
        }

        CNBlockTags(NameSpace nameSpace, String path, boolean optional, boolean alwaysDatagenDefault) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(nameSpace.id, path == null ? Lang.asId(name()) : path);
            if (optional) {
                tag = optionalTag(BuiltInRegistries.BLOCK, id);
            } else {
                tag = BlockTags.create(id);
            }
            this.alwaysDatagen = alwaysDatagenDefault;
        }

        public boolean matches(Block block) {
            return block.builtInRegistryHolder().is(tag);
        }

        public boolean matches(ItemStack stack) {
            return stack != null && stack.getItem() instanceof BlockItem blockItem && matches(blockItem.getBlock());
        }

        public boolean matches(BlockState state) {
            return state.is(tag);
        }

        private static void init() {}
    }

    public enum CNItemTags {
        CLOTH,
        FUEL,
        COOLER,
        URANIUM_ORES,
        LEAD_ORES,
        ANTI_RADIATION_HELMET_DYE,
        ANTI_RADIATION_CHESTPLATE_DYE,
        ANTI_RADIATION_LEGGINGS_DYE,
        ANTI_RADIATION_BOOTS_DYE,
        ANTI_RADIATION_ARMOR,
        ALL_ANTI_RADIATION_ARMORS,
        ANTI_RADIATION_HELMET_FULL_DYE,
        ANTI_RADIATION_CHESTPLATE_FULL_DYE,
        ANTI_RADIATION_LEGGINGS_FULL_DYE,
        ;

        public final TagKey<Item> tag;
        public final boolean alwaysDatagen;

        CNItemTags() {
            this(MOD);
        }

        CNItemTags(NameSpace namespace) {
            this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        CNItemTags(NameSpace nameSpace, String path) {
            this(nameSpace, path, nameSpace.optionalDefault, nameSpace.alwaysDatagenDefault);
        }

        CNItemTags(NameSpace nameSpace, boolean optional, boolean alwaysDatagenDefault) {
            this(nameSpace, null, optional, alwaysDatagenDefault);
        }

        CNItemTags(NameSpace nameSpace, String path, boolean optional, boolean alwaysDatagenDefault) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(nameSpace.id, path == null ? Lang.asId(name()) : path);
            if (optional) {
                tag = optionalTag(BuiltInRegistries.ITEM, id);
            } else {
                tag = ItemTags.create(id);
            }
            this.alwaysDatagen = alwaysDatagenDefault;
        }

        public boolean matches(Item item) {
            return item.builtInRegistryHolder().is(tag);
        }

        public boolean matches(ItemStack stack) {
            return stack.is(tag);
        }

        private static void init() {}
    }

    public enum CNFluidTags {
        URANIUM
        ;

        public final TagKey<Fluid> tag;
        public final boolean alwaysDatagen;

        CNFluidTags() {
            this(MOD);
        }

        CNFluidTags(NameSpace namespace) {
            this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        CNFluidTags(NameSpace nameSpace, String path) {
            this(nameSpace, path, nameSpace.optionalDefault, nameSpace.alwaysDatagenDefault);
        }

        CNFluidTags(NameSpace nameSpace, boolean optional, boolean alwaysDatagenDefault) {
            this(nameSpace, null, optional, alwaysDatagenDefault);
        }

        CNFluidTags(NameSpace nameSpace, String path, boolean optional, boolean alwaysDatagenDefault) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(nameSpace.id, path == null ? Lang.asId(name()) : path);
            if (optional) {
                tag = optionalTag(BuiltInRegistries.FLUID, id);
            } else {
                tag = FluidTags.create(id);
            }
            this.alwaysDatagen = alwaysDatagenDefault;
        }

        public boolean matches(Fluid fluid) {
            return fluid.is(tag);
        }

        public boolean matches(FluidState stack) {
            return stack.is(tag);
        }

        private static void init() {}
    }

    public enum CNEntityTags {
        IRRADIATED_IMMUNE
        ;

        public final TagKey<EntityType<?>> tag;
        public final boolean alwaysDatagen;

        CNEntityTags() {
            this(MOD);
        }

        CNEntityTags(NameSpace nameSpace) {
            this(nameSpace, nameSpace.optionalDefault, nameSpace.alwaysDatagenDefault);
        }

        CNEntityTags(NameSpace nameSpace, String path) {
            this(nameSpace, path, nameSpace.optionalDefault, nameSpace.alwaysDatagenDefault);
        }

        CNEntityTags(NameSpace nameSpace, boolean optional, boolean alwaysDatagenDefault) {
            this(nameSpace, null, optional, alwaysDatagenDefault);
        }

        CNEntityTags(NameSpace nameSpace, String path, boolean optional, boolean alwaysDatagenDefault) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(nameSpace.id, path == null ? Lang.asId(name()) : path);
            if (optional) {
                tag = optionalTag(BuiltInRegistries.ENTITY_TYPE, id);
            } else {
                tag = TagKey.create(Registries.ENTITY_TYPE, id);
            }
            this.alwaysDatagen = alwaysDatagenDefault;
        }

        public boolean matches(EntityType<?> type) {
            return type.is(tag);
        }

        public boolean matches(Entity entity) {
            return matches(entity.getType());
        }

        private static void init() {}
    }

    public enum CNRecipeSerializerTags {
        AUTOMATION_IGNORE,
        ;

        public final TagKey<RecipeSerializer<?>> tag;
        public final boolean alwaysDatagen;

        CNRecipeSerializerTags() {
            this(MOD);
        }

        CNRecipeSerializerTags(NameSpace namespace) {
            this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        CNRecipeSerializerTags(NameSpace namespace, String path) {
            this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        CNRecipeSerializerTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
            this(namespace, null, optional, alwaysDatagen);
        }

        CNRecipeSerializerTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace.id, path == null ? Lang.asId(name()) : path);
            if (optional) {
                tag = optionalTag(BuiltInRegistries.RECIPE_SERIALIZER, id);
            } else {
                tag = TagKey.create(Registries.RECIPE_SERIALIZER, id);
            }
            this.alwaysDatagen = alwaysDatagen;
        }

        public boolean matches(RecipeSerializer<?> recipeSerializer) {
            ResourceKey<RecipeSerializer<?>> key = BuiltInRegistries.RECIPE_SERIALIZER.getResourceKey(recipeSerializer).orElseThrow();
            return BuiltInRegistries.RECIPE_SERIALIZER.getHolder(key).orElseThrow().is(tag);
        }

        private static void init() {}
    }


    public static void init() {
        CreateNuclear.LOGGER.info("Registering mod tags for " + CreateNuclear.MOD_ID);
        CNBlockTags.init();
        CNItemTags.init();
        CNFluidTags.init();
        CNEntityTags.init();
        CNRecipeSerializerTags.init();
    }
}
