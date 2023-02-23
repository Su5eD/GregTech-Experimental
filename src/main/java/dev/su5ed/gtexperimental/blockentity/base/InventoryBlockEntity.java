package dev.su5ed.gtexperimental.blockentity.base;

import dev.su5ed.gtexperimental.blockentity.component.InventoryHandler;
import dev.su5ed.gtexperimental.util.BlockEntityProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public abstract class InventoryBlockEntity extends BaseBlockEntity {
    protected final InventoryHandler inventoryHandler;

    protected InventoryBlockEntity(BlockEntityProvider provider, BlockPos pos, BlockState state) {
        super(provider, pos, state);

        this.inventoryHandler = addComponent(new InventoryHandler(this, this::onInventoryChanged));
    }

    protected void onInventoryChanged() {}

    @Override
    public void provideAdditionalDrops(List<? super ItemStack> drops) {
        super.provideAdditionalDrops(drops);

        drops.addAll(this.inventoryHandler.getAllItems());
    }
}
