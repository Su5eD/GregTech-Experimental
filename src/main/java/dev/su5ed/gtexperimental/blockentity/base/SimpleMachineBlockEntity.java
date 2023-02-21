package dev.su5ed.gtexperimental.blockentity.base;

import dev.su5ed.gtexperimental.blockentity.RecipeHandler;
import dev.su5ed.gtexperimental.menu.SimpleMachineMenu;
import dev.su5ed.gtexperimental.object.ModMenus;
import dev.su5ed.gtexperimental.util.BlockEntityProvider;
import dev.su5ed.gtexperimental.util.inventory.InventorySlot;
import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public abstract class SimpleMachineBlockEntity extends MachineBlockEntity implements MenuProvider {
    private final ModMenus.BlockEntityMenuConstructor<SimpleMachineMenu> menuConstructor;
    public final InventorySlot inputSlot;
    public final InventorySlot outputSlot;
    public final RecipeHandler<?, ?, ?, ?> recipeHandler;

    public SimpleMachineBlockEntity(BlockEntityProvider provider, BlockPos pos, BlockState state, ModMenus.BlockEntityMenuConstructor<SimpleMachineMenu> menuConstructor) {
        super(provider, pos, state);

        this.menuConstructor = menuConstructor;
        this.inputSlot = addSlot("input", InventorySlot.Mode.BOTH, 1);
        this.outputSlot = addSlot("output", InventorySlot.Mode.OUTPUT, 1);
        this.recipeHandler = addComponent(createRecipeHandler());
    }

    protected abstract RecipeHandler<?, ?, ?, ?> createRecipeHandler();

    @Override
    protected void onInventoryChanged() {
        super.onInventoryChanged();
        this.recipeHandler.checkRecipe();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return this.menuConstructor.create(containerId, this.worldPosition, playerInventory, player);
    }
}
