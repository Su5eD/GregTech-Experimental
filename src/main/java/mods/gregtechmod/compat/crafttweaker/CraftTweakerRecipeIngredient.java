package mods.gregtechmod.compat.crafttweaker;

import com.google.common.base.MoreObjects;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import ic2.core.util.StackUtil;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import one.util.streamex.StreamEx;

import java.util.Arrays;
import java.util.List;

public class CraftTweakerRecipeIngredient implements IRecipeIngredient {
    private final IIngredient ingredient;

    public CraftTweakerRecipeIngredient(IIngredient ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public Ingredient asIngredient() {
        return CraftTweakerMC.getIngredient(this.ingredient);
    }

    @Override
    public int getCount() {
        return this.ingredient.getAmount();
    }

    @Override
    public boolean apply(ItemStack stack, boolean checkCount) {
        ItemStack matcher = checkCount ? stack : StackUtil.copyWithSize(stack, stack.getMaxStackSize());
        return this.ingredient.matches(CraftTweakerMC.getIItemStack(matcher));
    }

    @Override
    public boolean apply(IRecipeIngredient ingredient) {
        return ingredient.stream()
            .anyMatch(stack -> apply(stack, false)) && ingredient.getCount() >= this.ingredient.getAmount();
    }

    @Override
    public StreamEx<ItemStack> stream() {
        return StreamEx.of(getMatchingInputs());
    }

    @Override
    public List<ItemStack> getMatchingInputs() {
        return Arrays.asList(CraftTweakerMC.getItemStacks(this.ingredient.getItems()));
    }

    @Override
    public boolean isEmpty() {
        return getMatchingInputs().isEmpty();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("ingredient", ingredient)
            .toString();
    }
}
