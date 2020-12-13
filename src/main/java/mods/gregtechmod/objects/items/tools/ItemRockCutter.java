package mods.gregtechmod.objects.items.tools;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import ic2.api.item.ElectricItem;
import ic2.core.item.tool.HarvestLevel;
import ic2.core.item.tool.ToolClass;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.items.base.ItemToolElectricBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.EnumSet;

public class ItemRockCutter extends ItemToolElectricBase {

    public ItemRockCutter() {
        super("rock_cutter", null, 10000D, 100, 1, 500, HarvestLevel.Iron.level, EnumSet.of(ToolClass.Pickaxe));
        setRegistryName("rock_cutter");
        setTranslationKey("rock_cutter");
        setCreativeTab(GregTechMod.GREGTECH_TAB);
        this.efficiency = 2;
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        return HashMultimap.create();
    }

    private void checkEnchantments(ItemStack stack) {
        if (ElectricItem.manager.canUse(stack, operationEnergyCost)) {
            if (!stack.isItemEnchanted()) stack.addEnchantment(Enchantments.SILK_TOUCH, 3);
        }
        else if (stack.isItemEnchanted()) stack.getTagCompound().removeTag("ench");
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase player) {
        checkEnchantments(stack);
        return super.onBlockDestroyed(stack, world, state, pos, player);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        checkEnchantments(stack);
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack itemstack1, ItemStack itemstack2) {
        return false;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {
        checkEnchantments(stack);
        return super.onBlockStartBreak(stack, pos, player);
    }
}
