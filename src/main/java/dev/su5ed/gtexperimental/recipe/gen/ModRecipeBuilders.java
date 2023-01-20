package dev.su5ed.gtexperimental.recipe.gen;

import dev.su5ed.gtexperimental.recipe.AlloySmelterRecipe;
import dev.su5ed.gtexperimental.recipe.AssemblerRecipe;
import dev.su5ed.gtexperimental.recipe.BenderRecipe;
import dev.su5ed.gtexperimental.recipe.CanningMachineRecipe;
import dev.su5ed.gtexperimental.recipe.type.MIMORecipe;
import dev.su5ed.gtexperimental.recipe.type.MISORecipe;
import dev.su5ed.gtexperimental.recipe.type.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.type.SISORecipe;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class ModRecipeBuilders {
    public static AlloySmelterRecipeBuilder alloySmelter(RecipeIngredient<ItemStack> input, ItemStack output, int duration, double energyCost) {
        return alloySmelter(List.of(input), output, duration, energyCost);
    }

    public static AlloySmelterRecipeBuilder alloySmelter(RecipeIngredient<ItemStack> primary, RecipeIngredient<ItemStack> secondary, ItemStack output, int duration, double energyCost) {
        return alloySmelter(List.of(primary, secondary), output, duration, energyCost);
    }

    public static AlloySmelterRecipeBuilder alloySmelter(List<? extends RecipeIngredient<ItemStack>> inputs, ItemStack output, int duration, double energyCost) {
        MISORecipe recipe = new AlloySmelterRecipe(null, inputs, output, duration, energyCost);
        return new AlloySmelterRecipeBuilder(recipe);
    }

    public static MISORecipeBuilder assembler(RecipeIngredient<ItemStack> primary, ItemStack output, int duration, double energyCost) {
        return assembler(List.of(primary), output, duration, energyCost);
    }

    public static MISORecipeBuilder assembler(RecipeIngredient<ItemStack> primary, RecipeIngredient<ItemStack> secondary, ItemStack output, int duration, double energyCost) {
        return assembler(List.of(primary, secondary), output, duration, energyCost);
    }

    public static MISORecipeBuilder assembler(List<? extends RecipeIngredient<ItemStack>> inputs, ItemStack output, int duration, double energyCost) {
        MISORecipe recipe = new AssemblerRecipe(null, inputs, output, duration, energyCost);
        return new MISORecipeBuilder(recipe);
    }

    public static SISORecipeBuilder bender(RecipeIngredient<ItemStack> input, ItemStack output, int duration, double energyCost) {
        SISORecipe recipe = new BenderRecipe(null, input, output, duration, energyCost);
        return new SISORecipeBuilder(recipe);
    }

    public static MIMORecipeBuilder canningMachine(RecipeIngredient<ItemStack> primary, RecipeIngredient<ItemStack> secondary, ItemStack output, int duration, double energyCost) {
        return canningMachine(List.of(primary, secondary), List.of(output), duration, energyCost);
    }

    public static MIMORecipeBuilder canningMachine(RecipeIngredient<ItemStack> primary, RecipeIngredient<ItemStack> secondary, ItemStack primaryOutput, ItemStack secondaryOutput, int duration, double energyCost) {
        return canningMachine(List.of(primary, secondary), List.of(primaryOutput, secondaryOutput), duration, energyCost);
    }

    public static MIMORecipeBuilder canningMachine(List<? extends RecipeIngredient<ItemStack>> inputs, List<ItemStack> outputs, int duration, double energyCost) {
        MIMORecipe recipe = new CanningMachineRecipe(null, inputs, outputs, duration, energyCost);
        return new MIMORecipeBuilder(recipe);
    }

    private ModRecipeBuilders() {}
}
