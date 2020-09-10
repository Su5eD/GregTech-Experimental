package mods.gregtechmod.objects.items.tools;

import mods.gregtechmod.api.cover.CoverRegistry;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.machine.IGregtechMachine;
import mods.gregtechmod.objects.items.base.ItemToolBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneComparator;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemScrewdriver extends ItemToolBase {

    public ItemScrewdriver() {
        super("screwdriver", "To screw covers on machines \nCan switch the design of certain blocks \nCan rotate repeaters and comparators", 256, 4, ToolMaterial.IRON);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (player.isSneaking()) return EnumActionResult.PASS;
        Block block = world.getBlockState(pos).getBlock();
        ItemStack stack = player.inventory.getCurrentItem();
        if (block instanceof BlockRedstoneRepeater || block instanceof BlockRedstoneComparator) {
            block.rotateBlock(world, pos, EnumFacing.fromAngle(90));
            stack.damageItem(1, player);
            return EnumActionResult.SUCCESS;
        }
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof ICoverable) {
            ICover cover = ((ICoverable)te).getCoverAtSide(side);
            if (cover != null) {
                if (cover.onScrewdriverClick(player)) {
                    if (te instanceof IGregtechMachine) ((IGregtechMachine)te).markForCoverBehaviorUpdate();
                    stack.damageItem(1, player);
                }
                return EnumActionResult.SUCCESS;
            } else ((ICoverable)te).placeCoverAtSide(CoverRegistry.constructCover("normal", side, (ICoverable) te, null), side, false);
        }
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }
}