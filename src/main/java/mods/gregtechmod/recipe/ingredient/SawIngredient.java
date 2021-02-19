package mods.gregtechmod.recipe.ingredient;

import ic2.api.item.ElectricItem;
import ic2.core.item.tool.ItemElectricTool;
import mods.gregtechmod.util.IElectricCraftingItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreIngredient;

import javax.annotation.Nullable;

public class SawIngredient extends OreIngredient {

    public SawIngredient() {
        super("craftingToolSaw");
    }

    @Override
    public boolean apply(@Nullable ItemStack input) {
        boolean ret = super.apply(input);

        if (ret) {
            Item item = input.getItem();
            if (item instanceof IElectricCraftingItem && !((IElectricCraftingItem) item).canUse(input) ||
                    item instanceof ItemElectricTool && !ElectricItem.manager.canUse(input, ((ItemElectricTool) item).operationEnergyCost)) return false;
        }

        return ret;
    }
}
