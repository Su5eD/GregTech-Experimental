package dev.su5ed.gtexperimental.recipe;

import dev.su5ed.gtexperimental.api.recipe.RecipeProvider;
import dev.su5ed.gtexperimental.recipe.type.RecipePropertyMap;
import dev.su5ed.gtexperimental.recipe.type.VanillaRecipeIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

import static dev.su5ed.gtexperimental.util.GtUtil.location;

public class FurnaceRecipeProvider implements RecipeProvider<SISORecipe<ItemStack, ItemStack>, ItemStack> {
    private final Map<SmeltingRecipe, SISORecipe<ItemStack, ItemStack>> recipeCache = new HashMap<>();
    private final Map<ResourceLocation, SISORecipe<ItemStack, ItemStack>> byName = new HashMap<>();

    @Nullable
    @Override
    public SISORecipe<ItemStack, ItemStack> getRecipeFor(Level level, ItemStack input) {
        Container container = new SimpleContainer(input);
        return level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, container, level)
            .map(recipe -> this.recipeCache.computeIfAbsent(recipe, r -> {
                ResourceLocation id = location("borrowed", "furnace", r.getId().getNamespace(), r.getId().getPath());
                SISORecipe<ItemStack, ItemStack> gtRecipe = SISORecipe.furnace(id, new VanillaRecipeIngredient(r.getIngredients().get(0), 1), r.getResultItem(), RecipePropertyMap.builder().duration(100).energyCost(3).build());
                this.byName.put(id, gtRecipe);
                return gtRecipe;
            }))
            .orElse(null);
    }

    @Nullable
    @Override
    public SISORecipe<ItemStack, ItemStack> getById(Level level, ResourceLocation id) {
        return this.byName.get(id);
    }

    @Override
    public boolean hasRecipeFor(Level level, ItemStack input) {
        Container container = new SimpleContainer(input);
        return level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, container, level).isPresent();
    }

    @Override
    public void reset() {
        this.recipeCache.clear();
    }
}
