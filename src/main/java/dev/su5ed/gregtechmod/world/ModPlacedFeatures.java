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

public final class ModPlacedFeatures {
    private static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, Reference.MODID);

    // Overworld
    public static final RegistryObject<PlacedFeature> GALENA_PLACED = registerCommonPlaceFeature("galena_ore_placed", ModConfiguredFeatures.GALENA_ORE, 1, -20, 40);
    public static final RegistryObject<PlacedFeature> BAUXITE_PLACED = registerCommonPlaceFeature("bauxite_ore_placed", ModConfiguredFeatures.BAUXITE_ORE, 3, 32, 100);
    public static final RegistryObject<PlacedFeature> IRIDIUM_PLACED = registerCommonPlaceFeature("iridium_ore_placed", ModConfiguredFeatures.IRIDIUM_ORE, 2, 0, 80);
    public static final RegistryObject<PlacedFeature> RUBY_PLACED = registerCommonPlaceFeature("ruby_ore_placed", ModConfiguredFeatures.RUBY_ORE, 8, -16, 32);
    public static final RegistryObject<PlacedFeature> SAPPHIRE_PLACED = registerCommonPlaceFeature("sapphire_ore_placed", ModConfiguredFeatures.SAPPHIRE_ORE, 8, -16, 32);
    public static final RegistryObject<PlacedFeature> TETRAHEDRITE_PLACED = registerRarePlaceFeature("tetrahedrite_ore_placed", ModConfiguredFeatures.TETRAHEDRITE_ORE, 3, 24, 90);
    public static final RegistryObject<PlacedFeature> CASSITERITE_PLACED = registerRarePlaceFeature("cassiterite_ore_placed", ModConfiguredFeatures.CASSITERITE_ORE, 5, 16, 80);

    // Nether
    public static final RegistryObject<PlacedFeature> PYRITE_TINY_PLACED = registerCommonPlaceFeature("pyrite_ore_tiny_placed", ModConfiguredFeatures.PYRITE_ORE_TINY, 16, 0, 64);
    public static final RegistryObject<PlacedFeature> PYRITE_SMALL_PLACED = registerRareCountPlaceFeature("pyrite_ore_small_placed", ModConfiguredFeatures.PYRITE_ORE_SMALL, 8, 2, 0, 64);
    public static final RegistryObject<PlacedFeature> PYRITE_MEDIUM_PLACED = registerRareCountPlaceFeature("pyrite_ore_medium_placed", ModConfiguredFeatures.PYRITE_ORE_MEDIUM, 4, 4, 0, 64);
    public static final RegistryObject<PlacedFeature> PYRITE_LARGE_PLACED = registerRareCountPlaceFeature("pyrite_ore_large_placed", ModConfiguredFeatures.PYRITE_ORE_LARGE,2, 8, 0, 64);
    public static final RegistryObject<PlacedFeature> PYRITE_HUGE_PLACED = registerRarePlaceFeature("pyrite_ore_huge_placed", ModConfiguredFeatures.PYRITE_ORE_HUGE, 16, 0, 64);

    public static final RegistryObject<PlacedFeature> CINNABAR_TINY_PLACED = registerCommonPlaceFeature("cinnabar_ore_tiny_placed", ModConfiguredFeatures.CINNABAR_ORE_TINY, 14, 64, 128);
    public static final RegistryObject<PlacedFeature> CINNABAR_SMALL_PLACED = registerRareCountPlaceFeature("cinnabar_ore_small_placed", ModConfiguredFeatures.CINNABAR_ORE_SMALL, 8, 2, 64, 128);
    public static final RegistryObject<PlacedFeature> CINNABAR_MEDIUM_PLACED = registerRareCountPlaceFeature("cinnabar_ore_medium_placed", ModConfiguredFeatures.CINNABAR_ORE_MEDIUM, 4, 4, 64, 128);
    public static final RegistryObject<PlacedFeature> CINNABAR_LARGE_PLACED = registerRareCountPlaceFeature("cinnabar_ore_large_placed", ModConfiguredFeatures.CINNABAR_ORE_LARGE, 2, 8, 64, 128);
    public static final RegistryObject<PlacedFeature> CINNABAR_HUGE_PLACED = registerRarePlaceFeature("cinnabar_ore_huge_placed", ModConfiguredFeatures.CINNABAR_ORE_HUGE, 16, 64, 128);

    public static void init(IEventBus bus) {
        PLACED_FEATURES.register(bus);
    }

    private static RegistryObject<PlacedFeature> registerRarePlaceFeature(String name, RegistryObject<ConfiguredFeature<?, ?>> configuredFeature, int chance, int minY, int maxY) {
        return registerPlaceFeature(name, configuredFeature, rareOrePlacement(chance, minY, maxY));
    }
    
    private static RegistryObject<PlacedFeature> registerRareCountPlaceFeature(String name, RegistryObject<ConfiguredFeature<?, ?>> configuredFeature, int count, int chance, int minY, int maxY) {
            return registerPlaceFeature(name, configuredFeature, rareCountOrePlacement(count, chance, minY, maxY));
        }

    private static RegistryObject<PlacedFeature> registerCommonPlaceFeature(String name, RegistryObject<ConfiguredFeature<?, ?>> configuredFeature, int count, int minY, int maxY) {
        return registerPlaceFeature(name, configuredFeature, commonOrePlacement(count, minY, maxY));
    }

    private static RegistryObject<PlacedFeature> registerPlaceFeature(String name, RegistryObject<ConfiguredFeature<?, ?>> configuredFeature, List<PlacementModifier> modifiers) {
        return PLACED_FEATURES.register(name, () -> new PlacedFeature(configuredFeature.getHolder().orElseThrow(), modifiers));
    }

    private static List<PlacementModifier> rareOrePlacement(int chance, int minY, int maxY) {
        return orePlacement(RarityFilter.onAverageOnceEvery(chance), heightRange(minY, maxY));
    }

    private static List<PlacementModifier> rareCountOrePlacement(int count, int chance, int minY, int maxY) {
        return List.of(CountPlacement.of(count), RarityFilter.onAverageOnceEvery(chance), InSquarePlacement.spread(), heightRange(minY, maxY), BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int count, int minY, int maxY) {
        return orePlacement(CountPlacement.of(count), heightRange(minY, maxY));
    }

    private static List<PlacementModifier> orePlacement(PlacementModifier count, PlacementModifier height) {
        return List.of(count, InSquarePlacement.spread(), height, BiomeFilter.biome());
    }
    
    private static HeightRangePlacement heightRange(int minY, int maxY) {
        return HeightRangePlacement.triangle(VerticalAnchor.absolute(minY), VerticalAnchor.absolute(maxY));
    }

    private ModPlacedFeatures() {}
}
