package dev.su5ed.gtexperimental.recipe.gen;

import com.mojang.datafixers.util.Either;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.IFMORecipe;
import dev.su5ed.gtexperimental.recipe.MIMORecipe;
import dev.su5ed.gtexperimental.recipe.MISORecipe;
import dev.su5ed.gtexperimental.recipe.type.RecipePropertyMap;
import dev.su5ed.gtexperimental.recipe.SIMORecipe;
import dev.su5ed.gtexperimental.recipe.SISORecipe;
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
        MISORecipe<ItemStack, ItemStack> recipe = MISORecipe.alloySmelter(null, inputs, output, RecipePropertyMap.builder().duration(duration).energyCost(energyCost).build());
        return new AlloySmelterRecipeBuilder(recipe);
    }

    public static MISORecipeBuilder<ItemStack, ItemStack> assembler(RecipeIngredient<ItemStack> primary, ItemStack output, int duration, double energyCost) {
        return assembler(List.of(primary), output, duration, energyCost);
    }

    public static MISORecipeBuilder<ItemStack, ItemStack> assembler(RecipeIngredient<ItemStack> primary, RecipeIngredient<ItemStack> secondary, ItemStack output, int duration, double energyCost) {
        return assembler(List.of(primary, secondary), output, duration, energyCost);
    }

    public static MISORecipeBuilder<ItemStack, ItemStack> assembler(List<? extends RecipeIngredient<ItemStack>> inputs, ItemStack output, int duration, double energyCost) {
        MISORecipe<ItemStack, ItemStack> recipe = MISORecipe.assembler(null, inputs, output, RecipePropertyMap.builder().duration(duration).energyCost(energyCost).build());
        return new MISORecipeBuilder<>(recipe);
    }

    public static SISORecipeBuilder<ItemStack, ItemStack> bender(RecipeIngredient<ItemStack> input, ItemStack output, int duration, double energyCost) {
        SISORecipe<ItemStack, ItemStack> recipe = SISORecipe.bender(null, input, output, RecipePropertyMap.builder().duration(duration).energyCost(energyCost).build());
        return new SISORecipeBuilder<>(recipe);
    }

    public static MIMORecipeBuilder canningMachine(RecipeIngredient<ItemStack> primary, RecipeIngredient<ItemStack> secondary, ItemStack output, int duration, double energyCost) {
        return canningMachine(List.of(primary, secondary), List.of(output), duration, energyCost);
    }

    public static MIMORecipeBuilder canningMachine(RecipeIngredient<ItemStack> primary, RecipeIngredient<ItemStack> secondary, ItemStack primaryOutput, ItemStack secondaryOutput, int duration, double energyCost) {
        return canningMachine(List.of(primary, secondary), List.of(primaryOutput, secondaryOutput), duration, energyCost);
    }

    public static MIMORecipeBuilder canningMachine(List<? extends RecipeIngredient<ItemStack>> inputs, List<ItemStack> outputs, int duration, double energyCost) {
        MIMORecipe recipe = MIMORecipe.canningMachine(null, inputs, outputs, RecipePropertyMap.builder().duration(duration).energyCost(energyCost).build());
        return new MIMORecipeBuilder(recipe);
    }

    public static MISORecipeBuilder<FluidStack, FluidStack> chemical(RecipeIngredient<FluidStack> primary, FluidStack output, int duration) {
        return chemical(List.of(primary), output, duration);
    }

    public static MISORecipeBuilder<FluidStack, FluidStack> chemical(RecipeIngredient<FluidStack> primary, RecipeIngredient<FluidStack> secondary, FluidStack output, int duration) {
        return chemical(List.of(primary, secondary), output, duration);
    }

    public static MISORecipeBuilder<FluidStack, FluidStack> chemical(List<? extends RecipeIngredient<FluidStack>> inputs, FluidStack output, int duration) {
        MISORecipe<FluidStack, FluidStack> recipe = MISORecipe.chemical(null, inputs, output, RecipePropertyMap.builder().duration(duration).build());
        return new MISORecipeBuilder<>(recipe);
    }

    public static SIMORecipeBuilder<FluidStack, List<FluidStack>> distillation(RecipeIngredient<FluidStack> input, FluidStack first, FluidStack second, FluidStack third, FluidStack fourth, int duration) {
        return distillation(input, List.of(first, second, third, fourth), duration);
    }

    public static SIMORecipeBuilder<FluidStack, List<FluidStack>> distillation(RecipeIngredient<FluidStack> input, List<FluidStack> outputs, int duration) {
        SIMORecipe<FluidStack, List<FluidStack>> recipe = SIMORecipe.distillation(null, input, outputs, RecipePropertyMap.builder().duration(duration).build());
        return new SIMORecipeBuilder<>(recipe);
    }

    public static MISORecipeBuilder<FluidStack, ItemStack> fusionSolid(RecipeIngredient<FluidStack> primary, RecipeIngredient<FluidStack> secondary, ItemStack output, int duration, double energyCost, double startEnergy) {
        return fusionSolid(List.of(primary, secondary), output, duration, energyCost, startEnergy);
    }

    public static MISORecipeBuilder<FluidStack, ItemStack> fusionSolid(List<? extends RecipeIngredient<FluidStack>> inputs, ItemStack output, int duration, double energyCost, double startEnergy) {
        MISORecipe<FluidStack, ItemStack> recipe = MISORecipe.fusionSolid(null, inputs, output, RecipePropertyMap.builder().duration(duration).energyCost(energyCost).startEnergy(startEnergy).build());
        return new MISORecipeBuilder<>(recipe);
    }

    public static MISORecipeBuilder<FluidStack, FluidStack> fusionFluid(RecipeIngredient<FluidStack> primary, RecipeIngredient<FluidStack> secondary, FluidStack output, int duration, double energyCost, double startEnergy) {
        return fusionFluid(List.of(primary, secondary), output, duration, energyCost, startEnergy);
    }

    public static MISORecipeBuilder<FluidStack, FluidStack> fusionFluid(List<? extends RecipeIngredient<FluidStack>> inputs, FluidStack output, int duration, double energyCost, double startEnergy) {
        MISORecipe<FluidStack, FluidStack> recipe = MISORecipe.fusionFluid(null, inputs, output, RecipePropertyMap.builder().duration(duration).energyCost(energyCost).startEnergy(startEnergy).build());
        return new MISORecipeBuilder<>(recipe);
    }

    public static SIMORecipeBuilder<ItemStack, List<ItemStack>> implosion(RecipeIngredient<ItemStack> input, ItemStack first, ItemStack second, int tnt) {
        return implosion(input, List.of(first, second), tnt);
    }

    public static SIMORecipeBuilder<ItemStack, List<ItemStack>> implosion(RecipeIngredient<ItemStack> input, List<ItemStack> outputs, int tnt) {
        SIMORecipe<ItemStack, List<ItemStack>> recipe = SIMORecipe.implosion(null, input, outputs, RecipePropertyMap.builder().tnt(tnt).build());
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
        MIMORecipe recipe = MIMORecipe.blastFurnace(null, inputs, outputs, RecipePropertyMap.builder().duration(duration).heat(heat).build());
        return new BlastFurnaceRecipeBuilder(recipe);
    }

    public static SIMORecipeBuilder<Either<ItemStack, FluidStack>, List<Either<ItemStack, FluidStack>>> industrialCentrifuge(RecipeIngredient<Either<ItemStack, FluidStack>> input, Object first, int duration) {
        return industrialCentrifuge(input, List.of(convert(first)), duration);
    }

    public static SIMORecipeBuilder<Either<ItemStack, FluidStack>, List<Either<ItemStack, FluidStack>>> industrialCentrifuge(RecipeIngredient<Either<ItemStack, FluidStack>> input, Object first, Object second, int duration) {
        return industrialCentrifuge(input, List.of(convert(first), convert(second)), duration);
    }

    public static SIMORecipeBuilder<Either<ItemStack, FluidStack>, List<Either<ItemStack, FluidStack>>> industrialCentrifuge(RecipeIngredient<Either<ItemStack, FluidStack>> input, Object first, Object second, Object third, int duration) {
        return industrialCentrifuge(input, List.of(convert(first), convert(second), convert(third)), duration);
    }

    public static SIMORecipeBuilder<Either<ItemStack, FluidStack>, List<Either<ItemStack, FluidStack>>> industrialCentrifuge(RecipeIngredient<Either<ItemStack, FluidStack>> input, Object first, Object second, Object third, Object fourth, int duration) {
        return industrialCentrifuge(input, List.of(convert(first), convert(second), convert(third), convert(fourth)), duration);
    }

    public static SIMORecipeBuilder<Either<ItemStack, FluidStack>, List<Either<ItemStack, FluidStack>>> industrialCentrifuge(RecipeIngredient<Either<ItemStack, FluidStack>> input, List<Either<ItemStack, FluidStack>> outputs, int duration) {
        SIMORecipe<Either<ItemStack, FluidStack>, List<Either<ItemStack, FluidStack>>> recipe = SIMORecipe.industrialCentrifuge(null, input, outputs, RecipePropertyMap.builder().duration(duration).build());
        return new SIMORecipeBuilder<>(recipe);
    }

    public static SIMORecipeBuilder<Either<ItemStack, FluidStack>, List<Either<ItemStack, FluidStack>>> industrialElectrolyzer(RecipeIngredient<Either<ItemStack, FluidStack>> input, Object first, int duration, double energyCost) {
        return industrialElectrolyzer(input, List.of(convert(first)), duration, energyCost);
    }

    public static SIMORecipeBuilder<Either<ItemStack, FluidStack>, List<Either<ItemStack, FluidStack>>> industrialElectrolyzer(RecipeIngredient<Either<ItemStack, FluidStack>> input, Object first, Object second, int duration, double energyCost) {
        return industrialElectrolyzer(input, List.of(convert(first), convert(second)), duration, energyCost);
    }

    public static SIMORecipeBuilder<Either<ItemStack, FluidStack>, List<Either<ItemStack, FluidStack>>> industrialElectrolyzer(RecipeIngredient<Either<ItemStack, FluidStack>> input, Object first, Object second, Object third, int duration, double energyCost) {
        return industrialElectrolyzer(input, List.of(convert(first), convert(second), convert(third)), duration, energyCost);
    }

    public static SIMORecipeBuilder<Either<ItemStack, FluidStack>, List<Either<ItemStack, FluidStack>>> industrialElectrolyzer(RecipeIngredient<Either<ItemStack, FluidStack>> input, Object first, Object second, Object third, Object fourth, int duration, double energyCost) {
        return industrialElectrolyzer(input, List.of(convert(first), convert(second), convert(third), convert(fourth)), duration, energyCost);
    }

    public static SIMORecipeBuilder<Either<ItemStack, FluidStack>, List<Either<ItemStack, FluidStack>>> industrialElectrolyzer(RecipeIngredient<Either<ItemStack, FluidStack>> input, List<Either<ItemStack, FluidStack>> outputs, int duration, double energyCost) {
        SIMORecipe<Either<ItemStack, FluidStack>, List<Either<ItemStack, FluidStack>>> recipe = SIMORecipe.industrialElectrolyzer(null, input, outputs, RecipePropertyMap.builder().duration(duration).energyCost(energyCost).build());
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
        IFMORecipe recipe = IFMORecipe.industrialGrinder(null, input, fluid, output, RecipePropertyMap.empty());
        return new IFMORecipeBuilder(recipe);
    }

    public static SIMORecipeBuilder<ItemStack, List<ItemStack>> lathe(RecipeIngredient<ItemStack> input, ItemStack output, int duration, double energyCost) {
        return lathe(input, List.of(output), duration, energyCost);
    }

    public static SIMORecipeBuilder<ItemStack, List<ItemStack>> lathe(RecipeIngredient<ItemStack> input, List<ItemStack> outputs, int duration, double energyCost) {
        SIMORecipe<ItemStack, List<ItemStack>> recipe = SIMORecipe.lathe(null, input, outputs, RecipePropertyMap.builder().duration(duration).energyCost(energyCost).build());
        return new SIMORecipeBuilder<>(recipe);
    }

    public static MISORecipeBuilder<ItemStack, ItemStack> printer(RecipeIngredient<ItemStack> primary, ItemStack output, int duration, double energyCost) {
        return printer(List.of(primary), output, duration, energyCost);
    }

    public static MISORecipeBuilder<ItemStack, ItemStack> printer(RecipeIngredient<ItemStack> primary, RecipeIngredient<ItemStack> secondary, ItemStack output, int duration, double energyCost) {
        return printer(List.of(primary, secondary), output, duration, energyCost);
    }

    public static MISORecipeBuilder<ItemStack, ItemStack> printer(List<? extends RecipeIngredient<ItemStack>> inputs, ItemStack output, int duration, double energyCost) {
        MISORecipe<ItemStack, ItemStack> recipe = MISORecipe.printer(null, inputs, output, RecipePropertyMap.builder().duration(duration).energyCost(energyCost).build());
        return new MISORecipeBuilder<>(recipe);
    }

    public static SIMORecipeBuilder<ItemStack, List<ItemStack>> pulverizer(RecipeIngredient<ItemStack> input, ItemStack output) {
        return pulverizer(input, List.of(output), 3, 10);
    }

    public static SIMORecipeBuilder<ItemStack, List<ItemStack>> pulverizer(RecipeIngredient<ItemStack> input, ItemStack first, ItemStack second) {
        return pulverizer(input, List.of(first, second), 3, 10);
    }

    public static SIMORecipeBuilder<ItemStack, List<ItemStack>> pulverizer(RecipeIngredient<ItemStack> input, ItemStack first, ItemStack second, int chance) {
        return pulverizer(input, List.of(first, second), 3, chance);
    }

    public static SIMORecipeBuilder<ItemStack, List<ItemStack>> pulverizer(RecipeIngredient<ItemStack> input, ItemStack first, ItemStack second, double energyCost, int chance) {
        return pulverizer(input, List.of(first, second), energyCost, chance);
    }

    public static SIMORecipeBuilder<ItemStack, List<ItemStack>> pulverizer(RecipeIngredient<ItemStack> input, List<ItemStack> outputs, double energyCost, int chance) {
        SIMORecipe<ItemStack, List<ItemStack>> recipe = SIMORecipe.pulverizer(null, input, outputs, RecipePropertyMap.builder().energyCost(energyCost).chance(chance).build());
        return new PulverizerRecipeBuilder(recipe);
    }

    public static IFMORecipeBuilder industrialSawmill(RecipeIngredient<ItemStack> input, RecipeIngredient<FluidStack> fluid, ItemStack first, ItemStack second) {
        return industrialGrinder(input, fluid, List.of(first, second));
    }

    public static IFMORecipeBuilder industrialSawmill(RecipeIngredient<ItemStack> input, RecipeIngredient<FluidStack> fluid, List<ItemStack> output) {
        IFMORecipe recipe = IFMORecipe.industrialSawmill(null, input, fluid, output, RecipePropertyMap.empty());
        return new IndustrialSawmillRecipeBuilder(recipe);
    }

    public static SISORecipeBuilder<ItemStack, ItemStack> vacuumFreezerSolid(RecipeIngredient<ItemStack> input, ItemStack output, int duration) {
        SISORecipe<ItemStack, ItemStack> recipe = SISORecipe.vacuumFreezerSolid(null, input, output, RecipePropertyMap.builder().duration(duration).build());
        return new SISORecipeBuilder<>(recipe);
    }

    public static SISORecipeBuilder<FluidStack, FluidStack> vacuumFreezerFluid(RecipeIngredient<FluidStack> input, FluidStack output, int duration) {
        SISORecipe<FluidStack, FluidStack> recipe = SISORecipe.vacuumFreezerFluid(null, input, output, RecipePropertyMap.builder().duration(duration).build());
        return new SISORecipeBuilder<>(recipe);
    }

    public static SISORecipeBuilder<ItemStack, ItemStack> wiremill(RecipeIngredient<ItemStack> input, ItemStack output, int duration, double energyCost) {
        SISORecipe<ItemStack, ItemStack> recipe = SISORecipe.wiremill(null, input, output, RecipePropertyMap.builder().duration(duration).energyCost(energyCost).build());
        return new SISORecipeBuilder<>(recipe);
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
