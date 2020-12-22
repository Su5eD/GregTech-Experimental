package mods.gregtechmod.api.recipe;

import net.minecraft.item.ItemStack;

public interface IRecipeIngredientFactory {

    default IRecipeIngredient fromStacks(ItemStack... stacks) {
        return fromStacks(1, stacks);
    }

    IRecipeIngredient fromStacks(int count, ItemStack... stacks);

    default IRecipeIngredient fromOre(String ore) {
        return fromOre(ore, 1);
    }

    IRecipeIngredient fromOre(String ore, int count);
}
