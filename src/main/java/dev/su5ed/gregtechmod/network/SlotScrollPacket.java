package dev.su5ed.gregtechmod.network;

import dev.su5ed.gregtechmod.GregTechMod;
import dev.su5ed.gregtechmod.menu.BlockEntityMenu;
import dev.su5ed.gregtechmod.util.inventory.ScrollDirection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SlotScrollPacket(int containerId, int slotId, int stateId, ScrollDirection direction, boolean shift) {
    
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.containerId);
        buf.writeInt(this.slotId);
        buf.writeInt(this.stateId);
        buf.writeEnum(this.direction);
        buf.writeBoolean(this.shift);
    }
    
    public static SlotScrollPacket decode(FriendlyByteBuf buf) {
        int containerId = buf.readInt();
        int slotId = buf.readInt();
        int stateId = buf.readInt();
        ScrollDirection direction = buf.readEnum(ScrollDirection.class);
        boolean shift = buf.readBoolean();
        return new SlotScrollPacket(containerId, slotId, stateId, direction, shift);
    }
    
    public void processPacket(Supplier<NetworkEvent.Context> ctx) {
        ServerPlayer player = ctx.get().getSender();
        if (player.containerMenu.containerId == this.containerId) {
            if (!player.containerMenu.stillValid(player)) {
                GregTechMod.LOGGER.debug("Player {} interacted with invalid menu {}", player, player.containerMenu);
                return;
            }
            if (!player.containerMenu.isValidSlotIndex(this.slotId)) {
                GregTechMod.LOGGER.debug("Player {} clicked invalid slot index: {}, available slots: {}", player.getName(), this.slotId, player.containerMenu.slots.size());
                return;
            }
            if (player.containerMenu instanceof BlockEntityMenu<?> blockEntityMenu) {
                boolean modifiedState = this.stateId != player.containerMenu.getStateId();
                player.containerMenu.suppressRemoteUpdates();
                blockEntityMenu.mouseScrolled(this.slotId, player, this.direction, this.shift);

                player.containerMenu.resumeRemoteUpdates();
                if (modifiedState) {
                    player.containerMenu.broadcastFullState();
                }
                else {
                    player.containerMenu.broadcastChanges();
                }
            }
        }
    }
}
