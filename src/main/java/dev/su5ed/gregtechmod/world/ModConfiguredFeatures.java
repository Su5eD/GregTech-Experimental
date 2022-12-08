package dev.su5ed.gregtechmod.world;

import dev.su5ed.gregtechmod.api.util.Reference;
import dev.su5ed.gregtechmod.object.Ore;
import dev.su5ed.gregtechmod.util.BlockItemProvider;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
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

    public static void init(IEventBus bus) {
        CONFIGURED_FEATURES.register(bus);
    }
    
    private static RegistryObject<ConfiguredFeature<?, ?>> registerOre(String name, BlockItemProvider block, int size) {
        Supplier<List<OreConfiguration.TargetBlockState>> oreList = Lazy.of(() -> List.of(
            OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, block.getBlock().defaultBlockState())
        ));
        return CONFIGURED_FEATURES.register(name, () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(oreList.get(), size)));
    }

    private ModConfiguredFeatures() {}
}
