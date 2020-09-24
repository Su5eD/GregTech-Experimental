package mods.gregtechmod.objects.items.tools;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.IC2;
import ic2.core.item.BaseElectricItem;
import ic2.core.item.ElectricItemManager;
import ic2.core.item.IPseudoDamageItem;
import ic2.core.util.LogCategory;
import mods.gregtechmod.api.item.ISolderingMetal;
import mods.gregtechmod.api.machine.IGregtechMachine;
import mods.gregtechmod.objects.items.base.ItemToolBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemSolderingTool extends ItemToolBase implements IElectricItem, IPseudoDamageItem {

    public ItemSolderingTool(String name, String description, int durability) {
        super(name, description, durability, 0, ToolMaterial.IRON);
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return ElectricItem.manager.getCharge(stack) >= 1000 ? "item.soldering_tool" : "item.soldering_tool_empty";
    }

    public ItemStack findSolderingMetal(EntityPlayer player) {
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (stack.getItem() instanceof ISolderingMetal) return stack;
        }
        return null;
    }

    public boolean use(ItemStack stack, EntityPlayer player, boolean simulate) {
        if (!(ElectricItem.manager.getCharge(stack) >= 1000)) return false;
        ItemStack metalStack = findSolderingMetal(player);
        if (metalStack == null) return false;
        ISolderingMetal metal = (ISolderingMetal) metalStack.getItem();
        if (!metal.canUse()) return false;

        if (!simulate) {
            ElectricItem.manager.use(stack, 1000, player);
            metal.onUsed(player, metalStack);
        }
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (ElectricItem.manager.canUse(stack, 1000)) {
            super.addInformation(stack, worldIn, tooltip, flagIn);
            tooltip.add("Sets the strength of outputted redstone");
            tooltip.add("Needs Soldering Metal in inventory!");
        }
        else tooltip.add("Empty. You need to recharge it");
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof IGregtechMachine) {
            if (use(player.getHeldItem(hand), player, false)) System.out.println("success!");
            else System.out.println("fail :(");
            return EnumActionResult.SUCCESS;
        }
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }

    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return false;
    }

    @Override
    public double getMaxCharge(ItemStack itemStack) {
        return 10000;
    }

    @Override
    public int getTier(ItemStack itemStack) {
        return 1;
    }

    @Override
    public double getTransferLimit(ItemStack itemStack) {
        return 1000;
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        int prev = this.getDamage(stack);
        if (damage != prev && BaseElectricItem.logIncorrectItemDamaging) {
            IC2.log.warn(LogCategory.Armor, new Throwable(), "Detected invalid armor damage application (%d):", damage - prev);
        }
    }

    @Override
    public void setStackDamage(ItemStack itemStack, int damage) {
        super.setDamage(itemStack, damage);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if (isInCreativeTab(tab)) ElectricItemManager.addChargeVariants(this, subItems);
    }
}
