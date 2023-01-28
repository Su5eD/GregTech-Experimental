package dev.su5ed.gtexperimental.recipe;

import com.mojang.datafixers.util.Pair;
import dev.su5ed.gtexperimental.api.recipe.RecipeProvider;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipeUtil;
import dev.su5ed.gtexperimental.recipe.type.SIMORecipe;
import dev.su5ed.gtexperimental.util.GtUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

import static dev.su5ed.gtexperimental.api.Reference.location;

public class DynamicLatheRecipes implements RecipeProvider<LatheRecipe, SIMORecipe.Input<ItemStack>> {
    private final Map<TagKey<Item>, LatheRecipe> recipes;

    public DynamicLatheRecipes() {
        this.recipes = RecipeUtil.associateTags("ingots", "rods")
            .mapToValue((key, rodPair) -> {
                Pair<Item, Integer> output = RecipeUtil.findAssociatedTag(rodPair.getFirst(), "rods", "small_dusts")
                    .map(smallDustPair -> Pair.of(smallDustPair.getSecond(), 50))
                    .orElseGet(() -> Pair.of(rodPair.getSecond(), 150));
                Item outputItem = output.getFirst();
                String path = key.location().getPath();
                String material = StringUtils.substringAfter(path, "/").replace('/', '_');
                ResourceLocation id = location("lathe", material + "_ingot_to_" + GtUtil.itemName(outputItem));
                return new LatheRecipe(id, ModRecipeIngredientTypes.ITEM.of(key), List.of(new ItemStack(rodPair.getSecond()), new ItemStack(outputItem)), output.getSecond(), 16);
            })
            .toMap();
    }

    @Override
    public boolean hasRecipeFor(SIMORecipe.Input<ItemStack> input) {
        ItemStack item = input.item();
        return StreamEx.ofKeys(this.recipes)
            .anyMatch(item::is);
    }

    @Override
    public LatheRecipe getRecipeFor(SIMORecipe.Input<ItemStack> input) {
        ItemStack item = input.item();
        return EntryStream.of(this.recipes)
            .findFirst(entry -> item.is(entry.getKey()))
            .map(Map.Entry::getValue)
            .orElse(null);
    }
}
