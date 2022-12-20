package dev.su5ed.gregtechmod.compat;

import dev.su5ed.gregtechmod.util.upgrade.IC2UpgradeCapabilityProvider;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.upgrade.IUpgradeItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class IC2BaseMod implements BaseMod {
    @Override
    public boolean isEnergyItem(Item item) {
        return item instanceof IElectricItem;
    }

    @Override
    public double getEnergyCharge(ItemStack stack) {
        return ElectricItem.manager.getCharge(stack);
    }

    @Override
    public double getChargeLevel(ItemStack stack) {
        return ElectricItem.manager.getChargeLevel(stack);
    }

    @Override
    public boolean canUseEnergy(ItemStack stack, double energy) {
        return ElectricItem.manager.canUse(stack, energy);
    }

    @Override
    public boolean useEnergy(ItemStack stack, double energy, @Nullable LivingEntity user) {
        return ElectricItem.manager.use(stack, energy, user);
    }

    @Nullable
    @Override
    public String getEnergyTooltip(ItemStack stack) {
        return ElectricItem.manager.getToolTip(stack);
    }

    @Override
    public void depleteStackEnergy(ItemStack stack) {
        if (stack.getItem() instanceof IElectricItem) {
            ElectricItem.manager.discharge(stack, Double.MAX_VALUE, Integer.MAX_VALUE, true, false, false);
        }
    }

    @Override
    public List<ItemStack> getChargedVariants(Item item) {
        return Arrays.asList(getChargedStack(item, 0), getChargedStack(item, Double.MAX_VALUE));
    }

    @Override
    public ItemStack getChargedStack(Item item, double charge) {
        ItemStack stack = new ItemStack(item);
        chargeStack(stack, charge, Integer.MAX_VALUE, true, false);
        return stack;
    }

    @Override
    public double chargeStack(ItemStack stack, double amount, int tier, boolean ignoreTransferLimit, boolean simulate) {
        return ElectricItem.manager.charge(stack, amount, tier, ignoreTransferLimit, simulate);
    }

    @Override
    public double dischargeStack(ItemStack stack, double amount, int tier, boolean ignoreTransferLimit, boolean externally, boolean simulate) {
        return ElectricItem.manager.discharge(stack, amount, tier, ignoreTransferLimit, externally, simulate);
    }

    private static void onItemStackCapabilityAttach(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();
        if (stack.getItem() instanceof IUpgradeItem upgrade) {
            IC2UpgradeCapabilityProvider.IC2UpgradeAdapter.of(stack, upgrade)
                .map(adapter -> new IC2UpgradeCapabilityProvider(stack, adapter))
                .ifPresent(provider -> event.addCapability(IC2UpgradeCapabilityProvider.NAME, provider));
        }
    }

    @EventBusSubscriber
    public static class Provider implements BaseMod.Provider {
        @Override
        public String getModid() {
            return ModHandler.IC2_MODID;
        }

        @Override
        public String mapItemName(String name) {
            return name;
        }

        @Override
        public BaseMod createBaseMod() {
            return new IC2BaseMod();
        }

        @SubscribeEvent
        public static void onItemStackCapabilityAttach(AttachCapabilitiesEvent<ItemStack> event) {
            if (ModHandler.ic2Loaded) {
                IC2BaseMod.onItemStackCapabilityAttach(event);
            }
        }
    }
}
