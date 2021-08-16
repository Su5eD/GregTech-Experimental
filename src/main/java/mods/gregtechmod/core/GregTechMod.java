package mods.gregtechmod.core;

import ic2.core.IC2;
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
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.LootFunctionWriteBook;
import mods.gregtechmod.world.OreGenerator;
import mods.gregtechmod.world.RetrogenHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Mod(modid = Reference.MODID, dependencies = "required-after:ic2@[2.8.221-ex112,]; after:energycontrol@[0.1.8,]; after:thermalexpansion; after:buildcraftenergy; after:forestry; after:tconstruct")
public final class GregTechMod {
    @Instance
    public static GregTechMod instance;
    private static ClientProxy proxy;

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
        MinecraftForge.EVENT_BUS.register(OreGenerator.INSTANCE);
        MinecraftForge.EVENT_BUS.register(RetrogenHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(OreDictHandler.INSTANCE);
    }

    @EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();

        logger.info("Pre-init started");
        if (event.getSide() == Side.CLIENT) proxy = new ClientProxy();
        configDir = event.getSuggestedConfigurationFile().getParentFile();
        classic = IC2.version.isClassic();
        GregTechAPIImpl.createAndInject();
        DynamicConfig.init();
        ModHandler.gatherLoadedMods();

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
        if (event.getSide() == Side.CLIENT) ClientEventHandler.gatherModItems();
        GregTechTEBlock.buildDummies();
        
        OreDictRegistrar.registerItems();
        MachineRecipeLoader.loadRecipes();
        CraftingRecipeLoader.init();
        MachineRecipeLoader.loadDynamicRecipes();
        if (classic) MatterRecipeLoader.init();
        MachineRecipeLoader.loadFuels();
        MachineRecipeLoader.registerProviders();

        logger.debug("Registering loot");
        LootFunctionManager.registerFunction(new LootFunctionWriteBook.Serializer());
        Stream.of(
                "abandoned_mineshaft", "desert_pyramid", "jungle_temple", "jungle_temple_dispenser", 
                "simple_dungeon", "stronghold_crossing", "stronghold_library", "village_blacksmith"
        )
                .map(path -> new ResourceLocation(Reference.MODID, "chests/" + path))
                .forEach(LootTableList::register);
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
        GtUtil.withModContainerOverride(Loader.instance().getMinecraftModContainer(), AdvancementRecipeFixer::fixAdvancementRecipes);
    }
    
    public static void runProxy(Consumer<ClientProxy> consumer) {
        if (proxy != null) consumer.accept(proxy);
    }
}
