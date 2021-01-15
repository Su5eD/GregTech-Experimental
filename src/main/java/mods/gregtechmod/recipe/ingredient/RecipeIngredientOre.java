package mods.gregtechmod.recipe.ingredient;

import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.RecipeIngredientType;
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
    public RecipeIngredientType getType() {
        return RecipeIngredientType.ORE;
    }

    @Override
    public int compareTo(IRecipeIngredient other) {
        int total = 0;
        List<ItemStack> matchingStacks = this.getMatchingInputs();
        List<ItemStack> otherMatchingStacks = other.getMatchingInputs();

        if (other instanceof RecipeIngredientOre) {
            outerLoop:
            for (String firstOre : this.ingredient.getOres()) {
                for (String secondOre : ((RecipeIngredientOre) other).ingredient.getOres()) {
                    int oreDiff = Math.abs(firstOre.compareTo(secondOre));
                    if (oreDiff == 0) {
                        total = 0;
                        break outerLoop;
                    }
                    total -= oreDiff;
                }
            }

            if (total == 0) total -= this.count - other.getCount();
        } else if (matchingStacks.isEmpty()) {
            outerLoop:
            for (ItemStack stack : otherMatchingStacks) {
                String association = OreDictUnificator.getAssociation(stack);
                if (association.isEmpty()) association = stack.getItem().getRegistryName().toString();
                for (String ore : this.ingredient.getOres()) {
                    int diff = ore.compareTo(association);
                    if (diff == 0) {
                        total = 0;
                        break outerLoop;
                    }
                    total += diff;
                }
            }

            if (total == 0) total += this.count - other.getCount();
        } else total -= super.compareTo(other);

        return total;
    }

    @Override
    public String toString() {
        return "RecipeIngredientOre{ores=["+String.join(",", this.ingredient.getOres())+"],count="+this.count+"}";
    }
}
