package mods.gregtechmod.objects.items.tools;

import ic2.api.item.IC2Items;
import ic2.core.block.wiring.TileEntityCable;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.items.base.ItemToolCrafting;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

public class ItemSprayHardener extends ItemToolCrafting {
    private static final MethodHandle CHANGE_FOAM_HANDLE;
    private static final Object HARDENED_CABLE_FOAM;

    static {
        MethodHandle handleChangeFoam;
        Object cableFoamHardened;
        try {
            Class<?> classCableFoam = Class.forName("ic2.core.block.wiring.CableFoam");
            cableFoamHardened = classCableFoam.getEnumConstants()[2];

            Method methodChangeFoam = TileEntityCable.class.getDeclaredMethod("changeFoam", classCableFoam, boolean.class);
            methodChangeFoam.setAccessible(true);
            handleChangeFoam = MethodHandles.lookup().unreflect(methodChangeFoam);
        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException e) {
            handleChangeFoam = null;
            cableFoamHardened = null;
            GregTechMod.logger.error("Failed to set up foam hardening methods");
            e.printStackTrace();
        }
        CHANGE_FOAM_HANDLE = handleChangeFoam;
        HARDENED_CABLE_FOAM = cableFoamHardened;
    }

    public ItemSprayHardener() {
        super("spray_hardener", 256, 0, 16, 0);
        setRegistryName("spray_hardener");
        setTranslationKey("spray_hardener");
        setCreativeTab(GregTechMod.GREGTECH_TAB);
    }

    @Override
    public ItemStack getEmptyItem() {
        return new ItemStack(BlockItems.Miscellaneous.SPRAY_CAN_EMPTY.getInstance());
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (world.isRemote) return EnumActionResult.PASS;
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block == Blocks.AIR) return EnumActionResult.PASS;
        ItemStack stack = player.inventory.getCurrentItem();
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityCable) {
            if (((TileEntityCable)tileEntity).isFoamed() && GtUtil.damageStack(player, stack, 1)) {
                hardenCableFoam(tileEntity);
                return EnumActionResult.SUCCESS;
            }
            return EnumActionResult.PASS;
        }
        Item itemFoam = IC2Items.getItem("foam", "normal").getItem(),
             itemWall = IC2Items.getItem("wall", "light_gray").getItem();
        if (block.getRegistryName().equals(itemFoam.getRegistryName())) {
            if (GtUtil.damageStack(player, stack, 1))
                world.setBlockState(pos, Block.getBlockFromItem(itemWall).getStateFromMeta(7));
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }

    public static void hardenCableFoam(Object tileEntityCable) {
        if (CHANGE_FOAM_HANDLE == null || HARDENED_CABLE_FOAM == null) return;

        try {
            CHANGE_FOAM_HANDLE.invokeExact(tileEntityCable, HARDENED_CABLE_FOAM, false);
        }  catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
