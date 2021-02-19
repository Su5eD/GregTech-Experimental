package mods.gregtechmod.recipe.util.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.util.OreDictUnificator;
import mods.gregtechmod.util.ModHandler;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class ItemStackDeserializer extends JsonDeserializer<ItemStack> {
    public static final ItemStackDeserializer INSTANCE = new ItemStackDeserializer();

    @Override
    public ItemStack deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);
        return deserialize(node, getCount(node));
    }

    public ItemStack deserialize(JsonNode node, int count) {
        ItemStack ret = ItemStack.EMPTY;
        String name = node.has("item") ? node.get("item").asText() : node.asText();
        boolean logErrors = !node.has("fallback");

        if (node.has("ore")) {
            name = node.get("ore").asText();
            ret = OreDictUnificator.get(name);
            if (ret.isEmpty() && logErrors) GregTechAPI.logger.warn("Could not find an OreDict entry for "+name);
        } else if (name.contains("#")) {
            String[] parts = name.split("#");
            String[] nameParts = parts[0].split(":");
            ItemStack stack = ModHandler.getIC2ItemSafely(nameParts[1], parts[1]);
            if (stack.isEmpty()) {
                GregTechAPI.logger.warn("MultiItem " + name + " not found");
                stack = ItemStack.EMPTY;
            }

            ret = stack.copy();
        } else {
            ResourceLocation registryName = new ResourceLocation(name);
            Item item = ForgeRegistries.ITEMS.getValue(registryName);
            if (item == Items.AIR || item == null) {
                if (logErrors) GregTechAPI.logger.warn("Failed to deserialize ItemStack: Registry entry " + name + " not found");
            } else {
                int meta = node.has("meta") ? node.get("meta").asInt(0) : 0;
                ret = new ItemStack(item, 1, meta);
                if (node.has("nbt")) ret.setTagCompound(parseNBT(node.get("nbt")));
            }
        }

        if (ret.isEmpty() && node.has("fallback")) {
            JsonNode fallback = node.get("fallback");
            return deserialize(fallback, getCount(fallback));
        }

        if (!ret.isEmpty()) ret.setCount(count);
        return ret;
    }

    private static int getCount(JsonNode node) {
        return node.has("count") ? node.get("count").asInt(1) : 1;
    }

    private static NBTTagCompound parseNBT(JsonNode node) {
        NBTTagCompound tag = new NBTTagCompound();

        for (Iterator<Map.Entry<String, JsonNode>> it = node.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = it.next();
            String name = entry.getKey();
            JsonNode field = entry.getValue();
            if (field.isNumber()) {
                Number num = field.numberValue();
                if (num instanceof Integer) tag.setInteger(name, num.intValue());
                else if (num instanceof Double) tag.setDouble(name, num.doubleValue());
                else if (num instanceof Long) tag.setLong(name, num.longValue());
            } else if (field.isTextual()) tag.setString(name, field.asText());
        }

        return tag;
    }
}