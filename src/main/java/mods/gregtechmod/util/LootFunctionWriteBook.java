package mods.gregtechmod.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.BlockItems;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

import java.util.Locale;
import java.util.Random;

public class LootFunctionWriteBook extends LootFunction {
    private final String name;

    private LootFunctionWriteBook(LootCondition[] conditions, String name) {
        super(conditions);
        this.name = name;
    }

    @Override
    public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
        ItemStack book = BlockItems.Book.valueOf(this.name.toUpperCase(Locale.ROOT)).getInstance();
        stack.setTagCompound(book.getTagCompound());
        return stack;
    }

    public static class Serializer extends LootFunction.Serializer<LootFunctionWriteBook> {
        public Serializer() {
            super(new ResourceLocation(Reference.MODID, "write_book"), LootFunctionWriteBook.class);
        }

        @Override
        public void serialize(JsonObject object, LootFunctionWriteBook function, JsonSerializationContext serializationContext) {
            object.add("name", serializationContext.serialize(function.name));
        }

        @Override
        public LootFunctionWriteBook deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootCondition[] conditions) {
            return new LootFunctionWriteBook(conditions, JsonUtils.deserializeClass(object, "name", deserializationContext, String.class));
        }
    }
}
