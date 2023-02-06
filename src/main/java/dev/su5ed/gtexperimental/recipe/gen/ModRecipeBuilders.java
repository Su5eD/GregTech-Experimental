package dev.su5ed.gtexperimental.recipe.gen;

import com.mojang.datafixers.util.Either;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.AlloySmelterRecipe;
import dev.su5ed.gtexperimental.recipe.AssemblerRecipe;
import dev.su5ed.gtexperimental.recipe.BenderRecipe;
import dev.su5ed.gtexperimental.recipe.BlastFurnaceRecipe;
import dev.su5ed.gtexperimental.recipe.CanningMachineRecipe;
import dev.su5ed.gtexperimental.recipe.ChemicalRecipe;
import dev.su5ed.gtexperimental.recipe.DistillationRecipe;
import dev.su5ed.gtexperimental.recipe.FusionFluidRecipe;
import dev.su5ed.gtexperimental.recipe.FusionSolidRecipe;
import dev.su5ed.gtexperimental.recipe.ImplosionRecipe;
import dev.su5ed.gtexperimental.recipe.IndustrialCentrifugeRecipe;
import dev.su5ed.gtexperimental.recipe.IndustrialElectrolyzerRecipe;
import dev.su5ed.gtexperimental.recipe.IndustrialGrinderRecipe;
import dev.su5ed.gtexperimental.recipe.LatheRecipe;
import dev.su5ed.gtexperimental.recipe.PrinterRecipe;
import dev.su5ed.gtexperimental.recipe.PulverizerRecipe;
import dev.su5ed.gtexperimental.recipe.type.IFMORecipe;
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

    public static MIMORecipeBuilder blastFurnace(RecipeIngredient<ItemStack> input, ItemStack output, int duration, int heat) {
        return blastFurnace(List.of(input), List.of(output), duration, heat);
    }

    public static MIMORecipeBuilder blastFurnace(RecipeIngredient<ItemStack> input, ItemStack primaryOutput, ItemStack secondaryOutput, int duration, int heat) {
        return blastFurnace(List.of(input), List.of(primaryOutput, secondaryOutput), duration, heat);
    }

    public static MIMORecipeBuilder blastFurnace(RecipeIngredient<ItemStack> primary, RecipeIngredient<ItemStack> secondary, ItemStack output, int duration, int heat) {
        return blastFurnace(List.of(primary, secondary), List.of(output), duration, heat);
    }

    public static MIMORecipeBuilder blastFurnace(RecipeIngredient<ItemStack> primary, RecipeIngredient<ItemStack> secondary, ItemStack primaryOutput, ItemStack secondaryOutput, int duration, int heat) {
        return blastFurnace(List.of(primary, secondary), List.of(primaryOutput, secondaryOutput), duration, heat);
    }

    public static MIMORecipeBuilder blastFurnace(List<? extends RecipeIngredient<ItemStack>> inputs, List<ItemStack> outputs, int duration, int heat) {
        MIMORecipe recipe = new BlastFurnaceRecipe(null, inputs, outputs, duration, heat);
        return new MIMORecipeBuilder(recipe);
    }

    public static SIMORecipeBuilder<Either<ItemStack, FluidStack>> industrialCentrifuge(RecipeIngredient<Either<ItemStack, FluidStack>> input, Object first, int duration) {
        return industrialCentrifuge(input, List.of(convert(first)), duration);
    }

    public static SIMORecipeBuilder<Either<ItemStack, FluidStack>> industrialCentrifuge(RecipeIngredient<Either<ItemStack, FluidStack>> input, Object first, Object second, int duration) {
        return industrialCentrifuge(input, List.of(convert(first), convert(second)), duration);
    }

    public static SIMORecipeBuilder<Either<ItemStack, FluidStack>> industrialCentrifuge(RecipeIngredient<Either<ItemStack, FluidStack>> input, Object first, Object second, Object third, int duration) {
        return industrialCentrifuge(input, List.of(convert(first), convert(second), convert(third)), duration);
    }

    public static SIMORecipeBuilder<Either<ItemStack, FluidStack>> industrialCentrifuge(RecipeIngredient<Either<ItemStack, FluidStack>> input, Object first, Object second, Object third, Object fourth, int duration) {
        return industrialCentrifuge(input, List.of(convert(first), convert(second), convert(third), convert(fourth)), duration);
    }

    public static SIMORecipeBuilder<Either<ItemStack, FluidStack>> industrialCentrifuge(RecipeIngredient<Either<ItemStack, FluidStack>> input, List<Either<ItemStack, FluidStack>> outputs, int duration) {
        SIMORecipe<Either<ItemStack, FluidStack>> recipe = new IndustrialCentrifugeRecipe(null, input, outputs, duration);
        return new SIMORecipeBuilder<>(recipe);
    }

    public static SIMORecipeBuilder<Either<ItemStack, FluidStack>> industrialElectrolyzer(RecipeIngredient<Either<ItemStack, FluidStack>> input, Object first, int duration, double energyCost) {
        return industrialElectrolyzer(input, List.of(convert(first)), duration, energyCost);
    }

    public static SIMORecipeBuilder<Either<ItemStack, FluidStack>> industrialElectrolyzer(RecipeIngredient<Either<ItemStack, FluidStack>> input, Object first, Object second, int duration, double energyCost) {
        return industrialElectrolyzer(input, List.of(convert(first), convert(second)), duration, energyCost);
    }

    public static SIMORecipeBuilder<Either<ItemStack, FluidStack>> industrialElectrolyzer(RecipeIngredient<Either<ItemStack, FluidStack>> input, Object first, Object second, Object third, int duration, double energyCost) {
        return industrialElectrolyzer(input, List.of(convert(first), convert(second), convert(third)), duration, energyCost);
    }

    public static SIMORecipeBuilder<Either<ItemStack, FluidStack>> industrialElectrolyzer(RecipeIngredient<Either<ItemStack, FluidStack>> input, Object first, Object second, Object third, Object fourth, int duration, double energyCost) {
        return industrialElectrolyzer(input, List.of(convert(first), convert(second), convert(third), convert(fourth)), duration, energyCost);
    }

    public static SIMORecipeBuilder<Either<ItemStack, FluidStack>> industrialElectrolyzer(RecipeIngredient<Either<ItemStack, FluidStack>> input, List<Either<ItemStack, FluidStack>> outputs, int duration, double energyCost) {
        SIMORecipe<Either<ItemStack, FluidStack>> recipe = new IndustrialElectrolyzerRecipe(null, input, outputs, duration, energyCost);
        return new SIMORecipeBuilder<>(recipe);
    }

    public static IFMORecipeBuilder industrialGrinder(RecipeIngredient<ItemStack> input, RecipeIngredient<FluidStack> fluid, ItemStack output) {
        return industrialGrinder(input, fluid, List.of(output));
    }

    public static IFMORecipeBuilder industrialGrinder(RecipeIngredient<ItemStack> input, RecipeIngredient<FluidStack> fluid, ItemStack first, ItemStack second) {
        return industrialGrinder(input, fluid, List.of(first, second));
    }

    public static IFMORecipeBuilder industrialGrinder(RecipeIngredient<ItemStack> input, RecipeIngredient<FluidStack> fluid, ItemStack first, ItemStack second, ItemStack third) {
        return industrialGrinder(input, fluid, List.of(first, second, third));
    }

    public static IFMORecipeBuilder industrialGrinder(RecipeIngredient<ItemStack> input, RecipeIngredient<FluidStack> fluid, List<ItemStack> output) {
        IFMORecipe recipe = new IndustrialGrinderRecipe(null, input, fluid, output);
        return new IFMORecipeBuilder(recipe);
    }

    public static SIMORecipeBuilder<ItemStack> lathe(RecipeIngredient<ItemStack> input, ItemStack output, int duration, double energyCost) {
        return lathe(input, List.of(output), duration, energyCost);
    }

    public static SIMORecipeBuilder<ItemStack> lathe(RecipeIngredient<ItemStack> input, List<ItemStack> outputs, int duration, double energyCost) {
        SIMORecipe<ItemStack> recipe = new LatheRecipe(null, input, outputs, duration, energyCost);
        return new SIMORecipeBuilder<>(recipe);
    }

    public static MISORecipeBuilder<ItemStack, ItemStack> printer(RecipeIngredient<ItemStack> primary, ItemStack output, int duration, double energyCost) {
        return printer(List.of(primary), output, duration, energyCost);
    }

    public static MISORecipeBuilder<ItemStack, ItemStack> printer(RecipeIngredient<ItemStack> primary, RecipeIngredient<ItemStack> secondary, ItemStack output, int duration, double energyCost) {
        return printer(List.of(primary, secondary), output, duration, energyCost);
    }

    public static MISORecipeBuilder<ItemStack, ItemStack> printer(List<? extends RecipeIngredient<ItemStack>> inputs, ItemStack output, int duration, double energyCost) {
        MISORecipe<ItemStack, ItemStack> recipe = new PrinterRecipe(null, inputs, output, duration, energyCost);
        return new MISORecipeBuilder<>(recipe);
    }

    public static SIMORecipeBuilder<ItemStack> pulverizer(RecipeIngredient<ItemStack> input, ItemStack output) {
        return pulverizer(input, List.of(output), 3, 10);
    }

    public static SIMORecipeBuilder<ItemStack> pulverizer(RecipeIngredient<ItemStack> input, ItemStack first, ItemStack second) {
        return pulverizer(input, List.of(first, second), 3, 10);
    }

    public static SIMORecipeBuilder<ItemStack> pulverizer(RecipeIngredient<ItemStack> input, ItemStack first, ItemStack second, int chance) {
        return pulverizer(input, List.of(first, second), 3, 10);
    }

    public static SIMORecipeBuilder<ItemStack> pulverizer(RecipeIngredient<ItemStack> input, ItemStack first, ItemStack second, double energyCost, int chance) {
        return pulverizer(input, List.of(first, second), energyCost, chance);
    }

    public static SIMORecipeBuilder<ItemStack> pulverizer(RecipeIngredient<ItemStack> input, List<ItemStack> outputs, double energyCost, int chance) {
        SIMORecipe<ItemStack> recipe = new PulverizerRecipe(null, input, outputs, energyCost, chance);
        return new SIMORecipeBuilder<>(recipe);
    }

    private static Either<ItemStack, FluidStack> convert(Object obj) {
        if (obj instanceof ItemStack item) {
            return Either.left(item);
        }
        if (obj instanceof FluidStack fluid) {
            return Either.right(fluid);
        }
        throw new IllegalArgumentException("Object must either be an item or fluid");
    }

    private ModRecipeBuilders() {}
}
