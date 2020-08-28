package mods.gregtechmod.common.objects.items;

import mods.gregtechmod.api.item.ISolderingMetal;
import mods.gregtechmod.common.objects.items.base.ItemBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemSolderingMetal extends ItemBase implements ISolderingMetal {

    public ItemSolderingMetal(String material, int durability) {
        super("soldering_"+material, "Used in conjunction with soldering tools");
        setMaxStackSize(1);
        setMaxDamage(durability - 1);
        setNoRepair();
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public void onUsed(EntityPlayer player, ItemStack stack) {
        stack.damageItem(1, player);
    }
}
