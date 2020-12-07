package mods.gregtechmod.inventory;

import ic2.core.block.IInventorySlotHolder;
import ic2.core.block.invslot.InvSlotConsumable;
import mods.gregtechmod.api.recipe.IGtMachineRecipe;
import mods.gregtechmod.api.recipe.IGtRecipeManager;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class GtSlotProcessableItemStack<R extends IGtMachineRecipe<ItemStack, Collection<ItemStack>>, M> extends InvSlotConsumable {
    protected final IGtRecipeManager<ItemStack, R, M> recipeManager;

    public GtSlotProcessableItemStack(IInventorySlotHolder<?> base, String name, int count, IGtRecipeManager<ItemStack, R, M> recipeManager) {
        this(base, name, Access.I, count, InvSide.TOP, recipeManager);
    }

    public GtSlotProcessableItemStack(IInventorySlotHolder<?> base, String name, Access access, int count, InvSide preferredSide, IGtRecipeManager<ItemStack, R, M> recipeManager) {
        super(base, name, access, count, preferredSide);
        this.recipeManager = recipeManager;
    }

    public R process() {
        return process(Collections.emptyMap());
    }

    public R process(Map<String, M> metadata) {
        ItemStack input = this.get();
        return input.isEmpty() ? null : this.recipeManager.getRecipeFor(input, metadata);
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
