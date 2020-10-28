package mods.gregtechmod.world;

import gnu.trove.map.hash.TIntObjectHashMap;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayDeque;
import java.util.Random;

public class RetrogenHandler {
    public static RetrogenHandler instance = new RetrogenHandler();

    public static TIntObjectHashMap<ArrayDeque<ChunkPos>> chunksToGen = new TIntObjectHashMap<>();

    @SubscribeEvent
    public void tickEnd(TickEvent.WorldTickEvent event) {
        if (event.side != Side.SERVER) return;

        if (event.phase == TickEvent.Phase.END) {
            World world = event.world;
            int dimension = world.provider.getDimension();
            ArrayDeque<ChunkPos> chunks = chunksToGen.get(dimension);

            if (chunks != null && !chunks.isEmpty()) {
                ChunkPos c = chunks.pollFirst();
                long worldSeed = world.getSeed();
                Random rand = new Random(worldSeed);
                long xSeed = rand.nextLong() >> 2 + 1L;
                long zSeed = rand.nextLong() >> 2 + 1L;
                rand.setSeed(xSeed * c.x + zSeed * c.z ^ worldSeed);
                OreGenerator.instance.generateWorld(rand, c.x*16, c.z*16, world, false, null);
                chunksToGen.put(dimension, chunks);
            } else if (chunks != null) {
                chunksToGen.remove(dimension);
            }
        }
    }
}
