package mods.gregtechmod.objects.items.tools;

import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.items.base.ItemToolCrafting;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemSprayPepper extends ItemToolCrafting {

    public ItemSprayPepper() {
        super("spray_pepper", 128, 2, 1, 8);
        setRegistryName("spray_pepper");
        setTranslationKey("spray_pepper");
        setCreativeTab(GregTechMod.GREGTECH_TAB);
    }

    @Override
    public ItemStack getEmptyItem() {
        return BlockItems.Miscellaneous.SPRAY_CAN_EMPTY.getItemStack();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(GtUtil.translateItem("spray_pepper.description_2"));
        tooltip.add(GtUtil.translateItem("spray_pepper.description_3"));
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        target.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 1200, 2));
        target.addPotionEffect(new PotionEffect(MobEffects.POISON, 120, 2));
        target.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 200, 2));
        target.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 600, 2));
        return super.hitEntity(stack, target, attacker);
    }
}
