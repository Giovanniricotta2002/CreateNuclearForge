package net.nuclearteam.createnuclear.content.equipment.armor;

import com.simibubi.create.api.data.recipe.BaseRecipeProvider.GeneratedRecipe;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.nuclearteam.createnuclear.CNItems;
import net.nuclearteam.createnuclear.CNTags.CNItemTags;
import net.nuclearteam.createnuclear.CreateNuclear;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

@SuppressWarnings("unused")
public class AntiRadiationArmorItem {

    public static final ArmorItem.Type HELMET = ArmorItem.Type.HELMET;
    public static final ArmorItem.Type CHESTPLATE = ArmorItem.Type.CHESTPLATE;
    public static final ArmorItem.Type LEGGINGS = ArmorItem.Type.LEGGINGS;
    public static final ArmorItem.Type BOOTS = ArmorItem.Type.BOOTS;
    public static final ArmorMaterial ARMOR_MATERIAL = ArmorMaterials.ANTI_RADIATION_SUIT;


    public static class Helmet extends ArmorItem {
        protected final DyeColor color;
        public Helmet(Properties properties, DyeColor color) {
            super(ARMOR_MATERIAL, HELMET, properties);
            this.color = color;
        }

        @Override
        public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
            return String.valueOf(CreateNuclear.asResource("textures/models/armor/"+color+"_anti_radiation_suit_layer_"+(slot == EquipmentSlot.LEGS ? 2 : 1)+".png"));
        }

        public static class DyeItemHelmetList<T extends Helmet> implements Iterable<ItemEntry<T>> {
            private static final int COLOR_AMOUNT = DyeColor.values().length;

            private final ItemEntry<?>[] entry = new ItemEntry<?>[COLOR_AMOUNT];

            public DyeItemHelmetList(Function<DyeColor, ItemEntry<? extends T>> filler) {
                for (DyeColor color : DyeColor.values()) {
                    entry[color.ordinal()] = filler.apply(color);
                }
            }

            @SuppressWarnings("unchecked")
            public ItemEntry<T> get(DyeColor color) {
                return (ItemEntry<T>) entry[color.ordinal()];
            }

            public boolean contains(Item block) {
                for (ItemEntry<?> entry : entry) {
                    if (entry.is(block)) return true;
                }
                return false;
            }

            @SuppressWarnings("unchecked")
            public ItemEntry<T>[] toArray() {
                return (ItemEntry<T>[]) Arrays.copyOf(entry, entry.length);
            }

            @Override
            public Iterator<ItemEntry<T>> iterator() {
                return new Iterator<>() {
                    private int index = 0;
                    @Override
                    public boolean hasNext() {
                        return index < entry.length;
                    }

                    @SuppressWarnings("unchecked")
                    @Override
                    public ItemEntry<T> next() {
                        if (!hasNext()) throw new NoSuchElementException();
                        return (ItemEntry<T>) entry[index++];
                    }
                };
            }
        }

