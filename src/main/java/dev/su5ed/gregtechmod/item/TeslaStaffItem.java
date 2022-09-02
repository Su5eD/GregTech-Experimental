package dev.su5ed.gregtechmod.item;

import dev.su5ed.gregtechmod.GregTechConfig;
import dev.su5ed.gregtechmod.compat.ModHandler;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import one.util.streamex.StreamEx;

public class TeslaStaffItem extends ElectricItem {

    public TeslaStaffItem() {
        super(new ElectricItemProperties()
            .maxCharge(10000000)
            .transferLimit(8192)
            .energyTier(4)
            .operationEnergyCost(9000000)
            .showTier(false)
            .rarity(Rarity.EPIC)
            .autoDescription());
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!target.level.isClientSide && target instanceof Player player && ModHandler.canUseEnergy(stack, this.operationEnergyCost)) {
            ModHandler.useEnergy(stack, this.operationEnergyCost, attacker);
            
            if (GregTechConfig.COMMON.teslaStaffDestroysArmor.get()) {
                StreamEx.of(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET)
                    .forEach(slot -> {
                        ItemStack armor = player.getItemBySlot(slot);
                        if (ModHandler.isEnergyItem(armor)) {
                            player.setItemSlot(slot, ItemStack.EMPTY);
                            player.broadcastBreakEvent(slot);
                        }
                    });
            } else {
                player.getInventory().armor.forEach(ModHandler::depleteStackEnergy);
            }
            
            target.hurt(DamageSource.MAGIC, 19);
            attacker.hurt(DamageSource.MAGIC, 19);
            return true;
        }
        return super.hurtEnemy(stack, target, attacker);
    }
}
