package mods.gregtechmod.world;

import com.google.common.base.Predicate;
import mods.gregtechmod.api.BlockItems;
import mods.gregtechmod.api.GregTechConfig;
import mods.gregtechmod.api.WorldOres;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkGeneratorEnd;
import net.minecraft.world.gen.ChunkGeneratorHell;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

public class OreGenerator implements IWorldGenerator {
    public static final String RETRO_NAME = "GregTechOreGen";
    public static OreGenerator instance = new OreGenerator();
    public static final Predicate<IBlockState> MATCHER = blockstate -> blockstate.getBlock() == Blocks.STONE || blockstate.getBlock() == Blocks.NETHERRACK || blockstate.getBlock() == Blocks.END_STONE;
    public static final Predicate<IBlockState> MATCHER_END = blockstate -> blockstate.getBlock() == Blocks.AIR || blockstate.getBlock() == Blocks.END_STONE;

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        generateWorld(random, chunkX*16, chunkZ*16, world, true, chunkGenerator);
    }

    public void generateWorld(Random random, int chunkX, int chunkZ, World world, boolean newGen, IChunkGenerator chunkGenerator) {
        if (!newGen && !GregTechConfig.WORLDGEN.retrogen) return;

        random = new Random(random.nextInt());
        Biome biome = world.getBiomeForCoordsBody(new BlockPos(chunkX+8, 0, chunkZ+8));
        DimensionType dimensionType = DimensionType.OVERWORLD;

        if (biome.equals(Biomes.HELL) || world.provider.getDimensionType() == DimensionType.NETHER || chunkGenerator instanceof ChunkGeneratorHell) {
            dimensionType = DimensionType.NETHER;
        }
        else if (biome.equals(Biomes.SKY) || world.provider.getDimensionType() == DimensionType.THE_END || chunkGenerator instanceof ChunkGeneratorEnd) {
            generateEnd(world, random, chunkX, chunkZ);
            dimensionType = DimensionType.THE_END;
        }
        for (WorldOres ore : WorldOres.values()) {
            if (ore.enabled && dimensionType == ore.dimension && (ore.biomeList.isEmpty() || ore.biomeList.contains(biome.getRegistryName())) && (ore.probability <= 1 || random.nextInt(ore.probability) == 0)) {
                if (ore.type == WorldOres.OreType.NORMAL) genNormalOre(ore.getOre(), MATCHER, world, random, chunkX, chunkZ, ore.size, ore.amount, ore.minY, ore.maxY);
                else if (ore.type == WorldOres.OreType.SINGLE) genSingleOre(ore.getOre(), MATCHER, world, random, chunkX, chunkZ, ore.amount, ore.minY, ore.maxY);
                else if (ore.type == WorldOres.OreType.SINGLE_UNDER_LAVA) genSingleBlockUnderLava(ore.getOre(), MATCHER, world, random, chunkX, chunkZ, ore.amount, ore.minY, ore.maxY);
            }
        }

        if (!newGen) {
            world.getChunk(chunkX, chunkZ).markDirty();
        }
    }

    public void genNormalOre(Block block, Predicate<IBlockState> matcher, World world, Random random, int chunkX, int chunkZ, int size, int amount, int minY, int maxY) {
        WorldGenMinable ore = new WorldGenMinable(block.getDefaultState(), size, matcher);
        for (int i = 0; i < amount; i++) {
            int posX = chunkX + random.nextInt(16);
            int posY = minY + random.nextInt(maxY - minY);
            int posZ = chunkZ + random.nextInt(16);
            ore.generate(world, random, new BlockPos(posX, posY, posZ));
        }
    }

    public void genSingleOre(Block oreBlock, Predicate<IBlockState> matcher, World world, Random random, int chunkX, int chunkZ, int amount, int minY, int maxY) {
        for (int i = 0; i < amount; i++) {
            BlockPos orePos = getOrePos(random, chunkX, chunkZ, minY, maxY);
            IBlockState state = world.getBlockState(orePos);
            Block block = state.getBlock();
            if (block != Blocks.AIR && (block.isReplaceableOreGen(state, world, orePos, matcher))) {
                world.setBlockState(orePos, oreBlock.getDefaultState());
            }
        }
    }

    public void genSingleBlockUnderLava(Block oreBlock, Predicate<IBlockState> matcher, World world, Random random, int chunkX, int chunkZ, int amount, int minY, int maxY) {
        for (int i = 0; i < amount; i++) {
            BlockPos orePos = getOrePos(random, chunkX, chunkZ, minY, maxY);
            IBlockState state = world.getBlockState(orePos);
            Block block = state.getBlock();
            if (block.isReplaceableOreGen(state, world, orePos, matcher) || block.isReplaceableOreGen(state, world, orePos, matcher) && (world.getBlockState(orePos.offset(EnumFacing.UP)) == Blocks.LAVA.getDefaultState() || world.getBlockState(orePos.offset(EnumFacing.UP)) == Blocks.FLOWING_LAVA.getDefaultState()))
                world.setBlockState(orePos, oreBlock.getDefaultState());
        }
    }

    private void generateEnd(World world, Random random, int chunkX, int chunkZ) {
        if (GregTechConfig.WORLDGEN.endAsteroids && random.nextInt(100) == 0) {
            int posY = 10 + random.nextInt(237);
            if (random.nextInt(25) == 0) {
                new WorldGenMinable(Blocks.END_STONE.getDefaultState(), 100 + random.nextInt(101), MATCHER_END).generate(world, random, new BlockPos(chunkX + random.nextInt(16), posY, chunkZ + random.nextInt(16)));
                if (GregTechConfig.WORLDGEN.tungstate)
                    for (int j = 0; j < 5; j++)
                        new WorldGenMinable(BlockItems.Ores.tungstate.getInstance().getDefaultState(), 16, MATCHER_END).generate(world, random, new BlockPos(chunkX - 8 + random.nextInt(24), posY + random.nextInt(41), chunkZ - 8 + random.nextInt(24)));
                if (GregTechConfig.WORLDGEN.sheldonite)
                    for (int j = 0; j < 2; j++)
                        new WorldGenMinable(BlockItems.Ores.sheldonite.getInstance().getDefaultState(), 4, MATCHER_END).generate(world, random, new BlockPos(chunkX - 8 + random.nextInt(24), posY + random.nextInt(41), chunkZ - 8 + random.nextInt(24)));
                if (GregTechConfig.WORLDGEN.olivine)
                    for (int j = 0; j < 7; j++)
                        new WorldGenMinable(BlockItems.Ores.olivine.getInstance().getDefaultState(), 8, MATCHER_END).generate(world, random, new BlockPos(chunkX - 8 + random.nextInt(24), posY + random.nextInt(41), chunkZ - 8 + random.nextInt(24)));
                if (GregTechConfig.WORLDGEN.sodalite)
                    for (int j = 0; j < 12; j++)
                        new WorldGenMinable(BlockItems.Ores.sodalite.getInstance().getDefaultState(), 16, MATCHER_END).generate(world, random, new BlockPos(chunkX - 8 + random.nextInt(24), posY + random.nextInt(41), chunkZ - 8 + random.nextInt(24)));
            }
            int i;
            for (i = 0; i < 5; i++)
                (new WorldGenMinable(Blocks.END_STONE.getDefaultState(), 30 + random.nextInt(31), MATCHER_END)).generate(world, random, new BlockPos(chunkX + random.nextInt(16), posY + random.nextInt(51) - 25, chunkZ + random.nextInt(16)));
            if (GregTechConfig.WORLDGEN.tungstate)
                for (i = 0; i < 5; i++)
                    new WorldGenMinable(BlockItems.Ores.tungstate.getInstance().getDefaultState(), 12, MATCHER_END).generate(world, random, new BlockPos(chunkX - 8 + random.nextInt(24), posY + random.nextInt(41) - 20, chunkZ - 8 + random.nextInt(24)));
            if (GregTechConfig.WORLDGEN.sheldonite)
                for (i = 0; i < 1; i++)
                    new WorldGenMinable(BlockItems.Ores.sheldonite.getInstance().getDefaultState(), 4, MATCHER_END).generate(world, random, new BlockPos(chunkX - 8 + random.nextInt(24), posY + random.nextInt(41) - 20, chunkZ - 8 + random.nextInt(24)));
            if (GregTechConfig.WORLDGEN.olivine)
                for (i = 0; i < 3; i++)
                    new WorldGenMinable(BlockItems.Ores.olivine.getInstance().getDefaultState(), 8, MATCHER_END).generate(world, random, new BlockPos(chunkX - 8 + random.nextInt(24), posY + random.nextInt(41) - 20, chunkZ - 8 + random.nextInt(24)));
        }
        if (GregTechConfig.WORLDGEN.tungstate)
            for (int i = 0; i < 4; i++)
                new WorldGenMinable(BlockItems.Ores.tungstate.getInstance().getDefaultState(), 16, MATCHER).generate(world, random, new BlockPos(chunkX + random.nextInt(16), random.nextInt(128), chunkZ + random.nextInt(16)));
        if (GregTechConfig.WORLDGEN.sheldonite)
            for (int i = 0; i < 1; i++)
                new WorldGenMinable(BlockItems.Ores.sheldonite.getInstance().getDefaultState(), 4, MATCHER).generate(world, random, new BlockPos(chunkX + random.nextInt(16), random.nextInt(128), chunkZ + random.nextInt(16)));
        if (GregTechConfig.WORLDGEN.olivine)
            for (int i = 0; i < 5; i++)
                new WorldGenMinable(BlockItems.Ores.olivine.getInstance().getDefaultState(), 8, MATCHER).generate(world, random, new BlockPos(chunkX + random.nextInt(16), random.nextInt(128), chunkZ + random.nextInt(16)));
        if (GregTechConfig.WORLDGEN.sodalite)
            for (int i = 0; i < 8; i++)
                new WorldGenMinable(BlockItems.Ores.sodalite.getInstance().getDefaultState(), 16, MATCHER).generate(world, random, new BlockPos(chunkX + random.nextInt(16), random.nextInt(128), chunkZ + random.nextInt(16)));
    }

    public static BlockPos getOrePos(Random random, int chunkX, int chunkZ, int minY, int maxY) {
        int posX = chunkX + random.nextInt(16);
        int posY = minY + random.nextInt(maxY - minY);
        int posZ = chunkZ + random.nextInt(16);
        return new BlockPos(posX, posY, posZ);
    }

    @SubscribeEvent
    public void onChunkDataSave(ChunkDataEvent.Save event) {
        NBTTagCompound genTag = event.getData().getCompoundTag(RETRO_NAME);
        if (!genTag.hasKey("generated")) {
            genTag.setBoolean("generated", true);
        }
        event.getData().setTag(RETRO_NAME, genTag);
    }

    /*@SubscribeEvent
    public void onChunkDataLoad(ChunkDataEvent.Load event) {
        int dim = event.getWorld().provider.getDimension();

        boolean regen = false;
        NBTTagCompound tag = (NBTTagCompound) event.getData().getTag(RETRO_NAME);
        ChunkPos coord = event.getChunk().getPos();

        if (tag != null) {
            boolean generated = OregenConfig.RETROGEN && !tag.hasKey("generated");
            if (generated) {
                if (OregenConfig.VERBOSE) {
                    MyMod.logger.log(Level.DEBUG, "Queuing Retrogen for chunk: " + coord.toString() + ".");
                }
                regen = true;
            }
        } else {
            regen = OregenConfig.RETROGEN;
        }

        if (regen) {
            ArrayDeque<ChunkPos> chunks = WorldTickHandler.chunksToGen.get(dim);

            if (chunks == null) {
                WorldTickHandler.chunksToGen.put(dim, new ArrayDeque<>(128));
                chunks = WorldTickHandler.chunksToGen.get(dim);
            }
            if (chunks != null) {
                chunks.addLast(coord);
                WorldTickHandler.chunksToGen.put(dim, chunks);
            }
        }
    }*/
}