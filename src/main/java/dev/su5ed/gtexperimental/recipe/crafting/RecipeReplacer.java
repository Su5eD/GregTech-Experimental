package dev.su5ed.gtexperimental.recipe.crafting;

import com.google.common.base.Stopwatch;
import dev.su5ed.gtexperimental.GregTechConfig;
import dev.su5ed.gtexperimental.GregTechMod;
import dev.su5ed.gtexperimental.GregTechTags;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.items.ItemHandlerHelper;
import one.util.streamex.StreamEx;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static dev.su5ed.gtexperimental.api.Reference.location;

public final class RecipeReplacer {


    public static void runReplacements(ReloadableServerResources resources) {
        RecipeManager manager = resources.getRecipeManager();
        Stopwatch stopwatch = Stopwatch.createStarted();
        Collection<Recipe<?>> modifiedRecipes = StreamEx.of(manager.getRecipes())
            .flatMap(RecipeReplacer::mapCraftingRecipe)
            .toList();
        manager.replaceRecipes(modifiedRecipes);
        stopwatch.stop();
        GregTechMod.LOGGER.info("Finished processing recipes in {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    public static Stream<Recipe<?>> mapCraftingRecipe(Recipe<?> recipe) {
        // Replace plank -> stick recipes
        if (recipe instanceof ShapedRecipe shapedRecipe
            && shapedRecipe.getWidth() == 1 && shapedRecipe.getHeight() == 2
            && StreamEx.of(recipe.getIngredients()).flatMap(i -> Stream.of(i.getItems())).allMatch(stack -> stack.is(ItemTags.PLANKS))) {
            // Sawing
            ResourceLocation sawingId = location("replaced/sawing/" + recipe.getId().getPath());
            NonNullList<Ingredient> ingredients = NonNullList.createWithCapacity(3);
            ingredients.add(ToolCraftingIngredient.of(GregTechTags.SAW, 1));
            ingredients.addAll(recipe.getIngredients());
            ItemStack result = recipe.getResultItem();
            // Non-sawing
            ResourceLocation rawId = location("replaced/" + recipe.getId().getPath());
            ItemStack rawOutput = GregTechConfig.COMMON.woodNeedsSawForCrafting.get() ? ItemHandlerHelper.copyStackWithSize(result, result.getCount() / 2) : result;

            return Stream.of(
                new ToolShapedRecipe(sawingId, recipe.getGroup(), 1, 3, ingredients, result.copy()),
                new ShapedRecipe(rawId, recipe.getGroup(), shapedRecipe.getWidth(), shapedRecipe.getHeight(), recipe.getIngredients(), rawOutput)
            );
        }
        return Stream.of(recipe);
    }

    private RecipeReplacer() {}
}
