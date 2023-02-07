package dev.su5ed.gtexperimental.compat;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.type.VanillaRecipeIngredient;
import ic2.api.reactor.IReactorComponent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.AbstractIngredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;

import javax.annotation.Nullable;
import java.util.stream.Stream;

import static dev.su5ed.gtexperimental.api.Reference.location;

public class DamagedIC2ReactorComponentIngredient extends AbstractIngredient {

    public DamagedIC2ReactorComponentIngredient(ItemStack stack) {
        super(Stream.of(new ItemValue(stack)));
    }

    @Override
    public boolean isSimple() {
        return true;
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public JsonElement toJson() {
        JsonObject json = this.values[0].serialize();
        json.addProperty("type", CraftingHelper.getID(Serializer.INSTANCE).toString());
        return json;
    }

    @Override
    public boolean test(@Nullable ItemStack stack) {
        if (stack.getItem() instanceof IReactorComponent component) {
            boolean hasStoredHeat = false;
            try {
                hasStoredHeat = component.getCurrentHeat(stack, null, 0, 0) > 0;
            } catch (Throwable ignored) {

            }
            return hasStoredHeat && super.test(stack);
        }
        return false;
    }

    public static RecipeIngredient<ItemStack> recipeIngredient(ItemLike item) {
        return new VanillaRecipeIngredient(new DamagedIC2ReactorComponentIngredient(new ItemStack(item)));
    }

    public static class Serializer implements IIngredientSerializer<DamagedIC2ReactorComponentIngredient> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation NAME = location("damaged_ic2_reactor_component");

        @Override
        public DamagedIC2ReactorComponentIngredient parse(FriendlyByteBuf buffer) {
            return (DamagedIC2ReactorComponentIngredient) Ingredient.fromNetwork(buffer);
        }

        @Override
        public DamagedIC2ReactorComponentIngredient parse(JsonObject json) {
            return (DamagedIC2ReactorComponentIngredient) Ingredient.fromJson(json);
        }

        @Override
        public void write(FriendlyByteBuf buffer, DamagedIC2ReactorComponentIngredient ingredient) {
            ingredient.toNetwork(buffer);
        }
    }
}
