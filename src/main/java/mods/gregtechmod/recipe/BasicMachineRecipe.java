package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ic2.api.recipe.IRecipeInput;
import mods.gregtechmod.recipe.util.IBasicMachineRecipe;
import net.minecraft.item.ItemStack;

import java.util.List;

public class BasicMachineRecipe implements IBasicMachineRecipe {
    private final IRecipeInput input;
    private final List<ItemStack> output;
    private final boolean overwrite;

    private BasicMachineRecipe(IRecipeInput input, List<ItemStack> output, boolean overwrite) {
        this.input = input;
        this.output = output;
        this.overwrite = overwrite;
    }

    @JsonCreator
    public static BasicMachineRecipe create(@JsonProperty(value = "input", required = true) IRecipeInput input,
                                            @JsonProperty(value = "output", required = true) List<ItemStack> output,
                                            @JsonProperty(value = "overwrite") boolean overwrite) {
        return new BasicMachineRecipe(input, output, overwrite);
    }

    @Override
    public IRecipeInput getInput() {
        return this.input;
    }

    @Override
    public List<ItemStack> getOutput() {
        return this.output;
    }

    @Override
    public boolean shouldOverwrite() {
        return this.overwrite;
    }
}
