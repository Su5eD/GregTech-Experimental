package dev.su5ed.gtexperimental.recipe;

import com.mojang.datafixers.util.Either;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeTypes;
import dev.su5ed.gtexperimental.recipe.type.ModRecipe;
import dev.su5ed.gtexperimental.recipe.type.RecipePropertyMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;

/**
 * Single Input, Single Output recipe
 */
public class SISORecipe<IN, OUT> extends ModRecipe<SISORecipeType<?, IN, OUT>, RecipeIngredient<IN>, IN, OUT, SISORecipe<IN, OUT>> {
    public static SISORecipe<ItemStack, ItemStack> bender(ResourceLocation id, RecipeIngredient<ItemStack> input, ItemStack output, RecipePropertyMap properties) {
        return new SISORecipe<>(ModRecipeTypes.BENDER.get(), ModRecipeSerializers.BENDER.get(), id, input, output, properties);
    }

    public static SISORecipe<ItemStack, ItemStack> wiremill(ResourceLocation id, RecipeIngredient<ItemStack> input, ItemStack output, RecipePropertyMap properties) {
        return new SISORecipe<>(ModRecipeTypes.WIREMILL.get(), ModRecipeSerializers.WIREMILL.get(), id, input, output, properties);
    }

    public static SISORecipe<ItemStack, ItemStack> macerator(ResourceLocation id, RecipeIngredient<ItemStack> input, ItemStack output, RecipePropertyMap properties) {
        return new SISORecipe<>(ModRecipeTypes.MACERATOR.get(), ModRecipeSerializers.MACERATOR.get(), id, input, output, properties);
    }

    public static SISORecipe<ItemStack, ItemStack> extractor(ResourceLocation id, RecipeIngredient<ItemStack> input, ItemStack output, RecipePropertyMap properties) {
        return new SISORecipe<>(ModRecipeTypes.EXTRACTOR.get(), ModRecipeSerializers.EXTRACTOR.get(), id, input, output, properties);
    }

    public static SISORecipe<ItemStack, ItemStack> compressor(ResourceLocation id, RecipeIngredient<ItemStack> input, ItemStack output, RecipePropertyMap properties) {
        return new SISORecipe<>(ModRecipeTypes.COMPRESSOR.get(), ModRecipeSerializers.COMPRESSOR.get(), id, input, output, properties);
    }

    public static SISORecipe<ItemStack, ItemStack> recycler(ResourceLocation id, RecipeIngredient<ItemStack> input, ItemStack output, RecipePropertyMap properties) {
        return new SISORecipe<>(ModRecipeTypes.RECYCLER.get(), ModRecipeSerializers.RECYCLER.get(), id, input, output, properties);
    }

    public static SISORecipe<FluidStack, FluidStack> vacuumFreezerFluid(ResourceLocation id, RecipeIngredient<FluidStack> input, FluidStack output, RecipePropertyMap properties) {
        return new SISORecipe<>(ModRecipeTypes.VACUUM_FREEZER_FLUID.get(), ModRecipeSerializers.VACUUM_FREEZER_FLUID.get(), id, input, output, properties.withTransient(b -> b.energyCost(128)));
    }

    public static SISORecipe<ItemStack, ItemStack> vacuumFreezerSolid(ResourceLocation id, RecipeIngredient<ItemStack> input, ItemStack output, RecipePropertyMap properties) {
        return new SISORecipe<>(ModRecipeTypes.VACUUM_FREEZER_SOLID.get(), ModRecipeSerializers.VACUUM_FREEZER_SOLID.get(), id, input, output, properties.withTransient(b -> b.energyCost(128)));
    }

    public static SISORecipe<FluidStack, FluidStack> denseLiquid(ResourceLocation id, RecipeIngredient<FluidStack> input, FluidStack output, RecipePropertyMap properties) {
        return new SISORecipe<>(ModRecipeTypes.DENSE_LIQUID_FUEL.get(), ModRecipeSerializers.DENSE_LIQUID_FUEL.get(), id, input, output, properties, true);
    }

    public static SISORecipe<Either<ItemStack, FluidStack>, FluidStack> dieselFuel(ResourceLocation id, RecipeIngredient<Either<ItemStack, FluidStack>> input, FluidStack output, RecipePropertyMap properties) {
        return new SISORecipe<>(ModRecipeTypes.DIESEL_FUEL.get(), ModRecipeSerializers.DIESEL_FUEL.get(), id, input, output, properties, true);
    }

    public static SISORecipe<FluidStack, FluidStack> gasFuel(ResourceLocation id, RecipeIngredient<FluidStack> input, FluidStack output, RecipePropertyMap properties) {
        return new SISORecipe<>(ModRecipeTypes.GAS_FUEL.get(), ModRecipeSerializers.GAS_FUEL.get(), id, input, output, properties, true);
    }

    public static SISORecipe<Either<ItemStack, FluidStack>, ItemStack> magic(ResourceLocation id, RecipeIngredient<Either<ItemStack, FluidStack>> input, ItemStack output, RecipePropertyMap properties) {
        return new SISORecipe<>(ModRecipeTypes.MAGIC_FUEL.get(), ModRecipeSerializers.MAGIC_FUEL.get(), id, input, output, properties, true);
    }

    public static SISORecipe<FluidStack, FluidStack> plasma(ResourceLocation id, RecipeIngredient<FluidStack> input, FluidStack output, RecipePropertyMap properties) {
        return new SISORecipe<>(ModRecipeTypes.PLASMA_FUEL.get(), ModRecipeSerializers.PLASMA_FUEL.get(), id, input, output, properties, true);
    }

    public static SISORecipe<FluidStack, FluidStack> steam(ResourceLocation id, RecipeIngredient<FluidStack> input, FluidStack output, RecipePropertyMap properties) {
        return new SISORecipe<>(ModRecipeTypes.STEAM_FUEL.get(), ModRecipeSerializers.STEAM_FUEL.get(), id, input, output, properties, true);
    }

    public SISORecipe(SISORecipeType<?, IN, OUT> type, RecipeSerializer<?> serializer, ResourceLocation id, RecipeIngredient<IN> input, OUT output, int duration, double energyCost) {
        super(type, serializer, id, input, output, duration, energyCost);
    }

    public SISORecipe(SISORecipeType<?, IN, OUT> type, RecipeSerializer<?> serializer, ResourceLocation id, RecipeIngredient<IN> input, OUT output, RecipePropertyMap properties) {
        super(type, serializer, id, input, output, properties);
    }

    public SISORecipe(SISORecipeType<?, IN, OUT> type, RecipeSerializer<?> serializer, ResourceLocation id, RecipeIngredient<IN> input, OUT output, RecipePropertyMap properties, boolean allowEmptyOutput) {
        super(type, serializer, id, input, output, properties, allowEmptyOutput);
    }
}
