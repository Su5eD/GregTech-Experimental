package mods.gregtechmod.core;

import ic2.api.event.TeBlockFinalCallEvent;
import ic2.api.item.IC2Items;
import ic2.core.block.BlockTileEntity;
import ic2.core.block.TeBlockRegistry;
import ic2.core.block.comp.Components;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.GregTechConfig;
import mods.gregtechmod.api.GregTechObjectAPI;
import mods.gregtechmod.api.util.GtUtil;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.cover.CoverHandler;
import mods.gregtechmod.init.CoverLoader;
import mods.gregtechmod.init.OreDictRegistrar;
import mods.gregtechmod.init.RecipeLoader;
import mods.gregtechmod.init.RegistryHandler;
import mods.gregtechmod.objects.blocks.tileentities.TileEntitySonictron;
import mods.gregtechmod.objects.blocks.tileentities.machines.TileEntityIndustrialCentrifuge;
import mods.gregtechmod.util.IProxy;
import mods.gregtechmod.util.LootFunctionWriteBook;
import mods.gregtechmod.util.SidedRedstoneEmitter;
import mods.gregtechmod.world.OreGenerator;
import mods.gregtechmod.world.RetrogenHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.io.File;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, acceptedMinecraftVersions = Reference.MC_VERSION,
     dependencies = "required-after:ic2@[2.8.221-ex112,]; after:energycontrol@[0.1.8,]")
public final class GregTechMod {

    public static final ResourceLocation COMMON_TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/gtcommon.png");
    @Instance
    public static GregTechMod instance;
    @SidedProxy(clientSide = "mods.gregtechmod.core.ClientProxy", serverSide = "mods.gregtechmod.core.ServerProxy")
    public static IProxy proxy;

    public static final CreativeTabs GREGTECH_TAB = new GregTechTab("gregtechtab");
    public static boolean postLoadFinished;
    public static File configDir;

    static {
        FluidRegistry.enableUniversalBucket();
    }

    @EventHandler
    public void start(FMLConstructionEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        GregTechAPI.logger = event.getModLog();

        GregTechAPI.logger.info("Pre-init started");
        configDir = event.getSuggestedConfigurationFile().getParentFile();
        MinecraftForge.EVENT_BUS.register(OreGenerator.instance);
        MinecraftForge.EVENT_BUS.register(RetrogenHandler.instance);

        RegistryHandler.registerFluids();
        Components.register(CoverHandler.class, "gtcover");
        Components.register(SidedRedstoneEmitter.class, "gtsidedemitter");
        CoverLoader.registerCovers();
        GameRegistry.registerWorldGenerator(OreGenerator.instance, 5);
        //TODO: Move to recipe loader(or modificator) class
        IC2Items.getItem("upgrade", "overclocker").getItem().setMaxStackSize(GregTechConfig.FEATURES.upgradeStackSize);
    }

    @EventHandler
    public static void init(FMLInitializationEvent event) {
        GtUtil.emptyCell = IC2Items.getItem("fluid_cell");
        GregTechTEBlock.buildDummies();
        TileEntityIndustrialCentrifuge.init();

        BlockTileEntity blockTE = TeBlockRegistry.get(GregTechTEBlock.LOCATION);
        Map<String, ItemStack> teblocks = Arrays.stream(GregTechTEBlock.VALUES).collect(Collectors.toMap(teblock -> teblock.getName().toLowerCase(Locale.ROOT), teblock -> new ItemStack(blockTE, 1, teblock.getId())));
        GregTechObjectAPI.setTileEntityMap(teblocks);

        OreDictRegistrar.registerItems();

        RecipeLoader.load();

        GregTechAPI.logger.debug("Registering loot");
        LootFunctionManager.registerFunction(new LootFunctionWriteBook.Serializer());
        LootTableList.register(new ResourceLocation(Reference.MODID, "chests/abandoned_mineshaft"));
        LootTableList.register(new ResourceLocation(Reference.MODID, "chests/desert_pyramid"));
        LootTableList.register(new ResourceLocation(Reference.MODID, "chests/jungle_temple"));
        LootTableList.register(new ResourceLocation(Reference.MODID, "chests/jungle_temple_dispenser"));
        LootTableList.register(new ResourceLocation(Reference.MODID, "chests/simple_dungeon"));
        LootTableList.register(new ResourceLocation(Reference.MODID, "chests/stronghold_crossing"));
        LootTableList.register(new ResourceLocation(Reference.MODID, "chests/stronghold_library"));
        LootTableList.register(new ResourceLocation(Reference.MODID, "chests/village_blacksmith"));
    }
    @EventHandler
    public static void init(FMLPostInitializationEvent event) {
        TileEntitySonictron.loadSonictronSounds();

        postLoadFinished = true;
    }

    @SubscribeEvent
    public void registerTEBlocks(TeBlockFinalCallEvent event) {
        TeBlockRegistry.addAll(GregTechTEBlock.class, GregTechTEBlock.LOCATION);
        TeBlockRegistry.addCreativeRegisterer(GregTechTEBlock.INDUSTRIAL_CENTRIFUGE, GregTechTEBlock.LOCATION);
    }

    public static ResourceLocation getModelResourceLocation(String name, String folder) {
        if (folder == null) return new ResourceLocation(Reference.MODID, name);
        return new ResourceLocation(String.format("%s:%s/%s", Reference.MODID, folder, name));
    }
}