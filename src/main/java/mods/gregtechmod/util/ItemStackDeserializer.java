package mods.gregtechmod.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import ic2.api.item.IC2Items;
import ic2.core.ref.IMultiItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.io.IOException;

public class ItemStackDeserializer extends JsonDeserializer<ItemStack> {
    @Override
    public ItemStack deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);

        String name = node.get("item").asText();
        if (name.contains("#")) {
            String[] parts = name.split("#");
            Item item = IC2Items.getItemAPI().getItem(parts[0].split(":")[1]);
            if (item instanceof IMultiItem) return ((IMultiItem)item).getItemStack(parts[1]);
        }
        ResourceLocation registryName = new ResourceLocation(name);
        Item item = ForgeRegistries.ITEMS.getValue(registryName);
        if (item == null) item = Item.getItemFromBlock(ForgeRegistries.BLOCKS.getValue(registryName));
        if (item == Items.AIR || item == null) throw new IllegalArgumentException("Item "+name+" not found");

        int count = node.has("count") ? node.get("count").asInt(1) : 1;
        int meta = node.has("meta") ? node.get("meta").asInt(0) : 0;

        return new ItemStack(item, count, meta);
    }
}