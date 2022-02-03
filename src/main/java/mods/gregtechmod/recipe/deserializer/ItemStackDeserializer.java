package mods.gregtechmod.recipe.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ic2.core.block.BlockTileEntity;
import ic2.core.block.TeBlockRegistry;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.util.OptionalItemStack;
import mods.gregtechmod.util.OreDictUnificator;
import mods.gregtechmod.util.ProfileDelegate;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

import java.io.IOException;

public class ItemStackDeserializer extends JsonDeserializer<ItemStack> {
    public static final ItemStackDeserializer INSTANCE = new ItemStackDeserializer();

    @Override
    public ItemStack deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);
        return deserialize(node, getCount(node));
    }

    public ItemStack deserialize(JsonNode node, int count) {
        ItemStack ret;
        String name;
        if (node.has("item")) name = node.get("item").asText();
        else if (node.has("damaged")) name = node.get("damaged").asText();
        else name = node.asText();

        boolean logErrors = !node.has("fallback");

        if (node.has("ore")) {
            String ore = node.get("ore").asText();
            ret = OreDictUnificator.get(ore);
            if (ret.isEmpty() && logErrors) throw new RuntimeException("Could not find an OreDict entry for " + ore);
        }
        else if (name.contains("#")) {
            String[] parts = name.split("#");
            ResourceLocation location = new ResourceLocation(parts[0]);

            ret = OptionalItemStack.either(() -> getTEBlock(location, parts[1]), () -> ModHandler.getMultiItem(location, parts[1]))
                .orElseThrow(() -> new RuntimeException("MultiItem " + parts[1] + " not found"));
        }
        else if (node.has("cell")) {
            String fluid = node.get("cell").asText();
            ret = ProfileDelegate.getCell(fluid);
            if (ret == null) throw new RuntimeException("Fluid " + fluid + " not found");
        }
        else {
            ResourceLocation registryName = new ResourceLocation(name);
            Item item = ForgeRegistries.ITEMS.getValue(registryName);

            if (item == Items.AIR || item == null) {
                ret = ItemStack.EMPTY;
                if (logErrors) throw new RuntimeException("Failed to deserialize ItemStack: Registry entry " + name + " not found");
            }
            else {
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

        EntryStream.of(node.fields())
            .forKeyValue((name, field) -> {
                if (field.isInt()) tag.setInteger(name, field.asInt());
                else if (field.isDouble()) tag.setDouble(name, field.asDouble());
                else if (field.isLong()) tag.setLong(name, field.asLong());
                else if (field.isTextual()) tag.setString(name, field.asText());
                else if (field.isArray()) {
                    NBTTagList list = StreamEx.of(field)
                        .select(ObjectNode.class)
                        .map(ItemStackDeserializer::parseNBT)
                        .collect(NBTTagList::new, NBTTagList::appendTag, (left, right) -> right.forEach(left::appendTag));
                    tag.setTag(name, list);
                }
            });

        return tag;
    }

    private static OptionalItemStack getTEBlock(ResourceLocation location, String name) {
        BlockTileEntity blockTe = TeBlockRegistry.get(location);

        if (blockTe != null) {
            ItemStack stack = blockTe.getItemStack(name);
            return OptionalItemStack.of(stack);
        }

        return OptionalItemStack.EMPTY;
    }
}
