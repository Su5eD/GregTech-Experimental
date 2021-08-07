package mods.gregtechmod.init;

import mods.gregtechmod.api.util.Reference;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

import java.util.HashMap;
import java.util.Map;

public class BakedModelLoader implements ICustomModelLoader {
    private static final Map<ResourceLocation, IModel> MODELS = new HashMap<>();

    public void register(String path, IModel model) {
        MODELS.put(new ResourceLocation(Reference.MODID, path), model);
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
    public void onResourceManagerReload(IResourceManager resourceManager) {}
}
