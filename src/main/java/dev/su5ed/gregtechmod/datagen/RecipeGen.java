package dev.su5ed.gregtechmod.datagen;

import dev.su5ed.gregtechmod.api.Reference;
import dev.su5ed.gregtechmod.object.Ingot;
import dev.su5ed.gregtechmod.recipe.gen.ModRecipeBuilders;
import dev.su5ed.gregtechmod.recipe.type.VanillaRecipeIngredient;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;

import java.util.List;
import java.util.function.Consumer;

import static dev.su5ed.gregtechmod.api.Reference.location;

public class RecipeGen extends RecipeProvider {

    public RecipeGen(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        ModRecipeBuilders.alloySmelter(
            List.of(
                new VanillaRecipeIngredient(Ingredient.of(Tags.Items.INGOTS_GOLD)),
                new VanillaRecipeIngredient(Ingredient.of(Ingot.SILVER.getTag()))
            ),
            Ingot.ELECTRUM.getItemStack(2)
        ).build(finishedRecipeConsumer, location("alloy_smelter/electrum_ingot"));
    }

    @Override
    public String getName() {
        return Reference.NAME + super.getName();
    }
}
