package mods.gregtechmod.objects.items.components;

import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.objects.items.base.ItemBase;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.item.ItemStack;

public class ItemTurbineRotor extends ItemBase {

    public ItemTurbineRotor(String name, int durability, int efficiency, int efficiencyMultiplier) {
        super(name, () -> GtUtil.translateGenericDescription("turbine_rotor", efficiency), durability);
        setFolder("component");
        setEnchantable(false);
        setMaxStackSize(1);
        setNoRepair();
        GregTechAPI.registerTurbineRotor(new ItemStack(this), efficiency, efficiencyMultiplier, 1);
    }
}
