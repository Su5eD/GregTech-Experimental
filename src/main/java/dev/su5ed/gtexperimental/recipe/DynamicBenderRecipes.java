package dev.su5ed.gtexperimental.recipe;

import com.mojang.datafixers.util.Pair;
import dev.su5ed.gtexperimental.api.recipe.RecipeProvider;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipeUtil;
import dev.su5ed.gtexperimental.recipe.type.SISORecipe;
import dev.su5ed.gtexperimental.util.GtUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import static dev.su5ed.gtexperimental.api.Reference.location;

public class DynamicBenderRecipes implements RecipeProvider<BenderRecipe, SISORecipe.Input> {
    private final Map<TagKey<Item>, BenderRecipe> recipes;

    public DynamicBenderRecipes() {
        this.recipes = RecipeUtil.associateTags("plates", "ingots")
            .mapValues(Pair::getSecond)
            .mapToValue((key, output) -> {
                String path = key.location().getPath();
                String material = StringUtils.substringAfter(path, "/").replace('/', '_');
                ResourceLocation id = location("bender", material + "_plate_to_" + GtUtil.itemName(output));
                return new BenderRecipe(id, ModRecipeIngredientTypes.ITEM.of(key), new ItemStack(output), 50, 20);
            })
            .toMap();
    }

    @Override
    public boolean hasRecipeFor(SISORecipe.Input input) {
        ItemStack item = input.item();
        return StreamEx.ofKeys(this.recipes)
            .anyMatch(item::is);
    }

    @Override
    public BenderRecipe getRecipeFor(SISORecipe.Input input) {
        ItemStack item = input.item();
        return EntryStream.of(this.recipes)
            .findFirst(entry -> item.is(entry.getKey()))
            .map(Map.Entry::getValue)
            .orElse(null);
    }
}
