package mods.gregtechmod.objects.items.tools;

import ic2.core.item.ElectricItemManager;
import mods.gregtechmod.core.GregTechMod;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDebugScanner extends ItemScanner {

    public ItemDebugScanner() {
        super("debug_scanner",1000000000, 0, 4, false);
        setRegistryName("debug_scanner");
        setTranslationKey("debug_scanner");
        setFolder("tool");
        setCreativeTab(GregTechMod.GREGTECH_TAB);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {}

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) items.add(ElectricItemManager.getCharged(this, Double.POSITIVE_INFINITY));
    }
}
