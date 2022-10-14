package dev.su5ed.gregtechmod.compat;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface BaseMod {
    boolean isEnergyItem(Item item);
    
    double getEnergyCharge(ItemStack stack);
    
    double getChargeLevel(ItemStack stack);
    
    boolean canUseEnergy(ItemStack stack, double energy);
    
    boolean useEnergy(ItemStack stack, double energy, @Nullable LivingEntity user);
    
    @Nullable
    String getEnergyTooltip(ItemStack stack);
    
    void depleteStackEnergy(ItemStack stack);
    
    List<ItemStack> getChargedVariants(Item item);
    
    ItemStack getChargedStack(Item item, double charge);
    
    double chargeStack(ItemStack stack, double amount, int tier, boolean ignoreTransferLimit, boolean simulate);
    
    double dischargeStack(ItemStack stack, double amount, int tier, boolean ignoreTransferLimit, boolean externally, boolean simulate);
    
    interface Provider {
        String getModid();
        
        String mapItemName(String name);
        
        BaseMod createBaseMod();
    }
}
