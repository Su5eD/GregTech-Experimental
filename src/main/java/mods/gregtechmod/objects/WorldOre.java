package mods.gregtechmod.objects;

import mods.gregtechmod.core.GregTechConfig;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;

import java.util.ArrayList;
import java.util.List;

public enum WorldOre {
    GALENA(BlockItems.Ore.GALENA, GregTechConfig.WORLDGEN.galena, DimensionType.OVERWORLD, 1, 16, 3, 0, 32),
    IRIDIUM(BlockItems.Ore.IRIDIUM, GregTechConfig.WORLDGEN.iridium, DimensionType.OVERWORLD, OreType.SINGLE, 1, 0, 5, 0, 100),
    RUBY(BlockItems.Ore.RUBY, GregTechConfig.WORLDGEN.ruby, DimensionType.OVERWORLD, OreType.SINGLE, 8, 0, 1, 0, 32, "desert", "desert_hills", "mutated_desert", "savanna", "mutated_savanna", "biomesoplenty:shrubland", "biomesoplenty:wasteland", "twilightforest:fire_swamp", "biomesoplenty:volcanic_island", "biomesoplenty:steppe", "biomesoplenty:prairie", "biomesoplenty:lush_desert", "minecraft:mesa", "minecraft:mesa_rock", "biomesoplenty:outback"),
    SAPPHIRE(BlockItems.Ore.SAPPHIRE, GregTechConfig.WORLDGEN.ruby, DimensionType.OVERWORLD, OreType.SINGLE, 8, 0, 1, 0, 32, "ocean", "beaches", "frozen_ocean", "twilightforest:twilight_lake", "twilightforest:twilight_stream", "biomesoplenty:glacier", "biomesoplenty:mangrove", "biomesoplenty:oasis", "biomesoplenty:sacred_springs"),
    SPHALERITE(BlockItems.Ore.SPHALERITE, GregTechConfig.WORLDGEN.sphalerite_overworld, DimensionType.OVERWORLD, OreType.SINGLE_UNDER_LAVA, 8, 0, 1, 2, 10),
    BAUXITE(BlockItems.Ore.BAUXITE, GregTechConfig.WORLDGEN.bauxite, DimensionType.OVERWORLD, 2, 16, 1, 32, 80, "plains", "forest", "forest_hills", "biomesoplenty:rainforest", "biomesoplenty:highland", "biomesoplenty:rainforest", "birch_forest", "birch_forest_hills", "biomesoplenty:meadow", "biomesoplenty:redwood_forest", "biomesoplenty:woodland", "twilightforest:twilight_forest", "twilightforest:dense_twilight_forest", "twilightforest:dark_forest", "twilightforest:enchanted_forest", "biomesoplenty:seasonal_forest", "biomesoplenty:quagmire", "biomesoplenty:orchard", "biomesoplenty:pasture", "biomesoplenty:mystic_grove", "biomesoplenty:marsh", "biomesoplenty:maple_woods", "biomesoplenty:grove", "biomesoplenty:grassland", "biomesoplenty:fungi_forest", "biomesoplenty:coniferous_forest", "biomesoplenty:cherry_blossom_grove"),
    TETRAHEDRITE(BlockItems.Ore.TETRAHEDRITE, GregTechConfig.WORLDGEN.tetrahedrite, DimensionType.OVERWORLD, 1, 32, 5, 32, 100, "jungle", "jungle_hills", "swampland", "mushroom_island", "mushroom_island_shore", "extreme_hills", "smaller_extreme_hills", "biomesoplenty:bog", "biomesoplenty:overgrown_cliffs", "biomesoplenty:volcanic_island"),
    CASSITERITE(BlockItems.Ore.CASSITERITE, GregTechConfig.WORLDGEN.cassiterite, DimensionType.OVERWORLD, 1, 32, 10, 32, 100, "taiga", "taiga_hills", "ice_flats", "ice_mountains", "mushroom_island", "mushroom_island_shore", "extreme_hills", "smaller_extreme_hills", "biomesoplenty:alps", "biomesoplenty:glacier", "biomesoplenty:tundra", "biomesoplenty:volcanic_island"),
    PYRITE_TINY(BlockItems.Ore.PYRITE, GregTechConfig.WORLDGEN.pyriteTiny, DimensionType.NETHER, 16, 4, 1, 0, 64),
    PYRITE_SMALL(BlockItems.Ore.PYRITE, GregTechConfig.WORLDGEN.pyriteSmall, DimensionType.NETHER, 8, 8, 2, 0, 64),
    PYRITE_MEDIUM(BlockItems.Ore.PYRITE, GregTechConfig.WORLDGEN.pyriteMedium, DimensionType.NETHER, 4, 12, 4, 0, 64),
    PYRITE_LARGE(BlockItems.Ore.PYRITE, GregTechConfig.WORLDGEN.pyriteLarge, DimensionType.NETHER, 2, 24, 8, 0, 64),
    PYRITE_HUGE(BlockItems.Ore.PYRITE, GregTechConfig.WORLDGEN.pyriteHuge, DimensionType.NETHER, 1, 32, 16, 0, 64),
    CINNABAR_TINY(BlockItems.Ore.CINNABAR, GregTechConfig.WORLDGEN.cinnabarTiny, DimensionType.NETHER, 16, 4, 1, 64, 128),
    CINNABAR_SMALL(BlockItems.Ore.CINNABAR, GregTechConfig.WORLDGEN.cinnabarSmall, DimensionType.NETHER, 8, 8, 2, 64, 128),
    CINNABAR_MEDIUM(BlockItems.Ore.CINNABAR, GregTechConfig.WORLDGEN.cinnabarMedium, DimensionType.NETHER, 4, 12, 4, 64, 128),
    CINNABAR_LARGE(BlockItems.Ore.CINNABAR, GregTechConfig.WORLDGEN.cinnabarLarge, DimensionType.NETHER, 2, 24, 8, 64, 128),
    CINNABAR_HUGE(BlockItems.Ore.CINNABAR, GregTechConfig.WORLDGEN.cinnabarHuge, DimensionType.NETHER, 1, 32, 16, 64, 128),
    SPHALERITE_TINY(BlockItems.Ore.SPHALERITE, GregTechConfig.WORLDGEN.sphaleriteTiny, DimensionType.NETHER, 16, 4, 1, 32, 96),
    SPHALERITE_SMALL(BlockItems.Ore.SPHALERITE, GregTechConfig.WORLDGEN.sphaleriteSmall, DimensionType.NETHER, 8, 8, 2, 32, 96),
    SPHALERITE_MEDIUM(BlockItems.Ore.SPHALERITE, GregTechConfig.WORLDGEN.sphaleriteMedium, DimensionType.NETHER, 4, 12, 4, 32, 96),
    SPHALERITE_LARGE(BlockItems.Ore.SPHALERITE, GregTechConfig.WORLDGEN.sphaleriteLarge, DimensionType.NETHER, 2, 24, 8, 32, 96),
    SPHALERITE_HUGE(BlockItems.Ore.SPHALERITE, GregTechConfig.WORLDGEN.sphaleriteHuge, DimensionType.NETHER, 1, 32, 16, 32, 96),
    TUNGSTATE(BlockItems.Ore.TUNGSTATE, GregTechConfig.WORLDGEN.tungstate, DimensionType.THE_END, 4, 16),
    SHELDONITE(BlockItems.Ore.SHELDONITE, GregTechConfig.WORLDGEN.sheldonite, DimensionType.THE_END, 1, 4),
    OLIVINE(BlockItems.Ore.OLIVINE, GregTechConfig.WORLDGEN.olivine, DimensionType.THE_END, 5, 8),
    SODALITE(BlockItems.Ore.SODALITE, GregTechConfig.WORLDGEN.sodalite, DimensionType.THE_END, 8, 16);

