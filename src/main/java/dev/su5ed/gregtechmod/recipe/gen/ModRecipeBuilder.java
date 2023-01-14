package dev.su5ed.gregtechmod.recipe.gen;

import com.google.gson.JsonObject;
import dev.su5ed.gregtechmod.recipe.type.BaseRecipe;
import dev.su5ed.gregtechmod.recipe.type.RecipeUtil;
import net.minecraft.world.item.crafting.RecipeSerializer;

public abstract class ModRecipeBuilder<R extends BaseRecipe<?, ?, ? super R>> extends BaseRecipeBuilder {
    protected final R recipe;

    public ModRecipeBuilder(R recipe) {
        this.recipe = recipe;
    }

    @Override
    public void serializeRecipeData(JsonObject json) {
        super.serializeRecipeData(json);
        if (!ModRecipeBuilder.this.conditions.isEmpty()) {
            json.add("conditions", RecipeUtil.serializeConditions(ModRecipeBuilder.this.conditions));
        }
    }

    @Override
    public RecipeSerializer<?> getType() {
        return this.recipe.getSerializer();
    }
}
