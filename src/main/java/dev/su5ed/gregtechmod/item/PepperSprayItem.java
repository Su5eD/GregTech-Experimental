package dev.su5ed.gregtechmod.item;

import dev.su5ed.gregtechmod.object.Miscellaneous;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class PepperSprayItem extends CraftingToolItem {

    public PepperSprayItem() {
        super(new ToolItemProperties<>()
            .attackDamage(2)
            .selfDamageOnHit(8)
            .durability(128)
            .multiDescription(3), 1, Miscellaneous.EMPTY_SPRAY_CAN.getItem());
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 1200, 2));
        target.addEffect(new MobEffectInstance(MobEffects.POISON, 120, 2));
        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 2));
        target.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 600, 2));
        return super.hurtEnemy(stack, target, attacker);
    }
}
