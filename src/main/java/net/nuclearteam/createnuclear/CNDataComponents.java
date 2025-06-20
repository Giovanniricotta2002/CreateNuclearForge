package net.nuclearteam.createnuclear;

import com.simibubi.create.Create;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.nuclearteam.createnuclear.content.multiblock.bluePrintItem.ReactorBluePrintData;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.UnaryOperator;

public class CNDataComponents {
    private static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, CreateNuclear.MOD_ID);

    public static final DataComponentType<Float> HEAT = register(
            "heat",
            builder -> builder.persistent(ExtraCodecs.POSITIVE_FLOAT).networkSynchronized(ByteBufCodecs.FLOAT)
    );

    public static final DataComponentType<ReactorBluePrintData> REACTOR_BLUE_PRINT_DATA = register(
            "reactor_blue_print_data",
            builder -> builder.persistent(ReactorBluePrintData.CODEC).networkSynchronized(ReactorBluePrintData.STREAM_CODEC)
    );

    public static final DataComponentType<CompoundTag> PATTERN = register(
            "pattern",
            builder -> builder.persistent(CompoundTag.CODEC).networkSynchronized(ByteBufCodecs.COMPOUND_TAG)
    );

    public static final DataComponentType<Integer> URANIUM_TIME = register(
            "uranium_time",
            builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.INT)
    );

    public static final DataComponentType<Integer> GRAPHITE_TIME = register(
            "graphite_time",
            builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.INT)
    );

    public static final DataComponentType<Integer> COUNT_GRAPHITE_ROD = register(
            "count_graphite_rod",
            builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.INT)
    );

    public static final DataComponentType<Integer> COUNT_URANIUM_ROD = register(
            "count_uranium_rod",
            builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.INT)
    );

    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        DataComponentType<T> type = builder.apply(DataComponentType.builder()).build();
        DATA_COMPONENTS.register(name, () -> type);
        return type;
    }

    @ApiStatus.Internal
    public static void register(IEventBus modEventBus) {
        DATA_COMPONENTS.register(modEventBus);
    }
}
