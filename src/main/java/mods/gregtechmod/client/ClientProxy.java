package mods.gregtechmod.client;

import mods.gregtechmod.common.core.CommonProxy;
import mods.gregtechmod.common.core.GregtechMod;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

import javax.annotation.Nullable;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerModel(Item item, int metadata) {
        registerModel(item, metadata, null, null, null);
    }

    @Override
    public void registerModel(Item item, int metadata, String itemName, String prefix, @Nullable String folder)  {
        ResourceLocation location = (prefix != null || folder != null ? new ResourceLocation(String.format("%s:%s/%s", GregtechMod.MODID, folder != null ? folder : prefix, itemName)) : item.getRegistryName());
        ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(location, "inventory"));
    }
}
