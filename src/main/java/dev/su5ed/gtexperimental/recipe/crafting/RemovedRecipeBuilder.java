package dev.su5ed.gtexperimental.recipe.crafting;

import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.recipe.type.RecipeUtil;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.conditions.FalseCondition;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public final class RemovedRecipeBuilder {
    private static final Collection<ICondition> CONDITIONS = List.of(FalseCondition.INSTANCE);

    public static void build(Consumer<FinishedRecipe> finishedRecipeConsumer, ResourceLocation id) {
        Result finishedRecipe = new Result(id);
        finishedRecipeConsumer.accept(finishedRecipe);
    }
    
    public record Result(ResourceLocation id) implements FinishedRecipe {

        @Override
        public JsonObject serializeRecipe() {
            JsonObject json = new JsonObject();
            serializeRecipeData(json);
            return json;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.add("conditions", RecipeUtil.serializeConditions(CONDITIONS));
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return null;
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}
