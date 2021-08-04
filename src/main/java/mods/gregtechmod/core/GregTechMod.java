package mods.gregtechmod.core;

import ic2.api.event.TeBlockFinalCallEvent;
import ic2.core.block.TeBlockRegistry;
import ic2.core.block.comp.Components;
import ic2.core.ref.ItemName;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.init.*;
import mods.gregtechmod.objects.blocks.teblocks.TileEntitySonictron;
import mods.gregtechmod.objects.blocks.teblocks.TileEntityUniversalMacerator;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityEnergy;
import mods.gregtechmod.objects.blocks.teblocks.component.*;
import mods.gregtechmod.recipe.compat.ModRecipes;
import mods.gregtechmod.recipe.crafting.AdvancementRecipeFixer;
import mods.gregtechmod.recipe.util.DamagedOreIngredientFixer;
import mods.gregtechmod.util.IProxy;
import mods.gregtechmod.util.LootFunctionWriteBook;
import mods.gregtechmod.world.OreGenerator;
import mods.gregtechmod.world.RetrogenHandler;
import net.minecraft.creativetab.CreativeTabs;
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
import org.apache.logging.log4j.Logger;

import java.io.File;

@SuppressWarnings("unused")
@Mod(modid = Reference.MODID, dependencies = "required-after:ic2@[2.8.221-ex112,]; after:energycontrol@[0.1.8,]; after:thermalexpansion; after:buildcraftenergy; after:forestry; after:tconstruct")
public final class GregTechMod {
    @Instance
    public static GregTechMod instance;
    @SidedProxy(clientSide = "mods.gregtechmod.core.ClientProxy", serverSide = "mods.gregtechmod.core.ServerProxy")
    public static IProxy proxy;

    public static final CreativeTabs GREGTECH_TAB = new GregTechTab();
    public static final ResourceLocation COMMON_TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/common.png");
    public static File configDir;
    public static boolean classic;
    public static Logger logger;

    static {
        FluidRegistry.enableUniversalBucket();
    }

    @EventHandler
    public void start(FMLConstructionEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();

        logger.info("Pre-init started");
        configDir = event.getSuggestedConfigurationFile().getParentFile();
        GregTechAPIImpl.createAndInject();
        DynamicConfig.init();
        MinecraftForge.EVENT_BUS.register(OreGenerator.INSTANCE);
        MinecraftForge.EVENT_BUS.register(RetrogenHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(OreDictHandler.INSTANCE);
        ModHandler.checkLoadedMods();

        RegistryHandler.registerFluids();
        Components.register(CoverHandler.class, Reference.MODID + ":cover_handler");
        Components.register(SidedRedstoneEmitter.class, Reference.MODID + ":sided_emitter");
        Components.register(CoilHandler.class, Reference.MODID + ":coil_handler");
        Components.register(BasicTank.class, Reference.MODID + ":basic_tank");
        Components.register(Maintenance.class, Reference.MODID + ":maintenance");
        TileEntityEnergy.registerEnergyComponents();
        CoverLoader.registerCovers();
        GameRegistry.registerWorldGenerator(OreGenerator.INSTANCE, 5);
        
        GregTechAPI.instance().registerWrench(ItemName.wrench.getInstance());
        GregTechAPI.instance().registerWrench(ItemName.wrench_new.getInstance());
    }

    @EventHandler
    public static void init(FMLInitializationEvent event) {
        ModHandler.gatherModItems();
        proxy.init();
        GregTechTEBlock.buildDummies();
        
        OreDictRegistrar.registerItems();
        MachineRecipeLoader.loadRecipes();
        CraftingRecipeLoader.init();
        MachineRecipeLoader.loadDynamicRecipes();
        if (GregTechMod.classic) MatterRecipeLoader.init();
        MachineRecipeLoader.loadFuels();
        MachineRecipeLoader.registerProviders();

        logger.debug("Registering loot");
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
    public static void postInit(FMLPostInitializationEvent event) {
        TileEntitySonictron.loadSonictronSounds();
        ItemStackModificator.init();
        ModRecipes.init();
        TileEntityUniversalMacerator.initMaceratorRecipes();

        logger.info("Activating OreDictionary Handler");
        OreDictHandler.INSTANCE.activateHandler();
        OreDictHandler.registerValuableOres();

        MachineRecipeLoader.registerDynamicRecipes();
        DamagedOreIngredientFixer.fixRecipes();
        AdvancementRecipeFixer.fixAdvancementRecipes();
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
