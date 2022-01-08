package mods.gregtechmod.recipe.util;

import ic2.core.util.StackUtil;
import mods.gregtechmod.api.util.Reference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreIngredient;
import one.util.streamex.StreamEx;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DamagedOreIngredientFixer {
    public static List<IRecipe> fixedRecipes;

    public static void fixRecipes() {
        fixedRecipes = StreamEx.of(new ArrayList<>(ForgeRegistries.RECIPES.getValuesCollection()))
                .select(ShapelessRecipes.class)
                .mapPartial(recipe -> {
                    NonNullList<Ingredient> ingredients = NonNullList.create();
                    ingredients.addAll(recipe.getIngredients());

                    boolean changed = StreamEx.of(ingredients)
                            .select(OreIngredient.class)
                            .mapToEntry(ingredient -> StreamEx.of(ingredient.getMatchingStacks())
                                            .filter(stack -> {
                                                Item item = stack.getItem();
                                                return item.getRegistryName().getNamespace().equals(Reference.MODID) && item.getMaxDamage(stack) > 0;
                                            })
                                            .map(StackUtil::copyWithWildCard)
                                            .toArray(ItemStack[]::new)
                                    )
                            .mapValues(Ingredient::fromStacks)
                            .mapKeyValue((ingredient, newIngredient) -> ingredients.remove(ingredient) && ingredients.add(newIngredient))
                            .anyMatch(Boolean::booleanValue);

                    if (changed) {
                        ResourceLocation name = new ResourceLocation(Reference.MODID, "fixed/" + recipe.getRegistryName().getPath());
                        IRecipe newRecipe = new ShapelessRecipes(recipe.getGroup(), recipe.getRecipeOutput(), ingredients).setRegistryName(name);
                        ForgeRegistries.RECIPES.register(newRecipe);
                        
                        return Optional.of(newRecipe);
                    }
                    return Optional.empty();
                })
                .toImmutableList();
    }
}
