package dev.su5ed.gtexperimental.recipe;

import dev.su5ed.gtexperimental.api.recipe.ListRecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeTypes;
import dev.su5ed.gtexperimental.recipe.type.ModRecipe;
import dev.su5ed.gtexperimental.recipe.type.RecipePropertyMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

/**
 * Multi Input, Single Output recipe
 */
public class MISORecipe<IN, OUT> extends ModRecipe<MISORecipeType<?, IN, OUT>, ListRecipeIngredient<IN>, List<IN>, OUT, MISORecipe<IN, OUT>> {
    public static MISORecipe<ItemStack, ItemStack> printer(ResourceLocation id, List<? extends RecipeIngredient<ItemStack>> inputs, ItemStack output, RecipePropertyMap properties) {
        return new MISORecipe<>(ModRecipeTypes.PRINTER.get(), ModRecipeSerializers.PRINTER.get(), id, inputs, output, properties);
    }

    public static MISORecipe<ItemStack, ItemStack> alloySmelter(ResourceLocation id, List<? extends RecipeIngredient<ItemStack>> inputs, ItemStack output, RecipePropertyMap properties) {
        return new MISORecipe<>(ModRecipeTypes.ALLOY_SMELTER.get(), ModRecipeSerializers.ALLOY_SMELTER.get(), id, inputs, output, properties);
    }

    public static MISORecipe<ItemStack, ItemStack> assembler(ResourceLocation id, List<? extends RecipeIngredient<ItemStack>> inputs, ItemStack output, RecipePropertyMap properties) {
        return new MISORecipe<>(ModRecipeTypes.ASSEMBLER.get(), ModRecipeSerializers.ASSEMBLER.get(), id, inputs, output, properties);
    }

    public static MISORecipe<FluidStack, FluidStack> chemical(ResourceLocation id, List<? extends RecipeIngredient<FluidStack>> input, FluidStack output, RecipePropertyMap properties) {
        return new MISORecipe<>(ModRecipeTypes.CHEMICAL.get(), ModRecipeSerializers.CHEMICAL.get(), id, input, output, properties.withTransient(b -> b.energyCost(32)));
    }

    public static MISORecipe<FluidStack, FluidStack> fusionFluid(ResourceLocation id, List<? extends RecipeIngredient<FluidStack>> inputs, FluidStack output, RecipePropertyMap properties) {
        return new MISORecipe<>(ModRecipeTypes.FUSION_FLUID.get(), ModRecipeSerializers.FUSION_FLUID.get(), id, inputs, output, properties);
    }

    public static MISORecipe<FluidStack, ItemStack> fusionSolid(ResourceLocation id, List<? extends RecipeIngredient<FluidStack>> inputs, ItemStack output, RecipePropertyMap properties) {
        return new MISORecipe<>(ModRecipeTypes.FUSION_SOLID.get(), ModRecipeSerializers.FUSION_SOLID.get(), id, inputs, output, properties);
    }

    public MISORecipe(MISORecipeType<?, IN, OUT> type, RecipeSerializer<?> serializer, ResourceLocation id, List<? extends RecipeIngredient<IN>> input, OUT output, RecipePropertyMap properties) {
        super(type, serializer, id, type.getInputType().createIngredient(input), output, properties);
    }
}
