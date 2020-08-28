package mods.gregtechmod.common.objects.blocks;

import mods.gregtechmod.common.core.GregtechMod;
import mods.gregtechmod.common.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockBase extends Block implements IHasModel {

    public BlockBase(String name, Material material, float hardness, float resistance) {
        super(material);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(GregtechMod.GREGTECH_TAB);
        setHardness(hardness);
        setResistance(resistance);
    }

    @Override
    public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, EntityLiving.SpawnPlacementType type) {
        return false;
    }

    @Override
    public void registerModels() {
        GregtechMod.proxy.registerModel(Item.getItemFromBlock(this), 0);
    }
}
