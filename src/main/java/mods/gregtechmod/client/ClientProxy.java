package mods.gregtechmod.client;

import ic2.core.util.LogCategory;
import mods.gregtechmod.client.render.RenderTeBlock;
import mods.gregtechmod.common.core.CommonProxy;
import mods.gregtechmod.common.core.GregtechMod;
import mods.gregtechmod.common.core.GregtechTeBlock;
import mods.gregtechmod.common.init.BakedModelLoader;
import mods.gregtechmod.common.util.JsonHandler;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;

import javax.annotation.Nullable;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
        this.registerBakedModels();
    }

    @Override
    public void registerModel(Item item, int metadata) {
        registerModel(item, metadata, null, null, null);
    }

    @Override
    public void registerModel(Item item, int metadata, String itemName, String prefix, @Nullable String folder)  {
        ResourceLocation location = (prefix != null || folder != null ? new ResourceLocation(String.format("%s:%s/%s", GregtechMod.MODID, folder != null ? folder : prefix, itemName)) : item.getRegistryName());
        ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(location, "inventory"));
    }

    private void registerBakedModels() {
        //init covers
        BakedModelLoader loader = new BakedModelLoader();
        for (GregtechTeBlock teBlock : GregtechTeBlock.values()) {
            try {
                if (teBlock.hasBakedModel()) {
                    String name = teBlock.getName();
                    JsonHandler json = new JsonHandler(name);
                    loader.register("models/block/"+name, new RenderTeBlock(json.textures, json.particle));
                    if (teBlock.hasActive()) {
                        json = new JsonHandler(name+"_active");
                        loader.register("models/block/"+name+"_active", new RenderTeBlock(json.textures, json.particle));
                    }
                }
            } catch (Exception e) {
                GregtechMod.LOGGER.error(LogCategory.General, e.getMessage());
            }
        }
        ModelLoaderRegistry.registerLoader(loader);
    }
}
