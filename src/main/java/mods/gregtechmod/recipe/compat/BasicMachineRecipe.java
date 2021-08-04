package mods.gregtechmod.recipe.compat;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ic2.api.recipe.IRecipeInput;
import ic2.core.recipe.BasicMachineRecipeManager;
import mods.gregtechmod.recipe.util.IBasicMachineRecipe;
import net.minecraft.item.ItemStack;

import java.util.Collection;

/**
 * Used as a bridge between the GTE recipe config system and IC2 recipes
 * @see mods.gregtechmod.init.MachineRecipeLoader#registerRecipes(String, Collection, BasicMachineRecipeManager) MachineRecipeLoader#registerRecipes
 */
public class BasicMachineRecipe implements IBasicMachineRecipe {
    private final IRecipeInput input;
    private final ItemStack output;
    private final boolean overwrite;

    private BasicMachineRecipe(IRecipeInput input, ItemStack output, boolean overwrite) {
        this.input = input;
        this.output = output;
        this.overwrite = overwrite;
    }

    @JsonCreator
    public static BasicMachineRecipe create(@JsonProperty(value = "input", required = true) IRecipeInput input,
                                            @JsonProperty(value = "output", required = true) ItemStack output,
                                            @JsonProperty(value = "overwrite") boolean overwrite) {
        return new BasicMachineRecipe(input, output, overwrite);
    }

    @Override
    public IRecipeInput getInput() {
        return this.input;
    }

    @Override
    public ItemStack getOutput() {
        return this.output;
    }

    @Override
    public boolean shouldOverwrite() {
        return this.overwrite;
    }
}
