package dev.su5ed.gregtechmod.util.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import static dev.su5ed.gregtechmod.api.Reference.location;

public class FortuneLootFunction implements LootItemFunction {
    public static final ResourceLocation NAME = location("fortune");
    public static final LootItemFunctionType TYPE = new LootItemFunctionType(new Serializer());

    protected final float fortuneMultiplier;
    protected final int countMultiplier;

    private FortuneLootFunction(float fortuneMultiplier, int countMultiplier) {
        this.fortuneMultiplier = fortuneMultiplier;
        this.countMultiplier = countMultiplier;
    }
    
    public static Builder bonusDrop(int countMultiplier) {
        return () -> new FortuneLootFunction(1, countMultiplier);
    }
    
    public static Builder rareOreDrop() {
        return () -> new FortuneLootFunction(0.5f, 1);
    }

    @Override
    public LootItemFunctionType getType() {
        return TYPE;
    }

    @Override
    public ItemStack apply(ItemStack stack, LootContext context) {
        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
        if (tool != null) {
            int fortune = (int) (tool.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE) * this.fortuneMultiplier);
            int count = this.countMultiplier * context.getRandom().nextInt(1 + fortune);
            stack.grow(count);
        }
        return stack;
    }

    private static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<FortuneLootFunction> {
        @Override
        public void serialize(JsonObject json, FortuneLootFunction value, JsonSerializationContext context) {
            if (value.fortuneMultiplier != 1) {
                json.addProperty("fortune_multiplier", value.fortuneMultiplier);
            }
            if (value.countMultiplier != 1) {
                json.addProperty("count_multiplier", value.countMultiplier);
            }
        }

        @Override
        public FortuneLootFunction deserialize(JsonObject json, JsonDeserializationContext context) {
            float fortuneMultiplier = json.has("fortune_multiplier") ? GsonHelper.getAsFloat(json, "fortune_multiplier") : 1;
            int countMultiplier = json.has("count_multiplier") ? GsonHelper.getAsInt(json, "count_multiplier") : 1;
            return new FortuneLootFunction(fortuneMultiplier, countMultiplier);
        }
    }
}