        public static TagKey<Item> getHelmetTag(String key) {
            return key.equals("white")
                    ? CNItemTags.ANTI_RADIATION_ARMOR.tag
                    : CNItemTags.ANTI_RADIATION_HELMET_DYE.tag;
        }


    }

    public static class Chestplate extends ArmorItem {
        protected final DyeColor color;

        public Chestplate(Properties properties, DyeColor color) {
            super(ARMOR_MATERIAL, CHESTPLATE, properties);
            this.color = color;

        }

        @Override
        public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
            return String.valueOf(CreateNuclear.asResource("textures/models/armor/"+color+"_anti_radiation_suit_layer_"+(slot == EquipmentSlot.LEGS ? 2 : 1)+".png"));
        }

        public static class DyeItemChestplateList<T extends Chestplate> implements Iterable<ItemEntry<T>> {
            private static final int COLOR_AMOUNT = DyeColor.values().length;

            private final ItemEntry<?>[] entry = new ItemEntry<?>[COLOR_AMOUNT];

            public DyeItemChestplateList(Function<DyeColor, ItemEntry<? extends T>> filler) {
                for (DyeColor color : DyeColor.values()) {
                    entry[color.ordinal()] = filler.apply(color);
                }
            }

            @SuppressWarnings("unchecked")
            public ItemEntry<T> get(DyeColor color) {
                return (ItemEntry<T>) entry[color.ordinal()];
            }

            public boolean contains(Item block) {
                for (ItemEntry<?> entry : entry) {
                    if (entry.is(block)) return true;
                }
                return false;
            }

            @SuppressWarnings("unchecked")
            public ItemEntry<T>[] toArray() {
                return (ItemEntry<T>[]) Arrays.copyOf(entry, entry.length);
            }

            @Override
            public Iterator<ItemEntry<T>> iterator() {
                return new Iterator<>() {
                    private int index = 0;
                    @Override
                    public boolean hasNext() {
                        return index < entry.length;
                    }

                    @SuppressWarnings("unchecked")
                    @Override
                    public ItemEntry<T> next() {
                        if (!hasNext()) throw new NoSuchElementException();
                        return (ItemEntry<T>) entry[index++];
                    }
                };
            }
        }

        public static TagKey<Item> getChestplateTag(String key) {
            return key.equals("white")
                    ? CNItemTags.ANTI_RADIATION_ARMOR.tag
                    : CNItemTags.ANTI_RADIATION_CHESTPLATE_DYE.tag;
        }
    }

    public static class Leggings extends ArmorItem {
        protected final DyeColor color;

        public Leggings(Properties properties, DyeColor color) {
            super(ARMOR_MATERIAL, LEGGINGS, properties);
            this.color = color;

        }

        @Override
        public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
            return String.valueOf(CreateNuclear.asResource("textures/models/armor/"+color+"_anti_radiation_suit_layer_"+(slot == EquipmentSlot.LEGS ? 2 : 1)+".png"));
        }

        public static class DyeItemLeggingsList<T extends Leggings> implements Iterable<ItemEntry<T>> {
            private static final int COLOR_AMOUNT = DyeColor.values().length;

            private final ItemEntry<?>[] entry = new ItemEntry<?>[COLOR_AMOUNT];

            public DyeItemLeggingsList(Function<DyeColor, ItemEntry<? extends T>> filler) {
                for (DyeColor color : DyeColor.values()) {
                    entry[color.ordinal()] = filler.apply(color);
                }
            }

            @SuppressWarnings("unchecked")
            public ItemEntry<T> get(DyeColor color) {
                return (ItemEntry<T>) entry[color.ordinal()];
            }

            public boolean contains(Item block) {
                for (ItemEntry<?> entry : entry) {
                    if (entry.is(block)) return true;
                }
                return false;
            }

            @SuppressWarnings("unchecked")
            public ItemEntry<T>[] toArray() {
                return (ItemEntry<T>[]) Arrays.copyOf(entry, entry.length);
            }

            @Override
            public Iterator<ItemEntry<T>> iterator() {
                return new Iterator<>() {
                    private int index = 0;
                    @Override
                    public boolean hasNext() {
                        return index < entry.length;
                    }

                    @SuppressWarnings("unchecked")
                    @Override
                    public ItemEntry<T> next() {
                        if (!hasNext()) throw new NoSuchElementException();
                        return (ItemEntry<T>) entry[index++];
                    }
                };
            }
        }

        public static TagKey<Item> getLeggingsTag(String key) {
            return key.equals("white")
                    ? CNItemTags.ANTI_RADIATION_ARMOR.tag
                    : CNItemTags.ANTI_RADIATION_LEGGINGS_DYE.tag;
        }
    }

    public static class Boot extends ArmorItem {
        public Boot(Properties properties) {
            super(ARMOR_MATERIAL, BOOTS, properties);
        }

        @Override
        public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
            return String.valueOf(CreateNuclear.asResource("textures/models/armor/white_anti_radiation_suit_layer_1.png"));

        }
    }

    public static class DyeRecipeArmorList implements Iterable<GeneratedRecipe> {
        private static final int COLOR_AMOUNT = DyeColor.values().length;

        protected final GeneratedRecipe[] recipes = new GeneratedRecipe[][getColorCount()];

        public DyeRecipeArmorList(Function<@NotNull DyeColor, GeneratedRecipe> filler) {
            for (DyeColor color : DyeColor.values()) {
                recipes[color.ordinal()] = filler.apply(color);
            }
        }

        protected int getColorCount() {
            return COLOR_AMOUNT;
        }

        public GeneratedRecipe get(@Nullable DyeColor color) {
            return recipes[color.ordinal()];
        }

        public GeneratedRecipe[] toArrays() {
            return Arrays.copyOf(recipes, recipes.length);
        }

        @NotNull
        @Override
        public Iterator<GeneratedRecipe> iterator() {
            return new Iterator<>() {
                private int index = 0;

                @Override
                public boolean hasNext() {
                    return index < recipes.length;
                }

                @Override
                public GeneratedRecipe next() {
                    if (!hasNext()) throw new NoSuchElementException();
                    return recipes[index++];
                }
            };
        }
    }

    public enum Armor {
        WHITE_ARMOR(DyeColor.WHITE),
        YELLOW_ARMOR(DyeColor.YELLOW),
        RED_ARMOR(DyeColor.RED),
        BLUE_ARMOR(DyeColor.BLUE),
        GREEN_ARMOR(DyeColor.GREEN),
        BLACK_ARMOR(DyeColor.BLACK),
        ORANGE_ARMOR(DyeColor.ORANGE),
        PURPLE_ARMOR(DyeColor.PURPLE),
        BROWN_ARMOR(DyeColor.BROWN),
        PINK_ARMOR(DyeColor.PINK),
        CYAN_ARMOR(DyeColor.CYAN),
        LIGHT_GRAY_ARMOR(DyeColor.LIGHT_GRAY),
        GRAY_ARMOR(DyeColor.GRAY),
        LIGHT_BLUE_ARMOR(DyeColor.LIGHT_BLUE),
        LIME_ARMOR(DyeColor.LIME),
        MAGENTA_ARMOR(DyeColor.MAGENTA);

        private static final Map<DyeColor, ItemEntry<Helmet>> helmetMap = new EnumMap<>(DyeColor.class);
        private static final Map<DyeColor, ItemEntry<Chestplate>> chestplateMap = new EnumMap<>(DyeColor.class);
        private static final Map<DyeColor, ItemEntry<Leggings>> leggingsMap = new EnumMap<>(DyeColor.class);

        static {
            for (DyeColor color : DyeColor.values()) {
                helmetMap.put(color, CNItems.ANTI_RADIATION_HELMETS.get(color));
                chestplateMap.put(color, CNItems.ANTI_RADIATION_CHESTPLATES.get(color));
                leggingsMap.put(color, CNItems.ANTI_RADIATION_LEGGINGS.get(color));
            }
        }

        private final DyeColor color;

        Armor(DyeColor dyeColor) {
            this.color = dyeColor;
        }

        public ItemEntry<Helmet> getHelmetItem() {
            return helmetMap.get(this.color);
        }

        public static ItemEntry<Helmet> getHelmetByColor(DyeColor color) {
            return helmetMap.get(color);
        }

        public ItemEntry<Chestplate> getChestplateItem() {
            return chestplateMap.get(this.color);
        }

        public static ItemEntry<Chestplate> getChestplateByColor(DyeColor color) {
            return chestplateMap.get(color);
        }

        public ItemEntry<Leggings> getLeggingsItem() {
            return leggingsMap.get(this.color);
        }

        public static ItemEntry<Leggings> getLeggingsByColor(DyeColor color) {
            return leggingsMap.get(color);
        }

        public static boolean isArmored(ItemStack item) {
            return helmetMap.values().stream().anyMatch(entry -> entry.is(item.getItem())) ||
                    chestplateMap.values().stream().anyMatch(entry -> entry.is(item.getItem())) ||
                    leggingsMap.values().stream().anyMatch(entry -> entry.is(item.getItem()));
        }

        public static boolean isArmored2(ItemStack item) {
            return CNItems.ANTI_RADIATION_HELMETS.contains(item.getItem())
                    || CNItems.ANTI_RADIATION_CHESTPLATES.contains(item.getItem())
                    || CNItems.ANTI_RADIATION_LEGGINGS.contains(item.getItem())
                    || CNItems.ANTI_RADIATION_BOOTS.is(item.getItem())
                    ;
        }
    }

}
