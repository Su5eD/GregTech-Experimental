package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.AbstractIngredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;

import javax.annotation.Nullable;
import java.util.stream.Stream;

import static dev.su5ed.gtexperimental.api.Reference.location;

public class VanillaDamagedIngredient extends AbstractIngredient {

    public VanillaDamagedIngredient(ItemStack stack) {
        this(Stream.of(new ItemValue(stack)));
    }

    protected VanillaDamagedIngredient(Stream<? extends Value> values) {
        super(values);
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
        return super.test(stack) && stack.isDamaged();
    }

    public static class Serializer implements IIngredientSerializer<VanillaDamagedIngredient> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation NAME = location("damaged_item");

        @Override
        public VanillaDamagedIngredient parse(FriendlyByteBuf buffer) {
            return (VanillaDamagedIngredient) Ingredient.fromNetwork(buffer);
        }

        @Override
        public VanillaDamagedIngredient parse(JsonObject json) {
            return (VanillaDamagedIngredient) Ingredient.fromJson(json);
        }

        @Override
        public void write(FriendlyByteBuf buffer, VanillaDamagedIngredient ingredient) {
            ingredient.toNetwork(buffer);
        }
    }
}
