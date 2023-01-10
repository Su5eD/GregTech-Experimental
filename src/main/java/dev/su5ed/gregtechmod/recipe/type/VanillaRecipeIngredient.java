package dev.su5ed.gregtechmod.recipe.type;

import com.google.gson.JsonElement;
import dev.su5ed.gregtechmod.recipe.setup.ModRecipeIngredientTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class VanillaRecipeIngredient implements RecipeIngredient<ItemStack> {
    private final Ingredient ingredient;

    public VanillaRecipeIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public RecipeIngredientType<?> getType() {
        return ModRecipeIngredientTypes.ITEM;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        this.ingredient.toNetwork(buffer);
    }

    @Override
    public JsonElement toJson() {
        return this.ingredient.toJson();
    }

    @Override
    public boolean test(ItemStack stack) {
        return this.ingredient.test(stack);
    }
}
