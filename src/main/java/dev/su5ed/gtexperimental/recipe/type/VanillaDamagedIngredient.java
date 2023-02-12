package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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

public class VanillaDamagedIngredient extends AbstractIngredient {
    public static final ResourceLocation NAME = location("damaged_item");
    public static final IIngredientSerializer<VanillaDamagedIngredient> SERIALIZER = new SimpleIngredientSerializer<>(VanillaDamagedIngredient::new);

    public static VanillaDamagedIngredient of(ItemLike item) {
        return new VanillaDamagedIngredient(Stream.of(new ItemValue(new ItemStack(item))));
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
        return SERIALIZER;
    }

    @Override
    public JsonElement toJson() {
        JsonObject json = this.values[0].serialize();
        json.addProperty("type", CraftingHelper.getID(SERIALIZER).toString());
        return json;
    }

    @Override
    public boolean test(@Nullable ItemStack stack) {
        return super.test(stack) && stack.isDamaged();
    }
}
