package mods.gregtechmod.objects.blocks;

import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.IBlockCustomItem;
import mods.gregtechmod.util.PropertyHelper;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class BlockOre extends Block implements IBlockCustomItem {
    private final String name;
    private final int dropChance;
    private final int dropRandom;
    private final BiConsumer<Integer, List<ItemStack>> loot;

    public BlockOre(String name, int dropChance, int dropRandom, BiConsumer<Integer, List<ItemStack>> loot) {
        super(Material.ROCK);
        this.name = name;
        this.dropChance = dropChance;
        this.dropRandom = dropRandom;
        this.loot = loot;
        setResistance(10);
        setSoundType(SoundType.STONE);
    }

    @Override
    public String getTranslationKey() {
        return Reference.MODID+"."+super.getTranslationKey();
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
        if (this.dropRandom > 0) {
            int xp = this.dropChance + worldIn.rand.nextInt(this.dropRandom);
            if (xp > 0) dropXpOnBlockBreak(worldIn, pos, xp);
        }
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        this.loot.accept(fortune, drops);
        if (drops.isEmpty()) drops.add(new ItemStack(this.getItemDropped(state, world instanceof World ? ((World)world).rand : GtUtil.RANDOM, fortune)));
    }

    @Override
    public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
        if (entity instanceof EntityDragon) return false;
        return super.canEntityDestroy(state, world, pos, entity);
    }

    @Override
    public ResourceLocation getItemModel() {
        return new ResourceLocation(Reference.MODID, "ore/"+this.name);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this, new IProperty[0], new IUnlistedProperty[] { PropertyHelper.TEXTURE_INDEX_PROPERTY });
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        PropertyHelper.DimensionalTextureInfo textures = null;
        byte dimension = renderAsOre(world, pos);
        if (dimension > -128) {
            Map<EnumFacing, EnumFacing> overrides = new HashMap<>();
            for (EnumFacing side : EnumFacing.VALUES) {
                int sideIndex = Math.abs(side.getIndex() ^ pos.getX() ^ pos.getY() ^ pos.getZ());
                if (!GregTechConfig.GENERAL.hiddenOres || sideIndex%12 > 6) overrides.put(side, EnumFacing.byIndex(sideIndex % 6));
                else overrides.put(side, null);
            }
            textures = new PropertyHelper.DimensionalTextureInfo(overrides, DimensionType.getById(dimension));
        }
        return ((IExtendedBlockState) state).withProperty(PropertyHelper.TEXTURE_INDEX_PROPERTY, textures);
    }

    private byte renderAsOre(IBlockAccess world, BlockPos pos) {
        for (EnumFacing facing : EnumFacing.VALUES) {
            BlockPos neighbor = pos.offset(facing);
            IBlockState state = world.getBlockState(neighbor);
            if (!world.isAirBlock(neighbor) && state.getBlock().isReplaceableOreGen(state, world, neighbor, BlockMatcher.forBlock(Blocks.STONE))) return 0;
            else if (!world.isAirBlock(neighbor) && state.getBlock().isReplaceableOreGen(state, world, neighbor, BlockMatcher.forBlock(Blocks.NETHERRACK))) return -1;
            else if (!world.isAirBlock(neighbor) && state.getBlock().isReplaceableOreGen(state, world, neighbor, BlockMatcher.forBlock(Blocks.END_STONE))) return 1;
        }
        return -128;
    }
}
