package mods.gregtechmod.objects;

import mods.gregtechmod.core.GregTechConfig;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public enum WorldOre {
    GALENA(GregTechConfig.WORLDGEN.galena, DimensionType.OVERWORLD, 1, 16, 3, 0, 32),
    IRIDIUM(GregTechConfig.WORLDGEN.iridium, DimensionType.OVERWORLD, OreType.SINGLE, 1, 0, 5, 0, 100),
    RUBY(GregTechConfig.WORLDGEN.ruby, DimensionType.OVERWORLD, OreType.SINGLE, 8, 0, 1, 0, 32, "desert", "desert_hills", "mutated_desert", "savanna", "mutated_savanna", "biomesoplenty:shrubland", "biomesoplenty:wasteland", "twilightforest:fire_swamp", "biomesoplenty:volcanic_island", "biomesoplenty:steppe", "biomesoplenty:prairie", "biomesoplenty:lush_desert", "minecraft:mesa", "minecraft:mesa_rock", "biomesoplenty:outback"),
    SAPPHIRE(GregTechConfig.WORLDGEN.ruby, DimensionType.OVERWORLD, OreType.SINGLE, 8, 0, 1, 0, 32, "ocean", "beaches", "frozen_ocean", "twilightforest:twilight_lake", "twilightforest:twilight_stream", "biomesoplenty:glacier", "biomesoplenty:mangrove", "biomesoplenty:oasis", "biomesoplenty:sacred_springs"),
    SPHALERITE(GregTechConfig.WORLDGEN.sphalerite_overworld, DimensionType.OVERWORLD, OreType.SINGLE_UNDER_LAVA, 8, 0, 1, 2, 10),
    BAUXITE(GregTechConfig.WORLDGEN.bauxite, DimensionType.OVERWORLD, 2, 16, 1, 32, 80, "plains", "forest", "forest_hills", "biomesoplenty:rainforest", "biomesoplenty:highland", "biomesoplenty:rainforest", "birch_forest", "birch_forest_hills", "biomesoplenty:meadow", "biomesoplenty:redwood_forest", "biomesoplenty:woodland", "twilightforest:twilight_forest", "twilightforest:dense_twilight_forest", "twilightforest:dark_forest", "twilightforest:enchanted_forest", "biomesoplenty:seasonal_forest", "biomesoplenty:quagmire", "biomesoplenty:orchard", "biomesoplenty:pasture", "biomesoplenty:mystic_grove", "biomesoplenty:marsh", "biomesoplenty:maple_woods", "biomesoplenty:grove", "biomesoplenty:grassland", "biomesoplenty:fungi_forest", "biomesoplenty:coniferous_forest", "biomesoplenty:cherry_blossom_grove"),
    TETRAHEDRITE(GregTechConfig.WORLDGEN.tetrahedrite, DimensionType.OVERWORLD, 1, 32, 5, 32, 100, "jungle", "jungle_hills", "swampland", "mushroom_island", "mushroom_island_shore", "extreme_hills", "smaller_extreme_hills", "biomesoplenty:bog", "biomesoplenty:overgrown_cliffs", "biomesoplenty:volcanic_island"),
    CASSITERITE(GregTechConfig.WORLDGEN.cassiterite, DimensionType.OVERWORLD, 1, 32, 10, 32, 100, "taiga", "taiga_hills", "ice_flats", "ice_mountains", "mushroom_island", "mushroom_island_shore", "extreme_hills", "smaller_extreme_hills", "biomesoplenty:alps", "biomesoplenty:glacier", "biomesoplenty:tundra", "biomesoplenty:volcanic_island"),
    PYRITE_TINY("pyrite", GregTechConfig.WORLDGEN.pyrite_tiny, DimensionType.NETHER, 16, 4, 1, 0, 64),
    PYRITE_SMALL("pyrite", GregTechConfig.WORLDGEN.pyrite_small, DimensionType.NETHER, 8, 8, 2, 0, 64),
    PYRITE_MEDIUM("pyrite", GregTechConfig.WORLDGEN.pyrite_medium, DimensionType.NETHER, 4, 12, 4, 0, 64),
    PYRITE_LARGE("pyrite", GregTechConfig.WORLDGEN.pyrite_large, DimensionType.NETHER, 2, 24, 8, 0, 64),
    PYRITE_HUGE("pyrite", GregTechConfig.WORLDGEN.pyrite_huge, DimensionType.NETHER, 1, 32, 16, 0, 64),
    CINNABAR_TINY("cinnabar", GregTechConfig.WORLDGEN.cinnabar_tiny, DimensionType.NETHER, 16, 4, 1, 64, 128),
    CINNABAR_SMALL("cinnabar", GregTechConfig.WORLDGEN.cinnabar_small, DimensionType.NETHER, 8, 8, 2, 64, 128),
    CINNABAR_MEDIUM("cinnabar", GregTechConfig.WORLDGEN.cinnabar_medium, DimensionType.NETHER, 4, 12, 4, 64, 128),
    CINNABAR_LARGE("cinnabar", GregTechConfig.WORLDGEN.cinnabar_large, DimensionType.NETHER, 2, 24, 8, 64, 128),
    CINNABAR_HUGE("cinnabar", GregTechConfig.WORLDGEN.cinnabar_huge, DimensionType.NETHER, 1, 32, 16, 64, 128),
    SPHALERITE_TINY("sphalerite", GregTechConfig.WORLDGEN.sphalerite_tiny, DimensionType.NETHER, 16, 4, 1, 32, 96),
    SPHALERITE_SMALL("sphalerite", GregTechConfig.WORLDGEN.sphalerite_small, DimensionType.NETHER, 8, 8, 2, 32, 96),
    SPHALERITE_MEDIUM("sphalerite", GregTechConfig.WORLDGEN.sphalerite_medium, DimensionType.NETHER, 4, 12, 4, 32, 96),
    SPHALERITE_LARGE("sphalerite", GregTechConfig.WORLDGEN.sphalerite_large, DimensionType.NETHER, 2, 24, 8, 32, 96),
    SPHALERITE_HUGE("sphalerite", GregTechConfig.WORLDGEN.sphalerite_huge, DimensionType.NETHER, 1, 32, 16, 32, 96),
    TUNGSTATE(GregTechConfig.WORLDGEN.tungstate, DimensionType.THE_END, 4, 16),
    SHELDONITE(GregTechConfig.WORLDGEN.sheldonite, DimensionType.THE_END, 1, 4),
    OLIVINE(GregTechConfig.WORLDGEN.olivine, DimensionType.THE_END, 5, 8),
    SODALITE(GregTechConfig.WORLDGEN.sodalite, DimensionType.THE_END, 8, 16);

    private String oreName = name();
    public final boolean enabled;
    public final DimensionType dimension;
    public final OreType type;
    public final int amount;
    public final int size;
    public final int probability;
    public final int minY;
    public final int maxY;
    public final List<ResourceLocation> biomeList = new ArrayList<>();

    WorldOre(boolean enabled, DimensionType dimension, int amount, int size) {
        this(enabled, dimension, OreType.NORMAL, amount, size, 0, 0, 0);
    }

    WorldOre(boolean enabled, DimensionType dimension, int amount, int size, int probability, int minY, int maxY, String... biomes) {
        this(null, enabled, dimension, amount, size, probability, minY, maxY, biomes);
    }

    WorldOre(boolean enabled, DimensionType dimension, OreType type, int amount, int size, int probability, int minY, int maxY, String... biomes) {
        this(null, enabled, dimension, type, amount, size, probability, minY, maxY, biomes);
    }

    WorldOre(String oreName, boolean enabled, DimensionType dimension, int amount, int size, int probability, int minY, int maxY, String... biomes) {
        this(oreName, enabled, dimension, OreType.NORMAL, amount, size, probability, minY, maxY, biomes);
    }

    /**
     * @param oreName The ore name
     * @param dimension The dimension in which the ore will be generated
     * @param type Ore cluster type
     * @param amount Amount of clusters in a chunk
     * @param size Smount of ores in a cluster
     * @param probability Chance of generating a cluster
     * @param minY Minimum Y to generate
     * @param maxY Maximum Y to generate
     * @param biomes Biomes in which the ore spawns. Leave empty for all biomes.
     */
    WorldOre(String oreName, boolean enabled, DimensionType dimension, OreType type, int amount, int size, int probability, int minY, int maxY, String... biomes) {
        if (oreName != null) this.oreName = oreName;
        this.enabled = enabled;
        this.dimension = dimension;
        this.type = type;
        this.amount = amount;
        this.size = size;
        this.probability = probability;
        this.minY = minY;
        this.maxY = maxY;
        for (String biome : biomes) this.biomeList.add(new ResourceLocation(biome));
    }

    public Block getBlock() {
        return BlockItems.Ore.valueOf(this.oreName.toUpperCase(Locale.ROOT)).getInstance();
    }

    public enum OreType {
        NORMAL,
        SINGLE,
        SINGLE_UNDER_LAVA
    }
}
