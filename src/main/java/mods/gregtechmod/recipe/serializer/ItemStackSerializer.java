package mods.gregtechmod.recipe.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import ic2.core.ref.IMultiItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.io.IOException;

public class ItemStackSerializer extends JsonSerializer<ItemStack> {
    public static final ItemStackSerializer INSTANCE = new ItemStackSerializer();

    @Override
    public void serialize(ItemStack value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        serialize(value, gen, value.getCount());
        gen.writeEndObject();
    }

    public void serialize(ItemStack value, JsonGenerator gen, int count) throws IOException {
        Item item = value.getItem();
        String name = item.getRegistryName().toString();
        boolean writeMeta = true;
        if (item instanceof IMultiItem<?>) {
            String variant = ((IMultiItem<?>) item).getVariant(value);
            if (variant != null) {
                name += "#" + variant;
                writeMeta = false;
            }
        }

        gen.writeStringField("item", name);

        if (writeMeta) {
            int meta = value.getMetadata();
            if (meta > 0) gen.writeNumberField("meta", meta);
        }

        if (count > 1) gen.writeNumberField("count", count);
    }
}
