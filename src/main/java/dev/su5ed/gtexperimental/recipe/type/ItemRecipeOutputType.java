package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRecipeOutputType implements RecipeOutputType<ItemStack> {
    @Override
    public void toNetwork(FriendlyByteBuf buffer, ItemStack output) {
        buffer.writeItem(output);
    }

    @Override
    public JsonObject toJson(ItemStack output) {
        JsonObject json = new JsonObject();
        json.addProperty("item", ForgeRegistries.ITEMS.getKey(output.getItem()).toString());
        int count = output.getCount();
        if (count > 1) {
            json.addProperty("count", count);
        }
        return json;
    }

    @Override
    public ItemStack fromNetwork(FriendlyByteBuf buffer) {
        return buffer.readItem();
    }

    @Override
    public ItemStack fromJson(JsonElement element) {
        return RecipeIngredient.parseItemStack(element);
    }
}
