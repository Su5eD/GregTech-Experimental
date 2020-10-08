package mods.gregtechmod.init;

import ic2.core.model.IReloadableModel;
import ic2.core.model.ModelComparator;
import mods.gregtechmod.core.GregTechMod;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

import java.util.HashMap;
import java.util.Map;

public class BakedModelLoader implements ICustomModelLoader {

    private static final Map<ResourceLocation, IReloadableModel> MODELS = new HashMap<>();

    public void register(String path, IReloadableModel model) {
        register(new ResourceLocation(GregTechMod.MODID, path), model);
    }

    public void register(ResourceLocation location, IReloadableModel model) {
        MODELS.put(location, model);
    }

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return MODELS.containsKey(modelLocation);
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) {
        return MODELS.get(modelLocation);
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        for (IReloadableModel model : MODELS.values()) {
            model.onReload();
        }
        ModelComparator.onReload();
    }
}