package dev.su5ed.gregtechmod;

import dev.su5ed.gregtechmod.block.ConnectedBlock;
import dev.su5ed.gregtechmod.model.ConnectedModelLoader;
import dev.su5ed.gregtechmod.object.ModBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import one.util.streamex.StreamEx;

import static dev.su5ed.gregtechmod.api.util.Reference.location;

public final class ClientSetup {
    static final ClientSetup INSTANCE = new ClientSetup();

    private ClientSetup() {}

    @SubscribeEvent
    public void onModelRegistryEvent(ModelRegistryEvent event) {
        StreamEx.of(ModBlock.values())
            .filter(block -> block.getBlock() instanceof ConnectedBlock)
            .map(ModBlock::getName)
            .forEach(name -> {
                ResourceLocation location = getLoaderLocation(name);
                ConnectedModelLoader loader = new ConnectedModelLoader(name);
                ModelLoaderRegistry.registerLoader(location, loader);
            });
    }

    public static ResourceLocation getLoaderLocation(String name) {
        return location(name + "_model");
    }
}
