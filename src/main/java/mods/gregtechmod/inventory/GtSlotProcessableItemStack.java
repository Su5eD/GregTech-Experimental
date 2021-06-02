package mods.gregtechmod.inventory;

import ic2.core.block.IInventorySlotHolder;
import ic2.core.block.invslot.InvSlotConsumable;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManager;
import net.minecraft.item.ItemStack;

public class GtSlotProcessableItemStack<RM extends IGtRecipeManager<?, I, ?>, I> extends InvSlotConsumable {
    protected final RM recipeManager;

    public GtSlotProcessableItemStack(IInventorySlotHolder<?> base, String name, int count, RM recipeManager) {
        this(base, name, Access.I, count, InvSide.TOP, recipeManager);
    }

    public GtSlotProcessableItemStack(IInventorySlotHolder<?> base, String name, Access access, int count, InvSide preferredSide, RM recipeManager) {
        super(base, name, access, count, preferredSide);
        this.recipeManager = recipeManager;
    }

    @Override
    public boolean accepts(ItemStack stack) {
        return this.recipeManager == null || this.recipeManager.hasRecipeFor(stack);
    }

    public ItemStack consume(int count, boolean consumeContainers) {
        return this.consume(count, false, consumeContainers);
    }
}
