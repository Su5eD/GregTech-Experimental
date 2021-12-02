package mods.gregtechmod.objects.items.tools;

import ic2.api.item.IC2Items;
import ic2.core.block.wiring.TileEntityCable;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.items.base.ItemToolCrafting;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
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
            GregTechMod.LOGGER.error("Failed to reflect foam hardening methods", e);
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
        return BlockItems.Miscellaneous.SPRAY_CAN_EMPTY.getItemStack();
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (!world.isRemote) {
            IBlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            if (!block.isAir(state, world, pos)) {
                ItemStack stack = player.inventory.getCurrentItem();
                TileEntity te = world.getTileEntity(pos);
                
                if (te instanceof TileEntityCable) {
                    if (((TileEntityCable) te).isFoamed() && GtUtil.damageStack(player, stack, 1)) {
                        hardenCableFoam(te);
                        return EnumActionResult.SUCCESS;
                    }
                } else {
                    Item itemFoam = IC2Items.getItem("foam", "normal").getItem();
                    if (block.getRegistryName().equals(itemFoam.getRegistryName())) {
                        if (GtUtil.damageStack(player, stack, 1)) {
                            IBlockState wall = ModHandler.ic2ItemApi.getBlockState("wall", "light_gray");
                            world.setBlockState(pos, wall);
                        }
                        return EnumActionResult.SUCCESS;
                    }
                }
            }
        }
        return EnumActionResult.PASS;
    }

    public static void hardenCableFoam(Object tileEntityCable) {
        if (CHANGE_FOAM_HANDLE != null && HARDENED_CABLE_FOAM != null) {
            try {
                CHANGE_FOAM_HANDLE.invoke(tileEntityCable, HARDENED_CABLE_FOAM, false);
            } catch (Throwable t) {
                GregTechMod.LOGGER.catching(t);
            }
        }
    }
}
