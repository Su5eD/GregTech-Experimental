package dev.su5ed.gregtechmod.util.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;

import static dev.su5ed.gregtechmod.api.util.Reference.location;

public class LocationLootItem extends LootPoolSingletonContainer {
    public static final ResourceLocation NAME = location("item");
    public static final LootPoolEntryType TYPE = new LootPoolEntryType(new Serializer());

    private final ResourceLocation item;

    private LocationLootItem(ResourceLocation item, int weight, int quality, LootItemCondition[] conditions, LootItemFunction[] functions) {
        super(weight, quality, conditions, functions);
        this.item = item;
    }

    @Override
    public LootPoolEntryType getType() {
        return TYPE;
    }

    @Override
    public void createItemStack(Consumer<ItemStack> consumer, LootContext context) {
        Item item = ForgeRegistries.ITEMS.getValue(this.item);
        consumer.accept(new ItemStack(item));
    }

    public static LootPoolSingletonContainer.Builder<?> lootTableItem(ResourceLocation item) {
        return simpleBuilder((weight, quality, conditions, functions) -> new LocationLootItem(item, weight, quality, conditions, functions));
    }

    public static class Serializer extends LootPoolSingletonContainer.Serializer<LocationLootItem> {
        @Override
        public void serializeCustom(JsonObject json, LocationLootItem value, JsonSerializationContext context) {
            super.serializeCustom(json, value, context);

            json.addProperty("name", value.item.toString());
        }

        @Override
        protected LocationLootItem deserialize(JsonObject json, JsonDeserializationContext context, int weight, int quality, LootItemCondition[] conditions, LootItemFunction[] functions) {
            ResourceLocation item = new ResourceLocation(json.get("name").getAsString());
            return new LocationLootItem(item, weight, quality, conditions, functions);
        }
    }
}
