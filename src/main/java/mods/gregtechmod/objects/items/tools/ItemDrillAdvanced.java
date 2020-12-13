package mods.gregtechmod.objects.items.tools;

import ic2.core.item.tool.ToolClass;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.items.base.ItemToolElectricBase;
import mods.gregtechmod.util.IModelInfoProvider;

import java.util.EnumSet;

public class ItemDrillAdvanced extends ItemToolElectricBase implements IModelInfoProvider {

    public ItemDrillAdvanced() {
        super("drill_advanced", "For quickly making holes", 8, 128000, 3, 250, 5, EnumSet.of(ToolClass.Pickaxe, ToolClass.Shovel));
        setRegistryName("drill_advanced");
        setTranslationKey("drill_advanced");
        setCreativeTab(GregTechMod.GREGTECH_TAB);
        this.efficiency = 35;
        this.showTier = false;
        this.hasEmptyVariant = true;
    }
}
