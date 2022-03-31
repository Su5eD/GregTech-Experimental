package mods.gregtechmod.recipe.crafting;

import ic2.api.item.ElectricItem;
import ic2.core.item.tool.ItemElectricTool;
import mods.gregtechmod.util.IElectricCraftingTool;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreIngredient;

import javax.annotation.Nullable;

public class ToolOreIngredient extends OreIngredient {
    private final int damage;

    public ToolOreIngredient(String ore, int damage) {
        super(ore);
        this.damage = damage;
    }

    public static ToolOreIngredient saw() {
        return new ToolOreIngredient("craftingToolSaw", 1);
    }

    @Override
    public boolean apply(@Nullable ItemStack input) {
        boolean ret = super.apply(input);

        if (ret) {
            Item item = input.getItem();
            if (item instanceof IElectricCraftingTool && !((IElectricCraftingTool) item).canUse(input) ||
                item instanceof ItemElectricTool && !ElectricItem.manager.canUse(input, this.damage * 1000) ||
                input.getMaxDamage() - input.getItemDamage() < this.damage) return false;
        }

        return ret;
    }
}
