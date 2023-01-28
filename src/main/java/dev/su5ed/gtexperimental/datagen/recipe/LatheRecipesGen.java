package dev.su5ed.gtexperimental.datagen.recipe;

import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.api.Reference.location;
import static dev.su5ed.gtexperimental.recipe.gen.ModRecipeBuilders.lathe;

public final class LatheRecipesGen implements ModRecipeProvider {
    public static final LatheRecipesGen INSTANCE = new LatheRecipesGen();

    private LatheRecipesGen() {}

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        lathe(ModRecipeIngredientTypes.ITEM.of(ItemTags.PLANKS), new ItemStack(Items.STICK, 2), 25, 8)
            .build(finishedRecipeConsumer, id("planks"));
    }

    private static ResourceLocation id(String name) {
        return location("lathe/" + name);
    }
}
