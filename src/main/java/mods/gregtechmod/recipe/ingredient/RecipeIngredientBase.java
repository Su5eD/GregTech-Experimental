package mods.gregtechmod.recipe.ingredient;

import ic2.core.util.StackUtil;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.util.ItemStackComparator;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public abstract class RecipeIngredientBase<T extends Ingredient> implements IRecipeIngredient {
    protected final T ingredient;
    protected final int count;

    protected RecipeIngredientBase(T ingredient, int count) {
        this.ingredient = ingredient;
        this.count = count;
    }

    @Override
    public Ingredient asIngredient() {
        return this.ingredient;
    }

    @Override
    public int getCount() {
        return this.count;
    }

    @Override
    public boolean apply(@Nullable ItemStack input, boolean checkSize) {
        if (input != null) {
            for (ItemStack stack : this.getMatchingInputs()) {
                if (stack.getItem() == input.getItem()
                        && (input.getMetadata() == OreDictionary.WILDCARD_VALUE || input.getMetadata() == stack.getMetadata())
                        && StackUtil.checkNbtEquality(stack.getTagCompound(), input.getTagCompound())) {
                    if (checkSize) return input.getCount() >= this.count;
                    else return true;
                }
            }
        }

        return false;
    }

    @Override
    public List<ItemStack> getMatchingInputs() {
        return Arrays.asList(this.ingredient.getMatchingStacks());
    }

    @Override
    public int compareTo(IRecipeIngredient other) {
        int total = 0;
        List<ItemStack> matchingInputs = this.getMatchingInputs();
        List<ItemStack> otherMatchingInputs = other.getMatchingInputs();

        if (otherMatchingInputs.isEmpty()) return -other.compareTo(this);

        outerloop:
        for (ItemStack firstStack : matchingInputs) {
            for (ItemStack secondStack : otherMatchingInputs) {
                int diff = ItemStackComparator.INSTANCE.compare(firstStack, secondStack);
                if (diff == 0) {
                    total = 0;
                    break outerloop;
                }
                total += diff;
            }
        }

        if (total == 0) total += other.getCount() - this.count;

        return total;
    }
}
