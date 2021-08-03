package mods.gregtechmod.api.util;

import net.minecraft.item.ItemStack;

public class TurbineRotor {
    public final ItemStack item;
    public final int efficiency;
    public final int efficiencyMultiplier;
    public final int damageToComponent;

    public TurbineRotor(ItemStack item, int efficiency, int efficiencyMultiplier, int damageToComponent) {
        this.item = item;
        this.efficiency = efficiency;
        this.efficiencyMultiplier = efficiencyMultiplier;
        this.damageToComponent = damageToComponent;
    }
}
