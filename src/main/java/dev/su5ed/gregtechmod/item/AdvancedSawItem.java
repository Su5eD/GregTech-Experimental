package dev.su5ed.gregtechmod.item;

import dev.su5ed.gregtechmod.GregTechTags;
import dev.su5ed.gregtechmod.compat.ModHandler;
import dev.su5ed.gregtechmod.util.ElectricCraftingTool;
import dev.su5ed.gregtechmod.util.GtLocale;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.ToolActions;

public class AdvancedSawItem extends ElectricToolItem implements ElectricCraftingTool {
    private final double craftingEnergyCost;

    public AdvancedSawItem() {
        super(new ElectricToolItemProperties()
            .maxCharge(128000)
            .energyTier(3)
            .operationEnergyCost(200)
            .showTier(false)
            .hasEmptyVariant(true)
            .attackDamage(12)
            .destroySpeed(12)
            .dropLevel(4)
            .actions(ToolActions.AXE_DIG, ToolActions.SWORD_DIG, ToolActions.SWORD_SWEEP, ToolActions.SHEARS_DIG, ToolActions.SHEARS_HARVEST)
            .blockTags(BlockTags.MINEABLE_WITH_AXE, GregTechTags.MINEABLE_WITH_SHEARS)
            .rarity(Rarity.UNCOMMON)
            .description(GtLocale.translateGenericDescription("saw")));

        this.craftingEnergyCost = 1000;
    }
    
    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        ModHandler.useEnergy(stack, this.craftingEnergyCost, null);
        return stack.copy();
    }

    @Override
    public boolean canUse(ItemStack stack) {
        return ModHandler.canUseEnergy(stack, this.craftingEnergyCost);
    }
}
