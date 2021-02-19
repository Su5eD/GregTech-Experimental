package mods.gregtechmod.objects.items.tools;

import ic2.api.item.ElectricItem;
import ic2.core.item.tool.ToolClass;
import ic2.core.util.StackUtil;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.items.base.ItemToolElectricCrafting;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.EnumSet;

public class ItemSawAdvanced extends ItemToolElectricCrafting {

    public ItemSawAdvanced() {
        super("saw_advanced", "For sawing logs into planks", 1000, 12, 128000, 3, 200, false, 4, EnumSet.of(ToolClass.Axe, ToolClass.Sword, ToolClass.Shears));
        setRegistryName("saw_advanced");
        setTranslationKey("saw_advanced");
        setCreativeTab(GregTechMod.GREGTECH_TAB);
        this.efficiency = 12;
        this.showTier = false;
        this.hasEmptyVariant = true;
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        checkEnchantments(stack);
        return false;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
        checkEnchantments(itemstack);
        return super.onBlockStartBreak(itemstack, pos, player);
    }

    private void checkEnchantments(ItemStack stack) {
        if (ElectricItem.manager.canUse(stack, this.operationEnergyCost)) {
            if (!stack.isItemEnchanted()) stack.addEnchantment(Enchantment.getEnchantmentByID(33), 1);
        } else if (stack.isItemEnchanted()) StackUtil.getOrCreateNbtData(stack).removeTag("ench");

    }
}
