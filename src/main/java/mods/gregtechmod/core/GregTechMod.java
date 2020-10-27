package mods.gregtechmod.core;

import ic2.api.event.TeBlockFinalCallEvent;
import ic2.api.item.IC2Items;
import ic2.core.block.ITeBlock;
import ic2.core.block.TeBlockRegistry;
import ic2.core.block.comp.Components;
import mods.gregtechmod.api.GregTechConfig;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.cover.CoverHandler;
import mods.gregtechmod.init.CoverLoader;
import mods.gregtechmod.init.RecipeLoader;
import mods.gregtechmod.init.RegistryHandler;
import mods.gregtechmod.objects.blocks.tileentities.TileEntitySonictron;
import mods.gregtechmod.objects.blocks.tileentities.machines.TileEntityIndustrialCentrifuge;
import mods.gregtechmod.util.IProxy;
import mods.gregtechmod.util.SidedRedstoneEmitter;
import mods.gregtechmod.world.OreGenerator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
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
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;

import java.util.Set;

@SuppressWarnings("unused")
@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, acceptedMinecraftVersions = Reference.MC_VERSION,
     dependencies = "required-after:ic2@[2.8.218-ex112,]; after:energycontrol@[0.1.8,]")
public final class GregTechMod {

    public static Logger LOGGER;
    public static final ResourceLocation COMMON_TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/gtcommon.png");
    @Instance
    public static GregTechMod instance;
    @SidedProxy(clientSide = "mods.gregtechmod.core.ClientProxy", serverSide = "mods.gregtechmod.core.ServerProxy")
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
        RegistryHandler.registerFluids();
        Components.register(CoverHandler.class, "gtcover");
        Components.register(SidedRedstoneEmitter.class, "gtsidedemitter");
        CoverLoader.registerCovers();
        GameRegistry.registerWorldGenerator(OreGenerator.instance, 5);
        MinecraftForge.EVENT_BUS.register(OreGenerator.instance);
        //TODO: Move to recipe loader(or modificator) class
        ItemStack stack = IC2Items.getItem("upgrade", "overclocker");
        stack.getItem().setMaxStackSize(GregTechConfig.FEATURES.upgradeStackSize);
    }

    @EventHandler
    public static void init(FMLInitializationEvent event) {
        GregTechTEBlock.buildDummies();
        TileEntityIndustrialCentrifuge.init();
        RecipeLoader.loadRecipes();
    }
    @EventHandler
    public static void init(FMLPostInitializationEvent event) {
        TileEntitySonictron.loadSonictronSounds();
    }

    @SubscribeEvent
    public void registerTileEntities(TeBlockFinalCallEvent event) {
        TeBlockRegistry.addAll(GregTechTEBlock.class, GregTechTEBlock.LOCATION);
        TeBlockRegistry.addCreativeRegisterer(GregTechTEBlock.industrial_centrifuge, GregTechTEBlock.LOCATION);
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    public static ResourceLocation getModelResourceLocation(String name, String folder) {
        if (folder == null) return new ResourceLocation(Reference.MODID, name);
        return new ResourceLocation(String.format("%s:%s/%s", Reference.MODID, folder, name));
    }
}
