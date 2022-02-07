package dev.su5ed.gregtechmod;

import dev.su5ed.gregtechmod.model.ConnectedModelLoader;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class ClientSetup {
    static final ClientSetup INSTANCE = new ClientSetup();
    
    private ClientSetup() {}
    
    @SubscribeEvent
    public void onModelRegistryEvent(ModelRegistryEvent event) {
        ModelLoaderRegistry.registerLoader(ConnectedModelLoader.LOCATION, new ConnectedModelLoader("advanced_machine_casing"));
    }
}
