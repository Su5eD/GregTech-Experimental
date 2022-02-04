package mods.gregtechmod.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;

import javax.annotation.Nullable;
import java.io.File;

public class DummyWorld extends World {
    public static final DummyWorld INSTANCE = new DummyWorld();

    private DummyWorld() {
        this(new DummySaveHandler(), new WorldInfo(new NBTTagCompound()), new DummyWorldProvider(), new Profiler(), false);
    }

    protected DummyWorld(ISaveHandler saveHandler, WorldInfo info, WorldProvider provider, Profiler profiler, boolean client) {
        super(saveHandler, info, provider, profiler, client);
    }

    @Override
    protected IChunkProvider createChunkProvider() {
        return null;
    }

    @Override
    protected boolean isChunkLoaded(int x, int z, boolean allowEmpty) {
        return false;
    }

    private static class DummyWorldProvider extends WorldProvider {

        @Override
        public DimensionType getDimensionType() {
            return DimensionType.OVERWORLD;
        }
    }

    private static class DummySaveHandler implements ISaveHandler {

        @Nullable
        @Override
        public WorldInfo loadWorldInfo() {
            return null;
        }

        @Override
        public void checkSessionLock() {
        }

        @Override
        public IChunkLoader getChunkLoader(WorldProvider provider) {
            return null;
        }

        @Override
        public void saveWorldInfoWithPlayer(WorldInfo worldInformation, NBTTagCompound tagCompound) {
        }

        @Override
        public void saveWorldInfo(WorldInfo worldInformation) {
        }

        @Override
        public IPlayerFileData getPlayerNBTManager() {
            return null;
        }

        @Override
        public void flush() {
        }

        @Override
        public File getWorldDirectory() {
            return null;
        }

        @Override
        public File getMapFileFromName(String mapName) {
            return null;
        }

        @Override
        public TemplateManager getStructureTemplateManager() {
            return null;
        }
    }
}
