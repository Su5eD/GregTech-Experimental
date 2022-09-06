package dev.su5ed.gregtechmod;

import dev.su5ed.gregtechmod.api.util.Reference;
import dev.su5ed.gregtechmod.block.ConnectedBlock;
import dev.su5ed.gregtechmod.compat.ModHandler;
import dev.su5ed.gregtechmod.cover.ActiveDetectorCover;
import dev.su5ed.gregtechmod.cover.ConveyorCover;
import dev.su5ed.gregtechmod.cover.CraftingCover;
import dev.su5ed.gregtechmod.cover.DrainCover;
import dev.su5ed.gregtechmod.cover.EnergyMeterCover;
import dev.su5ed.gregtechmod.cover.EnergyOnlyCover;
import dev.su5ed.gregtechmod.cover.ItemMeterCover;
import dev.su5ed.gregtechmod.cover.LiquidMeterCover;
import dev.su5ed.gregtechmod.cover.MachineControllerCover;
import dev.su5ed.gregtechmod.cover.NormalCover;
import dev.su5ed.gregtechmod.cover.PumpCover;
import dev.su5ed.gregtechmod.cover.RedstoneConductorCover;
import dev.su5ed.gregtechmod.cover.RedstoneOnlyCover;
import dev.su5ed.gregtechmod.cover.RedstoneSignalizerCover;
import dev.su5ed.gregtechmod.cover.ScreenCover;
import dev.su5ed.gregtechmod.cover.SolarPanelCover;
import dev.su5ed.gregtechmod.cover.ValveCover;
import dev.su5ed.gregtechmod.cover.VentCover;
import dev.su5ed.gregtechmod.item.LithiumBatteryItem;
import dev.su5ed.gregtechmod.model.ConnectedModelLoader;
import dev.su5ed.gregtechmod.model.CoverableModelLoader;
import dev.su5ed.gregtechmod.model.OreModelLoader;
import dev.su5ed.gregtechmod.object.Component;
import dev.su5ed.gregtechmod.object.GTBlockEntity;
import dev.su5ed.gregtechmod.object.ModBlock;
import dev.su5ed.gregtechmod.object.ModContainers;
import dev.su5ed.gregtechmod.object.Ore;
import dev.su5ed.gregtechmod.screen.DestructorPackScreen;
import dev.su5ed.gregtechmod.util.BlockItemProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import one.util.streamex.StreamEx;

import java.util.function.Supplier;
import java.util.stream.Stream;

import static dev.su5ed.gregtechmod.api.util.Reference.location;

@EventBusSubscriber(modid = Reference.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientSetup {

    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        GregTechMod.LOGGER.info("Client setup started");

        event.enqueueWork(() -> {
            ItemProperties.register(
                Component.LITHIUM_RE_BATTERY.getItem(),
                LithiumBatteryItem.CHARGE_PROPERTY,
                (stack, level, entity, seed) -> ModHandler.getEnergyCharge(stack) > 0 ? 1 : 0
            );
            MenuScreens.register(ModContainers.DESTRUCTORPACK.get(), DestructorPackScreen::new);
        });
    }

    @SubscribeEvent
    public static void onModelRegistryEvent(ModelRegistryEvent event) {
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
    public static void onTextureStitchEvent(TextureStitchEvent.Pre event) {
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

    public static void playSound(SoundEvent sound, float pitch) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level != null && minecraft.player != null) {
            minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), sound, SoundSource.BLOCKS, 1, pitch);
        }
    }
}
