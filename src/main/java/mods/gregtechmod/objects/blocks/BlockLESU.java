package mods.gregtechmod.objects.blocks;

import mods.gregtechmod.objects.blocks.teblocks.TileEntityLESU;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class BlockLESU extends BlockBase {

    public BlockLESU() {
        super(Material.IRON);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntityLESU.stepToFindOrCallLESUController(world, pos, new ArrayList<>());
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        super.breakBlock(world, pos, state);
        TileEntityLESU.stepToFindOrCallLESUController(world, pos, new ArrayList<>());
    }
}