    public final BlockItems.Ore block;
    public final boolean enabled;
    public final DimensionType dimension;
    public final OreType type;
    public final int amount;
    public final int size;
    public final int probability;
    public final int minY;
    public final int maxY;
    public final List<ResourceLocation> biomeList = new ArrayList<>();

    WorldOre(BlockItems.Ore block, boolean enabled, DimensionType dimension, int amount, int size) {
        this(block, enabled, dimension, OreType.NORMAL, amount, size, 0, 0, 0);
    }

    WorldOre(BlockItems.Ore block, boolean enabled, DimensionType dimension, int amount, int size, int probability, int minY, int maxY, String... biomes) {
        this(block, enabled, dimension, OreType.NORMAL, amount, size, probability, minY, maxY, biomes);
    }

    /**
     * @param block The generated block
     * @param dimension The dimension in which the ore will be generated
     * @param type Ore cluster type
     * @param amount Amount of clusters in a chunk
     * @param size Smount of ores in a cluster
     * @param probability Chance of generating a cluster
     * @param minY Minimum Y to generate
     * @param maxY Maximum Y to generate
     * @param biomes Biomes in which the ore spawns. Leave empty for all biomes.
     */
    WorldOre(BlockItems.Ore block, boolean enabled, DimensionType dimension, OreType type, int amount, int size, int probability, int minY, int maxY, String... biomes) {
        this.block = block;
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

    public enum OreType {
        NORMAL,
        SINGLE,
        SINGLE_UNDER_LAVA
    }
}
