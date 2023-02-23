package dev.su5ed.gtexperimental.menu;

import dev.su5ed.gtexperimental.blockentity.base.BaseBlockEntity;
import dev.su5ed.gtexperimental.blockentity.base.SimpleMachineBlockEntity;
import dev.su5ed.gtexperimental.blockentity.component.BlockEntityComponent;
import dev.su5ed.gtexperimental.network.BlockEntityComponentUpdate;
import dev.su5ed.gtexperimental.network.GregTechNetwork;
import dev.su5ed.gtexperimental.network.NetworkHandler;
import dev.su5ed.gtexperimental.object.GTBlockEntity;
import dev.su5ed.gtexperimental.object.ModMenus;
import dev.su5ed.gtexperimental.util.GtUtil;
import dev.su5ed.gtexperimental.util.inventory.SlotButton;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

public class SimpleMachineMenu extends BlockEntityMenu<SimpleMachineBlockEntity> {

    public static SimpleMachineMenu autoMacerator(int containerId, BlockPos pos, Inventory playerInventory, Player player) {
        return new SimpleMachineMenu(ModMenus.AUTO_MACERATOR.get(), GTBlockEntity.AUTO_MACERATOR.getType(), containerId, pos, playerInventory, player);
    }

    public SimpleMachineMenu(@Nullable MenuType<?> menuType, BlockEntityType<?> blockEntityType, int containerId, BlockPos pos, Inventory playerInventory, Player player) {
        super(menuType, blockEntityType, containerId, pos, playerInventory, player);

        // Order matters
        addInventorySlot(this.blockEntity.inputSlot, 53, 25);
        addInventorySlot(this.blockEntity.queueInputSlot, 35, 25);
        addInventorySlot(this.blockEntity.queueOutputSlot, 107, 25);
        addInventorySlot(this.blockEntity.outputSlot, 125, 25);
        addInventorySlot(this.blockEntity.extraSlot, 80, 63);
        addPlayerInventorySlots(playerInventory);

        addInventorySlot(SlotButton.serverOnly(8, 63, this.blockEntity::switchProvideEnergy));
        addInventorySlot(SlotButton.serverOnly(26, 63, this.blockEntity::switchAutoOutput));
        addInventorySlot(SlotButton.serverOnly(44, 63, this.blockEntity::switchSplitInput));
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

        // TODO Track changes
        if (this.player instanceof ServerPlayer serverPlayer) {
            updatePlayerClientComponent(serverPlayer, this.blockEntity, this.blockEntity.recipeHandler);
        }
        GregTechNetwork.updateClientField(this.blockEntity, "provideEnergy");
        GregTechNetwork.updateClientField(this.blockEntity, "autoOutput");
        GregTechNetwork.updateClientField(this.blockEntity, "splitInput");
    }

    public static void updatePlayerClientComponent(ServerPlayer player, BaseBlockEntity be, BlockEntityComponent component) {
        GtUtil.assertServerSide(be.getLevel());
        FriendlyByteBuf data = NetworkHandler.serializeClass(component);
        BlockEntityComponentUpdate packet = new BlockEntityComponentUpdate(be, data, component.getName());
        GregTechNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }
}
