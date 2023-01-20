package dev.su5ed.gtexperimental.recipe;

import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.compat.ModHandler;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipeProvider;
import dev.su5ed.gtexperimental.recipe.type.SISORecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static dev.su5ed.gtexperimental.api.Reference.location;

public class DynamicBenderRecipes implements RecipeProvider<BenderRecipe, SISORecipe.Input> {
    private static final List<String> MOD_PRIORITY = List.of(ModHandler.FTBIC_MODID, ModHandler.IC2_MODID, Reference.MODID);

    private final Map<TagKey<Item>, BenderRecipe> recipes;

    public DynamicBenderRecipes() {
        ITagManager<Item> tags = ForgeRegistries.ITEMS.tags();
        this.recipes = StreamEx.of(tags.getTagNames())
            .filter(key -> key.location().getNamespace().equals("forge") && key.location().getPath().startsWith("plates/"))
            .mapToEntry(key -> tags.createTagKey(new ResourceLocation(key.location().toString().replace("plates/", "ingots/"))))
            .filterValues(tags::isKnownTagName)
            .mapValues(key -> tags.getTag(key).stream()
                .min(Comparator.comparingInt(item -> {
                    String namespace = ForgeRegistries.ITEMS.getKey(item).getNamespace();
                    return MOD_PRIORITY.indexOf(namespace);
                }))
                .orElse(null))
            .nonNullValues()
            .mapToValue((key, output) -> {
                String path = key.location().getPath();
                String material = StringUtils.substringAfter(path, "/").replace('/', '_');
                ResourceLocation id = location("bender", material + "_plate_to_" + ForgeRegistries.ITEMS.getKey(output).getPath());
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
