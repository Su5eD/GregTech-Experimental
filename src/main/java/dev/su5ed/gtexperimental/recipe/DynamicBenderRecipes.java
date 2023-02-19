package dev.su5ed.gtexperimental.recipe;

import com.mojang.datafixers.util.Pair;
import dev.su5ed.gtexperimental.api.recipe.RecipeProvider;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipeUtil;
import dev.su5ed.gtexperimental.util.GtUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

import java.util.Map;

import static dev.su5ed.gtexperimental.api.Reference.location;

public class DynamicBenderRecipes implements RecipeProvider<BenderRecipe, ItemStack> {
    private final Map<TagKey<Item>, BenderRecipe> recipes;

    public DynamicBenderRecipes() {
        this.recipes = RecipeUtil.associateTags("ingots", "plates")
            .mapValues(Pair::getSecond)
            .mapToValue((key, output) -> {
                ResourceLocation id = location("bender", GtUtil.tagName(key) + "_to_" + GtUtil.itemName(output));
                return new BenderRecipe(id, ModRecipeIngredientTypes.ITEM.of(key), new ItemStack(output), 50, 20);
            })
            .toMap();
    }

    @Override
    public boolean hasRecipeFor(ItemStack input) {
        return StreamEx.ofKeys(this.recipes)
            .anyMatch(input::is);
    }

    @Override
    public BenderRecipe getRecipeFor(ItemStack input) {
        return EntryStream.of(this.recipes)
            .findFirst(entry -> input.is(entry.getKey()))
            .map(Map.Entry::getValue)
            .orElse(null);
    }
}
