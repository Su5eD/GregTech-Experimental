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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DamagedOreIngredientFixer {
    public static final List<IRecipe> FIXED_RECIPES = new ArrayList<>();

    public static void fixRecipes() {
        new ArrayList<>(ForgeRegistries.RECIPES.getValuesCollection()).stream()
                .filter(ShapelessRecipes.class::isInstance)
                .forEach(recipe -> {
                    NonNullList<Ingredient> ingredients = NonNullList.create();
                    ingredients.addAll(recipe.getIngredients());

                    boolean changed = false;
                    for (int i = 0; i < ingredients.size(); i++) {
                        Ingredient ingredient = ingredients.get(i);
                        if (ingredient instanceof OreIngredient) {
                            ItemStack[] stacks = Arrays.stream(ingredient.getMatchingStacks())
                                    .filter(stack -> {
                                        Item item = stack.getItem();
                                        return item.getRegistryName().getNamespace().equals(Reference.MODID) && item.getMaxDamage(stack) > 0;
                                    })
                                    .map(StackUtil::copyWithWildCard)
                                    .toArray(ItemStack[]::new);
                            if (stacks.length > 0) {
                                ingredients.remove(ingredient);
                                ingredients.add(Ingredient.fromStacks(stacks));
                                changed = true;
                            }
                        }
                    }

                    if (changed) {
                        ResourceLocation name = new ResourceLocation(Reference.MODID, "fixed/"+recipe.getRegistryName().getPath());
                        IRecipe newRecipe = new ShapelessRecipes(recipe.getGroup(), recipe.getRecipeOutput(), ingredients).setRegistryName(name);
                        FIXED_RECIPES.add(newRecipe);
                        ForgeRegistries.RECIPES.register(newRecipe);
                    }
                });
    }
}
