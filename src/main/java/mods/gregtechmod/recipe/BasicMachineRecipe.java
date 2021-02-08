package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ic2.api.recipe.IRecipeInput;
import mods.gregtechmod.recipe.util.IBasicMachineRecipe;
import net.minecraft.item.ItemStack;

public class BasicMachineRecipe implements IBasicMachineRecipe {
    private final IRecipeInput input;
    private final ItemStack output;

    private BasicMachineRecipe(IRecipeInput input, ItemStack output) {
        this.input = input;
        this.output = output;
    }

    @JsonCreator
    public static BasicMachineRecipe create(@JsonProperty(value = "input", required = true) IRecipeInput input,
                                            @JsonProperty(value = "output", required = true) ItemStack output) {
        return new BasicMachineRecipe(input, output);
    }

    @Override
    public IRecipeInput getInput() {
        return this.input;
    }

    @Override
    public ItemStack getOutput() {
        return this.output;
    }
}
