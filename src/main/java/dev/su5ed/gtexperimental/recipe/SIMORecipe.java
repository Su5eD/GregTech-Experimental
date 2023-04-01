package dev.su5ed.gtexperimental.recipe;

import com.mojang.datafixers.util.Either;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeTypes;
import dev.su5ed.gtexperimental.recipe.type.BaseRecipeImpl;
import dev.su5ed.gtexperimental.recipe.type.RecipePropertyMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

/**
 * Single Input, Multi Output recipe
 */
public class SIMORecipe<IN, OUT> extends BaseRecipeImpl<SIMORecipeType<?, IN, OUT>, RecipeIngredient<IN>, IN, OUT, SIMORecipe<IN, OUT>> {
    public static SIMORecipe<Either<ItemStack, FluidStack>, List<Either<ItemStack, FluidStack>>> industrialElectrolyzer(ResourceLocation id, RecipeIngredient<Either<ItemStack, FluidStack>> input, List<Either<ItemStack, FluidStack>> output, RecipePropertyMap properties) {
        return new SIMORecipe<>(ModRecipeTypes.INDUSTRIAL_ELECTROLYZER.get(), ModRecipeSerializers.INDUSTRIAL_ELECTROLYZER.get(), id, input, output, properties);
    }

    public static SIMORecipe<ItemStack, List<ItemStack>> lathe(ResourceLocation id, RecipeIngredient<ItemStack> input, List<ItemStack> output, RecipePropertyMap properties) {
        return new SIMORecipe<>(ModRecipeTypes.LATHE.get(), ModRecipeSerializers.LATHE.get(), id, input, output, properties);
    }

    public static SIMORecipe<ItemStack, List<ItemStack>> pulverizer(ResourceLocation id, RecipeIngredient<ItemStack> input, List<ItemStack> output, RecipePropertyMap properties) {
        return new SIMORecipe<>(ModRecipeTypes.PULVERIZER.get(), ModRecipeSerializers.PULVERIZER.get(), id, input, output, properties.withTransient(b -> b.duration(300 * input.getCount())));
    }

    public static SIMORecipe<FluidStack, List<FluidStack>> distillation(ResourceLocation id, RecipeIngredient<FluidStack> input, List<FluidStack> outputs, RecipePropertyMap properties) {
        return new SIMORecipe<>(ModRecipeTypes.DISTILLATION.get(), ModRecipeSerializers.DISTILLATION.get(), id, input, outputs, properties.withTransient(b -> b.energyCost(16)));
    }

    public static SIMORecipe<ItemStack, List<ItemStack>> implosion(ResourceLocation id, RecipeIngredient<ItemStack> input, List<ItemStack> output, RecipePropertyMap properties) {
        return new SIMORecipe<>(ModRecipeTypes.IMPLOSION.get(), ModRecipeSerializers.IMPLOSION.get(), id, input, output, properties.withTransient(b -> b.duration(20).energyCost(32)));
    }

    public static SIMORecipe<Either<ItemStack, FluidStack>, List<Either<ItemStack, FluidStack>>> industrialCentrifuge(ResourceLocation id, RecipeIngredient<Either<ItemStack, FluidStack>> input, List<Either<ItemStack, FluidStack>> output, RecipePropertyMap properties) {
        return new SIMORecipe<>(ModRecipeTypes.INDUSTRIAL_CENTRIFUGE.get(), ModRecipeSerializers.INDUSTRIAL_CENTRIFUGE.get(), id, input, output, properties.withTransient(b -> b.energyCost(5)));
    }

    public static SIMORecipe<Either<ItemStack, FluidStack>, List<ItemStack>> hotFuel(ResourceLocation id, RecipeIngredient<Either<ItemStack, FluidStack>> input, List<ItemStack> output, RecipePropertyMap properties) {
        return new SIMORecipe<>(ModRecipeTypes.HOT_FUEL.get(), ModRecipeSerializers.HOT_FUEL.get(), id, input, output, properties, true);
    }

    public SIMORecipe(SIMORecipeType<?, IN, OUT> type, RecipeSerializer<?> serializer, ResourceLocation id, RecipeIngredient<IN> input, OUT output, RecipePropertyMap properties) {
        super(type, serializer, id, input, output, properties);
    }

    public SIMORecipe(SIMORecipeType<?, IN, OUT> type, RecipeSerializer<?> serializer, ResourceLocation id, RecipeIngredient<IN> input, OUT output, RecipePropertyMap properties, boolean allowEmptyOutput) {
        super(type, serializer, id, input, output, properties, allowEmptyOutput);
    }
}
