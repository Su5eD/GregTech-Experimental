package mods.gregtechmod.objects.items.tools;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.items.base.ItemElectricBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public class ItemTeslaStaff extends ItemElectricBase {

    public ItemTeslaStaff() {
        super("tesla_staff", 10000000, 8192, 4);
        setRegistryName("tesla_staff");
        setTranslationKey("tesla_staff");
        setFolder("tool");
        setCreativeTab(GregTechMod.GREGTECH_TAB);
        this.showTier = false;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (target instanceof EntityPlayer && ElectricItem.manager.canUse(stack, 9000000)) {
            ElectricItem.manager.use(stack, 9000000, attacker);
            for (int i = 0; i<4; i++) {
                ItemStack armor = ((EntityPlayer)target).inventory.armorInventory.get(i);
                if (armor.getItem() instanceof IElectricItem) {
                    ((EntityPlayer)target).inventory.armorInventory.set(i, ItemStack.EMPTY);
                    target.renderBrokenItemStack(armor);
                }
            }
            target.attackEntityFrom(DamageSource.MAGIC, 19);
            attacker.attackEntityFrom(DamageSource.MAGIC, 19);
        }
        return super.hitEntity(stack, target, attacker);
    }
}
