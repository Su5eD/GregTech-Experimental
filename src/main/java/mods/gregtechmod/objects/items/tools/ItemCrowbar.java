package mods.gregtechmod.objects.items.tools;

import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.items.base.ItemToolBase;
import mods.railcraft.api.items.IToolCrowbar;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.List;

@Optional.Interface(modid = "railcraft", iface = "mods.railcraft.api.items.IToolCrowbar")
public class ItemCrowbar extends ItemToolBase implements IToolCrowbar {

    public ItemCrowbar() {
        super("crowbar", "To remove covers from machines", 256, 6);
        setRegistryName("crowbar");
        setTranslationKey("crowbar");
        setCreativeTab(GregTechMod.GREGTECH_TAB);
        GregTechAPI.registerCrowbar(new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (ModHandler.railcraft) tooltip.add("Works as a Railcraft crowbar, too");
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public boolean canWhack(EntityPlayer entityPlayer, EnumHand enumHand, ItemStack itemStack, BlockPos blockPos) {
        return true;
    }

    @Override
    public void onWhack(EntityPlayer entityPlayer, EnumHand enumHand, ItemStack itemStack, BlockPos blockPos) {
        itemStack.damageItem(1, entityPlayer);
    }

    @Override
    public boolean canLink(EntityPlayer entityPlayer, EnumHand enumHand, ItemStack itemStack, EntityMinecart entityMinecart) {
        return true;
    }

    @Override
    public void onLink(EntityPlayer entityPlayer, EnumHand enumHand, ItemStack itemStack, EntityMinecart entityMinecart) {
        itemStack.damageItem(1, entityPlayer);
    }

    @Override
    public boolean canBoost(EntityPlayer entityPlayer, EnumHand enumHand, ItemStack itemStack, EntityMinecart entityMinecart) {
        return true;
    }

    @Override
    public void onBoost(EntityPlayer entityPlayer, EnumHand enumHand, ItemStack itemStack, EntityMinecart entityMinecart) {
        itemStack.damageItem(5, entityPlayer);
    }
}
