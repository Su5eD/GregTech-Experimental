package mods.gregtechmod.objects.items.tools;

import mods.gregtechmod.core.GregTechMod;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
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
}
