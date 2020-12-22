package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.recipe.IGtMachineRecipe;
import mods.gregtechmod.api.recipe.IGtRecipeManager;
import mods.gregtechmod.api.recipe.IRecipeIngredient;
import mods.gregtechmod.recipe.manager.RecipeManagerBasic;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RecipePulverizer extends Recipe<IRecipeIngredient, List<ItemStack>> {
    public static IGtRecipeManager<IRecipeIngredient, ItemStack, IGtMachineRecipe<IRecipeIngredient, List<ItemStack>>> manager = new RecipeManagerBasic<>(); //The manager is not exposed to the API
    public final int chance;

    private RecipePulverizer(IRecipeIngredient input, List<ItemStack> output, int chance) {
        super(input, output, 3, 300 * input.getCount());
        this.chance = chance;
    }

    public static RecipePulverizer create(IRecipeIngredient input, ItemStack output, int chance) {
        return create(input, Collections.singletonList(output), chance);
    }

    public static RecipePulverizer create(IRecipeIngredient input, ItemStack primaryOutput, ItemStack secondaryOutput, int chance) {
        return create(input, Arrays.asList(primaryOutput, secondaryOutput), chance);
    }

    @JsonCreator
    public static RecipePulverizer create(@JsonProperty(value = "input", required = true) IRecipeIngredient input,
                                          @JsonProperty(value = "output", required = true) List<ItemStack> output,
                                          @JsonProperty(value = "chance", required = true) int chance) {
        if (output.size() > 2) {
            GregTechAPI.logger.error("Tried to add a pulverizer recipe for " + output.stream().map(ItemStack::getTranslationKey).collect(Collectors.joining()) + " with way too many outputs! Reducing the outputs to 2");
            output = output.subList(0, 2);
        }
        return new RecipePulverizer(input, output, chance);
    }
}
