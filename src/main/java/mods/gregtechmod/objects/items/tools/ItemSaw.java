package mods.gregtechmod.objects.items.tools;

import mods.gregtechmod.objects.items.base.ItemToolCrafting;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class ItemSaw extends ItemToolCrafting {

    public ItemSaw(String material, int durability, int efficiency, int entityDamage) {
        super("saw_"+material, "saw", durability, entityDamage, 1);
        this.efficiency = efficiency;
        setHarvestLevel("axe", 2);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        Material material = state.getMaterial();
        if (material == Material.LEAVES) return efficiency;
        return super.getDestroySpeed(stack, state);
    }
}
