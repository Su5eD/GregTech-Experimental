package mods.gregtechmod.init;

import com.google.gson.JsonObject;
import ic2.api.item.ElectricItem;
import ic2.api.item.IC2Items;
import ic2.core.item.ItemFluidCell;
import mods.gregtechmod.api.GregTechObjectAPI;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.api.util.TriFunction;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.model.*;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.GregTechTEBlock;
import mods.gregtechmod.objects.blocks.teblocks.TileEntityButtonPanel;
import mods.gregtechmod.objects.blocks.teblocks.TileEntityRedstoneDisplay;
import mods.gregtechmod.objects.blocks.teblocks.TileEntityRedstoneScale;
import mods.gregtechmod.objects.blocks.teblocks.TileEntityShelf;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityIndustrialCentrifugeBase;
import mods.gregtechmod.objects.covers.*;
import mods.gregtechmod.objects.items.ItemCellClassic;
import mods.gregtechmod.objects.items.base.ItemArmorElectricBase;
import mods.gregtechmod.util.*;
import mods.gregtechmod.util.struct.Rotor;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import one.util.streamex.EntryStream;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@EventBusSubscriber(modid = Reference.MODID, value = Side.CLIENT)
public final class ClientEventHandler {
    private static final Map<ItemStack, Supplier<String>> EXTRA_TOOLTIPS = new HashMap<>();
    private static final String MACHINES_PATH = "blocks/machines/";

    private ClientEventHandler() {}

    public static void gatherModItems() {
        EXTRA_TOOLTIPS.put(IC2Items.getItem("dust", "coal"), () -> "C2");
        EXTRA_TOOLTIPS.put(IC2Items.getItem("dust", "iron"), () -> "Fe");
        EXTRA_TOOLTIPS.put(IC2Items.getItem("dust", "gold"), () -> "Au");
        EXTRA_TOOLTIPS.put(IC2Items.getItem("dust", "copper"), () -> "Cu");
        EXTRA_TOOLTIPS.put(IC2Items.getItem("dust", "tin"), () -> "Sn");
        EXTRA_TOOLTIPS.put(IC2Items.getItem("dust", "bronze"), () -> "SnCu3");
        EXTRA_TOOLTIPS.put(ModHandler.getModItem("energycontrol", "item_kit", 800), () -> GtLocale.translateItemDescription("sensor_kit"));
        EXTRA_TOOLTIPS.put(ModHandler.getModItem("energycontrol", "item_card", 800), () -> GtLocale.translateItemDescription("sensor_card"));
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        BlockItemLoader.getBlocks()
            .forEach(block -> {
                Item blockItem = Item.getItemFromBlock(block);
                if (blockItem != Items.AIR) {
                    if (block instanceof ICustomItemModel) registerModel(blockItem, ((ICustomItemModel) block).getItemModel());
                    else registerModel(blockItem);
                }
            });

        StreamEx.of(BlockItemLoader.getAllItems())
            .select(ICustomItemModel.class)
            .forEach(item -> registerModel((Item) item, item.getItemModel()));

        registerBakedModels();

        Arrays.stream(GregTechTEBlock.values())
            .filter(GregTechTEBlock::isStructure)
            .filter(GregTechTEBlock::hasItem)
            .map(GregTechTEBlock::getName)
            .forEach(name -> {
                ItemStack stack = GregTechObjectAPI.getTileEntity(name);
                ResourceLocation location = new ResourceLocation(Reference.MODID, "teblock/" + name + "_valid");
                ModelLoader.setCustomModelResourceLocation(stack.getItem(), stack.getMetadata(), new ModelResourceLocation(location, name));
            });
    }

