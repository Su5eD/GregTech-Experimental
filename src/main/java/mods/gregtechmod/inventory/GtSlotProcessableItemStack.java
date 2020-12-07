package mods.gregtechmod.inventory;

import ic2.core.block.IInventorySlotHolder;
import ic2.core.block.invslot.InvSlotConsumable;
import mods.gregtechmod.api.recipe.IGtMachineRecipe;
import mods.gregtechmod.api.recipe.IGtRecipeManager;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Collection;

public class GtSlotProcessableItemStack<R extends IGtMachineRecipe<ItemStack, Collection<ItemStack>>> extends InvSlotConsumable {
    protected final IGtRecipeManager<ItemStack, R> recipeManager;

    public GtSlotProcessableItemStack(IInventorySlotHolder<?> base, String name, int count, IGtRecipeManager<ItemStack, R> recipeManager) {
        this(base, name, Access.I, count, InvSide.TOP, recipeManager);
    }

    public GtSlotProcessableItemStack(IInventorySlotHolder<?> base, String name, Access access, int count, InvSide preferredSide, IGtRecipeManager<ItemStack, R> recipeManager) {
        super(base, name, access, count, preferredSide);
        this.recipeManager = recipeManager;
    }

    public R process() {
        ItemStack input = this.get();
        return input.isEmpty() ? null : this.recipeManager.getRecipeFor(input);
    }

    @Override
    public boolean accepts(ItemStack stack) {
        return this.recipeManager.getRecipeFor(stack) != null;
    }

    public void consume(@Nonnull IGtMachineRecipe<ItemStack, ?> recipe) {
        ItemStack input = this.get();
        if (input.isEmpty()) throw new IllegalStateException("Tried to consume item from an empty slot");

        this.put(ItemStack.EMPTY);
    }
}
