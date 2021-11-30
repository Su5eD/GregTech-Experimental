package mods.gregtechmod.init;

import com.google.gson.JsonObject;
import ic2.api.item.ElectricItem;
import ic2.api.item.IC2Items;
import ic2.core.item.ItemFluidCell;
import mods.gregtechmod.api.GregTechObjectAPI;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.model.*;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.GregTechTEBlock;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityIndustrialCentrifugeBase;
import mods.gregtechmod.objects.items.ItemCellClassic;
import mods.gregtechmod.objects.items.base.ItemArmorElectricBase;
import mods.gregtechmod.util.*;
import mods.gregtechmod.util.struct.Rotor;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@EventBusSubscriber(modid = Reference.MODID, value = Side.CLIENT)
public class ClientEventHandler {
    private static final Map<ItemStack, Supplier<String>> EXTRA_TOOLTIPS = new HashMap<>();

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
                        if (block instanceof ICustomItemModel) registerModel(blockItem, ((ICustomItemModel)block).getItemModel());
                        else registerModel(blockItem);
                    }
                });

        BlockItemLoader.getAllItems().stream()
                .filter(ICustomItemModel.class::isInstance)
                .forEach(item -> {
                    ResourceLocation path = ((ICustomItemModel) item).getItemModel();
                    registerModel(item, path);
                });
        
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
        JsonObject models = JsonHandler.readJSON("blockstates/teblock.json").getAsJsonObject("variants").getAsJsonObject("type");

        for (GregTechTEBlock teBlock : GregTechTEBlock.values()) {
            String name = teBlock.getName();
            GregTechTEBlock.ModelType modelType = teBlock.getModelType();
            if (modelType == GregTechTEBlock.ModelType.BAKED) {
                JsonObject obj = models.getAsJsonObject(name);
                if (obj == null) throw new RuntimeException("Missing blockstate model definition for TEBlock " + name);
                
                String modelPath = new ResourceLocation(obj.get("model").getAsString()).getPath();
                JsonHandler json = new JsonHandler(getItemModelPath("teblock", modelPath));
                ModelTeBlock model;
                if (teBlock.isStructure()) {
                    JsonHandler valid = new JsonHandler(getItemModelPath("teblock", name + "_valid"));
                    model = new ModelStructureTeBlock(valid.particle, json.generateTextureMap(), valid.generateTextureMap());
                } else {
                    model = new ModelTeBlock(json.particle, json.generateTextureMap());
                }
                loader.register("models/block/" + name, model);

                if (teBlock.hasActive()) {
                    JsonHandler active = new JsonHandler(getItemModelPath("teblock", name + "_active"));
                    loader.register("models/block/" + name + "_active", new ModelTeBlock(json.particle, active.generateTextureMap()));
                }
            }
            else if (modelType == GregTechTEBlock.ModelType.CONNECTED) {
                registerConnectedBakedModel(loader, name, "machines", "", ModelTEBlockConnected::new);
            }
        }
        
        for (BlockItems.Ore ore : BlockItems.Ore.values()) {
            JsonHandler json = new JsonHandler(getItemModelPath("ore", ore.name().toLowerCase(Locale.ROOT)));
            loader.register("models/block/ore/" + ore.name().toLowerCase(Locale.ROOT), new ModelBlockOre(json.particle, json.generateTextureMap(), json.generateTextureMap("textures_nether"), json.generateTextureMap("textures_end")));
        }
        
        registerConnectedBakedModels(loader);
        
        ModelLoaderRegistry.registerLoader(loader);
    }
    
    private static void registerConnectedBakedModels(BakedModelLoader loader) {
        NormalStateMapper mapper = new NormalStateMapper();
        Stream.of(BlockItems.Block.STANDARD_MACHINE_CASING, BlockItems.Block.REINFORCED_MACHINE_CASING, BlockItems.Block.ADVANCED_MACHINE_CASING, BlockItems.Block.IRIDIUM_REINFORCED_TUNGSTEN_STEEL, BlockItems.Block.TUNGSTEN_STEEL)
                .map(BlockItems.Block::getBlockInstance)
                .forEach(block -> ModelLoader.setCustomStateMapper(block, mapper));

        Map<String, ResourceLocation> steamTurbineRotor = getRotorTextures("large_steam_turbine");
        Map<String, ResourceLocation> gasTurbineRotor = getRotorTextures("large_gas_turbine");
        registerBlockConnectedBakedModel(loader, "standard_machine_casing", steamTurbineRotor);
        registerBlockConnectedBakedModel(loader, "reinforced_machine_casing", gasTurbineRotor);
        registerBlockConnectedBakedModel(loader, "advanced_machine_casing");
        registerBlockConnectedBakedModel(loader, "iridium_reinforced_tungsten_steel");
        registerBlockConnectedBakedModel(loader, "tungsten_steel");
    }
    
    private static Map<String, ResourceLocation> getRotorTextures(String name) {
        return Rotor.TEXTURE_NAMES.stream()
                .flatMap(str -> Stream.of(str, str + "_active"))
                .collect(Collectors.toMap(str -> "rotor_" + str, str -> new ResourceLocation(Reference.MODID, "blocks/machines/" + name + "/" + str)));
    }
    
    @SafeVarargs
    private static void registerBlockConnectedBakedModel(BakedModelLoader loader, String name, Map<String, ResourceLocation>... extraTextures) {
        registerConnectedBakedModel(loader, name, "connected", "block_", ModelBlockConnected::new, extraTextures);
    }
    
    @SafeVarargs
    private static void registerConnectedBakedModel(BakedModelLoader loader, String name, String dir, String prefix, BiFunction<Map<String, ResourceLocation>, Map<String, ResourceLocation>[], IModel> modelFactory, Map<String, ResourceLocation>... extraTextures) {
        Path texturesPath = GtUtil.getAssetPath("textures");
        Path path = texturesPath.resolve("blocks/" + dir + "/" + name);
        try {
            Map<String, ResourceLocation> textures = Files.walk(path)
                    .filter(p -> !p.equals(path))
                    .map(p -> {
                        String str = p.toString().replace(texturesPath.toString(), "");
                        while (str.startsWith(File.separator) || str.startsWith("/")) str = str.substring(1);
                        
                        return str.substring(0, str.lastIndexOf('.')).replace(File.separatorChar, '/');
                    })
                    .collect(Collectors.toMap(str -> {
                        String textureName = str.substring(str.lastIndexOf('/') + 1).replace(name, "");
                        if (textureName.startsWith("_")) return textureName.substring(1);
                        else return textureName;
                    }, str -> new ResourceLocation(Reference.MODID, str)));

            loader.register("models/block/" + prefix + name, modelFactory.apply(textures, extraTextures));
        } catch (IOException e) {
            GregTechMod.LOGGER.error("Error registering connected baked model " + name, e);
        }
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
        String covers = "blocks/covers/";
        String machines = "blocks/machines/";
        
        Stream.of(
                covers + "adv_machine_vent",
                covers + "adv_machine_vent_rotating",
                machines + "centrifuge/centrifuge_top_active2",
                machines + "centrifuge/centrifuge_top_active3",
                machines + "centrifuge/centrifuge_side_active2",
                machines + "centrifuge/centrifuge_side_active3",
                machines + "adv_machine_screen_random", //TODO: Remove when implemented in another machine
                covers + "machine_vent_rotating",
                covers + "drain",
                covers + "active_detector",
                covers + "eu_meter",
                covers + "item_meter",
                covers + "liquid_meter",
                covers + "normal",
                covers + "noredstone",
                covers + "machine_controller",
                covers + "solar_panel",
                covers + "crafting",
                covers + "conveyor",
                covers + "pump",
                covers + "valve",
                covers + "energy_only",
                covers + "redstone_only",
                covers + "redstone_conductor",
                covers + "redstone_signalizer",
                machines + "machine_top_pipe",
                machines + "hatch_maintenance/hatch_maintenance_front_ducttape", 
                machines + "lesu/lesu_lv_out",
                machines + "lesu/lesu_mv_out",
                machines + "lesu/lesu_hv_out",
                machines + "computer_cube/computer_cube_reactor",
                machines + "computer_cube/computer_cube_scanner"
        )
                .map(str -> new ResourceLocation(Reference.MODID, str))
                .forEach(map::registerSprite);

        FluidLoader.FLUIDS.stream()
                .map(FluidLoader.IFluidProvider::getTexture)
                .forEach(map::registerSprite);
    }

    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
        EntityPlayer player = event.getEntityPlayer();
        boolean fullInvisibility = player.isInvisible() && player.inventory.armorInventory.stream()
                .filter(stack -> !stack.isEmpty())
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

        EXTRA_TOOLTIPS.entrySet().stream()
                .filter(entry -> entry.getKey().isItemEqual(stack))
                .map(Map.Entry::getValue)
                .map(Supplier::get)
                .findFirst()
                .ifPresent(str -> tooltip.add(1, str));

        FluidStack fluidStack = FluidUtil.getFluidContained(stack);
        Item item = stack.getItem();
        if (fluidStack != null && TileEntityIndustrialCentrifugeBase.isCell(item) && !(item instanceof ItemCellClassic)) {
            Fluid fluid = fluidStack.getFluid();
            FluidLoader.FLUIDS.stream()
                    .filter(provider -> provider.getFluid() == fluid)
                    .map(FluidLoader.IFluidProvider::getDescription)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .ifPresent(desc -> tooltip.add(item instanceof ItemFluidCell ? 2 : 1, desc));
        }
    }
}