    private static void registerBakedModels() {
        GregTechMod.LOGGER.info("Registering baked models");
        BakedModelLoader loader = new BakedModelLoader();
        JsonObject models = JsonHandler.readAssetJSON("blockstates/teblock.json").getAsJsonObject("variants").getAsJsonObject("type");

        for (GregTechTEBlock teBlock : GregTechTEBlock.values()) {
            switch (teBlock.getModelType()) {
                case BAKED:
                    registerBakedModel(teBlock, models, loader, ModelTeBlock::new);
                    break;
                case CONNECTED:
                    registerConnectedBakedModel(loader, teBlock.getName(), "machines", "", ModelTEBlockConnected::new);
                    break;
                case ELECTRIC_BUFFER:
                    registerElectricBufferModel(teBlock.getName(), models, loader);
                    break;
                case BUTTON_PANEL:
                    registerBakedModel(teBlock, models, loader,
                        (particle, textures) -> new ModelTextureMode("button_panel", MACHINES_PATH, EnumSet.allOf(TileEntityButtonPanel.PanelMode.class), particle, textures));
                    break;
                case REDSTONE_DISPLAY:
                    registerBakedModel(teBlock, models, loader,
                        (particle, textures) -> new ModelTextureMode("redstone_display", MACHINES_PATH, EnumSet.allOf(TileEntityRedstoneDisplay.DisplayMode.class), particle, textures));
                    break;
                case REDSTONE_SCALE:
                    registerBakedModel(teBlock, models, loader,
                        (particle, textures) -> new ModelTextureMode("redstone_scale", MACHINES_PATH, EnumSet.allOf(TileEntityRedstoneScale.ScaleMode.class), particle, textures));
                    break;
                case SHELF:
                    registerJsonBakedModel(teBlock, models, loader, (json, particle, textures) -> {
                        JsonObject object = json.json.getAsJsonObject("typeTextures");
                        Map<TileEntityShelf.Type, ResourceLocation> typeTextures = EntryStream.of(object.entrySet().iterator())
                            .mapKeys(name -> TileEntityShelf.Type.valueOf(name.toUpperCase(Locale.ROOT)))
                            .mapValues(texture -> new ResourceLocation(texture.getAsString()))
                            .toImmutableMap();
                        return new ModelShelf(particle, textures, typeTextures);
                    });
                    break;
                case COMPARTMENT:
                    registerBakedModel(teBlock, models, loader, ModelCompartment::new);
                    break;
                case MACHINE_BOX:
                    registerJsonBakedModel(teBlock, models, loader, (json, particle, textures) -> {
                        Map<Integer, Map<EnumFacing, ResourceLocation>> tierTextures = IntStreamEx.range(2, 6)
                            .mapToEntry(i -> i, i -> json.generateTextureMap("textures_tier_" + i))
                            .toImmutableMap();
                        return new ModelMachineBox(particle, textures, tierTextures);
                    });
                    break;
            }
        }

        registerBlockConnectedBakedModels(loader);

        for (BlockItems.Ore ore : BlockItems.Ore.values()) {
            String name = ore.name().toLowerCase(Locale.ROOT);
            JsonHandler json = new JsonHandler(getItemModelPath("ore", name));
            loader.register("models/block/ore/" + name, new ModelBlockOre(json.particle, json.generateTextureMap(), json.generateTextureMap("textures_nether"), json.generateTextureMap("textures_end")));
        }

        ModelLoaderRegistry.registerLoader(loader);
    }
    
    private static void registerBakedModel(GregTechTEBlock teBlock, JsonObject models, BakedModelLoader loader, BiFunction<ResourceLocation, Map<EnumFacing, ResourceLocation>, IModel> factory) {
        registerJsonBakedModel(teBlock, models, loader, (json, particle, textures) -> factory.apply(particle, textures));
    }

    private static void registerJsonBakedModel(GregTechTEBlock teBlock, JsonObject models, BakedModelLoader loader, TriFunction<JsonHandler, ResourceLocation, Map<EnumFacing, ResourceLocation>, IModel> factory) {
        String name = teBlock.getName();
        JsonHandler json = getTeBlockModel(name, models);
        IModel model;
        if (teBlock.isStructure()) {
            JsonHandler valid = new JsonHandler(getItemModelPath("teblock", name + "_valid"));
            model = new ModelStructureTeBlock(json.particle, json.generateTextureMap(), valid.generateTextureMap());
        }
        else {
            model = factory.apply(json, json.particle, json.generateTextureMap());
        }
        loader.register("models/block/" + name, model);

        if (teBlock.hasActive()) {
            JsonHandler active = new JsonHandler(getItemModelPath("teblock", name + "_active"));
            loader.register("models/block/" + name + "_active", factory.apply(json, json.particle, active.generateTextureMap()));
        }
    }

