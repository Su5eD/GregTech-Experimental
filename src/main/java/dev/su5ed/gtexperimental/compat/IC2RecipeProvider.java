package dev.su5ed.gtexperimental.compat;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeProvider;
import dev.su5ed.gtexperimental.recipe.SISORecipe;
import dev.su5ed.gtexperimental.recipe.type.BaseRecipeFactory;
import dev.su5ed.gtexperimental.recipe.type.RecipePropertyMap;
import dev.su5ed.gtexperimental.recipe.type.VanillaRecipeIngredient;
import dev.su5ed.gtexperimental.util.GtUtil;
import ic2.api.recipe.IBasicMachineRecipeManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipe;
import ic2.api.recipe.MachineRecipeResult;
import ic2.api.recipe.Recipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static dev.su5ed.gtexperimental.util.GtUtil.location;

public class IC2RecipeProvider implements RecipeProvider<SISORecipe<ItemStack, ItemStack>, ItemStack> {
    private final String name;
    private final Supplier<Recipes.IGetter<IBasicMachineRecipeManager>> getter;
    private final BaseRecipeFactory<SISORecipe<ItemStack, ItemStack>, RecipeIngredient<ItemStack>, ItemStack> factory;
    
    private final Map<MachineRecipe<IRecipeInput, Collection<ItemStack>>, SISORecipe<ItemStack, ItemStack>> recipeCache = new HashMap<>();
    private final Map<ResourceLocation, SISORecipe<ItemStack, ItemStack>> byName = new HashMap<>();

    public IC2RecipeProvider(String name, Supplier<Recipes.IGetter<IBasicMachineRecipeManager>> getter, BaseRecipeFactory<SISORecipe<ItemStack, ItemStack>, RecipeIngredient<ItemStack>, ItemStack> factory) {
        this.name = name;
        this.getter = getter;
        this.factory = factory;
    }

    @Nullable
    @Override
    public SISORecipe<ItemStack, ItemStack> getRecipeFor(Level level, ItemStack input) {
        MachineRecipeResult<IRecipeInput, Collection<ItemStack>, ItemStack> result = this.getter.get().get(level).apply(input, false);
        if (result != null) {
            return this.recipeCache.computeIfAbsent(result.getRecipe(), recipe -> {
                IRecipeInput recipeInput = recipe.getInput();
                ItemStack output = recipe.getOutput().iterator().next();
                ResourceLocation id = location("borrowed", this.name, GtUtil.itemName(input) + "_to_" + GtUtil.itemName(output));
                SISORecipe<ItemStack, ItemStack> gtRecipe = this.factory.create(id, new VanillaRecipeIngredient(recipeInput.getIngredient(), recipeInput.getAmount()), output, RecipePropertyMap.builder().duration(300).energyCost(2).build());
                this.byName.put(id, gtRecipe);
                return gtRecipe;
            });
        }
        return null;
    }

    @Nullable
    @Override
    public SISORecipe<ItemStack, ItemStack> getById(Level level, ResourceLocation id) {
        return this.byName.get(id);
    }

    @Override
    public boolean hasRecipeFor(Level level, ItemStack input) {
        return this.getter.get().get(level).apply(input, false) != null;
    }

    @Override
    public void reset() {
        this.recipeCache.clear();
    }
}
