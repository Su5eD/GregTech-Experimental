package dev.su5ed.gtexperimental.datagen.recipe;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.object.Component;
import dev.su5ed.gtexperimental.object.Ingot;
import dev.su5ed.gtexperimental.object.Miscellaneous;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipeName;
import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.recipe.gen.ModRecipeBuilders.wiremill;

public final class WiremillRecipesGen implements ModRecipeProvider {
    public static final WiremillRecipesGen INSTANCE = new WiremillRecipesGen();

    private WiremillRecipesGen() {}

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        wiremill(ModRecipeIngredientTypes.ITEM.of(Ingot.SOLDERING_ALLOY.getTag()), Miscellaneous.SOLDERING_TIN.getItemStack(), 100, 4).build(finishedRecipeConsumer, id("soldering_tin"));
        wiremill(ModRecipeIngredientTypes.ITEM.of(Ingot.LEAD.getTag()), Miscellaneous.SOLDERING_LEAD.getItemStack(), 100, 4).build(finishedRecipeConsumer, id("soldering_lead"));
        wiremill(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ingot("kanthal"), 4), Component.KANTHAL_COIL.getItemStack(), 450, 12).build(finishedRecipeConsumer, id("kanthal_coil"));
        wiremill(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ingot("nichrome"), 5), Component.NICHROME_COIL.getItemStack(), 600, 16).build(finishedRecipeConsumer, id("nichrome_coil"));
        wiremill(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ingot("cupronickel"), 3), Component.CUPRONICKEL_COIL.getItemStack(), 300, 8).build(finishedRecipeConsumer, id("cupronickel_coil"));
    }

    private static RecipeName id(String name) {
        return RecipeName.common(Reference.MODID, "wiremill", name);
    }
}
