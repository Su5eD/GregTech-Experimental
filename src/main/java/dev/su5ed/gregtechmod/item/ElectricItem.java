package dev.su5ed.gregtechmod.item;

import dev.su5ed.gregtechmod.compat.IC2Handler;
import dev.su5ed.gregtechmod.compat.ModHandler;
import dev.su5ed.gregtechmod.util.GtUtil;
import ic2.api.item.IElectricItem;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ElectricItem extends ResourceItem implements IElectricItem {
    protected final double maxCharge;
    protected final double transferLimit;
    protected final int energyTier;
    protected final boolean showTier;
    protected final int operationEnergyCost;
    protected final boolean providesEnergy;
    protected final boolean hasEmptyVariant;

    public ElectricItem(ElectricItemProperties properties) {
        super(properties);

        this.maxCharge = properties.maxCharge;
        this.transferLimit = properties.transferLimit < 1 ? GtUtil.calculateTransferLimit(properties.energyTier) : properties.transferLimit;
        this.energyTier = properties.energyTier;
        this.showTier = properties.showTier;
        this.operationEnergyCost = properties.operationEnergyCost;
        this.providesEnergy = properties.providesEnergy;
        this.hasEmptyVariant = properties.hasEmptyVariant;
    }

    @Override
    public boolean canProvideEnergy(ItemStack stack) {
        return this.providesEnergy;
    }

    @Override
    public double getMaxCharge(ItemStack stack) {
        return this.maxCharge;
    }

    @Override
    public int getTier(ItemStack stack) {
        return this.energyTier;
    }

    @Override
    public double getTransferLimit(ItemStack stack) {
        return this.transferLimit;
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return isEmpty(stack) ? Rarity.COMMON : super.getRarity(stack);
    }

    @Override
    public void fillItemCategory(CreativeModeTab category, NonNullList<ItemStack> items) {
        if (allowdedIn(category)) {
            items.addAll(IC2Handler.getChargedVariants(this));
        }
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        String ret = super.getDescriptionId(stack);
        if (isEmpty(stack)) {
            ret += ".empty";
        }
        return ret;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag isAdvanced) {
        GtUtil.appendEnergyHoverText(stack, components, this.energyTier, this.description.get(), this.showTier, isEmpty(stack));
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return ModHandler.getEnergyCharge(stack) < this.maxCharge;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return (int) Math.round(ModHandler.getChargeLevel(stack) * 13.0);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return Mth.hsvToRgb((float) (ModHandler.getChargeLevel(stack) / 3.0), 1.0F, 1.0F);
    }

    private boolean isEmpty(ItemStack stack) {
        return this.hasEmptyVariant && !ModHandler.canUseEnergy(stack, this.operationEnergyCost);
    }

    public static class ElectricItemProperties extends ExtendedItemProperties<ElectricItemProperties> {
        private double maxCharge;
        private double transferLimit;
        private int energyTier;
        private int operationEnergyCost;
        private boolean providesEnergy;
        private boolean hasEmptyVariant;
        private boolean showTier = true;

        public ElectricItemProperties() {}

        public ElectricItemProperties(Properties properties) {
            super(properties);
        }

        public ElectricItemProperties maxCharge(double maxCharge) {
            this.maxCharge = maxCharge;
            return this;
        }

        public ElectricItemProperties transferLimit(double transferLimit) {
            this.transferLimit = transferLimit;
            return this;
        }

        public ElectricItemProperties energyTier(int energyTier) {
            this.energyTier = energyTier;
            return this;
        }

        public ElectricItemProperties operationEnergyCost(int operationEnergyCost) {
            this.operationEnergyCost = operationEnergyCost;
            return this;
        }

        public ElectricItemProperties providesEnergy(boolean providesEnergy) {
            this.providesEnergy = providesEnergy;
            return this;
        }

        public ElectricItemProperties hasEmptyVariant(boolean hasEmptyVariant) {
            this.hasEmptyVariant = hasEmptyVariant;
            return this;
        }

        public ElectricItemProperties showTier(boolean showTier) {
            this.showTier = showTier;
            return this;
        }
    }
}
