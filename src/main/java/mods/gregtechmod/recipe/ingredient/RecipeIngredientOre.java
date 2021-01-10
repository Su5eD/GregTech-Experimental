package mods.gregtechmod.recipe.ingredient;

import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.util.OreDictUnificator;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class RecipeIngredientOre extends RecipeIngredientBase<GtOreIngredient> {

    private RecipeIngredientOre(List<String> ores, int count) {
        super(new GtOreIngredient(ores), count);
    }

    public static RecipeIngredientOre create(String ore) {
        return create(ore, 1);
    }

    public static RecipeIngredientOre create(String ore, int count) {
        if (ore.isEmpty()) return null;
        return new RecipeIngredientOre(Collections.singletonList(ore), count);
    }

    public static RecipeIngredientOre create(List<String> ores, int count) {
        for (String ore : ores) {
            if (ore.isEmpty()) {
                GregTechAPI.logger.error("Found empty string among ores: "+String.join(", ", ores));
                return null;
            }
        }

        return new RecipeIngredientOre(ores, count);
    }

    @Override
    public int compareTo(IRecipeIngredient other) {
        int diff = 0;
        List<ItemStack> matchingStacks = this.getMatchingInputs();
        List<ItemStack> otherMatchingStacks = other.getMatchingInputs();

        if (other instanceof RecipeIngredientOre) {
            for (String firstOre : this.ingredient.getOres()) {
                for (String secondOre : ((RecipeIngredientOre) other).ingredient.getOres()) {
                    System.out.println(firstOre + " <-> " + secondOre + " => " + firstOre.compareTo(secondOre));
                    diff -= firstOre.compareTo(secondOre);
                }
            }

            if (diff == 0) diff -= this.count - other.getCount();
        } else if (matchingStacks.isEmpty()) {
            for (ItemStack stack : otherMatchingStacks) {
                String association = OreDictUnificator.getAssociation(stack);
                if (association.isEmpty()) association = stack.getItem().getRegistryName().toString();
                for (String ore : this.ingredient.getOres()) {
                    diff += ore.compareTo(association);
                }
            }

            if (diff == 0) diff += this.count - other.getCount();
        } else diff -= super.compareTo(other);

        return diff;
    }

    @Override
    public String toString() {
        return "RecipeIngredientOre{ores=["+String.join(",", this.ingredient.getOres())+"],count="+this.count+"}";
    }
}
