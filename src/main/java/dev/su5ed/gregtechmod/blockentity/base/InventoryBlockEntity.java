package dev.su5ed.gregtechmod.blockentity.base;

import dev.su5ed.gregtechmod.blockentity.component.InventoryHandler;
import dev.su5ed.gregtechmod.util.BlockEntityProvider;
import dev.su5ed.gregtechmod.util.inventory.InventorySlot;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class InventoryBlockEntity extends BaseBlockEntity {
    protected final InventoryHandler inventoryHandler;
    
    protected InventoryBlockEntity(BlockEntityProvider provider, BlockPos pos, BlockState state) {
        super(provider, pos, state);
        
        this.inventoryHandler = addComponent(new InventoryHandler(this, this::onInventoryChanged));
    }

    protected InventorySlot addSlot(String name, InventorySlot.Mode mode, int size) {
        return addSlot(name, mode, size, stack -> true);
    }

    protected InventorySlot addSlot(String name, InventorySlot.Mode mode, int size, Predicate<ItemStack> filter) {
        return addSlot(name, mode, size, filter, stack -> {});
    }

    protected InventorySlot addSlot(String name, InventorySlot.Mode mode, int size, Predicate<ItemStack> filter, Consumer<ItemStack> onChanged) {
        return this.inventoryHandler.addSlot(name, mode, size, filter, onChanged);
    }

    protected void onInventoryChanged() {}
}
