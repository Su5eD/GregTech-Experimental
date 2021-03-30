package mods.gregtechmod.init;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import ic2.api.item.IC2Items;
import ic2.core.item.ItemFluidCell;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.core.GregTechTEBlock;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.TileEntityIndustrialCentrifuge;
import mods.gregtechmod.render.RenderBlockOre;
import mods.gregtechmod.render.RenderTeBlock;
import mods.gregtechmod.util.*;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Mod.EventBusSubscriber(value = Side.CLIENT)
public class ClientEventHandler {
    private static final List<UUID> GT_CAPES;
    private static final List<UUID> CAPES;
    private static final ResourceLocation GT_CAPE_TEXTURE = new ResourceLocation(Reference.MODID, "textures/gregorious_cape.png");
    private static final ResourceLocation CAPE_TEXTURE = new ResourceLocation(Reference.MODID, "textures/gregtech_cape.png");
    private static ItemStack dustCoal = ItemStack.EMPTY;
    private static ItemStack dustIron = ItemStack.EMPTY;
    private static ItemStack dustGold = ItemStack.EMPTY;
    private static ItemStack dustCopper = ItemStack.EMPTY;
    private static ItemStack dustTin = ItemStack.EMPTY;
    private static ItemStack dustBronze = ItemStack.EMPTY;
    public static ItemStack sensorKit = ItemStack.EMPTY;
    public static ItemStack sensorCard = ItemStack.EMPTY;

