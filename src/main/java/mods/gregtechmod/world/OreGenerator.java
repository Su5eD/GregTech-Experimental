package mods.gregtechmod.world;

import com.google.common.base.Predicate;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.objects.WorldOre;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Arrays;
import java.util.Random;

@SuppressWarnings("Guava")
public final class OreGenerator implements IWorldGenerator {
    public static final OreGenerator INSTANCE = new OreGenerator();
    public static final Predicate<IBlockState> MATCHER = blockstate -> blockstate.getBlock() == Blocks.STONE || blockstate.getBlock() == Blocks.NETHERRACK || blockstate.getBlock() == Blocks.END_STONE;
    public static final Predicate<IBlockState> MATCHER_VOID = blockstate -> blockstate.getBlock() == Blocks.AIR || blockstate.getBlock() == Blocks.STONE || blockstate.getBlock() == Blocks.NETHERRACK || blockstate.getBlock() == Blocks.END_STONE;

    private OreGenerator() {}

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        generateWorld(random, chunkX, chunkZ, world, true);
    }

    public void generateWorld(Random random, int chunkX, int chunkZ, World world, boolean newGen) {
        if (!newGen && !GregTechConfig.WORLDGEN.retrogen) return;

        int xPos = chunkX * 16;
        int zPos = chunkZ * 16;
        Biome biome = world.getBiomeForCoordsBody(new BlockPos(xPos + 8, 0, zPos + 8));
        DimensionType worldDimension = world.provider.getDimensionType();
        DimensionType oreDimension;

        if (biome.equals(Biomes.HELL) || worldDimension == DimensionType.NETHER) {
            oreDimension = DimensionType.NETHER;
        }
        else if (biome.equals(Biomes.SKY) || worldDimension == DimensionType.THE_END) {
            if (GregTechConfig.WORLDGEN.endAsteroids) generateEndAsteroids(world, random, xPos, zPos);
            oreDimension = DimensionType.THE_END;
        }
        else oreDimension = DimensionType.OVERWORLD;

        Arrays.stream(WorldOre.values())
            .filter(ore -> ore.enabled.getAsBoolean() && oreDimension == ore.dimension && (ore.biomeList.isEmpty() || ore.biomeList.contains(biome.getRegistryName())) && (ore.probability <= 1 || random.nextInt(ore.probability) == 0))
            .forEach(ore -> {
                Block block = ore.block.getBlockInstance();
                if (ore.type == WorldOre.OreType.NORMAL) {
                    if (ore.dimension == DimensionType.THE_END) genEndOre(block, MATCHER, world, random, xPos, zPos, ore.size, ore.amount);
                    else genNormalOre(block, GregTechConfig.WORLDGEN.generateInVoid ? MATCHER_VOID : MATCHER, world, random, xPos, zPos, ore.size, ore.amount, ore.minY, ore.maxY);
                }
                else if (ore.type == WorldOre.OreType.SINGLE) genSingleOre(block, MATCHER, world, random, xPos, zPos, ore.amount, ore.minY, ore.maxY);
                else if (ore.type == WorldOre.OreType.SINGLE_UNDER_LAVA) genSingleBlockUnderLava(block, MATCHER, world, random, xPos, zPos, ore.amount, ore.minY, ore.maxY);
            });

        if (!newGen) {
            world.getChunk(chunkX, chunkZ).markDirty();
        }
    }

    public void genNormalOre(Block block, Predicate<IBlockState> matcher, World world, Random random, int xPos, int zPos, int size, int amount, int minY, int maxY) {
        WorldGenMinable ore = new WorldGenMinable(block.getDefaultState(), size, matcher);
        int heightDiff = maxY - minY + 1;
        for (int i = 0; i < amount; i++) {
            int posX = xPos + random.nextInt(16);
            int posY = minY + random.nextInt(heightDiff);
            int posZ = zPos + random.nextInt(16);
            ore.generate(world, random, new BlockPos(posX, posY, posZ));
        }
    }

    public void genSingleOre(Block oreBlock, Predicate<IBlockState> matcher, World world, Random random, int chunkX, int chunkZ, int amount, int minY, int maxY) {
        for (int i = 0; i < amount; i++) {
            BlockPos orePos = getOrePos(random, chunkX, chunkZ, minY, maxY).add(8, 0, 8);
            IBlockState state = world.getBlockState(orePos);
            Block block = state.getBlock();
            if (GregTechConfig.WORLDGEN.generateInVoid && block.isReplaceableOreGen(state, world, orePos, MATCHER_VOID) || block.isReplaceableOreGen(state, world, orePos, matcher)) {
                world.setBlockState(orePos, oreBlock.getDefaultState());
            }
        }
    }

    public void genSingleBlockUnderLava(Block oreBlock, Predicate<IBlockState> matcher, World world, Random random, int chunkX, int chunkZ, int amount, int minY, int maxY) {
        for (int i = 0; i < amount; i++) {
            BlockPos orePos = getOrePos(random, chunkX, chunkZ, minY, maxY).add(8, 0, 8);
            IBlockState state = world.getBlockState(orePos);
            Block block = state.getBlock();
            if ((GregTechConfig.WORLDGEN.generateInVoid && block.isReplaceableOreGen(state, world, orePos, MATCHER_VOID) || block.isReplaceableOreGen(state, world, orePos, matcher))
                && (world.getBlockState(orePos.offset(EnumFacing.UP)).getBlock() == Blocks.LAVA || world.getBlockState(orePos).getBlock() == Blocks.FLOWING_LAVA)) {
                world.setBlockState(orePos, oreBlock.getDefaultState());
            }
        }
    }

    public void genEndOre(Block block, Predicate<IBlockState> matcher, World world, Random random, int chunkX, int chunkZ, int size, int amount) {
        WorldGenMinable ore = new WorldGenMinable(block.getDefaultState(), size, matcher);
        for (int i = 0; i < amount; i++) {
            ore.generate(world, random, new BlockPos(chunkX + random.nextInt(16), random.nextInt(128), chunkZ + random.nextInt(16)));
        }
    }

    private void generateEndAsteroids(World world, Random random, int chunkX, int chunkZ) {
        if (random.nextInt(100) == 0) {
            int posY = 10 + random.nextInt(237);
            if (random.nextInt(25) == 0) {
                new WorldGenMinable(Blocks.END_STONE.getDefaultState(), 100 + random.nextInt(101), MATCHER_VOID)
                    .generate(world, random, new BlockPos(chunkX + random.nextInt(16), posY, chunkZ + random.nextInt(16)));
                if (GregTechConfig.WORLDGEN.tungstate) {
                    genLargeEndAsteroid(world, random, chunkX, chunkZ, posY, WorldOre.TUNGSTATE, WorldOre.TUNGSTATE.amount * 5 / 4F);
                }
                if (GregTechConfig.WORLDGEN.sheldonite) {
                    genLargeEndAsteroid(world, random, chunkX, chunkZ, posY, WorldOre.SHELDONITE, WorldOre.SHELDONITE.amount + 1);
                }
                if (GregTechConfig.WORLDGEN.olivine) {
                    genLargeEndAsteroid(world, random, chunkX, chunkZ, posY, WorldOre.OLIVINE, WorldOre.OLIVINE.amount * 7 / 5F);
                }
                if (GregTechConfig.WORLDGEN.sodalite) {
                    genLargeEndAsteroid(world, random, chunkX, chunkZ, posY, WorldOre.SODALITE, WorldOre.SODALITE.amount * 1.5F);
                }
            }
            for (int i = 0; i < 5; i++)
                new WorldGenMinable(Blocks.END_STONE.getDefaultState(), 30 + random.nextInt(31), MATCHER_VOID)
                    .generate(world, random, new BlockPos(chunkX + random.nextInt(16), posY + random.nextInt(51) - 25, chunkZ + random.nextInt(16)));
            if (GregTechConfig.WORLDGEN.tungstate) {
                genSmallEndAsteroid(world, random, chunkX, chunkZ, posY, WorldOre.TUNGSTATE, WorldOre.TUNGSTATE.size * 0.75, WorldOre.TUNGSTATE.amount * 5 / 4F);
            }
            if (GregTechConfig.WORLDGEN.sheldonite) {
                genSmallEndAsteroid(world, random, chunkX, chunkZ, posY, WorldOre.SHELDONITE, WorldOre.SHELDONITE.size, WorldOre.SHELDONITE.amount);
            }
            if (GregTechConfig.WORLDGEN.olivine) {
                genSmallEndAsteroid(world, random, chunkX, chunkZ, posY, WorldOre.OLIVINE, WorldOre.OLIVINE.size, WorldOre.OLIVINE.amount * 0.6);
            }
        }
    }

    private static void genSmallEndAsteroid(World world, Random random, int chunkX, int chunkZ, int posY, WorldOre worldOre, double size, double amount) {
        WorldGenMinable ore = new WorldGenMinable(worldOre.block.getBlockInstance().getDefaultState(), (int) size, MATCHER_VOID);
        for (int i = 0; i < amount; i++) {
            ore.generate(world, random, new BlockPos(chunkX - 8 + random.nextInt(24), posY + random.nextInt(41) - 20, chunkZ - 8 + random.nextInt(24)));
        }
    }

    private static void genLargeEndAsteroid(World world, Random random, int chunkX, int chunkZ, int posY, WorldOre worldOre, double amount) {
        WorldGenMinable ore = new WorldGenMinable(worldOre.block.getBlockInstance().getDefaultState(), worldOre.size, MATCHER_VOID);
        for (int i = 0; i < amount; i++) {
            ore.generate(world, random, new BlockPos(chunkX - 8 + random.nextInt(24), posY + random.nextInt(41), chunkZ - 8 + random.nextInt(24)));
        }
    }

    public static BlockPos getOrePos(Random random, int chunkX, int chunkZ, int minY, int maxY) {
        int posX = chunkX + random.nextInt(16);
        int posY = minY + random.nextInt(maxY - minY);
        int posZ = chunkZ + random.nextInt(16);
        return new BlockPos(posX, posY, posZ);
    }
}
