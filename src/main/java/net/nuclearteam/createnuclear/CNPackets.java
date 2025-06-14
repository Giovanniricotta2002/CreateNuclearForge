package net.nuclearteam.createnuclear;

import net.createmod.catnip.net.base.BasePacketPayload;
import net.createmod.catnip.net.base.CatnipPacketRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import net.nuclearteam.createnuclear.content.multiblock.bluePrintItem.ReactorBluePrintItemPacket;
import net.nuclearteam.createnuclear.content.multiblock.controller.EventTriggerPacket;

import java.util.Locale;

public enum CNPackets implements BasePacketPayload.PacketTypeProvider {
    CONFIGURE_REACTOR_PATTERN(ReactorBluePrintItemPacket.class, ReactorBluePrintItemPacket.STREAM_CODEC),

    // To client
    TRIGGER_EVENT_TEXT_OVERLAY(EventTriggerPacket.class, EventTriggerPacket.STREAM_CODEC),
    ;

    private final CatnipPacketRegistry.PacketType<?> type;

    <T extends BasePacketPayload> CNPackets(Class<T> clazz, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
        String name = this.name().toLowerCase(Locale.ROOT);
        this.type = new CatnipPacketRegistry.PacketType<>(
                new CustomPacketPayload.Type<>(CreateNuclear.asResource(name)),
                clazz, codec
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends CustomPacketPayload> CustomPacketPayload.Type<T> getType() {
        return (CustomPacketPayload.Type<T>) this.type.type();
    }

    public static void register() {
        CatnipPacketRegistry packetRegistry = new CatnipPacketRegistry(CreateNuclear.MOD_ID, 1);
        for (CNPackets packet : CNPackets.values()) {
            packetRegistry.registerPacket(packet.type);
        }
        packetRegistry.registerAllPackets();
    }

}
