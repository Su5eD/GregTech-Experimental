package mods.gregtechmod.objects.items.base;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.item.ElectricItemManager;
import ic2.core.item.IPseudoDamageItem;
import ic2.core.item.tool.ToolClass;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.JavaUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class ItemToolElectricBase extends ItemToolBase implements IElectricItem, IPseudoDamageItem {
    protected final double maxCharge;
    protected final double transferLimit;
    protected final int tier;
    protected boolean showTier = true;
    protected final double operationEnergyCost;
    protected final boolean providesEnergy;
    protected boolean hasEmptyVariant = false;

    public ItemToolElectricBase(String name, double maxCharge, double transferLimit, int tier, double operationEnergyCost, Set<ToolClass> toolClasses) {
        this(name, JavaUtil.NULL_SUPPLIER, 28, 1, maxCharge, transferLimit, tier, operationEnergyCost, false, 0, toolClasses);
    }

    public ItemToolElectricBase(String name, float attackDamage, double maxCharge, int tier, double operationEnergyCost, int harvestLevel, Set<ToolClass> toolClasses) {
        this(name, () -> GtLocale.translateItemDescription(name), 28, attackDamage, maxCharge, getTransferLimit(tier), tier, operationEnergyCost, false, harvestLevel, toolClasses);
    }

    public ItemToolElectricBase(String name, String descriptionKey, int durability, float attackDamage, double maxCharge, int tier, double operationEnergyCost, boolean providesEnergy, int harvestLevel, Set<ToolClass> toolClasses) {
        this(name, () -> GtLocale.translateGenericDescription(descriptionKey), durability, attackDamage, maxCharge, getTransferLimit(tier), tier, operationEnergyCost, providesEnergy, harvestLevel, toolClasses);
    }

    public ItemToolElectricBase(String name, Supplier<String> description, int durability, float attackDamage, double maxCharge, double transferLimit, int tier, double operationEnergyCost, boolean providesEnergy, int harvestLevel, Set<ToolClass> toolClasses) {
        super(name, description, durability, attackDamage, 0, harvestLevel, toolClasses);
        this.maxCharge = maxCharge;
        this.transferLimit = transferLimit;
        this.tier = tier;
        this.operationEnergyCost = operationEnergyCost;
        this.providesEnergy = providesEnergy;
        setNoCustomRepair();
    }

    public static double getTransferLimit(int tier) {
        return (1 << tier) * 128;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (this.showTier && this.tier > 0) {
            tooltip.add(GtLocale.translateInfo("tier", this.tier));
        }

        String description = this.description.get();
        if (description != null) {
            if (isEmpty(stack)) {
                tooltip.add(GtLocale.translateInfo("empty"));
            }
            else tooltip.add(description);
        }
    }

    @Override
    public IRarity getForgeRarity(ItemStack stack) {
        return isEmpty(stack) ? EnumRarity.COMMON : super.getForgeRarity(stack);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        return !ElectricItem.manager.canUse(stack, this.operationEnergyCost) ? 1 : super.getDestroySpeed(stack, state);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        return this.toolClasses.contains(ToolClass.Sword) && ElectricItem.manager.use(stack, this.operationEnergyCost, attacker) || super.hitEntity(stack, target, attacker);
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
    public String getTranslationKey(ItemStack stack) {
        if (isEmpty(stack)) {
            return super.getTranslationKey(stack) + ".empty";
        }
        return super.getTranslationKey(stack);
    }
    
    private boolean isEmpty(ItemStack stack) {
        return this.hasEmptyVariant && !ElectricItem.manager.canUse(stack, this.operationEnergyCost);
    }
}
