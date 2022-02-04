package mods.gregtechmod.world;

import gnu.trove.map.hash.TIntObjectHashMap;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.core.GregTechMod;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayDeque;
import java.util.Random;

public final class RetrogenHandler {
    public static final String RETRO_NAME = "GregTechOreGen";
    public static final RetrogenHandler INSTANCE = new RetrogenHandler();
    public TIntObjectHashMap<ArrayDeque<ChunkPos>> chunksToGen = new TIntObjectHashMap<>();

    @SubscribeEvent
    public void tickEnd(TickEvent.WorldTickEvent event) {
        if (event.side == Side.SERVER && event.phase == TickEvent.Phase.END) {
            World world = event.world;
            int dimension = world.provider.getDimension();
            ArrayDeque<ChunkPos> chunks = this.chunksToGen.get(dimension);

            if (chunks != null && !chunks.isEmpty()) {
                ChunkPos c = chunks.pollFirst();
                long worldSeed = world.getSeed();
                Random rand = new Random(worldSeed);
                long xSeed = rand.nextLong() >> 2 + 1L;
                long zSeed = rand.nextLong() >> 2 + 1L;
                rand.setSeed(xSeed * c.x + zSeed * c.z ^ worldSeed);
                OreGenerator.INSTANCE.generateWorld(rand, c.x, c.z, world, false);
                this.chunksToGen.put(dimension, chunks);
            }
            else if (chunks != null) {
                this.chunksToGen.remove(dimension);
            }
        }
    }

    @SubscribeEvent
    public void onChunkDataSave(ChunkDataEvent.Save event) {
        NBTTagCompound genTag = event.getData().getCompoundTag(RETRO_NAME);
        if (!genTag.hasKey("generated")) {
            genTag.setBoolean("generated", true);
        }
        event.getData().setTag(RETRO_NAME, genTag);
    }

    @SubscribeEvent
    public void onChunkDataLoad(ChunkDataEvent.Load event) {
        int dim = event.getWorld().provider.getDimension();

        boolean regen = false;
        NBTTagCompound tag = (NBTTagCompound) event.getData().getTag(RETRO_NAME);
        ChunkPos coord = event.getChunk().getPos();

        if (tag != null) {
            boolean generated = GregTechConfig.WORLDGEN.retrogen && !tag.hasKey("generated");
            if (generated) {
                GregTechMod.LOGGER.debug("Queuing Retrogen for chunk: " + coord + ".");
                regen = true;
            }
        }
        else {
            regen = GregTechConfig.WORLDGEN.retrogen;
        }

        if (regen) {
            ArrayDeque<ChunkPos> chunks = this.chunksToGen.get(dim);

            if (chunks == null) {
                this.chunksToGen.put(dim, new ArrayDeque<>(128));
                chunks = this.chunksToGen.get(dim);
            }
            if (chunks != null) {
                chunks.addLast(coord);
                this.chunksToGen.put(dim, chunks);
            }
        }
    }
}
