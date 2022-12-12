package dev.su5ed.gregtechmod.world;

import dev.su5ed.gregtechmod.api.util.Reference;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.BiFunction;

public final class ModPlacedFeatures {
    private static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, Reference.MODID);

    public static final RegistryObject<PlacedFeature> GALENA_PLACED = registerCommonPlaceFeature("galena_ore_placed", ModConfiguredFeatures.GALENA_ORE, 1, -20, 40);
    public static final RegistryObject<PlacedFeature> BAUXITE_PLACED = registerCommonPlaceFeature("bauxite_ore_placed", ModConfiguredFeatures.BAUXITE_ORE, 3, 32, 100);
    public static final RegistryObject<PlacedFeature> IRIDIUM_PLACED = registerCommonPlaceFeature("iridium_ore_placed", ModConfiguredFeatures.IRIDIUM_ORE, 2, 0, 80);
    public static final RegistryObject<PlacedFeature> RUBY_PLACED = registerCommonPlaceFeature("ruby_ore_placed", ModConfiguredFeatures.RUBY_ORE, 8, -16, 32);
    public static final RegistryObject<PlacedFeature> SAPPHIRE_PLACED = registerCommonPlaceFeature("sapphire_ore_placed", ModConfiguredFeatures.SAPPHIRE_ORE, 8, -16, 32);
    public static final RegistryObject<PlacedFeature> TETRAHEDRITE_PLACED = registerRarePlaceFeature("tetrahedrite_ore_placed", ModConfiguredFeatures.TETRAHEDRITE_ORE, 3, 24, 90);

    public static void init(IEventBus bus) {
        PLACED_FEATURES.register(bus);
    }

    private static RegistryObject<PlacedFeature> registerRarePlaceFeature(String name, RegistryObject<ConfiguredFeature<?, ?>> configuredFeature, int chance, int minY, int maxY) {
        return registerPlaceFeature(name, configuredFeature, chance, minY, maxY, ModPlacedFeatures::rareOrePlacement);
    }

    private static RegistryObject<PlacedFeature> registerCommonPlaceFeature(String name, RegistryObject<ConfiguredFeature<?, ?>> configuredFeature, int count, int minY, int maxY) {
        return registerPlaceFeature(name, configuredFeature, count, minY, maxY, ModPlacedFeatures::commonOrePlacement);
    }

    private static RegistryObject<PlacedFeature> registerPlaceFeature(String name, RegistryObject<ConfiguredFeature<?, ?>> configuredFeature, int modifier, int minY, int maxY,
                                                                      BiFunction<Integer, PlacementModifier, List<PlacementModifier>> placement) {
        return PLACED_FEATURES.register(name,
            () -> new PlacedFeature(
                configuredFeature.getHolder().orElseThrow(),
                placement.apply(modifier, HeightRangePlacement.triangle(VerticalAnchor.absolute(minY), VerticalAnchor.absolute(maxY)))));
    }

    private static List<PlacementModifier> rareOrePlacement(int chance, PlacementModifier heightRange) {
        return orePlacement(RarityFilter.onAverageOnceEvery(chance), heightRange);
    }

    private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier heightRange) {
        return orePlacement(CountPlacement.of(count), heightRange);
    }

    private static List<PlacementModifier> orePlacement(PlacementModifier count, PlacementModifier height) {
        return List.of(count, InSquarePlacement.spread(), height, BiomeFilter.biome());
    }

    private ModPlacedFeatures() {}
}
