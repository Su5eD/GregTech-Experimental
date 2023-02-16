package dev.su5ed.gtexperimental.datagen.recipe;

import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.object.Dust;
import dev.su5ed.gtexperimental.object.ModFluid;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipeName;
import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.recipe.gen.ModRecipeBuilders.fusionFluid;
import static dev.su5ed.gtexperimental.recipe.gen.ModRecipeBuilders.fusionSolid;

public final class FusionRecipesGen implements ModRecipeProvider {
    public static final FusionRecipesGen INSTANCE = new FusionRecipesGen();

    private FusionRecipesGen() {}

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        fusionSolid(ModRecipeIngredientTypes.FLUID.of(ModFluid.WOLFRAMIUM.getTag()), ModRecipeIngredientTypes.FLUID.of(ModFluid.BERYLIUM.getTag()), Dust.PLATINUM.getItemStack(), 512, 32768, 100000000).build(finishedRecipeConsumer, solidId("platinum_dust"));

        fusionFluid(ModRecipeIngredientTypes.FLUID.of(ModFluid.TRITIUM.getTag()), ModRecipeIngredientTypes.FLUID.of(ModFluid.DEUTERIUM.getTag()), ModFluid.HELIUM_PLASMA.getBuckets(1), 128, 4096, 40000000).build(finishedRecipeConsumer, fluidId("helium_plasma_h3_h2"));
        fusionFluid(ModRecipeIngredientTypes.FLUID.of(ModFluid.HELIUM3.getTag()), ModRecipeIngredientTypes.FLUID.of(ModFluid.DEUTERIUM.getTag()), ModFluid.HELIUM_PLASMA.getBuckets(1), 128, 2048, 60000000).build(finishedRecipeConsumer, fluidId("helium_plasma_he3_h2"));
    }

    private static RecipeName solidId(String name) {
        return RecipeName.common(Reference.MODID, "fusion_solid", name);
    }

    private static RecipeName fluidId(String name) {
        return RecipeName.common(Reference.MODID, "fusion_fluid", name);
    }
}
