package mods.gregtechmod.objects.items.base;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.item.ElectricItemManager;
import ic2.core.item.IPseudoDamageItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemElectricBase extends ItemBase implements IElectricItem, IPseudoDamageItem {
    protected final double maxCharge;
    protected final double transferLimit;
    protected final int tier;
    protected final double operationEnergyCost;
    protected final boolean providesEnergy;
    protected boolean hasEmptyVariant;
    protected boolean showTier = true;

    public ItemElectricBase(String name, @Nullable String description, double maxCharge, double transferLimit, int tier) {
        this(name, description, maxCharge, transferLimit, tier, 0, false);
    }

    public ItemElectricBase(String name, @Nullable String description, double maxCharge, double transferLimit, int tier, double operationEnergyCost, boolean providesEnergy) {
        super(name, description, 28);
        this.maxCharge = maxCharge;
        this.transferLimit = transferLimit;
        this.tier = tier;
        this.operationEnergyCost = operationEnergyCost;
        this.providesEnergy = providesEnergy;
        this.showDurability = false;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (this.showTier && this.tier > 0) tooltip.add("Tier: "+this.tier);
        String durability = getDurabilityInfo(stack);
        if (this.showDurability && !durability.isEmpty()) tooltip.add(durability);
        if (this.toolTip != null) {
            if (this.hasEmptyVariant && !ElectricItem.manager.canUse(stack, this.operationEnergyCost)) {
                tooltip.add("Empty. You need to recharge it.");
            }
            else tooltip.add(this.toolTip);
        }
    }

    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return this.providesEnergy;
    }

    @Override
    public double getMaxCharge(ItemStack itemStack) {
        return this.maxCharge;
    }

    @Override
    public int getTier(ItemStack itemStack) {
        return this.tier;
    }

    @Override
    public double getTransferLimit(ItemStack itemStack) {
        return this.transferLimit;
    }

    @Override
    public void setStackDamage(ItemStack stack, int damage) {
        setDamage(stack, damage);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) ElectricItemManager.addChargeVariants(this, items);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (this.hasEmptyVariant && !ElectricItem.manager.canUse(stack, this.operationEnergyCost)) {
            return "Empty " + super.getItemStackDisplayName(stack);
        }
        return super.getItemStackDisplayName(stack);
    }
}