    private static void registerElectricBufferModel(String name, JsonObject models, BakedModelLoader loader) {
        JsonHandler json = getTeBlockModel(name, models);
        ResourceLocation textureDown = json.getResouceLocationElement("textureDown");
        ResourceLocation textureDownRedstone = json.getResouceLocationElement("textureDownRedstone");

        ModelElectricBuffer model = new ModelElectricBuffer(json.particle, json.generateTextureMap(), json.generateTextureMap("texturesRedstone"), textureDown, textureDownRedstone);
        loader.register("models/block/" + name, model);
    }

    private static JsonHandler getTeBlockModel(String name, JsonObject models) {
        JsonObject obj = Objects.requireNonNull(models.getAsJsonObject(name), "Missing blockstate model definition for TEBlock " + name);

        String modelPath = new ResourceLocation(obj.get("model").getAsString()).getPath();
        return new JsonHandler(getItemModelPath("teblock", modelPath));
    }

    private static void registerBlockConnectedBakedModels(BakedModelLoader loader) {
        Arrays.stream(BlockItems.Block.values())
            .filter(BlockItems.Block::hasConnectedModel)
            .forEach(block -> {
                Block instance = block.getBlockInstance();
                ModelLoader.setCustomStateMapper(instance, NormalStateMapper.INSTANCE);

                String name = block.name().toLowerCase(Locale.ROOT);
                String rotorTextures = block.getExtraTextures();

                if (rotorTextures != null) registerBlockConnectedBakedModel(loader, instance, name, getRotorTextures(rotorTextures));
                else registerBlockConnectedBakedModel(loader, instance, name);
            });
    }

    /**
     * Get all rotor textures from specific texture paths,
     * which follow the format <code>blocks/machines/{@literal <name>}/rotor_{@literal <texture part>}</code>
     *
     * @see Rotor#TEXTURE_PARTS
     */
    private static Map<String, ResourceLocation> getRotorTextures(String name) {
        return StreamEx.of(Rotor.TEXTURE_PARTS)
            .flatMap(str -> StreamEx.of("rotor_" + str, "rotor_" + str + "_active"))
            .toMap(str -> new ResourceLocation(Reference.MODID, "blocks/machines/" + name + "/" + str));
    }

    @SafeVarargs
    private static void registerBlockConnectedBakedModel(BakedModelLoader loader, Block block, String name, Map<String, ResourceLocation>... extraTextures) {
        registerConnectedBakedModel(loader, name, "connected", "block_", textures -> new ModelBlockConnected(block, textures, extraTextures));
    }

    /**
     * Register a connected baked model using specific texture paths,
     * which follow the format <code>blocks/{@literal <dir>}/{@literal <name>}/{@literal <name>}{@literal <texture part>}</code>
     *
     * @see ModelBlockConnected#TEXTURE_PARTS
     */
    private static void registerConnectedBakedModel(BakedModelLoader loader, String name, String dir, String prefix, Function<Map<String, ResourceLocation>, IModel> modelFactory) {
        String basePath = "blocks/" + dir + "/" + name + "/" + name;
        Map<String, ResourceLocation> textures = StreamEx.of(ModelBlockConnected.TEXTURE_PARTS)
            .toMap(str -> {
                String resPath = str.isEmpty() ? basePath : basePath + "_" + str;
                return new ResourceLocation(Reference.MODID, resPath);
            });

        loader.register("models/block/" + prefix + name, modelFactory.apply(textures));
    }

    private static void registerModel(Item item) {
        registerModel(item, item.getRegistryName());
    }

