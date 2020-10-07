package mods.gregtechmod.core;

import ic2.api.event.TeBlockFinalCallEvent;
import ic2.api.item.IC2Items;
import ic2.core.block.ITeBlock;
import ic2.core.block.TeBlockRegistry;
import ic2.core.block.comp.Components;
import mods.gregtechmod.cover.CoverHandler;
import mods.gregtechmod.init.CoverLoader;
import mods.gregtechmod.init.RecipeLoader;
import mods.gregtechmod.init.RegistryHandler;
import mods.gregtechmod.objects.blocks.tileentities.TileEntitySonictron;
import mods.gregtechmod.objects.blocks.tileentities.machines.TileEntityIndustrialCentrifuge;
import mods.gregtechmod.util.IProxy;
import mods.gregtechmod.util.SidedRedstoneEmitter;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
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
import org.apache.logging.log4j.Logger;

import java.util.Set;

@SuppressWarnings("unused")
@Mod(modid = GregtechMod.MODID, name = GregtechMod.NAME, version = GregtechMod.VERSION, acceptedMinecraftVersions = GregtechMod.MC_VERSION,
     dependencies = "required-after:ic2@[2.8.218-ex112,]; after:energycontrol@[0.1.8,]")
public final class GregtechMod {
    public static final String NAME = "Gregtech Experimental";
    public static final String MODID = "gregtechmod";
    public static final String MC_VERSION = "1.12.2";
    static final String VERSION = "1.0";
    public static Logger LOGGER;
    public static final ResourceLocation COMMON_TEXTURE = new ResourceLocation(GregtechMod.MODID, "textures/gui/gtcommon.png");
    @Instance
    public static GregtechMod instance;
    @SidedProxy(clientSide = "mods.gregtechmod.core.ClientProxy")
    public static IProxy proxy;

    public static final CreativeTabs GREGTECH_TAB = new GregTechTab("gregtechtab");
    private Set<ITeBlock> allTypes;

    static {
        FluidRegistry.enableUniversalBucket();
    }

    @EventHandler
    public void start(FMLConstructionEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        LOGGER = event.getModLog();

        LOGGER.info("Pre-init started");
        ConfigLoader.loadConfig(event);
        RegistryHandler.registerFluids();
        Components.register(CoverHandler.class, "gtcover");
        Components.register(SidedRedstoneEmitter.class, "gtsidedemitter");
        CoverLoader.registerCovers();
        if (event.getSide().isClient()) RegistryHandler.registerBakedModels();
        //TODO: Move to recipe loader(or modificator) class
        IC2Items.getItem("upgrade", "overclocker").getItem().setMaxStackSize(ConfigLoader.upgradeStackSize);
    }

    @EventHandler
    public static void init(FMLInitializationEvent event) {
        GregtechTeBlock.buildDummies();
        TileEntityIndustrialCentrifuge.init();
        RecipeLoader.loadRecipes();
    }
    @EventHandler
    public static void init(FMLPostInitializationEvent event) {
        TileEntitySonictron.loadSonictronSounds();
    }

    @SubscribeEvent
    public void registerTileEntities(TeBlockFinalCallEvent event) {
        TeBlockRegistry.addAll(GregtechTeBlock.class, GregtechTeBlock.LOCATION);
        TeBlockRegistry.addCreativeRegisterer(GregtechTeBlock.industrial_centrifuge, GregtechTeBlock.LOCATION);
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    public static ResourceLocation getModelResourceLocation(String name, String folder) {
        if (folder == null) return new ResourceLocation(GregtechMod.MODID, name);
        return new ResourceLocation(String.format("%s:%s/%s", MODID, folder, name));
    }
}
