package dev.su5ed.gregtechmod.recipe.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.su5ed.gregtechmod.recipe.setup.ModRecipeIngredientTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class VanillaRecipeIngredient implements RecipeIngredient<ItemStack> {
    private final Ingredient ingredient;
    private final int count;
    
    public VanillaRecipeIngredient(Ingredient ingredient) {
        this(ingredient, 1);
    }

    public VanillaRecipeIngredient(Ingredient ingredient, int count) {
        this.ingredient = ingredient;
        this.count = count;
    }

    @Override
    public RecipeIngredientType<?> getType() {
        return ModRecipeIngredientTypes.ITEM;
    }

    @Override
    public int getCount() {
        return this.count;
    }

    @Override
    public boolean isEmpty() {
        return this.ingredient.isEmpty() || this.count <= 0;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        this.ingredient.toNetwork(buffer);
    }

    @Override
    public JsonElement toJson() {
        JsonObject json = new JsonObject();
        json.add("value", this.ingredient.toJson());
        if (this.count > 1) {
            json.addProperty("count", this.count);
        }
        return json;
    }

    @Override
    public boolean test(ItemStack stack) {
        return this.ingredient.test(stack) && stack.getCount() >= getCount();
    }
}
