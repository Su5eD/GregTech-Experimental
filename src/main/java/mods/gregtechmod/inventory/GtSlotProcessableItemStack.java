package mods.gregtechmod.inventory;

import ic2.core.block.IInventorySlotHolder;
import ic2.core.block.invslot.InvSlotConsumable;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManager;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class GtSlotProcessableItemStack<RM extends IGtRecipeManager<IRecipeIngredient, ItemStack, ?>> extends InvSlotConsumable {
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
        return this.recipeManager.hasRecipeFor(stack);
    }

    public ItemStack consume(@Nonnull IMachineRecipe<IRecipeIngredient, ?> recipe) {
        return this.consume(recipe.getInput().getCount());
    }

    public ItemStack consume(@Nonnull IMachineRecipe<IRecipeIngredient, ?> recipe, boolean consumeContainers) {
        return this.consume(recipe.getInput().getCount(), false, true);
    }
}
