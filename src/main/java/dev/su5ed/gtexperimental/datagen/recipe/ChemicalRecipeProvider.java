package dev.su5ed.gtexperimental.datagen.recipe;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.object.ModFluid;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.api.Reference.location;
import static dev.su5ed.gtexperimental.recipe.gen.ModRecipeBuilders.chemical;

public final class ChemicalRecipeProvider implements ModRecipeProvider {
    public static final ChemicalRecipeProvider INSTANCE = new ChemicalRecipeProvider();

    private ChemicalRecipeProvider() {}

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        chemical(ModRecipeIngredientTypes.FLUID.of(ModFluid.CARBON.getTag()), ModRecipeIngredientTypes.FLUID.of(ModFluid.HYDROGEN.getTag(), buckets(4)), ModFluid.METHANE.getFluidStack(buckets(5)), 3500)
            .build(finishedRecipeConsumer, id("methane"));
        chemical(ModRecipeIngredientTypes.FLUID.of(ModFluid.CARBON.getTag()), ModRecipeIngredientTypes.FLUID.of(ModFluid.NITROGEN.getTag()), ModFluid.NITROCARBON.getFluidStack(buckets(2)), 1500)
            .build(finishedRecipeConsumer, id("nitrocarbon"));
        chemical(ModRecipeIngredientTypes.FLUID.of(ModFluid.CALCIUM.getTag()), ModRecipeIngredientTypes.FLUID.of(ModFluid.CARBON.getTag()), ModFluid.CALCIUM_CARBONATE.getFluidStack(buckets(2)), 250)
            .build(finishedRecipeConsumer, id("calcium_carbonate"));
        chemical(ModRecipeIngredientTypes.FLUID.of(ModFluid.SULFUR.getTag()), ModRecipeIngredientTypes.FLUID.of(ModFluid.SODIUM.getTag()), ModFluid.SODIUM_SULFIDE.getFluidStack(buckets(2)), 100)
            .build(finishedRecipeConsumer, id("sodium_sulfide"));
        chemical(ModRecipeIngredientTypes.FLUID.of(ModFluid.SULFUR.getTag()), ModRecipeIngredientTypes.FLUID.of(FluidTags.WATER, buckets(2)), ModFluid.SULFURIC_ACID.getFluidStack(buckets(3)), 1150)
            .build(finishedRecipeConsumer, id("sulfuric_acid"));
        chemical(ModRecipeIngredientTypes.FLUID.of(ModFluid.SODIUM_SULFIDE.getTag(), buckets(2)), ModRecipeIngredientTypes.FLUID.of(GregTechTags.AIR, buckets(3)), ModFluid.SODIUM_PERSULFATE.getFluidStack(buckets(5)), 4000)
            .build(finishedRecipeConsumer, id("sodium_persulfate"));
        chemical(ModRecipeIngredientTypes.FLUID.of(ModFluid.NITROCARBON.getTag(), buckets(3)), ModRecipeIngredientTypes.FLUID.of(FluidTags.WATER, buckets(3)), ModFluid.GLYCERYL.getFluidStack(buckets(6)), 1750)
            .build(finishedRecipeConsumer, id("glyceryl"));
        chemical(ModRecipeIngredientTypes.FLUID.of(ModFluid.HYDROGEN.getTag(), buckets(4)), ModRecipeIngredientTypes.FLUID.of(GregTechTags.AIR), new FluidStack(Fluids.WATER, buckets(5)), 10)
            .build(finishedRecipeConsumer, id("water"));
        chemical(ModRecipeIngredientTypes.FLUID.of(ModFluid.NITROGEN.getTag()), ModRecipeIngredientTypes.FLUID.of(GregTechTags.AIR), ModFluid.NITROGEN_DIOXIDE.getFluidStack(buckets(2)), 1250)
            .build(finishedRecipeConsumer, id("nitrogen_dioxide"));
        chemical(ModRecipeIngredientTypes.FLUID.of(ModFluid.GLYCERYL.getTag()), ModRecipeIngredientTypes.FLUID.of(ModFluid.DIESEL.getTag(), buckets(4)), ModFluid.NITRO_DIESEL.getFluidStack(buckets(5)), 250)
            .build(finishedRecipeConsumer, id("nitro_diesel"));

        // Classic
        // TODO ItemFluid recipes
//        chemical(ModRecipeIngredientTypes.FLUID.of(ModFluid.GLYCERYL.getTag()), Ic2Items.COALFUEL_CELL, )
    }

    private static ResourceLocation id(String name) {
        return location("chemical/" + name);
    }

    private static int buckets(int buckets) {
        return buckets * FluidType.BUCKET_VOLUME;
    }
}