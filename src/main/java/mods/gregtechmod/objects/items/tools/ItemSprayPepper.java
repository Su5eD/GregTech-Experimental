package mods.gregtechmod.objects.items.tools;

import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.init.BlockItems;
import mods.gregtechmod.objects.items.base.ItemToolCrafting;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemSprayPepper extends ItemToolCrafting {

    public ItemSprayPepper() {
        super("spray_pepper", "To defend yourself against Bears", 128, 2, 1, 8);
        setRegistryName("spray_pepper");
        setTranslationKey("spray_pepper");
        setCreativeTab(GregTechMod.GREGTECH_TAB);
    }

    @Override
    public ItemStack getEmptyItem() {
        return new ItemStack(BlockItems.Miscellaneous.SPRAY_CAN_EMPTY.getInstance());
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add("especially Pedobears, Care Bears,");
        tooltip.add("Confession Bears and Bear Grylls");
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        target.addPotionEffect(new PotionEffect(Potion.getPotionById(15), 1200, 2));
        target.addPotionEffect(new PotionEffect(Potion.getPotionById(19), 120, 2));
        target.addPotionEffect(new PotionEffect(Potion.getPotionById(18), 200, 2));
        target.addPotionEffect(new PotionEffect(Potion.getPotionById(9), 600, 2));
        return super.hitEntity(stack, target, attacker);
    }
}
