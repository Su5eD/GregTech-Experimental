package mods.gregtechmod.common.objects.items.tools;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.item.BaseElectricItem;
import mods.gregtechmod.common.core.GregtechMod;
import mods.gregtechmod.common.util.IHasModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemTeslaStaff extends BaseElectricItem implements IHasModel {

    public ItemTeslaStaff() {
        super(null, 10000000, 8192, 4);
        setRegistryName("tesla_staff");
    }

    @Override
    public String getTranslationKey() {
        return "item.tesla_staff";
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add("No warranty!");
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (target instanceof EntityPlayer && ElectricItem.manager.canUse(stack, 9000000)) {
            ElectricItem.manager.use(stack, 9000000, attacker);
            for (int i = 0; i<4; i++) {
                ItemStack armor = ((EntityPlayer)target).inventory.armorInventory.get(i);
                if (armor.getItem() instanceof IElectricItem) ((EntityPlayer)target).inventory.armorInventory.set(i, ItemStack.EMPTY);
            }
            target.attackEntityFrom(DamageSource.MAGIC, 19);
            attacker.attackEntityFrom(DamageSource.MAGIC, 19);
        }
        return super.hitEntity(stack, target, attacker);
    }

    @Override
    public void registerModels() {
        GregtechMod.proxy.registerModel(this, 0, "tesla_staff", null, "tool");
    }
}
