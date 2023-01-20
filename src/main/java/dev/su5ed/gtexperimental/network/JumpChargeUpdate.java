package dev.su5ed.gtexperimental.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record JumpChargeUpdate(double charge) {

    public static void encode(JumpChargeUpdate packet, FriendlyByteBuf buf) {
        buf.writeDouble(packet.charge);
    }

    public static JumpChargeUpdate decode(FriendlyByteBuf buf) {
        double fatigue = buf.readDouble();
        return new JumpChargeUpdate(fatigue);
    }

    public static void processPacket(JumpChargeUpdate packet, Supplier<NetworkEvent.Context> ctx) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.updateJumpCharge(packet));
    }
}