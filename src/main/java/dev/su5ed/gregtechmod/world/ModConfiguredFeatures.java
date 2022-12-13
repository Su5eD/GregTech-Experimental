package dev.su5ed.gregtechmod.world;

import dev.su5ed.gregtechmod.api.util.Reference;
import dev.su5ed.gregtechmod.object.Ore;
import dev.su5ed.gregtechmod.util.BlockItemProvider;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;

public final class ModConfiguredFeatures {
    private static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, Reference.MODID);

    public static final RegistryObject<ConfiguredFeature<?, ?>> GALENA_ORE = registerOre("galena_ore", Ore.GALENA, 16);
    public static final RegistryObject<ConfiguredFeature<?, ?>> BAUXITE_ORE = registerOre("bauxite_ore", Ore.BAUXITE, 16);
    public static final RegistryObject<ConfiguredFeature<?, ?>> IRIDIUM_ORE = registerOre("iridium_ore", Ore.IRIDIUM, 3);
    public static final RegistryObject<ConfiguredFeature<?, ?>> RUBY_ORE = registerOre("ruby_ore", Ore.RUBY, 3);
    public static final RegistryObject<ConfiguredFeature<?, ?>> SAPPHIRE_ORE = registerOre("sapphire_ore", Ore.SAPPHIRE, 3);
    public static final RegistryObject<ConfiguredFeature<?, ?>> TETRAHEDRITE_ORE = registerOre("tetrahedrite_ore", Ore.TETRAHEDRITE, 32);
    public static final RegistryObject<ConfiguredFeature<?, ?>> CASSITERITE_ORE = registerOre("cassiterite_ore", Ore.CASSITERITE, 32);

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

    public static void init(IEventBus bus) {
        CONFIGURED_FEATURES.register(bus);
    }

    private static RegistryObject<ConfiguredFeature<?, ?>> registerOre(String name, BlockItemProvider block, int size) {
        return registerOre(name, OreFeatures.STONE_ORE_REPLACEABLES, block, size);
    }

    private static RegistryObject<ConfiguredFeature<?, ?>> registerNetherOre(String name, BlockItemProvider block, int size) {
        return registerOre(name, OreFeatures.NETHER_ORE_REPLACEABLES, block, size);
    }

    private static RegistryObject<ConfiguredFeature<?, ?>> registerOre(String name, RuleTest replaceables, BlockItemProvider block, int size) {
        Supplier<List<OreConfiguration.TargetBlockState>> oreList = Lazy.of(() -> List.of(
            OreConfiguration.target(replaceables, block.getBlock().defaultBlockState())
        ));
        return CONFIGURED_FEATURES.register(name, () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(oreList.get(), size)));
    }

    private ModConfiguredFeatures() {}
}
