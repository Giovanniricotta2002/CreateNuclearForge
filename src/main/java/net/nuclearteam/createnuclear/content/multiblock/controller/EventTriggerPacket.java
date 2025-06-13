package net.nuclearteam.createnuclear.content.multiblock.controller;

import net.createmod.catnip.net.base.ClientboundPacketPayload;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.nuclearteam.createnuclear.CNPackets;
import net.nuclearteam.createnuclear.foundation.events.overlay.EventTextOverlay;

/**
 * Packet sent from server to client to trigger a localized event overlay.
 */
public record EventTriggerPacket(int duration) implements ClientboundPacketPayload {
    public static final StreamCodec<RegistryFriendlyByteBuf, EventTriggerPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, EventTriggerPacket::duration,
            EventTriggerPacket::new
    );

    @Override
    public PacketTypeProvider getTypeProvider() {
        return CNPackets.TRIGGER_EVENT_TEXT_OVERLAY;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handle(LocalPlayer player) {
        EventTextOverlay.triggerEvent(duration);
    }
}
