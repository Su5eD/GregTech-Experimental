package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.api.recipe.RecipeOutputType;
import dev.su5ed.gtexperimental.util.GtUtil;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ItemRecipeOutputType implements RecipeOutputType<ItemStack> {
    @Override
    public void toNetwork(FriendlyByteBuf buffer, ItemStack value) {
        buffer.writeItem(value);
    }

    @Override
    public JsonElement toJson(ItemStack value) {
        JsonObject json = new JsonObject();
        json.addProperty("item", GtUtil.itemId(value));
        int count = value.getCount();
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
    public ItemStack fromJson(JsonElement json) {
        return RecipeUtil.parseItemStack(json);
    }

    @Override
    public Tag toNBT(ItemStack value) {
        return ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, value).getOrThrow(false, s -> {});
    }

    @Override
    public ItemStack fromNBT(Tag tag) {
        return ItemStack.CODEC.decode(NbtOps.INSTANCE, tag)
            .getOrThrow(false, s -> {})
            .getFirst();
    }

    @Override
    public ItemStack copy(ItemStack value) {
        return value.copy();
    }

    @Override
    public void validate(ResourceLocation id, String name, ItemStack value, boolean allowEmpty) {
        if (value == null) {
            throw new RuntimeException("Null " + name + " item in recipe " + id);
        }
        else if (!allowEmpty && value.isEmpty()) {
            throw new RuntimeException("Empty " + name + " item in recipe " + id);
        }
    }

    @Override
    public RecipeOutputType<List<ItemStack>> listOf(int count) {
        return new ListRecipeOutputType<>(this, count);
    }
}
