package mods.gregtechmod.objects.blocks;

import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.util.IBlockCustomItem;
import mods.gregtechmod.util.PropertyHelper;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.List;
import java.util.function.BiFunction;

public class BlockOre extends Block implements IBlockCustomItem {
    private final String name;
    private final int dropChance;
    private final int dropRandom;
    private final BiFunction<World, Integer, List<ItemStack>> loot;

    public BlockOre(String name, float hardness, int dropChance, int dropRandom, BiFunction<World, Integer, List<ItemStack>> loot) {
        super(Material.ROCK);
        this.name = name;
        this.dropChance = dropChance;
        this.dropRandom = dropRandom;
        this.loot = loot;
        setHardness(hardness);
        setResistance(10);
        setSoundType(SoundType.STONE);
    }

    @Override
    public String getTranslationKey() {
        return GregTechMod.MODID+"."+super.getTranslationKey();
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
        int xp = this.dropChance + worldIn.rand.nextInt(this.dropRandom);
        if (xp > 0) dropXpOnBlockBreak(worldIn, pos, xp);
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        super.getDrops(drops, world, pos, state, fortune);
        assert world instanceof World;
        drops.addAll(this.loot.apply((World) world, fortune));
    }

    @Override
    public ResourceLocation getItemModel() {
        return new ResourceLocation(GregTechMod.MODID, "ore/"+this.name);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this, new IProperty[0], new IUnlistedProperty[] { PropertyHelper.TEXTURE_INDEX_PROPERTY });
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        Integer[] textureIndexes = null;
        if (renderAsOre(world, pos)) {
            textureIndexes = new Integer[6];
            for (EnumFacing side : EnumFacing.VALUES) {
                int sideIndex = Math.abs(side.getIndex() ^ pos.getX() ^ pos.getY() ^ pos.getZ());
                if (!GregTechConfig.GENERAL.hiddenOres || sideIndex%12 > 6) textureIndexes[side.getIndex()] = sideIndex % 6;
                else textureIndexes[side.getIndex()] = -1;
            }
        }
        return ((IExtendedBlockState) state).withProperty(PropertyHelper.TEXTURE_INDEX_PROPERTY, textureIndexes);
    }

    private boolean renderAsOre(IBlockAccess world, BlockPos pos) {
        for (EnumFacing facing : EnumFacing.VALUES) {
            BlockPos neighbor = pos.offset(facing);
            IBlockState state = world.getBlockState(neighbor);
            if (!world.isAirBlock(neighbor) && state.getBlock().isReplaceableOreGen(state, world, neighbor, BlockMatcher.forBlock(Blocks.STONE))) return true;
        }
        return false;
    }
}
