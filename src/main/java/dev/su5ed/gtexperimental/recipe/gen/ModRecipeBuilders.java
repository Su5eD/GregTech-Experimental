package dev.su5ed.gtexperimental.recipe.gen;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.AlloySmelterRecipe;
import dev.su5ed.gtexperimental.recipe.AssemblerRecipe;
import dev.su5ed.gtexperimental.recipe.BenderRecipe;
import dev.su5ed.gtexperimental.recipe.CanningMachineRecipe;
import dev.su5ed.gtexperimental.recipe.ChemicalRecipe;
import dev.su5ed.gtexperimental.recipe.DistillationRecipe;
import dev.su5ed.gtexperimental.recipe.FusionFluidRecipe;
import dev.su5ed.gtexperimental.recipe.FusionSolidRecipe;
import dev.su5ed.gtexperimental.recipe.ImplosionRecipe;
import dev.su5ed.gtexperimental.recipe.type.MIMORecipe;
import dev.su5ed.gtexperimental.recipe.type.MISORecipe;
import dev.su5ed.gtexperimental.recipe.type.SIMORecipe;
import dev.su5ed.gtexperimental.recipe.type.SISORecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public final class ModRecipeBuilders {
    public static AlloySmelterRecipeBuilder alloySmelter(RecipeIngredient<ItemStack> input, ItemStack output, int duration, double energyCost) {
        return alloySmelter(List.of(input), output, duration, energyCost);
    }

    public static AlloySmelterRecipeBuilder alloySmelter(RecipeIngredient<ItemStack> primary, RecipeIngredient<ItemStack> secondary, ItemStack output, int duration, double energyCost) {
        return alloySmelter(List.of(primary, secondary), output, duration, energyCost);
    }

    public static AlloySmelterRecipeBuilder alloySmelter(List<? extends RecipeIngredient<ItemStack>> inputs, ItemStack output, int duration, double energyCost) {
        AlloySmelterRecipe recipe = new AlloySmelterRecipe(null, inputs, output, duration, energyCost);
        return new AlloySmelterRecipeBuilder(recipe);
    }

    public static MISORecipeBuilder<ItemStack, ItemStack> assembler(RecipeIngredient<ItemStack> primary, ItemStack output, int duration, double energyCost) {
        return assembler(List.of(primary), output, duration, energyCost);
    }

    public static MISORecipeBuilder<ItemStack, ItemStack> assembler(RecipeIngredient<ItemStack> primary, RecipeIngredient<ItemStack> secondary, ItemStack output, int duration, double energyCost) {
        return assembler(List.of(primary, secondary), output, duration, energyCost);
    }

    public static MISORecipeBuilder<ItemStack, ItemStack> assembler(List<? extends RecipeIngredient<ItemStack>> inputs, ItemStack output, int duration, double energyCost) {
        AssemblerRecipe recipe = new AssemblerRecipe(null, inputs, output, duration, energyCost);
        return new MISORecipeBuilder<>(recipe);
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

    public static MISORecipeBuilder<FluidStack, FluidStack> chemical(RecipeIngredient<FluidStack> primary, FluidStack output, int duration) {
        return chemical(List.of(primary), output, duration);
    }

    public static MISORecipeBuilder<FluidStack, FluidStack> chemical(RecipeIngredient<FluidStack> primary, RecipeIngredient<FluidStack> secondary, FluidStack output, int duration) {
        return chemical(List.of(primary, secondary), output, duration);
    }

    public static MISORecipeBuilder<FluidStack, FluidStack> chemical(List<? extends RecipeIngredient<FluidStack>> inputs, FluidStack output, int duration) {
        MISORecipe<FluidStack, FluidStack> recipe = new ChemicalRecipe(null, inputs, output, duration);
        return new MISORecipeBuilder<>(recipe);
    }

    public static SIMORecipeBuilder<FluidStack> distillation(RecipeIngredient<FluidStack> input, FluidStack first, FluidStack second, FluidStack third, FluidStack fourth, int duration) {
        return distillation(input, List.of(first, second, third, fourth), duration);
    }

    public static SIMORecipeBuilder<FluidStack> distillation(RecipeIngredient<FluidStack> input, List<FluidStack> outputs, int duration) {
        SIMORecipe<FluidStack> recipe = new DistillationRecipe(null, input, outputs, duration);
        return new SIMORecipeBuilder<>(recipe);
    }

    public static MISORecipeBuilder<FluidStack, ItemStack> fusionSolid(RecipeIngredient<FluidStack> primary, RecipeIngredient<FluidStack> secondary, ItemStack output, int duration, double energyCost, double startEnergy) {
        return fusionSolid(List.of(primary, secondary), output, duration, energyCost, startEnergy);
    }

    public static MISORecipeBuilder<FluidStack, ItemStack> fusionSolid(List<? extends RecipeIngredient<FluidStack>> inputs, ItemStack output, int duration, double energyCost, double startEnergy) {
        MISORecipe<FluidStack, ItemStack> recipe = new FusionSolidRecipe(null, inputs, output, duration, energyCost, startEnergy);
        return new MISORecipeBuilder<>(recipe);
    }

    public static MISORecipeBuilder<FluidStack, FluidStack> fusionFluid(RecipeIngredient<FluidStack> primary, RecipeIngredient<FluidStack> secondary, FluidStack output, int duration, double energyCost, double startEnergy) {
        return fusionFluid(List.of(primary, secondary), output, duration, energyCost, startEnergy);
    }

    public static MISORecipeBuilder<FluidStack, FluidStack> fusionFluid(List<? extends RecipeIngredient<FluidStack>> inputs, FluidStack output, int duration, double energyCost, double startEnergy) {
        MISORecipe<FluidStack, FluidStack> recipe = new FusionFluidRecipe(null, inputs, output, duration, energyCost, startEnergy);
        return new MISORecipeBuilder<>(recipe);
    }

    public static SIMORecipeBuilder<ItemStack> implosion(RecipeIngredient<ItemStack> input, ItemStack first, ItemStack second, int tnt) {
        return implosion(input, List.of(first, second), tnt);
    }

    public static SIMORecipeBuilder<ItemStack> implosion(RecipeIngredient<ItemStack> input, List<ItemStack> outputs, int tnt) {
        SIMORecipe<ItemStack> recipe = new ImplosionRecipe(null, input, outputs, tnt);
        return new SIMORecipeBuilder<>(recipe);
    }

    private ModRecipeBuilders() {}
}
