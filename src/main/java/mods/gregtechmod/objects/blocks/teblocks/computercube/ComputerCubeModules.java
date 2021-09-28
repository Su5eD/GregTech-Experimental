package mods.gregtechmod.objects.blocks.teblocks.computercube;

import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.util.nbt.NBTSaveHandler;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

public class ComputerCubeModules { // TODO API
    static final Map<ResourceLocation, Pair<BooleanSupplier, Function<TileEntityComputerCube, IComputerCubeModule>>> MODULES = new LinkedHashMap<>();

    public static IComputerCubeModule getModule(ResourceLocation name, TileEntityComputerCube te) {
        Function<TileEntityComputerCube, IComputerCubeModule> supplier = MODULES.get(name).getRight();
        if (supplier == null) throw new IllegalArgumentException("Module with name " + name + " not found");
        return supplier.apply(te);
    }

    public static void registerModule(ResourceLocation name, Class<?> clazz, BooleanSupplier enable, Function<TileEntityComputerCube, IComputerCubeModule> factory) {
        if (MODULES.containsKey(name))
            throw new IllegalArgumentException("A module with name " + name + " already exists");

        NBTSaveHandler.initClass(clazz);
        MODULES.put(name, Pair.of(enable, factory));
    }

    public enum Module {
        MAIN(ComputerCubeMain.class, () -> true, te -> ComputerCubeMain.INSTANCE),
        REACTOR(ComputerCubeReactor.class, () -> GregTechConfig.FEATURES.reactorPlanner, ComputerCubeReactor::new);

        public final ResourceLocation name;
        private final Class<?> clazz;
        private final BooleanSupplier enable;
        private final Function<TileEntityComputerCube, IComputerCubeModule> factory;

        Module(Class<?> clazz, BooleanSupplier enable, Function<TileEntityComputerCube, IComputerCubeModule> factory) {
            this.name = new ResourceLocation(Reference.MODID, name().toLowerCase(Locale.ROOT));
            this.clazz = clazz;
            this.enable = enable;
            this.factory = factory;
        }

        public static void registerModules() {
            for (Module module : values()) {
                ComputerCubeModules.registerModule(module.name, module.clazz, module.enable, module.factory);
            }
        }
    }
}
