package mods.gregtechmod.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import ic2.api.item.IC2Items;
import mods.gregtechmod.api.GregTechAPI;
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
        return deserialize(parser.getCodec().readTree(parser));
    }

    public ItemStack deserialize(JsonNode node) {
        String name = node.get("item").asText();
        int count = node.has("count") ? node.get("count").asInt(1) : 1;
        int meta = node.has("meta") ? node.get("meta").asInt(0) : 0;

        if (name.contains("#")) {
            String[] parts = name.split("#");
            String[] nameParts = parts[0].split(":");
            ItemStack stack;
            try {
                stack = IC2Items.getItem(nameParts[1], parts[1]);
            } catch (Throwable throwable) {
                stack = ItemStack.EMPTY;
            }
            if (stack == null || stack.isEmpty()) {
                GregTechAPI.logger.error("MultiItem " + name + " not found");
                return ItemStack.EMPTY;
            }
            stack.setCount(count);
            return stack;
        }

        ResourceLocation registryName = new ResourceLocation(name);
        Item item = ForgeRegistries.ITEMS.getValue(registryName);
        if (item == Items.AIR || item == null) item = Item.getItemFromBlock(ForgeRegistries.BLOCKS.getValue(registryName));
        if (item == Items.AIR) {
            GregTechAPI.logger.error("Failed to deserialize ItemStack: Registry entry " + name + " not found");
            return ItemStack.EMPTY;
        }

        return new ItemStack(item, count, meta);
    }
}