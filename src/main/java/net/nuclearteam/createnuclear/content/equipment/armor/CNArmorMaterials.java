package net.nuclearteam.createnuclear.content.equipment.armor;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;

import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.nuclearteam.createnuclear.CNItems;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class CNArmorMaterials {
    private static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(Registries.ARMOR_MATERIAL, CreateNuclear.MOD_ID);

    public static final Holder<ArmorMaterial>  ANTI_RADIATION_SUIT = register(
            "anti_radiation_suit",
            new int[]{2, 4, 3, 1, 4 },
            12,
            SoundEvents.ARMOR_EQUIP_NETHERITE,
            0.0f,
            0.0f,
            () -> Ingredient.of(CNItems.LEAD_INGOT)
    );

    private static Holder<ArmorMaterial> register(
            String name,
            int[] defense,
            int enchantmentValue,
            Holder<SoundEvent> equipSound,
            float toughness,
            float knockbackResistance,
            Supplier<Ingredient> repairIngredient
    ) {
        List<ArmorMaterial.Layer> list = List.of(new ArmorMaterial.Layer(CreateNuclear.asResource(name)));
        return register(name, defense, enchantmentValue, equipSound, toughness, knockbackResistance, repairIngredient, list);
    }

    private static Holder<ArmorMaterial> register(String name, int[] defense, int enchantmentValue, Holder<SoundEvent> equipSound, float toughness,
                                                  float knockbackResistance,
                                                  Supplier<Ingredient> repairIngredient,
                                                  List<ArmorMaterial.Layer> layers) {
        EnumMap<ArmorItem.Type, Integer> enumMap = new EnumMap<>(ArmorItem.Type.class);
        for (ArmorItem.Type armorItem : ArmorItem.Type.values()) {
            enumMap.put(armorItem, defense[armorItem.ordinal()]);
        }

        return ARMOR_MATERIALS.register(name,
                () -> new ArmorMaterial(enumMap, enchantmentValue, equipSound, repairIngredient, layers, toughness, knockbackResistance)
        );
    }

    @ApiStatus.Internal
    public static void register(IEventBus eventBus) {
        ARMOR_MATERIALS.register(eventBus);
    }

    public static int durabilityForType(ArmorItem.Type type) {
        int[] BASE_DURABILITY = {11, 16, 15, 13};
        int durabilityMultiplier = 15;
        return BASE_DURABILITY[type.ordinal()] * durabilityMultiplier;
    }
}
