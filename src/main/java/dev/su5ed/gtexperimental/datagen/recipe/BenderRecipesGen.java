package dev.su5ed.gtexperimental.datagen.recipe;

import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.recipe.type.RecipeName;
import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

public final class BenderRecipesGen implements ModRecipeProvider {
    public static final BenderRecipesGen INSTANCE = new BenderRecipesGen();

    private BenderRecipesGen() {}

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        buildOtherModRecipes(finishedRecipeConsumer);
    }

    private void buildOtherModRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        // Railcraft
//        bender(ModRecipeIngredientTypes.ITEM.of(Rod.ALUMINIUM.getTag(), 3), new ItemStack(RAILCRAFT_RAIL), 150, 10)
//            .addConditions(railcraftLoaded)
//            .build(finishedRecipeConsumer, id("rail_from_aluminium"));
//        bender(ModRecipeIngredientTypes.ITEM.of(Rod.IRON.getTag(), 3), new ItemStack(RAILCRAFT_RAIL, 2), 150, 10)
//            .addConditions(railcraftLoaded)
//            .build(finishedRecipeConsumer, id("rail_from_iron"));
//        bender(ModRecipeIngredientTypes.ITEM.of(Rod.BRONZE.getTag(), 2), new ItemStack(RAILCRAFT_RAIL), 50, 20)
//            .addConditions(railcraftLoaded)
//            .build(finishedRecipeConsumer, id("rail_from_bronze"));
//        bender(ModRecipeIngredientTypes.ITEM.of(Rod.STEEL.getTag(), 3), new ItemStack(RAILCRAFT_RAIL, 4), 150, 30)
//            .addConditions(railcraftLoaded)
//            .build(finishedRecipeConsumer, id("rail_from_steel"));
//        bender(ModRecipeIngredientTypes.ITEM.ofTags(3, Rod.TITANIUM.getTag(), Rod.TUNGSTEN.getTag()), new ItemStack(RAILCRAFT_RAIL, 8), 150, 30)
//            .addConditions(railcraftLoaded)
//            .build(finishedRecipeConsumer, id("rail_from_titanium"));
//        bender(ModRecipeIngredientTypes.ITEM.of(Rod.TUNGSTEN_STEEL.getTag()), new ItemStack(RAILCRAFT_RAIL, 4), 100, 30)
//            .addConditions(railcraftLoaded)
//            .build(finishedRecipeConsumer, id("rail_from_tungsten_steel"));
//        bender(ModRecipeIngredientTypes.ITEM.of(Rod.REFINED_IRON.getTag(), 6), new ItemStack(RAILCRAFT_RAIL, 5), 150, 20)
//            .addConditions(railcraftLoaded, SelectedProfileCondition.REFINED_IRON)
//            .build(finishedRecipeConsumer, id("rail_from_refined_iron"));
    }

    private static RecipeName id(String name) {
        return RecipeName.common(Reference.MODID, "bender", name);
    }
}