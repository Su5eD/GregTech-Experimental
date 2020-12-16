package mods.gregtechmod.recipe;

import mods.gregtechmod.api.recipe.IGtMachineRecipe;
import mods.gregtechmod.api.recipe.IRecipeCentrifuge;
import mods.gregtechmod.api.recipe.IRecipeFactory;
import mods.gregtechmod.api.recipe.IRecipeIngredient;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientItemStack;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RecipeFactory implements IRecipeFactory {

    @Override
    public IRecipeCentrifuge makeCentrifugeRecipe(ItemStack input, List<ItemStack> outputs, int cells, int duration) {
        return RecipeCentrifuge.create(new RecipeIngredientItemStack(input), outputs, cells, duration);
    }

    @Override
    public IGtMachineRecipe<List<IRecipeIngredient>, ItemStack> makeAssemblerRecipe(IRecipeIngredient primaryInput, IRecipeIngredient secondaryInput, ItemStack output, double energyCost, int duration) {
        return RecipeAssembler.create(nonNullList(primaryInput, secondaryInput), output, energyCost, duration);
    }

    private <T> List<T> nonNullList(T... elements) {
        return Stream.of(elements)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
