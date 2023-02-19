package dev.su5ed.gtexperimental.datagen.recipe;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.object.Dust;
import dev.su5ed.gtexperimental.object.Miscellaneous;
import dev.su5ed.gtexperimental.object.Ore;
import dev.su5ed.gtexperimental.recipe.gen.SmeltingRecipeBuilder;
import dev.su5ed.gtexperimental.recipe.gen.compat.InductionSmelterRecipeBuilder;
import dev.su5ed.gtexperimental.recipe.type.RecipeName;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.recipe.type.RecipeUtil.hasTags;

public final class SmeltingRecipesGen implements ModRecipeProvider {
    public static final SmeltingRecipesGen INSTANCE = new SmeltingRecipesGen();

    private SmeltingRecipesGen() {}

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        new SmeltingRecipeBuilder(Ingredient.of(GregTechTags.ore("sulfur")), Dust.SULFUR.getItemStack(3), 0, 200)
            .unlockedBy("has_ores_sulfur", hasTags(GregTechTags.ore("sulfur")))
            .build(finishedRecipeConsumer, id("sulfur_dust"));
        new SmeltingRecipeBuilder(Ingredient.of(GregTechTags.ore("saltpeter")), Dust.SALTPETER.getItemStack(3), 0, 200)
            .unlockedBy("has_ores_saltpeter", hasTags(GregTechTags.ore("saltpeter")))
            .build(finishedRecipeConsumer, id("saltpeter_dust"));
        new SmeltingRecipeBuilder(Ingredient.of(GregTechTags.ore("phosphorus")), Dust.PHOSPHORUS.getItemStack(2), 0, 200)
            .unlockedBy("has_ores_phosphorus", hasTags(GregTechTags.ore("phosphorus")))
            .build(finishedRecipeConsumer, id("phosphorus_dust"));
        new SmeltingRecipeBuilder(Ingredient.of(Miscellaneous.FLOUR.getTag()), new ItemStack(Items.BREAD), 0, 200)
            .unlockedBy("has_flour", hasTags(Miscellaneous.FLOUR.getTag()))
            .build(finishedRecipeConsumer, id("bread"));
        
        new InductionSmelterRecipeBuilder(Ingredient.of(Ore.PYRITE.getTag()), List.of(new InductionSmelterRecipeBuilder.Result(new ItemStack(Items.IRON_INGOT), 95)), 3000)
            .build(finishedRecipeConsumer, RecipeName.foreign(InductionSmelterRecipeBuilder.TYPE, "iron_ingot"));
    }

    private static RecipeName id(String name) {
        return RecipeName.common(Reference.MODID, "smelting", name);
    }
}
