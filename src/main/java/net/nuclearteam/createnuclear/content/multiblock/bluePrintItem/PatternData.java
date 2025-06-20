package net.nuclearteam.createnuclear.content.multiblock.bluePrintItem;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.nuclearteam.createnuclear.CNTags.CNItemTags;

public record PatternData(int slot, ItemStack stack) {
    private static final ItemStack DEFAULT_STACK = ItemStack.EMPTY;
    public static final Codec<PatternData> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.INT.fieldOf("slot").forGetter(PatternData::slot),
            ItemStack.CODEC
                .flatXmap(stack -> {
                        if (!stack.isEmpty() || stack.is(CNItemTags.FUEL.tag) || stack.is(CNItemTags.COOLER.tag)) return DataResult.success(stack);
                        return DataResult.success(DEFAULT_STACK);
                    },
                    DataResult::success
                )
                .fieldOf("Stack")
                .forGetter(PatternData::stack)
        ).apply(i, PatternData::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, PatternData> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.INT, PatternData::slot,
                    ItemStack.STREAM_CODEC, PatternData::stack,
                    PatternData::new
            );

    public static ItemStack getDefaultStack() {
        return DEFAULT_STACK;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PatternData(int slot1, ItemStack stack1))) return false;
        return slot == slot1 && ItemStack.isSameItem(this.stack, stack1);
    }

    @Override
    public int hashCode() {
        return 31 * Integer.hashCode(slot) + (stack == null ? 0 : stack.getItem().hashCode());
    }
}
