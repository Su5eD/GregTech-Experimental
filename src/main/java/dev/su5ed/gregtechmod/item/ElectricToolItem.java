package dev.su5ed.gregtechmod.item;

import dev.su5ed.gregtechmod.compat.ModHandler;
import dev.su5ed.gregtechmod.util.GtUtil;
import ic2.api.item.IElectricItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ElectricToolItem extends ToolItem implements IElectricItem {
    protected final double maxCharge;
    protected final double transferLimit;
    protected final int energyTier;
    protected final boolean showTier;
    protected final int operationEnergyCost;
    protected final boolean providesEnergy;
    protected final boolean hasEmptyVariant;

    public ElectricToolItem(ElectricToolItemProperties properties) {
        super(properties.stacksTo(1));
        
        this.maxCharge = properties.maxCharge;
        this.transferLimit = properties.transferLimit < 1 ? GtUtil.calculateTransferLimit(properties.energyTier) : properties.transferLimit;
        this.energyTier = properties.energyTier;
        this.showTier = properties.showTier;
        this.operationEnergyCost = properties.operationEnergyCost;
        this.providesEnergy = properties.providesEnergy;
        this.hasEmptyVariant = properties.hasEmptyVariant;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return !ModHandler.canUseEnergy(stack, this.operationEnergyCost) ? 1 : super.getDestroySpeed(stack, state);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return canPerformAction(stack, ToolActions.SWORD_SWEEP) && ModHandler.useEnergy(stack, this.operationEnergyCost, attacker) || super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        if (!level.isClientSide && state.getDestroySpeed(level, pos) != 0) {
            ModHandler.useEnergy(stack, this.operationEnergyCost, miningEntity);
        }
        return true;
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
        if (allowedIn(category)) {
            items.addAll(ModHandler.getChargedVariants(this));
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
        GtUtil.appendEnergyHoverText(components, this.energyTier, this.description.get(), this.showTier, isEmpty(stack));
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
        return Mth.hsvToRgb((float) (ModHandler.getChargeLevel(stack) / 3.0), 1, 1);
    }

    private boolean isEmpty(ItemStack stack) {
        return this.hasEmptyVariant && !ModHandler.canUseEnergy(stack, this.operationEnergyCost);
    }
    
    public static class ElectricToolItemProperties extends ToolItemProperties<ElectricToolItemProperties> {
        private double maxCharge;
        private double transferLimit;
        private int energyTier;
        private int operationEnergyCost;
        private boolean providesEnergy;
        private boolean hasEmptyVariant;
        private boolean showTier = true;

        public ElectricToolItemProperties() {}

        public ElectricToolItemProperties(Properties properties) {
            super(properties);
        }
        
        public ElectricToolItemProperties maxCharge(double maxCharge) {
            this.maxCharge = maxCharge;
            return this;
        }

        public ElectricToolItemProperties transferLimit(double transferLimit) {
            this.transferLimit = transferLimit;
            return this;
        }

        public ElectricToolItemProperties energyTier(int energyTier) {
            this.energyTier = energyTier;
            return this;
        }

        public ElectricToolItemProperties operationEnergyCost(int operationEnergyCost) {
            this.operationEnergyCost = operationEnergyCost;
            return this;
        }

        public ElectricToolItemProperties providesEnergy(boolean providesEnergy) {
            this.providesEnergy = providesEnergy;
            return this;
        }

        public ElectricToolItemProperties hasEmptyVariant(boolean hasEmptyVariant) {
            this.hasEmptyVariant = hasEmptyVariant;
            return this;
        }

        public ElectricToolItemProperties showTier(boolean showTier) {
            this.showTier = showTier;
            return this;
        }
    }
}
