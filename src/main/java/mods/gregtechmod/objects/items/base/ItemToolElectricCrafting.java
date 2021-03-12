package mods.gregtechmod.objects.items.base;

import ic2.api.item.ElectricItem;
import ic2.core.item.tool.ToolClass;
import mods.gregtechmod.util.IElectricCraftingTool;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Set;

public class ItemToolElectricCrafting extends ItemToolElectricBase implements IElectricCraftingTool {
    protected final int craftingEnergyCost;

    public ItemToolElectricCrafting(String name, @Nullable String description, int craftingEnergyCost, float attackDamage, double maxCharge, int tier, double operationEnergyCost, boolean providesEnergy, int harvestLevel, Set<ToolClass> toolClasses) {
        super(name, description, 28, attackDamage, maxCharge, tier, operationEnergyCost, providesEnergy, harvestLevel, toolClasses);
        this.craftingEnergyCost = craftingEnergyCost;
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        ElectricItem.manager.use(stack, this.craftingEnergyCost, null);
        return stack.copy();
    }

    @Override
    public boolean canUse(ItemStack stack) {
        return ElectricItem.manager.canUse(stack, this.craftingEnergyCost);
    }
}
