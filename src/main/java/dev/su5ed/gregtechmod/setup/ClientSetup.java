package dev.su5ed.gregtechmod.setup;

import dev.su5ed.gregtechmod.block.ConnectedBlock;
import dev.su5ed.gregtechmod.cover.*;
import dev.su5ed.gregtechmod.model.ConnectedModelLoader;
import dev.su5ed.gregtechmod.model.CoverableModelLoader;
import dev.su5ed.gregtechmod.model.OreModelLoader;
import dev.su5ed.gregtechmod.object.GTBlockEntity;
import dev.su5ed.gregtechmod.object.ModBlock;
import dev.su5ed.gregtechmod.object.Ore;
import dev.su5ed.gregtechmod.util.BlockItemProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import one.util.streamex.StreamEx;

import java.util.function.Supplier;
import java.util.stream.Stream;

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

        registerBlockProviderModels(Ore.values(), OreModelLoader::new);
        registerBlockProviderModels(GTBlockEntity.values(), CoverableModelLoader::new);
    }

    @SubscribeEvent
    public void onTextureStitchEvent(TextureStitchEvent.Pre event) {
        if (event.getAtlas().location().equals(InventoryMenu.BLOCK_ATLAS)) {
            StreamEx.of(ScreenCover.TEXTURE)
                .append(
                    ActiveDetectorCover.TEXTURE,
                    ConveyorCover.TEXTURE,
                    CraftingCover.TEXTURE,
                    DrainCover.TEXTURE,
                    EnergyOnlyCover.TEXTURE,
                    EnergyMeterCover.TEXTURE,
                    ItemMeterCover.TEXTURE,
                    LiquidMeterCover.TEXTURE,
                    MachineControllerCover.TEXTURE,
                    NormalCover.TEXTURE_NORMAL,
                    NormalCover.TEXTURE_NOREDSTONE,
                    PumpCover.TEXTURE,
                    RedstoneConductorCover.TEXTURE,
                    RedstoneOnlyCover.TEXTURE,
                    RedstoneSignalizerCover.TEXTURE,
                    SolarPanelCover.TEXTURE,
                    ValveCover.TEXTURE
                )
                .append(Stream.of(VentCover.VentType.values())
                    .map(VentCover.VentType::getIcon))
                .forEach(event::addSprite);
        }
    }

    private static void registerBlockProviderModels(BlockItemProvider[] providers, Supplier<IModelLoader<?>> supplier) {
        StreamEx.of(providers)
            .map(provider -> provider.getBlock().getRegistryName().getPath())
            .map(ClientSetup::getLoaderLocation)
            .forEach(location -> ModelLoaderRegistry.registerLoader(location, supplier.get()));
    }

    public static ResourceLocation getLoaderLocation(String name) {
        return location(name + "_model");
    }
}
