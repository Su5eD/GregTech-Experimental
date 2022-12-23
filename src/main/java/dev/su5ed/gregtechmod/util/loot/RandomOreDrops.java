package dev.su5ed.gregtechmod.util.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import static dev.su5ed.gregtechmod.api.Reference.location;

public class RandomOreDrops implements LootItemCondition {
    public static final ResourceLocation NAME = location("random_ore_drops");
    public static final LootItemConditionType TYPE = new LootItemConditionType(new Serializer());

    private final int chance;

    private RandomOreDrops(int chance) {
        this.chance = chance;
    }

    public static LootItemCondition.Builder randomDrop(int chance) {
        return () -> new RandomOreDrops(chance);
    }

    @Override
    public boolean test(LootContext context) {
        ItemStack stack = context.getParamOrNull(LootContextParams.TOOL);
        if (stack != null) {
            int fortune = stack.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE) + 1;
            return context.getRandom().nextInt(Math.max(1, this.chance / fortune)) == 0;
        }
        return false;
    }

    @Override
    public LootItemConditionType getType() {
        return TYPE;
    }

    private static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<RandomOreDrops> {

        @Override
        public void serialize(JsonObject json, RandomOreDrops value, JsonSerializationContext context) {
            json.addProperty("chance", value.chance);
        }

        @Override
        public RandomOreDrops deserialize(JsonObject json, JsonDeserializationContext context) {
            int chance = GsonHelper.getAsInt(json, "chance");
            return new RandomOreDrops(chance);
        }
    }
}
