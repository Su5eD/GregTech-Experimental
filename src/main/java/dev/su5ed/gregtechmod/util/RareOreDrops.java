package dev.su5ed.gregtechmod.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import static dev.su5ed.gregtechmod.api.util.Reference.location;

public class RareOreDrops extends LootItemConditionalFunction {
    public static final ResourceLocation NAME = location("rare_ore_drops");
    public static final LootItemFunctionType TYPE = new LootItemFunctionType(new Serializer());

    public RareOreDrops() {
        this(new LootItemCondition[0]);
    }
    
    protected RareOreDrops(LootItemCondition[] pConditions) {
        super(pConditions);
    }

    @Override
    protected ItemStack run(ItemStack pStack, LootContext pContext) {
        ItemStack stack = pContext.getParamOrNull(LootContextParams.TOOL);
        if (stack != null) {
            int fortune = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, stack);
            int count = stack.getCount() + pContext.getRandom().nextInt(1 + fortune / 2);
            pStack.setCount(count);
        }
        return pStack;
    }

    @Override
    public LootItemFunctionType getType() {
        return TYPE;
    }

    private static class Serializer extends LootItemConditionalFunction.Serializer<RareOreDrops> {

        @Override
        public RareOreDrops deserialize(JsonObject pObject, JsonDeserializationContext pDeserializationContext, LootItemCondition[] pConditions) {
            return new RareOreDrops(pConditions);
        }
    }
}
