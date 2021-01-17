package mods.gregtechmod.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import ic2.api.item.IC2Items;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.util.OreDictUnificator;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.io.IOException;

public class ItemStackDeserializer extends JsonDeserializer<ItemStack> {
    public static final ItemStackDeserializer INSTANCE = new ItemStackDeserializer();

    @Override
    public ItemStack deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);
        int count = node.has("count") ? node.get("count").asInt(1) : 1;
        return deserialize(node, count);
    }

    public ItemStack deserialize(JsonNode node, int count) {
        ItemStack ret = ItemStack.EMPTY;
        String name = node.has("item") ? node.get("item").asText() : node.asText();

        if (node.has("ore")) {
            name = node.get("ore").asText();
            ret = OreDictUnificator.get(name);
            if (ret.isEmpty()) GregTechAPI.logger.error("Could not find an OreDict entry for "+name);
        } else if (name.contains("#")) {
            String[] parts = name.split("#");
            String[] nameParts = parts[0].split(":");
            ItemStack stack = ItemStack.EMPTY;
            try {
                stack = IC2Items.getItem(nameParts[1], parts[1]);
            } catch (Throwable ignored) {}

            if (stack.isEmpty()) GregTechAPI.logger.error("MultiItem " + name + " not found");

            ret = stack;
        } else {
            ResourceLocation registryName = new ResourceLocation(name);
            Item item = ForgeRegistries.ITEMS.getValue(registryName);
            if (item == Items.AIR || item == null) {
                GregTechAPI.logger.error("Failed to deserialize ItemStack: Registry entry " + name + " not found");
            } else {
                int meta = node.has("meta") ? node.get("meta").asInt(0) : 0;
                ret = new ItemStack(item, 1, meta);
            }
        }

        if (ret.isEmpty() && node.has("fallback")) return deserialize(node.get("fallback"), 1);

        ret.setCount(count);
        return ret;
    }
}