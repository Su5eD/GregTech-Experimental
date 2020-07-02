package mods.gregtechmod.client;

import mods.gregtechmod.common.core.CommonProxy;
import mods.gregtechmod.common.core.GregtechMod;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerModel(Item item, int metadata) {
        registerModel(item, metadata, null, null);
    }

    @Override
    public void registerModel(Item item, int metadata, String prefix, String itemName)  {
        ResourceLocation location = (prefix != null ? new ResourceLocation(String.format("%s:%s/%s", GregtechMod.MODID, prefix, itemName)) : item.getRegistryName());
        ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(location, "inventory"));
    }
}
