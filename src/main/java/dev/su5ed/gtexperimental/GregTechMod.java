package dev.su5ed.gtexperimental;

import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.blockentity.SonictronBlockEntity;
import dev.su5ed.gtexperimental.compat.ModHandler;
import dev.su5ed.gtexperimental.datagen.DataGenerators;
import dev.su5ed.gtexperimental.network.GregTechNetwork;
import dev.su5ed.gtexperimental.network.NetworkHandler;
import dev.su5ed.gtexperimental.object.ModCovers;
import dev.su5ed.gtexperimental.object.ModMenus;
import dev.su5ed.gtexperimental.object.ModObjects;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeTypes;
import dev.su5ed.gtexperimental.util.ProfileManager;
import dev.su5ed.gtexperimental.util.loot.ConditionLootModifier;
import dev.su5ed.gtexperimental.world.ModConfiguredFeatures;
import dev.su5ed.gtexperimental.world.ModPlacedFeatures;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Reference.MODID)
public class GregTechMod {
    public static final Logger LOGGER = LogManager.getLogger();

    public static final ProfileManager PROFILE_MANAGER = new ProfileManager();

    public GregTechMod() {
        PROFILE_MANAGER.init();
        
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);
        bus.addListener(this::addPackFinders);
        ModHandler.initMods();

        GregTechAPIImpl.createAndInject();
        ModCovers.init(bus);
        ModMenus.init(bus);
        ModConfiguredFeatures.init(bus);
        ModPlacedFeatures.init(bus);
        ConditionLootModifier.init(bus);
        ModRecipeTypes.init(bus);
        ModRecipeSerializers.init(bus);
        bus.register(ModObjects.class);
        bus.register(DataGenerators.class);
        bus.register(Capabilities.class);

        ModLoadingContext ctx = ModLoadingContext.get();
        ctx.registerConfig(ModConfig.Type.CLIENT, GregTechConfig.CLIENT_SPEC);
        ctx.registerConfig(ModConfig.Type.COMMON, GregTechConfig.COMMON_SPEC);

        NetworkHandler.init();
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Common setup started");

        GregTechNetwork.registerPackets();
        ModHandler.registerCrops();
        SonictronBlockEntity.loadSonictronSounds();
    }

    private void addPackFinders(final AddPackFindersEvent event) {
        if (event.getPackType() == PackType.SERVER_DATA) {
            event.addRepositorySource(GregTechPacks.INSTANCE);
        }
    }
}
