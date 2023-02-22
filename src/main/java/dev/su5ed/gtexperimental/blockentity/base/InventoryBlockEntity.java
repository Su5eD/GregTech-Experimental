package dev.su5ed.gtexperimental.blockentity.base;

import dev.su5ed.gtexperimental.blockentity.component.InventoryHandler;
import dev.su5ed.gtexperimental.util.BlockEntityProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public abstract class InventoryBlockEntity extends BaseBlockEntity {
    protected final InventoryHandler inventoryHandler;
    
    protected InventoryBlockEntity(BlockEntityProvider provider, BlockPos pos, BlockState state) {
        super(provider, pos, state);
        
        this.inventoryHandler = addComponent(new InventoryHandler(this, this::onInventoryChanged));
    }

    protected void onInventoryChanged() {}
}
