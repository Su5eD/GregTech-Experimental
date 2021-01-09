package mods.gregtechmod.recipe.ingredient;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.util.OreDictUnificator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreIngredient;

import java.util.List;

public class RecipeIngredientOre extends RecipeIngredientBase<OreIngredient> {
    private final String ore;

    private RecipeIngredientOre(String ore, int count) {
        super(new OreIngredient(ore), count);
        this.ore = ore;
    }

    public static RecipeIngredientOre create(String ore) {
        return create(ore, 1);
    }

    public static RecipeIngredientOre create(String ore, int count) {
        if (ore.isEmpty()) return null;
        return new RecipeIngredientOre(ore, count);
    }

    public String getOre() {
        return this.ore;
    }

    @Override
    public int compareTo(IRecipeIngredient other) {
        int diff = 0;
        List<ItemStack> matchingStacks = this.getMatchingInputs();
        List<ItemStack> otherMatchingStacks = other.getMatchingInputs();

        if (other instanceof RecipeIngredientOre) {
            diff -= this.ore.compareTo(((RecipeIngredientOre) other).getOre());

            if (diff == 0) diff -= this.count - other.getCount();
        } else if (matchingStacks.isEmpty()) {
            for (ItemStack stack : otherMatchingStacks) {
                String association = OreDictUnificator.getAssociation(stack);
                if (association.isEmpty()) association = stack.getItem().getRegistryName().toString();
                diff += this.ore.compareTo(association);
            }

            if (diff == 0) diff += this.count - other.getCount();
        } else diff -= super.compareTo(other);

        return diff;
    }

    @Override
    public String toString() {
        return "RecipeIngredientOre{ore="+this.ore+",count="+this.count+"}";
    }
}
