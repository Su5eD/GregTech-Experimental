package mods.gregtechmod.common.init;

import ic2.core.model.IReloadableModel;
import ic2.core.model.ModelComparator;
import mods.gregtechmod.common.core.GregtechMod;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

import java.util.HashMap;
import java.util.Map;

public class BakedModelLoader implements ICustomModelLoader {

    private static Map<ResourceLocation, IReloadableModel> models = new HashMap<>();

    public void register(String path, IReloadableModel model) {
        register(new ResourceLocation(GregtechMod.MODID, path), model);
    }

    public void register(ResourceLocation location, IReloadableModel model) {
        models.put(location, model);
    }

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return models.containsKey(modelLocation);
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws Exception {
        return models.get(modelLocation);
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        for (IReloadableModel model : models.values()) {
            model.onReload();
        }
        ModelComparator.onReload();
    }
}