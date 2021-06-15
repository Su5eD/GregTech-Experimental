package mods.gregtechmod.api.recipe.manager;

import net.minecraft.item.ItemStack;

public interface IRecipeHolder<I> {
    /**
     * Checks if a recipe exists for the target input without checking the stacksize
     */
    boolean hasRecipeFor(I input);
    
    boolean hasRecipeFor(ItemStack input);
}
