package dev.su5ed.gregtechmod.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev.su5ed.gregtechmod.util.GtUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ToolItem extends ResourceItem {
    private final float attackDamage;
    private final Multimap<Attribute, AttributeModifier> attributeModifiers;
    private final List<String> effectiveAganist;

    public ToolItem(ToolItemProperties properties) {
        super(properties);

        this.attackDamage = properties.attackDamage - 1;
        this.attributeModifiers = ImmutableMultimap.of(
            Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.attackDamage, AttributeModifier.Operation.ADDITION),
            Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2, AttributeModifier.Operation.ADDITION)
        );
        this.effectiveAganist = Collections.unmodifiableList(properties.effectiveAganist);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(slot, stack);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, entity -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        ResourceLocation name = target.getType().getRegistryName();
        if (this.effectiveAganist.contains(name.toString())) {
            GtUtil.damageEntity(target, attacker, this.attackDamage + 1);
        }
        return true;
    }

    public static class ToolItemProperties extends ExtendedItemProperties<ToolItemProperties> {
        private float attackDamage;
        private final List<String> effectiveAganist = new ArrayList<>();

        public ToolItemProperties(Properties properties) {
            super(properties);
        }

        public ToolItemProperties attackDamage(float attackDamage) {
            this.attackDamage = attackDamage;
            return this;
        }
        
        public ToolItemProperties effectiveAganist(String... entityNames) {
            this.effectiveAganist.addAll(Arrays.asList(entityNames));
            return this;
        }
    }
}