    static {
        List<UUID> gtCapes = new ArrayList<>();
        gtCapes.add(UUID.fromString("989e39a1-7d39-4829-87f1-286a06fab3bd")); // Su5eD
        GT_CAPES = Collections.unmodifiableList(gtCapes);

        List<UUID> capes = new ArrayList<>();
        try {
            Path path = GtUtil.getModFile().getPath("assets", Reference.MODID, "GregTechCapes.txt");
            Files.newBufferedReader(path).lines()
                    .forEach(line -> {
                        String id = (line.contains("#") ? line.split("#")[0] : line).trim();
                        capes.add(UUID.fromString(id));
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        CAPES = Collections.unmodifiableList(capes);
    }

    public static void gatherModItems() {
        dustCoal = IC2Items.getItem("dust", "coal");
        dustIron = IC2Items.getItem("dust", "iron");
        dustGold = IC2Items.getItem("dust", "gold");
        dustCopper = IC2Items.getItem("dust", "copper");
        dustTin = IC2Items.getItem("dust", "tin");
        dustBronze = IC2Items.getItem("dust", "bronze");
        sensorKit = ModHandler.getModItem("energycontrol", "item_kit", 800);
        sensorCard = ModHandler.getModItem("energycontrol", "item_card", 800);
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        BlockItemLoader.BLOCKS
                .forEach(block -> {
                    Item blockItem = Item.getItemFromBlock(block);
                    if (blockItem == Items.AIR) return;
                    if (block instanceof IBlockCustomItem) registerModel(blockItem, 0, ((IBlockCustomItem)block).getItemModel());
                    else registerModel(blockItem);
                });

        BlockItemLoader.ITEMS.stream()
                .filter(item -> item instanceof IModelInfoProvider)
                .forEach(item -> {
                    ModelInformation info = ((IModelInfoProvider) item).getModelInformation();
                    registerModel(item, info.metadata, info.path);
                });
        registerBakedModels();
    }

    private static void registerModel(Item item) {
        registerModel(item, 0, item.getRegistryName());
    }

    private static void registerModel(Item item, int metadata, ResourceLocation path) {
        ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(path, "inventory"));
    }

    public static void registerBakedModels() {
        GregTechMod.logger.info("Registering baked models");
        BakedModelLoader loader = new BakedModelLoader();
        for (GregTechTEBlock teBlock : GregTechTEBlock.values()) {
            try {
                if (teBlock.hasBakedModel()) {
                    String name = teBlock.getName();
                    JsonHandler json = new JsonHandler(name, "teblock");
                    loader.register("models/block/"+name, new RenderTeBlock(json.generateMapFromJSON("textures"), json.particle));
                    if (teBlock.hasActive()) {
                        json = new JsonHandler(name+"_active", "teblock");
                        loader.register("models/block/"+name+"_active", new RenderTeBlock(json.generateMapFromJSON("textures"), json.particle));
                    }
                }
            } catch (Exception e) {
                GregTechMod.logger.error(e.getMessage());
            }
        }
        for (BlockItems.Ore ore : BlockItems.Ore.values()) {
            JsonHandler json = new JsonHandler(ore.name().toLowerCase(Locale.ROOT), "ore");
            loader.register("models/block/ore/"+ore.name().toLowerCase(Locale.ROOT), new RenderBlockOre(json.generateMapFromJSON("textures"), json.generateMapFromJSON("textures_nether"), json.generateMapFromJSON("textures_end"), json.particle));
        }
        ModelLoaderRegistry.registerLoader(loader);
    }

    @SubscribeEvent
    public static void registerIcons(TextureStitchEvent.Pre event) {
        TextureMap map = event.getMap();
        String path = "blocks/covers/";
        String centrifuge = "blocks/machines/centrifuge/";
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"adv_machine_vent"));
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"adv_machine_vent_rotating"));
        map.registerSprite(new ResourceLocation(Reference.MODID, centrifuge+"centrifuge_top_active2"));
        map.registerSprite(new ResourceLocation(Reference.MODID, centrifuge+"centrifuge_top_active3"));
        map.registerSprite(new ResourceLocation(Reference.MODID, centrifuge+"centrifuge_side_active2"));
        map.registerSprite(new ResourceLocation(Reference.MODID, centrifuge+"centrifuge_side_active3"));
        map.registerSprite(new ResourceLocation(Reference.MODID, "blocks/machines/adv_machine_screen_random")); //TODO: Remove when implemented in another machine
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"machine_vent_rotating"));
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"drain"));
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"active_detector"));
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"eu_meter"));
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"item_meter"));
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"liquid_meter"));
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"normal"));
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"noredstone"));
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"machine_controller"));
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"solar_panel"));
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"crafting"));
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"conveyor"));
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"pump"));
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"valve"));
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"energy_only"));
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"redstone_only"));
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"redstone_conductor"));
        map.registerSprite(new ResourceLocation(Reference.MODID, path+"redstone_signalizer"));

        FluidLoader.FLUIDS.forEach(provider -> map.registerSprite(provider.getTexture()));
    }

    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
        if (GtUtil.getFullInvisibility(event.getEntityPlayer())) event.setCanceled(true);

        if (GregTechConfig.GENERAL.showCapes) {
            AbstractClientPlayer clientPlayer = (AbstractClientPlayer) event.getEntityPlayer();
            UUID playerId = clientPlayer.getUniqueID();
            boolean gtCape = GT_CAPES.contains(playerId);
            boolean cape = CAPES.contains(playerId);
            if ((gtCape || cape) && clientPlayer.hasPlayerInfo() && clientPlayer.getLocationCape() == null) {
                NetworkPlayerInfo playerInfo = ObfuscationReflectionHelper.getPrivateValue(AbstractClientPlayer.class, clientPlayer, "field_175157_a");
                Map<MinecraftProfileTexture.Type, ResourceLocation> playerTextures = ObfuscationReflectionHelper.getPrivateValue(NetworkPlayerInfo.class, playerInfo, "field_187107_a");
                playerTextures.put(MinecraftProfileTexture.Type.CAPE, gtCape ? GT_CAPE_TEXTURE : CAPE_TEXTURE);
            }
        }
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        List<String> tooltip = event.getToolTip();

        if (stack.isItemEqual(dustCoal)) tooltip.add(1, "C2");
        else if (stack.isItemEqual(dustIron)) tooltip.add(1, "Fe");
        else if (stack.isItemEqual(dustGold)) tooltip.add(1, "Au");
        else if (stack.isItemEqual(dustCopper)) tooltip.add(1, "Cu");
        else if (stack.isItemEqual(dustTin)) tooltip.add(1, "Sn");
        else if (stack.isItemEqual(dustBronze)) tooltip.add(1, "SnCu3");
        else if (stack.isItemEqual(sensorKit)) tooltip.add(1, GtUtil.translateItemDescription("sensor_kit"));
        else if (stack.isItemEqual(sensorCard)) tooltip.add(1, GtUtil.translateItemDescription("sensor_card"));

        FluidStack fluidContained = FluidUtil.getFluidContained(stack);
        Item item = stack.getItem();
        if (TileEntityIndustrialCentrifuge.isCell(item) && fluidContained != null) {
            FluidLoader.FLUIDS.forEach(provider -> {
                if (provider.getFluid() == fluidContained.getFluid()) {
                    String description = provider.getDescription();
                    if (description != null) tooltip.add(item instanceof ItemFluidCell ? 2 : 1, description);
                }
            });
        }
    }
}
