package mods.gregtechmod.recipe.ingredient;

import com.google.common.base.MoreObjects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class RecipeIngredientItemStack extends RecipeIngredient<Ingredient> {
    public static final RecipeIngredientItemStack EMPTY = new RecipeIngredientItemStack(1);
    private static final Logger LOGGER = LogManager.getLogger();

    protected RecipeIngredientItemStack(int count, ItemStack... stacks) {
        super(Ingredient.fromStacks(stacks), count);
    }

    public static RecipeIngredientItemStack create(Item item) {
        return create(item, 1);
    }

    public static RecipeIngredientItemStack create(Item item, int count) {
        return create(item, count, 0);
    }

    public static RecipeIngredientItemStack create(Item item, int count, int meta) {
        return create(new ItemStack(item, count, meta));
    }

    public static RecipeIngredientItemStack create(ItemStack stack) {
        return create(stack, 1);
    }

    public static RecipeIngredientItemStack create(ItemStack stack, int count) {
        if (stack.isEmpty()) return EMPTY;
        return new RecipeIngredientItemStack(count, stack);
    }

    public static RecipeIngredientItemStack create(List<ItemStack> stacks, int count) {
        for (ItemStack stack : stacks) {
            if (stack.isEmpty()) {
                LOGGER.error("Tried to parse a RecipeIngredientItemStack with an empty stack among the matching stacks: " + stacks);
                return EMPTY;
            }
        }
        return new RecipeIngredientItemStack(count, stacks.toArray(new ItemStack[0]));
    }

    @Override
    public boolean isEmpty() {
        return this.getMatchingInputs().isEmpty();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("inputs", getMatchingInputs())
            .add("count", count)
            .toString();
    }
}
