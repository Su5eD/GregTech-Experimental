package dev.su5ed.gtexperimental.datagen.recipe;

import dev.su5ed.gtexperimental.object.ModFluid;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.api.Reference.location;
import static dev.su5ed.gtexperimental.recipe.gen.ModRecipeBuilders.distillation;
import static dev.su5ed.gtexperimental.util.GtUtil.buckets;

public final class DistillationRecipesGen implements ModRecipeProvider {
    public static final DistillationRecipesGen INSTANCE = new DistillationRecipesGen();

    private DistillationRecipesGen() {}

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        distillation(
            ModRecipeIngredientTypes.FLUID.of(ModFluid.OIL, buckets(16)),
            ModFluid.DIESEL.getFluidStack(buckets(16)),
            ModFluid.SULFURIC_ACID.getFluidStack(buckets(16)),
            ModFluid.GLYCERYL.getFluidStack(buckets(1)),
            ModFluid.METHANE.getFluidStack(buckets(16)),
            16000, 16
        )
            .build(finishedRecipeConsumer, id("oil"));
        
        buildOtherModRecipes(finishedRecipeConsumer);
    }

    private void buildOtherModRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
//        ICondition forestryCondition = FORESTRY_LOADED;
//        
//        distillation(
//            ModRecipeIngredientTypes.FLUID.of(BIOMASS, buckets(16)),
//            List.of(ETHANOL.getFluidStack(buckets(8))),
//            400, 16
//        )
//            .addConditions(forestryCondition)
//            .build(finishedRecipeConsumer, id("forestry/biomass"));
    }

    private static ResourceLocation id(String name) {
        return location("distillation/" + name);
    }
}
