package mods.gregtechmod.objects.items.tools;

import ic2.api.item.IC2Items;
import ic2.core.block.wiring.TileEntityCable;
import ic2.core.util.StackUtil;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.items.base.ItemToolCrafting;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class ItemSprayFoam extends ItemToolCrafting {

    public ItemSprayFoam() {
        super("spray_foam", 400, 0, 25, 0);
        setRegistryName("spray_foam");
        setTranslationKey("spray_foam");
        setCreativeTab(GregTechMod.GREGTECH_TAB);
    }

    @Override
    public ItemStack getEmptyItem() {
        return BlockItems.Miscellaneous.SPRAY_CAN_EMPTY.getItemStack();
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if (!world.isRemote && player.isSneaking()) switchMode(player.inventory.getCurrentItem(), player);
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(I18n.format(getMode(stack).getTooltipKey()));
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (world.isRemote || player.isSneaking()) return EnumActionResult.PASS;

        ItemStack stack = player.inventory.getCurrentItem();
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityCable) {
            if (!((TileEntityCable) tileEntity).isFoamed() && GtUtil.damageStack(player, stack, 1)) {
                ((TileEntityCable) tileEntity).foam();
                return EnumActionResult.SUCCESS;
            }
            return EnumActionResult.PASS;
        }
        pos = pos.offset(side);
        Item itemFoam = IC2Items.getItem("foam", "normal").getItem();
        boolean factorX, factorY, factorZ;
        int rotationpitch = Math.round(player.rotationPitch);
        EnumFacing facing;
        if (rotationpitch >= 65) {
            facing = EnumFacing.UP;
        }
        else if (rotationpitch <= -65) {
            facing = EnumFacing.DOWN;
        }
        else {
            switch (MathHelper.floor(player.rotationYaw * 4F / 360F + 0.5) & 3) {
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
            case SINGLE:
                if (world.isAirBlock(pos) && GtUtil.damageStack(player, stack, 1)) {
                    world.setBlockState(pos, Block.getBlockFromItem(itemFoam).getDefaultState());
                    return EnumActionResult.SUCCESS;
                }
                break;
            case LINE:
                for (int i = 0; i < 4; i++) {
                    if (world.isAirBlock(pos)) {
                        world.setBlockState(pos, Block.getBlockFromItem(itemFoam).getDefaultState());
                        if (!GtUtil.damageStack(player, stack, 1)) return EnumActionResult.PASS;
                    }
                    pos = pos.subtract(new Vec3i(facing.getXOffset(), facing.getYOffset(), facing.getZOffset()));
                }
                return EnumActionResult.SUCCESS;
            case AREA:
                factorX = facing.getXOffset() == 0;
                factorY = facing.getYOffset() == 0;
                factorZ = facing.getZOffset() == 0;
                pos = pos.subtract(new Vec3i(factorX ? 1 : 0, factorY ? 1 : 0, factorZ ? 1 : 0));
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        BlockPos placePos = new BlockPos(pos.getX() + (factorX ? i : 0), pos.getY() + (!factorX && factorY ? i : 0) + (!factorZ && factorY ? j : 0), pos.getZ() + (factorZ ? j : 0));
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
        SprayMode mode = getMode(stack).next();
        setMode(stack, mode);
        GtUtil.sendMessage(player, mode.getTooltipKey());
    }

    public SprayMode getMode(ItemStack stack) {
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        return nbt.hasKey("mode") ? SprayMode.VALUES[nbt.getInteger("mode")] : SprayMode.SINGLE;
    }

    private void setMode(ItemStack stack, SprayMode mode) {
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        nbt.setInteger("mode", mode.ordinal());
    }

    private enum SprayMode {
        SINGLE,
        LINE,
        AREA;

        private static final SprayMode[] VALUES = values();

        public SprayMode next() {
            return VALUES[(ordinal() + 1) % VALUES.length];
        }

        public String getTooltipKey() {
            return GtLocale.buildKeyItem("spray_foam", "mode", name().toLowerCase(Locale.ROOT));
        }
    }
}
