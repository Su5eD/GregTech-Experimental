package dev.su5ed.gregtechmod;

import dev.su5ed.gregtechmod.api.util.Reference;
import dev.su5ed.gregtechmod.block.ConnectedBlock;
import dev.su5ed.gregtechmod.model.ConnectedModelLoader;
import dev.su5ed.gregtechmod.object.ModObjects;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import one.util.streamex.StreamEx;

public final class ClientSetup {
    static final ClientSetup INSTANCE = new ClientSetup();

    private ClientSetup() {}

    @SubscribeEvent
    public void onModelRegistryEvent(ModelRegistryEvent event) {
        StreamEx.of(ModObjects.ModBlock.values())
            .filter(block -> block.getBlockInstance() instanceof ConnectedBlock)
            .map(ModObjects.ModBlock::getName)
            .forEach(name -> {
                ResourceLocation location = getLoaderLocation(name);
                ConnectedModelLoader loader = new ConnectedModelLoader(name);
                ModelLoaderRegistry.registerLoader(location, loader);
            });
    }

    public static ResourceLocation getLoaderLocation(String name) {
        return new ResourceLocation(Reference.MODID, name + "_model");
    }
}
