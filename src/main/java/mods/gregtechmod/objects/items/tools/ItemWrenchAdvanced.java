package mods.gregtechmod.objects.items.tools;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.IC2;
import ic2.core.item.BaseElectricItem;
import ic2.core.item.ElectricItemManager;
import ic2.core.item.IPseudoDamageItem;
import ic2.core.util.LogCategory;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemWrenchAdvanced extends ItemWrench implements IElectricItem, IPseudoDamageItem {

    public ItemWrenchAdvanced() {
        super("advanced", 28);
        setNoRepair();
        this.rotateDamage = 1000;
        this.removeDamage = 3000;
    }

    public boolean canTakeDamage(ItemStack stack, int amount) {
        return ElectricItem.manager.getCharge(stack) >= amount;
    }

    public void damage(ItemStack stack, int amount, EntityPlayer player) {
        ElectricItem.manager.use(stack, amount, player);
    }

    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return false;
    }

    @Override
    public double getMaxCharge(ItemStack itemStack) {
        return 128000;
    }

    @Override
    public int getTier(ItemStack itemStack) {
        return 2;
    }

    @Override
    public double getTransferLimit(ItemStack itemStack) {
        return 1000;
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        int prev = this.getDamage(stack);
        if (damage != prev && BaseElectricItem.logIncorrectItemDamaging) {
            IC2.log.warn(LogCategory.Armor, new Throwable(), "Detected invalid armor damage application (%d):", damage - prev);
        }
    }

    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if (isInCreativeTab(tab)) ElectricItemManager.addChargeVariants(this, subItems);
    }

    @Override
    public void setStackDamage(ItemStack itemStack, int damage) {
        super.setDamage(itemStack, damage);
    }
}
