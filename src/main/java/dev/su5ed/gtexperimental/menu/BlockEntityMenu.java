package dev.su5ed.gtexperimental.menu;

import dev.su5ed.gtexperimental.blockentity.base.BaseBlockEntity;
import dev.su5ed.gtexperimental.util.TriFunction;
import dev.su5ed.gtexperimental.util.inventory.ButtonClick;
import dev.su5ed.gtexperimental.util.inventory.InteractiveSlot;
import dev.su5ed.gtexperimental.util.inventory.InventorySlot;
import dev.su5ed.gtexperimental.util.inventory.ScrollDirection;
import dev.su5ed.gtexperimental.util.inventory.SlotInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BlockEntityMenu<T extends BaseBlockEntity> extends BaseMenu {
    public final T blockEntity;
    protected final Player player;
    protected final IItemHandler playerInventory;

    private final List<Slot> blockEntitySlots = new ArrayList<>();

    public BlockEntityMenu(@Nullable MenuType<?> menuType, BlockEntityType<?> blockEntityType, int containerId, BlockPos pos, Inventory playerInventory, Player player) {
        super(menuType, containerId);

        //noinspection unchecked
        this.blockEntity = (T) player.getCommandSenderWorld().getBlockEntity(pos, blockEntityType).orElseThrow();
        this.player = player;
        this.playerInventory = new InvWrapper(playerInventory);
    }
    
    protected Slot addInventorySlot(InventorySlot slot, int x, int y) {
        return addInventorySlot(slot, 0, x, y);
    }

    protected Slot addInventorySlot(InventorySlot slot, int index, int x, int y) {
        return addInventorySlot(new SlotInventory(slot, index, x, y));
    }

    protected Slot addInventorySlot(Slot slot) {
        Slot ret = addSlot(slot);
        this.blockEntitySlots.add(ret);
        return ret;
    }

    protected int addInventorySlotBox(int x, int y, int horAmount, int verAmount, int xOffset, int yOffset, TriFunction<Integer, Integer, Integer, Slot> factory) {
        return addSlotBox(0, x, y, horAmount, verAmount, xOffset, yOffset, (index, sX, sY) -> addInventorySlot(factory.apply(index, sX, sY)));
    }
    
    public void mouseScrolled(int slotId, Player player, ScrollDirection direction, boolean shift) {
        interactWithSlot(slotId, slot -> slot.slotScroll(player, direction, shift));
    }

    @Override
    public void clicked(int slotId, int dragType, ClickType clickType, Player player) {
        interactWithSlot(slotId, slot -> {
            ButtonClick click = ButtonClick.fromClickType(clickType, dragType);
            if (click != null) {
                slot.slotClick(click, this.player, getCarried());
                this.blockEntity.setChanged();
            }
        });
        super.clicked(slotId, dragType, clickType, player);
    }
    
    private void interactWithSlot(int slotId, Consumer<InteractiveSlot> consumer) {
        if (slotId >= 0 && slotId < this.blockEntitySlots.size()) {
            Slot slot = getSlot(slotId);
            if (slot instanceof InteractiveSlot interactiveSlot) {
                consumer.accept(interactiveSlot);
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY; // TODO
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(this.blockEntity.getLevel(), this.blockEntity.getBlockPos()), this.player, this.blockEntity.getBlockState().getBlock());
    }
}
