package mods.gregtechmod.recipe.ingredient;

import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.recipe.ingredient.RecipeIngredientType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RecipeIngredientItemStack extends RecipeIngredientBase<Ingredient> {

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
        return create(stack, stack.getCount());
    }

    public static RecipeIngredientItemStack create(ItemStack stack, int count) {
        if (stack.isEmpty()) return null;
        return new RecipeIngredientItemStack(count, stack);
    }

    public static RecipeIngredientItemStack create(List<ItemStack> stacks, int count) {
        for (ItemStack stack : stacks) {
            if (stack.isEmpty()) {
                List<String> stacksToString = stacks.stream()
                        .map(Objects::toString)
                        .collect(Collectors.toList());
                GregTechAPI.logger.error("Tried to parse a RecipeIngredientItemStack with an empty stack among the matching stacks: ["+String.join(",", stacksToString)+"], removing it...");
                stacks.remove(stack);
            }
        }
        return new RecipeIngredientItemStack(count, stacks.toArray(new ItemStack[0]));
    }

    @Override
    public String toString() {
        List<String> stacks = this.getMatchingInputs().stream()
                .map(Objects::toString)
                .collect(Collectors.toList());
        return "RecipeIngredientItemStack{inputs=["+String.join(",", stacks)+"],count="+this.count+"}";
    }

    @Override
    public RecipeIngredientType getType() {
        return RecipeIngredientType.ITEM;
    }
}
