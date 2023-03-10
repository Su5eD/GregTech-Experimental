package dev.su5ed.gtexperimental.recipe.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.compat.ModHandler;
import dev.su5ed.gtexperimental.recipe.type.RecipeUtil;
import dev.su5ed.gtexperimental.util.ElectricCraftingTool;
import dev.su5ed.gtexperimental.util.GtUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.AbstractIngredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

import static dev.su5ed.gtexperimental.util.GtUtil.location;

public class ToolCraftingIngredient extends AbstractIngredient {
    private final int damage;

    public static ToolCraftingIngredient of(ItemLike item, int damage) {
        return of(new ItemStack(item, 1), damage);
    }

    public static ToolCraftingIngredient of(ItemStack stack, int damage) {
        return new ToolCraftingIngredient(Stream.of(new ItemValue(stack)), damage);
    }

    public static ToolCraftingIngredient of(TagKey<Item> tag, int damage) {
        return new ToolCraftingIngredient(Stream.of(new TagValue(tag)), damage);
    }

    protected ToolCraftingIngredient(Stream<? extends Value> values, int damage) {
        super(values);
        this.damage = damage;
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
        JsonObject json = new JsonObject();
        json.addProperty("type", CraftingHelper.getID(Serializer.INSTANCE).toString());
        json.add("value", serializeValues());
        json.addProperty("damage", this.damage);
        return json;
    }

    public JsonElement serializeValues() {
        if (this.values.length == 1) {
            return this.values[0].serialize();
        }

        JsonArray array = new JsonArray();
        for (Ingredient.Value value : this.values) {
            array.add(value.serialize());
        }
        return array;
    }

    public ItemStack damageItem(ItemStack stack) {
        if (ModHandler.isEnergyItem(stack)) {
            ModHandler.useEnergy(stack, this.damage * 1000, null);
            return stack;
        }
        else {
            return stack.hurt(this.damage, GtUtil.RANDOM, null) ? ItemStack.EMPTY : stack;
        }
    }

    @Override
    public boolean test(@Nullable ItemStack stack) {
        return super.test(stack) && (stack.getItem() instanceof ElectricCraftingTool electricCraftingTool && electricCraftingTool.canUseInCrafting(stack)
            || ModHandler.isEnergyItem(stack) && ModHandler.canUseEnergy(stack, this.damage * 1000)
            || stack.getMaxDamage() - stack.getDamageValue() >= this.damage);
    }

    public static class Serializer implements IIngredientSerializer<ToolCraftingIngredient> {
        public static final ResourceLocation NAME = location("tool");
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public ToolCraftingIngredient parse(FriendlyByteBuf buffer) {
            Stream<? extends Value> values = RecipeUtil.ingredientValuesFromNetwork(buffer);
            int damage = buffer.readInt();
            return new ToolCraftingIngredient(values, damage);
        }

        @Override
        public ToolCraftingIngredient parse(JsonObject json) {
            JsonElement valuesJson = json.get("value");
            Stream<? extends Value> values = RecipeUtil.ingredientFromJson(valuesJson);
            int damage = GsonHelper.getAsInt(json, "damage");
            return new ToolCraftingIngredient(values, damage);
        }

        @Override
        public void write(FriendlyByteBuf buffer, ToolCraftingIngredient ingredient) {
            buffer.writeCollection(List.of(ingredient.getItems()), FriendlyByteBuf::writeItem);
        }
    }
}
