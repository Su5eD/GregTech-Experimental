package dev.su5ed.gtexperimental.datagen.recipe;

import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.object.ModFluid;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipeName;
import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.recipe.gen.ModFuelBuilders.denseLiquid;
import static dev.su5ed.gtexperimental.util.GtUtil.buckets;

public final class DenseLiquidFuelGen implements ModRecipeProvider {
    public static final DenseLiquidFuelGen INSTANCE = new DenseLiquidFuelGen();

    private DenseLiquidFuelGen() {}

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        denseLiquid(ModRecipeIngredientTypes.FLUID.of(ModFluid.SEED_OIL.getTag(), buckets(1)), 2).build(finishedRecipeConsumer, id("seed_oil"));
        denseLiquid(ModRecipeIngredientTypes.FLUID.of(ModFluid.SODIUM.getTag(), buckets(1)), 30).build(finishedRecipeConsumer, id("sodium"));
        denseLiquid(ModRecipeIngredientTypes.FLUID.of(ModFluid.LITHIUM.getTag(), buckets(1)), 60).build(finishedRecipeConsumer, id("lithium"));
        denseLiquid(ModRecipeIngredientTypes.FLUID.of(ModFluid.OIL.getTag(), buckets(1)), 64).build(finishedRecipeConsumer, id("oil"));

        buildOtherModRecipes(finishedRecipeConsumer);
    }

    private void buildOtherModRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        // Forestry
//        denseLiquid(ModRecipeIngredientTypes.FLUID.of(ModFluid.BIOMASS, buckets(1)), 8)
//            .addConditions(FORESTRY_LOADED)
//            .build(finishedRecipeConsumer, id("biomass"));

        // RailCraft
//        denseLiquid(ModRecipeIngredientTypes.FLUID.of(ModFluid.CREOSOTE, buckets(1)), 3)
//            .addConditions(RAILCRAFT_LOADED)
//            .build(finishedRecipeConsumer, id("creosote"));
    }

    private static RecipeName id(String name) {
        return RecipeName.common(Reference.MODID, "fuels/dense_liquid", name);
    }
}
