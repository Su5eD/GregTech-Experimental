package mods.gregtechmod.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.init.BlockItems;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

import java.util.Random;

public class LootFunctionWriteBook extends LootFunction {
    private final String name;

    protected LootFunctionWriteBook(LootCondition[] conditionsIn, String name) {
        super(conditionsIn);
        this.name = name;
    }

    @Override
    public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
        ItemStack book = BlockItems.Books.valueOf(this.name).getInstance();
        stack.setTagCompound(book.getTagCompound());
        return stack;
    }

    public static class Serializer extends LootFunction.Serializer<LootFunctionWriteBook> {
        public Serializer()
        {
            super(new ResourceLocation(Reference.MODID, "write_book"), LootFunctionWriteBook.class);
        }

        public void serialize(JsonObject object, LootFunctionWriteBook functionClazz, JsonSerializationContext serializationContext)
        {
            object.add("name", serializationContext.serialize(functionClazz.name));
        }

        public LootFunctionWriteBook deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootCondition[] conditionsIn)
        {
            return new LootFunctionWriteBook(conditionsIn, JsonUtils.deserializeClass(object, "name", deserializationContext, String.class));
        }
    }
}
