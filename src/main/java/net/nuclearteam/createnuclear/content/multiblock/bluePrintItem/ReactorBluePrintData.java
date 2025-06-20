package net.nuclearteam.createnuclear.content.multiblock.bluePrintItem;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.createmod.catnip.codecs.stream.CatnipStreamCodecBuilders;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

public record ReactorBluePrintData(int countGraphiteRod, int countUraniumRod, int graphiteTime, int uraniumTime, PatternData[] pattern, PatternData[] patternAll) {
    private static final Codec<PatternData[]> PATTERN_ARRAY_CODEC = PatternData.CODEC.listOf().xmap(
        list -> list.toArray(PatternData[]::new),
        List::of
    );

    private static final StreamCodec<RegistryFriendlyByteBuf, PatternData[]> STREAM_PATTERN_ARRAY_CODEC = CatnipStreamCodecBuilders.array(PatternData.STREAM_CODEC, PatternData.class);

    public static final Codec<ReactorBluePrintData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("countGraphiteRod").forGetter(ReactorBluePrintData::countGraphiteRod),
                    Codec.INT.fieldOf("countUraniumRod").forGetter(ReactorBluePrintData::countUraniumRod),
                    Codec.INT.fieldOf("graphiteTime").forGetter(ReactorBluePrintData::graphiteTime),
                    Codec.INT.fieldOf("uraniumTime").forGetter(ReactorBluePrintData::uraniumTime),
                    PATTERN_ARRAY_CODEC.fieldOf("pattern").forGetter(ReactorBluePrintData::pattern),
                    PATTERN_ARRAY_CODEC.fieldOf("patternAll").forGetter(ReactorBluePrintData::patternAll)
            ).apply(instance, ReactorBluePrintData::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ReactorBluePrintData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ReactorBluePrintData::countGraphiteRod,
            ByteBufCodecs.INT, ReactorBluePrintData::countUraniumRod,
            ByteBufCodecs.INT, ReactorBluePrintData::graphiteTime,
            ByteBufCodecs.INT, ReactorBluePrintData::uraniumTime,
            STREAM_PATTERN_ARRAY_CODEC, ReactorBluePrintData::pattern,
            STREAM_PATTERN_ARRAY_CODEC, ReactorBluePrintData::patternAll,
            ReactorBluePrintData::new
    );
}