    private static void registerModel(Item item, ResourceLocation path) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(path, "inventory"));
    }

    private static String getItemModelPath(String... paths) {
        return "models/item/" + String.join("/", paths) + ".json";
    }

    @SubscribeEvent
    public static void registerIcons(TextureStitchEvent.Pre event) {
        TextureMap map = event.getMap();

        StreamEx.of(
                MACHINES_PATH + "adv_machine_screen_random", //TODO: Remove when implemented in another machine
                MACHINES_PATH + "adv_machine_pipe_blue",
                MACHINES_PATH + "adv_machine_pipe_blue_redstone",
                MACHINES_PATH + "centrifuge/centrifuge_top_active2",
                MACHINES_PATH + "centrifuge/centrifuge_top_active3",
                MACHINES_PATH + "centrifuge/centrifuge_side_active2",
                MACHINES_PATH + "centrifuge/centrifuge_side_active3",
                MACHINES_PATH + "computer_cube/computer_cube_reactor",
                MACHINES_PATH + "computer_cube/computer_cube_scanner",
                MACHINES_PATH + "hatch_maintenance/hatch_maintenance_front_ducttape",
                MACHINES_PATH + "lesu/lesu_lv_out",
                MACHINES_PATH + "lesu/lesu_mv_out",
                MACHINES_PATH + "lesu/lesu_hv_out",
                MACHINES_PATH + "machine_top_pipe"
            )
            .map(str -> new ResourceLocation(Reference.MODID, str))
            .append(
                CoverActiveDetector.TEXTURE,
                CoverConveyor.TEXTURE,
                CoverCrafting.TEXTURE,
                CoverDrain.TEXTURE,
                CoverEnergyOnly.TEXTURE,
                CoverEnergyMeter.TEXTURE,
                CoverItemMeter.TEXTURE,
                CoverLiquidMeter.TEXTURE,
                CoverMachineController.TEXTURE,
                CoverNormal.TEXTURE_NORMAL,
                CoverNormal.TEXTURE_NOREDSTONE,
                CoverPump.TEXTURE,
                CoverRedstoneConductor.TEXTURE,
                CoverRedstoneOnly.TEXTURE,
                CoverRedstoneSignalizer.TEXTURE,
                CoverSolarPanel.TEXTURE,
                CoverValve.TEXTURE
            )
            .append(StreamEx.of(CoverVent.VentType.values())
                .map(CoverVent.VentType::getIcon))
            .append(StreamEx.of(FluidLoader.FLUIDS)
                .map(FluidLoader.IFluidProvider::getTexture))
            .forEach(map::registerSprite);
    }

    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
        EntityPlayer player = event.getEntityPlayer();
        boolean fullInvisibility = player.isInvisible() && StreamEx.of(player.inventory.armorInventory)
            .remove(ItemStack::isEmpty)
            .anyMatch(stack -> {
                Item item = stack.getItem();
                return item instanceof ItemArmorElectricBase
                    && ((ItemArmorElectricBase) item).perks.contains(ArmorPerk.INVISIBILITY_FIELD)
                    && ElectricItem.manager.canUse(stack, 10000);
            });

        if (fullInvisibility) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        List<String> tooltip = event.getToolTip();

        EntryStream.of(EXTRA_TOOLTIPS)
            .filterKeys(stack::isItemEqual)
            .values()
            .map(Supplier::get)
            .findFirst()
            .ifPresent(str -> tooltip.add(1, str));

        FluidStack fluidStack = FluidUtil.getFluidContained(stack);
        Item item = stack.getItem();
        if (fluidStack != null && TileEntityIndustrialCentrifugeBase.isCell(item) && !(item instanceof ItemCellClassic)) {
            Fluid fluid = fluidStack.getFluid();
            StreamEx.of(FluidLoader.FLUIDS)
                .filter(provider -> provider.getFluid() == fluid)
                .map(FluidLoader.IFluidProvider::getDescription)
                .nonNull()
                .findFirst()
                .ifPresent(desc -> tooltip.add(item instanceof ItemFluidCell ? 2 : 1, desc));
        }
    }
}
