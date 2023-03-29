package dev.su5ed.gtexperimental;

import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.compat.ModHandler;
import dev.su5ed.gtexperimental.cover.ActiveDetectorCover;
import dev.su5ed.gtexperimental.cover.ConveyorCover;
import dev.su5ed.gtexperimental.cover.CraftingCover;
import dev.su5ed.gtexperimental.cover.DrainCover;
import dev.su5ed.gtexperimental.cover.EnergyMeterCover;
import dev.su5ed.gtexperimental.cover.EnergyOnlyCover;
import dev.su5ed.gtexperimental.cover.ItemMeterCover;
import dev.su5ed.gtexperimental.cover.LiquidMeterCover;
import dev.su5ed.gtexperimental.cover.MachineControllerCover;
import dev.su5ed.gtexperimental.cover.NormalCover;
import dev.su5ed.gtexperimental.cover.PumpCover;
import dev.su5ed.gtexperimental.cover.RedstoneConductorCover;
import dev.su5ed.gtexperimental.cover.RedstoneOnlyCover;
import dev.su5ed.gtexperimental.cover.RedstoneSignalizerCover;
import dev.su5ed.gtexperimental.cover.ScreenCover;
import dev.su5ed.gtexperimental.cover.SolarPanelCover;
import dev.su5ed.gtexperimental.cover.ValveCover;
import dev.su5ed.gtexperimental.cover.VentCover;
import dev.su5ed.gtexperimental.item.LithiumBatteryItem;
import dev.su5ed.gtexperimental.model.ConnectedModelLoader;
import dev.su5ed.gtexperimental.model.CoverableModel;
import dev.su5ed.gtexperimental.model.CoverableModelLoader;
import dev.su5ed.gtexperimental.model.OreModelLoader;
import dev.su5ed.gtexperimental.object.Component;
import dev.su5ed.gtexperimental.object.ModFluid;
import dev.su5ed.gtexperimental.object.ModMenus;
import dev.su5ed.gtexperimental.screen.DestructorPackScreen;
import dev.su5ed.gtexperimental.screen.SimpleMachineScreen;
import dev.su5ed.gtexperimental.screen.SonictronScreen;
import dev.su5ed.gtexperimental.util.ItemProvider;
import dev.su5ed.gtexperimental.util.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.DynamicFluidContainerModel;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import one.util.streamex.StreamEx;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@EventBusSubscriber(modid = Reference.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public final class ClientSetup {
    private static final ItemColor BUCKET_ITEM_COLOR = new DynamicFluidContainerModel.Colors();

    private static final Map<BlockPos, SoundInstance> SINGLE_PLAYING_SOUNDS = new HashMap<>();

    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        GregTechMod.LOGGER.info("Client setup started");

        event.enqueueWork(() -> {
            ItemProperties.register(
                Component.LITHIUM_RE_BATTERY.getItem(),
                LithiumBatteryItem.CHARGE_PROPERTY,
                (stack, level, entity, seed) -> ModHandler.getEnergyCharge(stack) > 0 ? 1 : 0
            );
            registerScreens();
        });

        Options options = Minecraft.getInstance().options;
        ClientEventHandler.KEY_MAP.put(options.keyJump.getKey().getValue(), KeyboardHandler.Key.JUMP);
        ClientEventHandler.KEY_MAP.put(options.keySprint.getKey().getValue(), KeyboardHandler.Key.SPRINT);
        ClientEventHandler.KEY_MAP.put(GLFW.GLFW_KEY_LEFT_ALT, KeyboardHandler.Key.LEFT_ALT);

        StreamEx.of(ModFluid.values())
            .flatMap(provider -> StreamEx.of(provider.getSourceFluid(), provider.getFlowingFluid()))
            .forEach(fluid -> ItemBlockRenderTypes.setRenderLayer(fluid, RenderType.translucent()));
    }

    @SubscribeEvent
    public static void onModelRegistryEvent(ModelEvent.RegisterGeometryLoaders event) {
        event.register(ConnectedModelLoader.NAME.getPath(), new ConnectedModelLoader());
        event.register(OreModelLoader.NAME.getPath(), new OreModelLoader());
        event.register(CoverableModelLoader.NAME.getPath(), new CoverableModelLoader());
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
                .append(CoverableModel.OUTPUT_SIDE_TEXTURES.values())
                .forEach(event::addSprite);
        }
    }

    @SubscribeEvent
    public static void registerItemColorHandlers(RegisterColorHandlersEvent.Item event) {
        event.register(BUCKET_ITEM_COLOR, StreamEx.of(ModFluid.values())
            .map(ItemProvider::getItem)
            .toArray(ItemLike[]::new));
    }

    public static void playSingleSound(SoundEvent sound, float pitch) {
        Minecraft minecraft = Minecraft.getInstance();
        BlockPos pos = minecraft.player.blockPosition();
        SoundInstance playing = SINGLE_PLAYING_SOUNDS.get(pos);
        if (playing == null) {
            SoundInstance instance = SimpleSoundInstance.forUI(sound, pitch, 1);
            SINGLE_PLAYING_SOUNDS.put(pos, instance);
            minecraft.getSoundManager().play(instance);
        }
        else if (!minecraft.getSoundManager().isActive(playing)) {
            SINGLE_PLAYING_SOUNDS.remove(pos);
        }
    }

    private static void registerScreens() {
        MenuScreens.register(ModMenus.DESTRUCTORPACK.get(), DestructorPackScreen::new);
        MenuScreens.register(ModMenus.SONICTRON.get(), SonictronScreen::new);
        MenuScreens.register(ModMenus.AUTO_MACERATOR.get(), SimpleMachineScreen.AutomaticMaceratorScreen::new);
        MenuScreens.register(ModMenus.AUTO_EXTRACTOR.get(), SimpleMachineScreen.AutomaticExtractorScreen::new);
        MenuScreens.register(ModMenus.AUTO_COMPRESSOR.get(), SimpleMachineScreen.AutomaticCompressorScreen::new);
        MenuScreens.register(ModMenus.AUTO_RECYCLER.get(), SimpleMachineScreen.AutomaticRecyclerScreen::new);
        MenuScreens.register(ModMenus.AUTO_ELECTRIC_FURNACE.get(), SimpleMachineScreen.AUtomaticElectricFurnaceScreen::new);
    }
}
