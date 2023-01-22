package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.api.recipe.RecipeOutputType;
import dev.su5ed.gtexperimental.util.GtUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
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
        json.addProperty("item", GtUtil.itemId(output));
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
        return RecipeUtil.parseItemStack(element);
    }

    @Override
    public void validate(ResourceLocation id, String name, ItemStack item) {
        if (item.isEmpty()) {
            throw new RuntimeException("Empty " + name + " item in recipe " + id);
        }
    }
}
