package mods.gregtechmod.objects.items.base;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.item.ElectricItemManager;
import ic2.core.item.IPseudoDamageItem;
import ic2.core.item.tool.ToolClass;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class ItemToolElectricBase extends ItemToolBase implements IElectricItem, IPseudoDamageItem {
    protected final double maxCharge;
    protected final double transferLimit;
    protected final int tier;
    protected boolean showTier = true;
    protected final double operationEnergyCost;
    protected final boolean providesEnergy;
    protected boolean hasEmptyVariant = false;

    public ItemToolElectricBase(String name, @Nullable String description, double maxCharge, double transferLimit, int tier, double operationEnergyCost, Set<ToolClass> toolClasses) {
        this(name, description, 28, 1, maxCharge, transferLimit, tier, operationEnergyCost, false, 0, toolClasses);
    }

    public ItemToolElectricBase(String name, @Nullable String description, double maxCharge, double transferLimit, int tier, double operationEnergyCost, int harvestLevel, Set<ToolClass> toolClasses) {
        this(name, description, 28, 1, maxCharge, transferLimit, tier, operationEnergyCost, false, harvestLevel, toolClasses);
    }

    public ItemToolElectricBase(String name, @Nullable String description, float attackDamage, double maxCharge, int tier, double operationEnergyCost, int harvestLevel, Set<ToolClass> toolClasses) {
        this(name, description, 28, attackDamage, maxCharge, GtUtil.getTransferLimit(tier), tier, operationEnergyCost, false, harvestLevel, toolClasses);
    }

    public ItemToolElectricBase(String name, @Nullable String description, int damage, float attackDamage, double maxCharge, int tier, double operationEnergyCost, boolean providesEnergy, int harvestLevel, Set<ToolClass> toolClasses) {
        this(name, description, damage, attackDamage, maxCharge, GtUtil.getTransferLimit(tier), tier, operationEnergyCost, providesEnergy, harvestLevel, toolClasses);
    }

    public ItemToolElectricBase(String name, @Nullable String description, int damage, float attackDamage, double maxCharge, double transferLimit, int tier, double operationEnergyCost, boolean providesEnergy, int harvestLevel, Set<ToolClass> toolClasses) {
        super(name, description, damage, attackDamage, 0, harvestLevel, toolClasses);
        this.maxCharge = maxCharge;
        this.transferLimit = transferLimit;
        this.tier = tier;
        this.operationEnergyCost = operationEnergyCost;
        this.providesEnergy = providesEnergy;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (this.showTier && this.tier > 0) tooltip.add("Tier: "+this.tier);
        String durability = getDurabilityInfo(stack);
        if (!durability.isEmpty()) tooltip.add(durability);
        if (this.toolTip != null) {
            if (this.hasEmptyVariant && !ElectricItem.manager.canUse(stack, this.operationEnergyCost)) {
                tooltip.add("Empty. You need to recharge it.");
            }
            else tooltip.add(this.toolTip);
        }
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        return !ElectricItem.manager.canUse(stack, this.operationEnergyCost) ? 1 : super.getDestroySpeed(stack, state);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (this.toolClasses.contains(ToolClass.Sword) && ElectricItem.manager.use(stack, this.operationEnergyCost, attacker)) return true;
        return super.hitEntity(stack, target, attacker);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase player) {
        if (!world.isRemote && state.getBlockHardness(world, pos) != 0) ElectricItem.manager.use(stack, this.operationEnergyCost, player);
        return true;
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
