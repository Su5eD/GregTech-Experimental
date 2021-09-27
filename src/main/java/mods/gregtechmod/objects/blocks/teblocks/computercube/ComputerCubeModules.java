package mods.gregtechmod.objects.blocks.teblocks.computercube;

import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ComputerCubeModules {
    private static final Map<ResourceLocation, Supplier<IComputerCubeModule>> MODULES = new LinkedHashMap<>();
    
    public static IComputerCubeModule getModule(ResourceLocation name) {
        Supplier<IComputerCubeModule> supplier = MODULES.get(name);
        if (supplier == null) throw new IllegalArgumentException("Module with name " + name + " not found");
        return supplier.get();
    }
    
    public static void registerModule(ResourceLocation name, Supplier<IComputerCubeModule> supplier) {
        if (MODULES.containsKey(name)) throw new IllegalArgumentException("A module with name " + name + " already exists");
        MODULES.put(name, supplier);
    }
    
    public static IComputerCubeModule getNextModule(ResourceLocation current) {
        List<ResourceLocation> list = new ArrayList<>(MODULES.keySet());
        int size = list.size();
        
        for (int i = 0; i < size; i++) {
            ResourceLocation loc = list.get(i);
            if (current.equals(loc)) {
                ResourceLocation next = list.get((i + 1) % size);
                return getModule(next);
            }
        }
        
        throw new IllegalArgumentException("Module " + current + " not found");
    }
}
