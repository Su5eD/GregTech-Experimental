package dev.su5ed.gtexperimental.datagen.recipe;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import twilightforest.init.TFItems;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.api.Reference.location;
import static dev.su5ed.gtexperimental.datagen.RecipeGen.TWILIGHT_FOREST_LOADED;
import static dev.su5ed.gtexperimental.recipe.gen.ModRecipeBuilders.industrialSawmill;
import static dev.su5ed.gtexperimental.util.GtUtil.buckets;

public final class IndustrialSawmillRecipesGen implements ModRecipeProvider {
    public static final IndustrialSawmillRecipesGen INSTANCE = new IndustrialSawmillRecipesGen();
    private static final RecipeIngredient<FluidStack> WATER = ModRecipeIngredientTypes.FLUID.of(Fluids.WATER, buckets(1));

    private IndustrialSawmillRecipesGen() {}

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        buildOtherModRecipes(finishedRecipeConsumer);
    }

    private void buildOtherModRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        // Twilight Forest
        industrialSawmill(ModRecipeIngredientTypes.ITEM.of(TFItems.LIVEROOT.get()), WATER, new ItemStack(Items.STICK, 4), new ItemStack(Items.STICK, 2))
            .addConditions(TWILIGHT_FOREST_LOADED)
            .build(finishedRecipeConsumer, id("twilight_forest/liveroot"));
    }

    private static ResourceLocation id(String name) {
        return location("industrial_sawmill/" + name);
    }
}
