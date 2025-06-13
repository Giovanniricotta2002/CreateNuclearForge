package net.nuclearteam.createnuclear.content.multiblock.bluePrintItem;

import io.netty.buffer.ByteBuf;
import net.createmod.catnip.codecs.stream.CatnipStreamCodecBuilders;
import net.createmod.catnip.codecs.stream.CatnipStreamCodecs;
import net.createmod.catnip.net.base.ServerboundPacketPayload;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.nuclearteam.createnuclear.CNPackets;
import net.nuclearteam.createnuclear.infrastructure.config.CNConfigs;

public record ReactorBluePrintItemPacket(CompoundTag tag, float heat, int graphiteTime, int uraniumTime, int countGraphiteRod, int countUraniumRod) implements ServerboundPacketPayload {

    public static final StreamCodec<ByteBuf, ReactorBluePrintItemPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG, ReactorBluePrintItemPacket::tag,
            ByteBufCodecs.FLOAT, ReactorBluePrintItemPacket::heat,
            ByteBufCodecs.INT, ReactorBluePrintItemPacket::graphiteTime,
            ByteBufCodecs.INT, ReactorBluePrintItemPacket::uraniumTime,
            ByteBufCodecs.INT, ReactorBluePrintItemPacket::countGraphiteRod,
            ByteBufCodecs.INT, ReactorBluePrintItemPacket::countUraniumRod,
            ReactorBluePrintItemPacket::new
    );

    @Override
    public PacketTypeProvider getTypeProvider() {
        return CNPackets.CONFIGURE_REACTOR_PATTERN;
    }

    @Override
    public void handle(ServerPlayer player) {
        if (player.containerMenu instanceof ReactorBluePrintMenu c) {
            // On crée directement le state et on l’applique
            this.applyState(new ReactorState(
                    this.countUraniumRod,
                    this.countGraphiteRod,
                    this.graphiteTime,
                    this.uraniumTime,
                    this.heat
            ), c);
        }
    }

    public record ReactorState(
            int countUraniumRod,
            int countGraphiteRod,
            int graphiteTime,
            int uraniumTime,
            float heat
    ) {}

    public void applyState(ReactorState state, ReactorBluePrintMenu c) {
        c.countUraniumRod = state.countUraniumRod();
        c.countGraphiteRod = state.countGraphiteRod();
        c.graphiteTime = state.graphiteTime();
        c.uraniumTime = state.uraniumTime();
        c.heat = state.heat();
        c.sendUpdate = true;
    }

}
