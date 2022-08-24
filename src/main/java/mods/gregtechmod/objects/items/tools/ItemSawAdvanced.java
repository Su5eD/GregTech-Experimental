package mods.gregtechmod.objects.items.tools;

import ic2.core.item.tool.ToolClass;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.items.base.ItemToolElectricCrafting;
import net.minecraft.item.EnumRarity;

import java.util.EnumSet;

public class ItemSawAdvanced extends ItemToolElectricCrafting {

    public ItemSawAdvanced() {
        super("saw_advanced", "saw", 1000, 12, 128000, 3, 200, false, 4, EnumSet.of(ToolClass.Axe, ToolClass.Sword, ToolClass.Shears));
        setRarity(EnumRarity.UNCOMMON);
        setRegistryName("saw_advanced");
        setTranslationKey("saw_advanced");
        setCreativeTab(GregTechMod.GREGTECH_TAB);
        this.efficiency = 12;
        this.showTier = false;
        this.hasEmptyVariant = true;
    }
}
