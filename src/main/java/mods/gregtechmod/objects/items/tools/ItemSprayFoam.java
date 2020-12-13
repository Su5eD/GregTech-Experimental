package mods.gregtechmod.objects.items.tools;

import ic2.api.item.IC2Items;
import ic2.core.IC2;
import ic2.core.block.wiring.TileEntityCable;
import ic2.core.util.StackUtil;
import mods.gregtechmod.api.BlockItems;
import mods.gregtechmod.api.util.GtUtil;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.items.base.ItemToolCrafting;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemSprayFoam extends ItemToolCrafting {

    public ItemSprayFoam() {
        super("spray_foam", "Precision Spray", 400, 0, 25, 0);
        setRegistryName("spray_foam");
        setTranslationKey("spray_foam");
        setCreativeTab(GregTechMod.GREGTECH_TAB);
    }

    @Override
    public ItemStack getEmptyItem() {
        return new ItemStack(BlockItems.Miscellaneous.spray_can_empty.getInstance());
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (!worldIn.isRemote && playerIn.isSneaking()) switchMode(playerIn.inventory.getCurrentItem(), playerIn);
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        switch (getMode(stack)) {
            case 0:
                tooltip.add("Single Block Mode");
                break;
            case 1:
                tooltip.add("4m Line Mode");
                break;
            case 2:
                tooltip.add("3mx3m Area Mode");
                break;
        }
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (world.isRemote || player.isSneaking()) return EnumActionResult.PASS;
        ItemStack stack = player.inventory.getCurrentItem();
        TileEntity aTileEntity = world.getTileEntity(pos);
        if (aTileEntity instanceof TileEntityCable) {
            if (!((TileEntityCable)aTileEntity).isFoamed() && GtUtil.damageStack(player, stack, 1)) {
                ((TileEntityCable)aTileEntity).foam();
                return EnumActionResult.SUCCESS;
            }
            return EnumActionResult.PASS;
        }
        pos = pos.offset(side);
        Item itemFoam = IC2Items.getItem("foam", "normal").getItem();
        boolean temp, factorX, factorY, factorZ;
        int tRotationPitch = Math.round(player.rotationPitch);
        EnumFacing facing;
        if (tRotationPitch >= 65) {
            facing = EnumFacing.UP;
        } else if (tRotationPitch <= -65) {
            facing = EnumFacing.DOWN;
        } else {
            switch (MathHelper.floor((player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 0x3) {
                case 0:
                    facing = EnumFacing.NORTH;
                    break;
                case 1:
                    facing = EnumFacing.EAST;
                    break;
                case 2:
                    facing = EnumFacing.SOUTH;
                    break;
                case 3:
                    facing = EnumFacing.WEST;
                    break;
                default:
                    facing = side;
                    break;
            }
        }
        switch (getMode(stack)) {
            case 0:
                if (world.isAirBlock(pos) && GtUtil.damageStack(player, stack, 1)) {
                    world.setBlockState(pos, Block.getBlockFromItem(itemFoam).getDefaultState());
                    return EnumActionResult.SUCCESS;
                }
                break;
            case 1:
                for (byte i = 0; i < 4; i = (byte)(i + 1)) {
                    if (world.isAirBlock(pos)) {
                        world.setBlockState(pos, Block.getBlockFromItem(itemFoam).getDefaultState());
                        if (!GtUtil.damageStack(player, stack, 1)) return EnumActionResult.PASS;
                    }
                    pos = pos.subtract(new Vec3i(facing.getXOffset(), facing.getYOffset(), facing.getZOffset()));
                }
                return EnumActionResult.SUCCESS;
            case 2:
                factorX = facing.getXOffset() == 0;
                factorY = facing.getYOffset() == 0;
                factorZ = facing.getZOffset() == 0;
                pos = pos.subtract(new Vec3i(factorX ? 1 : 0, factorY ? 1 : 0, factorZ ? 1 : 0));
                for (byte i = 0; i < 3; i = (byte)(i + 1)) {
                    for (byte j = 0; j < 3; j = (byte)(j + 1)) {
                        BlockPos placePos = new BlockPos(pos.getX() + (factorX ? i : 0), pos.getY() + ((!factorX && factorY) ? i : 0) + ((!factorZ && factorY) ? j : 0), pos.getZ() + (factorZ ? j : 0));
                        if (world.isAirBlock(placePos)) {
                            world.setBlockState(placePos, Block.getBlockFromItem(itemFoam).getDefaultState());
                            if (!GtUtil.damageStack(player, stack, 1)) return EnumActionResult.PASS;
                        }
                    }
                }
                return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }

    public void switchMode(ItemStack stack, EntityPlayer player) {
        setMode(stack, (getMode(stack) + 1) % 3);
        switch (getMode(stack)) {
            case 0:
                IC2.platform.messagePlayer(player, "Single Block Mode");
                break;
            case 1:
                IC2.platform.messagePlayer(player, "4m Line Mode");
                break;
            case 2:
                IC2.platform.messagePlayer(player, "3mx3m Area Mode");
                break;
        }
    }

    public int getMode(ItemStack stack) {
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        return nbt.getInteger("mode");
    }

    private void setMode(ItemStack stack, int mode) {
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        nbt.setInteger("mode", mode);
    }
}
