package mods.gregtechmod.objects.blocks;

import ic2.core.item.tool.HarvestLevel;
import ic2.core.item.tool.ToolClass;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.api.util.TriConsumer;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.ICustomItemModel;
import mods.gregtechmod.util.JavaUtil;
import mods.gregtechmod.util.PropertyHelper;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
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
import net.minecraftforge.common.property.IExtendedBlockState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BlockOre extends Block implements ICustomItemModel {
    private final String name;
    private final int dropChance;
    private final int dropRandom;
    private final TriConsumer<Integer, List<ItemStack>, Random> loot;

    public BlockOre(String name, HarvestLevel harvestLevel, int dropChance, int dropRandom, TriConsumer<Integer, List<ItemStack>, Random> loot) {
        super(Material.ROCK);
        this.name = name;
        this.dropChance = dropChance;
        this.dropRandom = dropRandom;
        this.loot = loot;
        setResistance(10);
        setSoundType(SoundType.STONE);
        setHarvestLevel(ToolClass.Pickaxe.name, harvestLevel.level);
    }

    @Override
    public String getTranslationKey() {
        return GtLocale.buildKey(super.getTranslationKey());
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
        Random rand = world instanceof World ? ((World) world).rand : JavaUtil.RANDOM;
        this.loot.accept(fortune, drops, rand);
        if (drops.isEmpty()) drops.add(new ItemStack(this.getItemDropped(state, rand, fortune)));
    }

    @Override
    public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
        return !(entity instanceof EntityDragon) && super.canEntityDestroy(state, world, pos, entity);
    }

    @Override
    public ResourceLocation getItemModel() {
        return new ResourceLocation(Reference.MODID, "ore/" + this.name);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer.Builder(this)
            .add(PropertyHelper.TEXTURE_INDEX_PROPERTY)
            .build();
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        PropertyHelper.DimensionalTextureInfo textures = null;
        byte dimension = renderAsOre(world, pos);
        if (dimension > -128) {
            Map<EnumFacing, EnumFacing> overrides = new HashMap<>();
            for (EnumFacing side : EnumFacing.VALUES) {
                int sideIndex = Math.abs(side.getIndex() ^ pos.getX() ^ pos.getY() ^ pos.getZ());
                if (!GregTechConfig.GENERAL.hiddenOres || sideIndex % 12 > 6) overrides.put(side, EnumFacing.byIndex(sideIndex % 6));
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
