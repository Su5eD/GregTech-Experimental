package dev.su5ed.gregtechmod.network;

import dev.su5ed.gregtechmod.util.KeyboardHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record KeyPressUpdate(Action action, KeyboardHandler.Key key) {

    public static void encode(KeyPressUpdate packet, FriendlyByteBuf buf) {
        buf.writeEnum(packet.action);
        buf.writeEnum(packet.key);
    }

    public static KeyPressUpdate decode(FriendlyByteBuf buf) {
        Action action = buf.readEnum(Action.class);
        KeyboardHandler.Key key = buf.readEnum(KeyboardHandler.Key.class);
        return new KeyPressUpdate(action, key);
    }

    public static void processPacket(KeyPressUpdate packet, Supplier<NetworkEvent.Context> ctx) {
        ServerPlayer player = ctx.get().getSender();
        KeyboardHandler.Key key = packet.key();

        if (packet.action() == Action.PRESS) {
            KeyboardHandler.pressKey(player, key);
        }
        else {
            KeyboardHandler.releaseKey(player, key);
        }
    }

    public enum Action {
        PRESS,
        RELEASE
    }
}
