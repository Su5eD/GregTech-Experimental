package dev.su5ed.gregtechmod.world;

import dev.su5ed.gregtechmod.GregTechTags;
import dev.su5ed.gregtechmod.api.util.Reference;
import dev.su5ed.gregtechmod.object.Ore;
import dev.su5ed.gregtechmod.util.BlockItemProvider;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;

public final class ModConfiguredFeatures {
    private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Reference.MODID);
    private static final RegistryObject<SingleOreFeature> SINGLE_ORE = FEATURES.register("single_ore", SingleOreFeature::new);
    private static final RegistryObject<SingleUnderLavaOreFeature> SINGLE_UNDER_LAVA_ORE = FEATURES.register("single_under_lava", SingleUnderLavaOreFeature::new);

    private static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, Reference.MODID);
    private static final RuleTest END_STONE_REPLACEABLES = new TagMatchTest(Tags.Blocks.END_STONES);
    private static final RuleTest LAVA_REPLACEABLES = new TagMatchTest(GregTechTags.SUBMERGE_ORE_LAVA);

    // Overworld
    public static final RegistryObject<ConfiguredFeature<?, ?>> GALENA_ORE = registerOre("galena_ore", Ore.GALENA, 16);
    public static final RegistryObject<ConfiguredFeature<?, ?>> BAUXITE_ORE = registerOre("bauxite_ore", Ore.BAUXITE, 16);
    public static final RegistryObject<ConfiguredFeature<?, ?>> IRIDIUM_ORE = registerSingleOre("iridium_ore", Ore.IRIDIUM);
    public static final RegistryObject<ConfiguredFeature<?, ?>> RUBY_ORE = registerSingleOre("ruby_ore", Ore.RUBY);
    public static final RegistryObject<ConfiguredFeature<?, ?>> SAPPHIRE_ORE = registerSingleOre("sapphire_ore", Ore.SAPPHIRE);
    public static final RegistryObject<ConfiguredFeature<?, ?>> TETRAHEDRITE_ORE = registerOre("tetrahedrite_ore", Ore.TETRAHEDRITE, 32);
    public static final RegistryObject<ConfiguredFeature<?, ?>> CASSITERITE_ORE = registerOre("cassiterite_ore", Ore.CASSITERITE, 32);
    public static final RegistryObject<ConfiguredFeature<?, ?>> SPHALERITE_OVERWORLD_ORE = registerOre(SINGLE_UNDER_LAVA_ORE, "sphalerite_overworld_ore", LAVA_REPLACEABLES, Ore.SPHALERITE, 3, 1);

    // Nether
    public static final RegistryObject<ConfiguredFeature<?, ?>> PYRITE_ORE_TINY = registerNetherOre("pyrite_ore_tiny", Ore.PYRITE, 4);
    public static final RegistryObject<ConfiguredFeature<?, ?>> PYRITE_ORE_SMALL = registerNetherOre("pyrite_ore_small", Ore.PYRITE, 8);
    public static final RegistryObject<ConfiguredFeature<?, ?>> PYRITE_ORE_MEDIUM = registerNetherOre("pyrite_ore_medium", Ore.PYRITE, 12);
    public static final RegistryObject<ConfiguredFeature<?, ?>> PYRITE_ORE_LARGE = registerNetherOre("pyrite_ore_large", Ore.PYRITE, 24);
    public static final RegistryObject<ConfiguredFeature<?, ?>> PYRITE_ORE_HUGE = registerNetherOre("pyrite_ore_huge", Ore.PYRITE, 32);

    public static final RegistryObject<ConfiguredFeature<?, ?>> CINNABAR_ORE_TINY = registerNetherOre("cinnabar_ore_tiny", Ore.CINNABAR, 4);
    public static final RegistryObject<ConfiguredFeature<?, ?>> CINNABAR_ORE_SMALL = registerNetherOre("cinnabar_ore_small", Ore.CINNABAR, 8);
    public static final RegistryObject<ConfiguredFeature<?, ?>> CINNABAR_ORE_MEDIUM = registerNetherOre("cinnabar_ore_medium", Ore.CINNABAR, 12);
    public static final RegistryObject<ConfiguredFeature<?, ?>> CINNABAR_ORE_LARGE = registerNetherOre("cinnabar_ore_large", Ore.CINNABAR, 24);
    public static final RegistryObject<ConfiguredFeature<?, ?>> CINNABAR_ORE_HUGE = registerNetherOre("cinnabar_ore_huge", Ore.CINNABAR, 32);

    public static final RegistryObject<ConfiguredFeature<?, ?>> SPHALERITE_ORE_TINY = registerNetherOre("sphalerite_ore_tiny", Ore.SPHALERITE, 4);
    public static final RegistryObject<ConfiguredFeature<?, ?>> SPHALERITE_ORE_SMALL = registerNetherOre("sphalerite_ore_small", Ore.SPHALERITE, 8);
    public static final RegistryObject<ConfiguredFeature<?, ?>> SPHALERITE_ORE_MEDIUM = registerNetherOre("sphalerite_ore_medium", Ore.SPHALERITE, 12);
    public static final RegistryObject<ConfiguredFeature<?, ?>> SPHALERITE_ORE_LARGE = registerNetherOre("sphalerite_ore_large", Ore.SPHALERITE, 24);
    public static final RegistryObject<ConfiguredFeature<?, ?>> SPHALERITE_ORE_HUGE = registerNetherOre("sphalerite_ore_huge", Ore.SPHALERITE, 32);

    // The end
    public static final RegistryObject<ConfiguredFeature<?, ?>> TUNGSTATE_ORE = registerEndOre("tungstate_ore", Ore.TUNGSTATE, 16);
    public static final RegistryObject<ConfiguredFeature<?, ?>> SHELDONITE_ORE = registerEndOre("sheldonite_ore", Ore.SHELDONITE, 4);
    public static final RegistryObject<ConfiguredFeature<?, ?>> OLIVINE_ORE = registerEndOre("olivine_ore", Ore.OLIVINE, 8);
    public static final RegistryObject<ConfiguredFeature<?, ?>> SODALITE_ORE = registerEndOre("sodalite_ore", Ore.SODALITE, 16);

    public static void init(IEventBus bus) {
        FEATURES.register(bus);
        CONFIGURED_FEATURES.register(bus);
    }
    
    private static RegistryObject<ConfiguredFeature<?, ?>> registerSingleOre(String name, BlockItemProvider block) {
            return registerOre(SINGLE_ORE, name, OreFeatures.STONE_ORE_REPLACEABLES, block, 3, 0);
        }

    private static RegistryObject<ConfiguredFeature<?, ?>> registerOre(String name, BlockItemProvider block, int size) {
        return registerOre(name, OreFeatures.STONE_ORE_REPLACEABLES, block, size);
    }

    private static RegistryObject<ConfiguredFeature<?, ?>> registerNetherOre(String name, BlockItemProvider block, int size) {
        return registerOre(name, OreFeatures.NETHER_ORE_REPLACEABLES, block, size);
    }

    private static RegistryObject<ConfiguredFeature<?, ?>> registerEndOre(String name, BlockItemProvider block, int size) {
        return registerOre(name, END_STONE_REPLACEABLES, block, size);
    }

    private static RegistryObject<ConfiguredFeature<?, ?>> registerOre(String name, RuleTest replaceables, BlockItemProvider block, int size) {
        return registerOre(() -> Feature.ORE, name, replaceables, block, size, 0);
    }

    private static RegistryObject<ConfiguredFeature<?, ?>> registerOre(Supplier<? extends Feature<OreConfiguration>> feature, String name, RuleTest replaceables, BlockItemProvider block, int size, float discardChanceOnAirExposure) {
        Supplier<List<OreConfiguration.TargetBlockState>> oreList = Lazy.of(() -> List.of(
            OreConfiguration.target(replaceables, block.getBlock().defaultBlockState())
        ));
        return CONFIGURED_FEATURES.register(name, () -> new ConfiguredFeature<>(feature.get(), new OreConfiguration(oreList.get(), size, discardChanceOnAirExposure)));
    }

    private ModConfiguredFeatures() {}
}
