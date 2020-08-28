package mods.gregtechmod.common.core;

import ic2.api.event.TeBlockFinalCallEvent;
import ic2.api.item.IC2Items;
import ic2.core.block.ITeBlock;
import ic2.core.block.TeBlockRegistry;
import ic2.core.block.comp.Components;
import ic2.core.util.Log;
import ic2.core.util.LogCategory;
import mods.gregtechmod.api.cover.CoverRegistry;
import mods.gregtechmod.client.render.RenderTeBlock;
import mods.gregtechmod.common.cover.CoverHandler;
import mods.gregtechmod.common.init.BakedModelLoader;
import mods.gregtechmod.common.init.RecipeLoader;
import mods.gregtechmod.common.init.RegistryHandler;
import mods.gregtechmod.common.objects.blocks.machines.tileentity.TileEntityGtCentrifuge;
import mods.gregtechmod.common.util.JsonHandler;
import mods.gregtechmod.common.util.SidedRedstoneEmitter;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;
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

import java.util.Set;

@SuppressWarnings("unused")
@Mod(modid = GregtechMod.MODID, name = GregtechMod.NAME, version = GregtechMod.VERSION, acceptedMinecraftVersions = GregtechMod.MC_VERSION,
     dependencies = "after:ic2@[2.8.218-ex112,]")
public final class GregtechMod {
    public static final String NAME = "Gregtech Experimental";
    public static final String MODID = "gregtechmod";
    public static final String MC_VERSION = "1.12";
    public static KeyBinding IC2_MODE_KEY;
    static final String VERSION = "0.01";
    public static Log LOGGER;
    public static final ResourceLocation COMMON_TEXTURE = new ResourceLocation(GregtechMod.MODID, "textures/gui/gtcommon.png");
    @Instance
    public static GregtechMod instance;
    @SidedProxy(modId = GregtechMod.MODID, clientSide = "mods.gregtechmod.client.ClientProxy", serverSide = "mods.gregtechmod.common.core.CommonProxy")
    public static CommonProxy proxy;

    public static final CreativeTabs GREGTECH_TAB = new GregTechTab("gregtechtab");
    private Set<ITeBlock> allTypes;

    //TODO: Add classic profile

    static {
        FluidRegistry.enableUniversalBucket();
    }

    @EventHandler
    public void start(FMLConstructionEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        LOGGER = new Log(event.getModLog());
        LOGGER.debug(LogCategory.General, "Pre-init:");
        ConfigLoader.loadConfig(event);
        RegistryHandler.registerFluids();
        Components.register(CoverHandler.class, "gtcover");
        Components.register(SidedRedstoneEmitter.class, "gtsidedemitter");
        CoverRegistry.init();
        //init covers
        BakedModelLoader loader = new BakedModelLoader();
        for (GregtechTeBlock teBlock : GregtechTeBlock.values()) {
            try {
                if (teBlock.hasBakedModel()) {
                    String name = teBlock.getName();
                    JsonHandler json = new JsonHandler(name);
                    loader.register("models/block/"+name, new RenderTeBlock(json.textures, json.particle));
                    if (teBlock.hasActive()) {
                        json = new JsonHandler(name+"_active");
                        loader.register("models/block/"+name+"_active", new RenderTeBlock(json.textures, json.particle));
                    }
                }
            } catch (Exception e) {
                LOGGER.error(LogCategory.General, e.getMessage());
            }
        }
        ModelLoaderRegistry.registerLoader(loader);

        //TODO: Move to recipe loader(or modificator) class
        IC2Items.getItem("upgrade", "overclocker").getItem().setMaxStackSize(ConfigLoader.upgradeStackSize);
    }
    @EventHandler
    public static void init(FMLInitializationEvent event) {
        GregtechTeBlock.buildDummies();
        TileEntityGtCentrifuge.init();
        RecipeLoader.registerRecipes();
    }
    @EventHandler
    public static void init(FMLPostInitializationEvent event) {
        IC2_MODE_KEY = GregtechMod.proxy.getModeKeyBinding();
    }
    @SubscribeEvent
    public void registerTileEntities(TeBlockFinalCallEvent event) {
        TeBlockRegistry.addAll(GregtechTeBlock.class, GregtechTeBlock.LOCATION);
        TeBlockRegistry.addCreativeRegisterer(GregtechTeBlock.gtcentrifuge, GregtechTeBlock.LOCATION);
        MinecraftForge.EVENT_BUS.unregister(this);
    }
}
