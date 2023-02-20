package dev.su5ed.gtexperimental.datagen.recipe;

import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.object.Dust;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipeName;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.recipe.gen.ModRecipeBuilders.printer;

public final class PrinterRecipesGen implements ModRecipeProvider {
    public static final PrinterRecipesGen INSTANCE = new PrinterRecipesGen();

    private PrinterRecipesGen() {}

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        printer(ModRecipeIngredientTypes.ITEM.ofValues(new Ingredient.TagValue(Dust.WOOD.getTag()), new Ingredient.ItemValue(new ItemStack(Items.SUGAR_CANE))), new ItemStack(Items.PAPER), 200, 1).build(finishedRecipeConsumer, id("paper"));
        printer(ModRecipeIngredientTypes.ITEM.of(Items.PAPER, 3), ModRecipeIngredientTypes.ITEM.of(Items.LEATHER), new ItemStack(Items.BOOK), 400, 2).build(finishedRecipeConsumer, id("book"));
        printer(ModRecipeIngredientTypes.ITEM.of(Items.PAPER, 8), ModRecipeIngredientTypes.ITEM.of(Items.COMPASS), new ItemStack(Items.MAP), 400, 2).build(finishedRecipeConsumer, id("map"));
    }

    private static RecipeName id(String name) {
        return RecipeName.common(Reference.MODID, "printer", name);
    }
}
