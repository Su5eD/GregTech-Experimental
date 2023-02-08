package dev.su5ed.gtexperimental.datagen.recipe;

import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.object.ModFluid;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipeName;
import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.recipe.gen.ModRecipeBuilders.distillation;
import static dev.su5ed.gtexperimental.util.GtUtil.buckets;

public final class DistillationRecipesGen implements ModRecipeProvider {
    public static final DistillationRecipesGen INSTANCE = new DistillationRecipesGen();

    private DistillationRecipesGen() {}

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        distillation(
            ModRecipeIngredientTypes.FLUID.of(ModFluid.OIL, buckets(16)),
            ModFluid.DIESEL.getBuckets(16),
            ModFluid.SULFURIC_ACID.getBuckets(16),
            ModFluid.GLYCERYL.getBuckets(1),
            ModFluid.METHANE.getBuckets(16),
            16000
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

    private static RecipeName id(String name) {
        return RecipeName.common(Reference.MODID, "distillation", name);
    }
}
