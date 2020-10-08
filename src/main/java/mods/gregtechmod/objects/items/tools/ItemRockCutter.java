package mods.gregtechmod.objects.items.tools;

import ic2.api.item.ElectricItem;
import ic2.core.item.tool.HarvestLevel;
import ic2.core.item.tool.ItemElectricTool;
import ic2.core.item.tool.ToolClass;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.util.IModelInfoProvider;
import mods.gregtechmod.util.ModelInformation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.EnumSet;

public class ItemRockCutter extends ItemElectricTool implements IModelInfoProvider {

    public ItemRockCutter() {
        super(null, 500, HarvestLevel.Iron, EnumSet.of(ToolClass.Pickaxe));
        this.tier = 1;
        this.transferLimit = 200;
        this.maxCharge = 10000;
        this.efficiency = 3;
    }

    @Override
    public String getTranslationKey() {
        return GregTechMod.MODID+".item.rock_cutter";
    }

    @Override
    protected ItemStack getItemStack(double charge) {
        ItemStack ret = super.getItemStack(charge);
        ret.addEnchantment(Enchantments.SILK_TOUCH, 3);
        return ret;
    }

    public void checkEnchantments(ItemStack stack) {
        if (ElectricItem.manager.canUse(stack, operationEnergyCost) && !stack.isItemEnchanted()) stack.addEnchantment(Enchantments.SILK_TOUCH, 3);
        else if (stack.isItemEnchanted()) stack.getTagCompound().removeTag("ench");
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase player) {
        checkEnchantments(stack);
        return super.onBlockDestroyed(stack, world, state, pos, player);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
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

    @Override
    public ModelInformation getModelInformation() {
        return new ModelInformation(GregTechMod.getModelResourceLocation("rock_cutter", "tool"));
    }
}
