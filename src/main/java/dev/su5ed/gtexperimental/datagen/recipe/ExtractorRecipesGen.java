package dev.su5ed.gtexperimental.datagen.recipe;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.object.Miscellaneous;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.recipe.gen.compat.CompatRecipeBuilders.extractor;

public final class ExtractorRecipesGen implements ModRecipeProvider {
    public static final ExtractorRecipesGen INSTANCE = new ExtractorRecipesGen();

    private ExtractorRecipesGen() {}

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        extractor(Miscellaneous.INDIGO_BLOSSOM.getItem(), 1, Miscellaneous.INDIGO_DYE.getItemStack(), finishedRecipeConsumer);
        extractor(GregTechTags.COLORED_WOOL, 1, new ItemStack(Items.WHITE_WOOL), finishedRecipeConsumer);
    }
}
