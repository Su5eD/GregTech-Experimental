package mods.gregtechmod.common.objects.items.tools;

import mods.gregtechmod.common.objects.items.base.ItemCraftingTool;

public class ItemFile extends ItemCraftingTool {

    public ItemFile(String material, int durability, float attackDamage, ToolMaterial toolMaterial) {
        super("file_"+material, "For sharpening or rounding Edges", durability, attackDamage, toolMaterial, 1);
    }
}
