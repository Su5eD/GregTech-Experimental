package dev.su5ed.gtexperimental.datagen.recipe;

import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipeName;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.recipe.gen.ModRecipeBuilders.lathe;

public final class LatheRecipesGen implements ModRecipeProvider {
    public static final LatheRecipesGen INSTANCE = new LatheRecipesGen();

    private LatheRecipesGen() {}

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        lathe(ModRecipeIngredientTypes.ITEM.of(ItemTags.PLANKS), new ItemStack(Items.STICK, 2), 25, 8).build(finishedRecipeConsumer, id("planks"));
    }

    private static RecipeName id(String name) {
        return RecipeName.common(Reference.MODID, "lathe", name);
    }
}
