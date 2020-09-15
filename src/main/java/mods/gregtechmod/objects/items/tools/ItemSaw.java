package mods.gregtechmod.objects.items.tools;

import mods.gregtechmod.objects.items.base.ItemCraftingTool;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class ItemSaw extends ItemCraftingTool {

    public ItemSaw(String material, int durability, int efficiency, int entityDamage, ToolMaterial toolMaterial) {
        super("saw_"+material, "For sawing logs into planks", durability, entityDamage, toolMaterial, 1);
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
