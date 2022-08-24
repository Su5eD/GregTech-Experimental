package mods.gregtechmod.objects.items.tools;

import ic2.core.item.tool.ToolClass;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.items.base.ItemToolElectricBase;
import net.minecraft.item.EnumRarity;

import java.util.EnumSet;

public class ItemDrillAdvanced extends ItemToolElectricBase {

    public ItemDrillAdvanced() {
        super("drill_advanced", 8, 128000, 3, 250, 5, EnumSet.of(ToolClass.Pickaxe, ToolClass.Shovel));
        setRarity(EnumRarity.UNCOMMON);
        setRegistryName("drill_advanced");
        setTranslationKey("drill_advanced");
        setCreativeTab(GregTechMod.GREGTECH_TAB);
        this.efficiency = 35;
        this.showTier = false;
        this.hasEmptyVariant = true;
    }
}
