package mods.gregtechmod.recipe.ingredient;

import ic2.core.util.StackUtil;
import mods.gregtechmod.api.recipe.IRecipeIngredient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.Arrays;
import java.util.Collection;

public class RecipeIngredientItemStack extends Ingredient implements IRecipeIngredient {
    private final int count;

    public RecipeIngredientItemStack(Item item) {
        this(item, 1);
    }

    public RecipeIngredientItemStack(Item item, int count) {
        this(item, count, 0);
    }

    public RecipeIngredientItemStack(Item item, int count, int meta) {
        this(new ItemStack(item, count, meta));
    }

    public RecipeIngredientItemStack(ItemStack stack) {
        super(StackUtil.copyWithSize(stack, 1));
        this.count = stack.getCount();
    }

    @Override
    public Ingredient asIngredient() {
        return this;
    }

    @Override
    public int getCount() {
        return this.count;
    }

    @Override
    public Collection<ItemStack> getMatchingInputs() {
        return Arrays.asList(super.getMatchingStacks());
    }
}
