package mods.gregtechmod.objects.items.components;

import mods.gregtechmod.objects.items.base.ItemBase;
import mods.gregtechmod.util.GtUtil;

public class ItemTurbineRotor extends ItemBase {
    public final int efficiency;
    public final int efficiencyMultiplier;

    public ItemTurbineRotor(String name, int durability, int efficiency, int efficiencyMultiplier) {
        super(name, () -> GtUtil.translateGenericDescription("turbine_rotor", efficiency), durability);
        this.efficiency = efficiency;
        this.efficiencyMultiplier = efficiencyMultiplier;
        setFolder("component");
        setEnchantable(false);
        setMaxStackSize(1);
        setNoRepair();
    }
}
