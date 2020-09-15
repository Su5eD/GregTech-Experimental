package mods.gregtechmod.objects.blocks;

import mods.gregtechmod.core.GregtechMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockBase extends Block {

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
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add("Mobs can't spawn on this block");
    }
}
