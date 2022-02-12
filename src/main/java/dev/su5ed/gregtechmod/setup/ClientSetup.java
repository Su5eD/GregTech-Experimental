package dev.su5ed.gregtechmod.setup;

import dev.su5ed.gregtechmod.block.ConnectedBlock;
import dev.su5ed.gregtechmod.model.ConnectedModelLoader;
import dev.su5ed.gregtechmod.model.OreModelLoader;
import dev.su5ed.gregtechmod.object.ModBlock;
import dev.su5ed.gregtechmod.object.Ore;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import one.util.streamex.StreamEx;

import static dev.su5ed.gregtechmod.api.util.Reference.location;

public final class ClientSetup {
    public static final ClientSetup INSTANCE = new ClientSetup();

    private ClientSetup() {}

    @SubscribeEvent
    public void onModelRegistryEvent(ModelRegistryEvent event) {
        StreamEx.of(ModBlock.values())
            .filter(block -> block.getBlock() instanceof ConnectedBlock)
            .forEach(block -> {
                String path = block.getBlock().getRegistryName().getPath();
                ResourceLocation location = getLoaderLocation(path);
                String name = block.getName();
                ConnectedModelLoader loader = new ConnectedModelLoader(name);
                ModelLoaderRegistry.registerLoader(location, loader);
            });
        
        StreamEx.of(Ore.values())
            .map(ore -> ore.getBlock().getRegistryName().getPath())
            .map(ClientSetup::getLoaderLocation)
            .forEach(location -> ModelLoaderRegistry.registerLoader(location, new OreModelLoader()));
    }

    public static ResourceLocation getLoaderLocation(String name) {
        return location(name + "_model");
    }
}
