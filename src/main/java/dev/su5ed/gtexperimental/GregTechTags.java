package dev.su5ed.gtexperimental;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import one.util.streamex.StreamEx;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static dev.su5ed.gtexperimental.api.Reference.location;

public final class GregTechTags {
    public static final TagKey<EntityType<?>> SLIMES = forgeEntityTag("slimes");
    public static final TagKey<EntityType<?>> SCREWDRIVER_EFFECTIVE = entityTag("screwdriver_effective");
    public static final TagKey<EntityType<?>> WRENCH_EFFECTIVE = entityTag("wrench_effective");
    public static final TagKey<EntityType<?>> BUG_SPRAY_EFFECTIVE = entityTag("bug_spray_effective");
    public static final TagKey<EntityType<?>> ICE_SPRAY_EFFECTIVE = entityTag("ice_spray_effective");
    public static final TagKey<EntityType<?>> HARD_HAMMER_EFFECTIVE = entityTag("hard_hammer");

    public static final TagKey<Block> MINEABLE_WITH_SHEARS = forgeBlockTag("mineable/shears");
    public static final TagKey<Block> SUBMERGE_ORE_LAVA = blockTag("submerge_ore_lava");

    public static final TagKey<Item> PLATES = forgeItemTag("plates");
    public static final TagKey<Item> SMALL_DUSTS = forgeItemTag("small_dusts");

    // Action tags, used for interacting with machines
    public static final TagKey<Item> DUCT_TAPE = itemTag("duct_tape");

    public static final TagKey<Item> CRAFTING_LI_BATTERY = itemTag("crafting/li_battery");
    public static final TagKey<Item> CRAFTING_GRINDER = itemTag("crafting/grinder");
    public static final TagKey<Item> CRAFTING_SPRAY_CAN = itemTag("crafting/spray_can");
    public static final TagKey<Item> CRAFTING_RAW_MACHINE_TIER_0 = itemTag("crafting/raw_machine_tier_0");
    public static final TagKey<Item> CRAFTING_RAW_MACHINE_TIER_1 = itemTag("crafting/raw_machine_tier_1");
    public static final TagKey<Item> CRAFTING_RAW_MACHINE_TIER_2 = itemTag("crafting/raw_machine_tier_2");
    public static final TagKey<Item> CRAFTING_RAW_MACHINE_TIER_3 = itemTag("crafting/raw_machine_tier_3");
    public static final TagKey<Item> CRAFTING_RAW_MACHINE_TIER_4 = itemTag("crafting/raw_machine_tier_4");
    public static final TagKey<Item> CRAFTING_60K_COOLANT_STORE = itemTag("crafting/60k_coolant_store");
    public static final TagKey<Item> CRAFTING_180K_COOLANT_STORE = itemTag("crafting/180k_coolant_store");
    public static final TagKey<Item> CRAFTING_360K_COOLANT_STORE = itemTag("crafting/360k_coolant_store");
    // Small EU store: 100K / 1KK
    public static final TagKey<Item> SMALL_EU_STORE = itemTag("small_eu_store");
    // Medium EU store: 1KK / 10KK
    public static final TagKey<Item> MEDIUM_EU_STORE = itemTag("medium_eu_store");
    // Large EU store: 10KK / 100KK
    public static final TagKey<Item> LARGE_EU_STORE = itemTag("large_eu_store");

    public static final TagKey<Item> COLORED_WOOL = itemTag("colored_wool");
    public static final TagKey<Item> RAW_FOOD = itemTag("raw_food");
    public static final TagKey<Item> COOKED_FOOD = itemTag("cooked_food");
    public static final TagKey<Item> MORTAR = itemTag("mortar");
    public static final TagKey<Item> CROWBAR = forgeItemTag("tools/crowbar");
    public static final TagKey<Item> SCREWDRIVER = forgeItemTag("tools/screwdriver");
    public static final TagKey<Item> LARGE_DRILL = forgeItemTag("tools/large_drill");
    public static final TagKey<Item> SOFT_HAMMER = forgeItemTag("tools/soft_hammer");
    public static final TagKey<Item> HARD_HAMMER = forgeItemTag("tools/hard_hammer");
    public static final TagKey<Item> FILE = forgeItemTag("tools/file");
    public static final TagKey<Item> SOLDERING_IRON = forgeItemTag("tools/soldering_iron");
    public static final TagKey<Item> SAW = forgeItemTag("tools/saw");

    // Mod Items
    public static final TagKey<Item> ADVANCED_CIRCUIT = itemTag("advanced_circuit");
    public static final TagKey<Item> ADVANCED_HEAT_VENT = itemTag("advanced_heat_vent");
    public static final TagKey<Item> BATPACK = itemTag("batpack");
    public static final TagKey<Item> CARBON_MESH = itemTag("carbon_mesh");
    public static final TagKey<Item> CARBON_PLATE = itemTag("carbon_plate");
    public static final TagKey<Item> CIRCUIT = itemTag("circuit");
    public static final TagKey<Item> COAL_CHUNK = itemTag("coal_chunk");
    public static final TagKey<Item> COMPONENT_HEAT_VENT = itemTag("component_heat_vent");
    public static final TagKey<Item> COMPRESSED_COAL_BALL = itemTag("compressed_coal_ball");
    public static final TagKey<Item> DENSE_COPPER_PLATE = itemTag("dense_copper_plate");
    public static final TagKey<Item> DOUBLE_INSULATED_GOLD_CABLE = itemTag("double_insulated_gold_cable");
    public static final TagKey<Item> EMPTY_FLUID_CELL = itemTag("empty_fluid_cell");
    public static final TagKey<Item> EMPTY_FUEL_CAN = itemTag("empty_fuel_can");
    public static final TagKey<Item> ENERGY_CRYSTAL = itemTag("energy_crystal");
    public static final TagKey<Item> GENERATOR = itemTag("generator");
    public static final TagKey<Item> HEAT_VENT = itemTag("heat_vent");
    public static final TagKey<Item> HV_TRANSFORMER = itemTag("hv_transformer");
    public static final TagKey<Item> ILLUMINATOR_FLAT = itemTag("illuminator");
    public static final TagKey<Item> INSULATED_COPPER_CABLE = itemTag("insulated_copper_cable");
    public static final TagKey<Item> IRIDIUM_ALLOY = itemTag("plates/iridium_alloy");
    public static final TagKey<Item> IRIDIUM_NEUTRON_REFLECTOR = itemTag("illuminator");
    public static final TagKey<Item> LAPOTRON_CRYSTAL = itemTag("lapotron_crystal");
    public static final TagKey<Item> LAPPACK = itemTag("lappack");
    public static final TagKey<Item> OIL_SAND = itemTag("oil_sand");
    public static final TagKey<Item> OVERCLOCKED_HEAT_VENT = itemTag("overclocked_heat_vent");
    public static final TagKey<Item> PUMP = itemTag("pump");
    public static final TagKey<Item> REACTOR_COOLANT_CELL = itemTag("reactor_coolant_cell");
    public static final TagKey<Item> REINFORCED_STONE = itemTag("reinforced_stone");
    public static final TagKey<Item> RESIN = itemTag("resin");
    public static final TagKey<Item> RE_BATTERY = itemTag("re_battery");
    public static final TagKey<Item> RUBBER = itemTag("rubber");
    public static final TagKey<Item> SEXTUPLE_REACTOR_COOLANT_CELL = itemTag("sextuple_reactor_coolant_cell");
    public static final TagKey<Item> SOLAR_GENERATOR = itemTag("solar_generator");
    public static final TagKey<Item> SOLAR_HELMET = itemTag("solar_helmet");
    public static final TagKey<Item> TELEPORTER = itemTag("teleporter");
    public static final TagKey<Item> TRANSFORMER_UPGRADE = itemTag("transformer_upgrade");
    public static final TagKey<Item> WRENCH = itemTag("tools/wrench");
    // Regular Iron OR Refined Iron
    public static final TagKey<Item> UNIVERSAL_IRON_INGOT = itemTag("ingots/universal_iron");
    public static final TagKey<Item> UNIVERSAL_IRON_PLATE = itemTag("plates/universal_iron");
    public static final TagKey<Item> UNIVERSAL_IRON_ROD = itemTag("rods/universal_iron");
    // Regular Iron AND Refined iron
    public static final TagKey<Item> ANY_IRON_INGOT = itemTag("ingots/any_iron");

    public static final TagKey<Biome> PLACE_BAUXITE = biomeTag("place_bauxite");
    public static final TagKey<Biome> PLACE_RUBY = biomeTag("place_ruby");
    public static final TagKey<Biome> PLACE_SAPPHIRE = biomeTag("place_sapphire");
    public static final TagKey<Biome> PLACE_TETRAHEDRITE = biomeTag("place_tetrahedrite");
    public static final TagKey<Biome> PLACE_CASSITERITE = biomeTag("place_cassiterite");

    // Let's keep ic2 steam only tagged for GT now
    public static final TagKey<Fluid> STEAM = fluidTag("steam");
    public static final TagKey<Fluid> FORGE_STEAM = forgeFluidTag("steam");
    public static final TagKey<Fluid> AIR = forgeFluidTag("air");

    private static final Map<String, Map<String, TagKey<Item>>> FORGE_MATERIALS = new HashMap<>();

    public static TagKey<Item> ingot(String name) {
        return material("ingots", name);
    }

    public static TagKey<Item> dust(String name) {
        return material("dusts", name);
    }

    public static TagKey<Item> stone(String name) {
        return material("stone", name);
    }

    public static TagKey<Item> smallDust(String name) {
        return material("small_dusts", name);
    }

    public static TagKey<Item> ore(String name) {
        return material("ores", name);
    }

    public static TagKey<Item> gear(String name) {
        return material("gears", name);
    }

    public static TagKey<Item> gem(String name) {
        return material("gems", name);
    }

    public static TagKey<Item> material(String type, String name) {
        return FORGE_MATERIALS.computeIfAbsent(type, s -> new HashMap<>())
            .computeIfAbsent(name, s -> forgeItemTag(type + "/" + name));
    }

    public static Collection<TagKey<Item>> getAllMaterials() {
        return StreamEx.of(FORGE_MATERIALS.values())
            .flatMap(map -> StreamEx.of(map.values()))
            .toList();
    }

    private static TagKey<Item> forgeItemTag(String name) {
        return ItemTags.create(new ResourceLocation("forge", name));
    }

    public static TagKey<Item> itemTag(String name) {
        return ItemTags.create(location(name));
    }

    private static TagKey<Block> blockTag(String name) {
        return BlockTags.create(location(name));
    }

    private static TagKey<Block> forgeBlockTag(String name) {
        return BlockTags.create(new ResourceLocation("forge", name));
    }

    private static TagKey<EntityType<?>> entityTag(String name) {
        return TagKey.create(ForgeRegistries.Keys.ENTITY_TYPES, location(name));
    }

    private static TagKey<EntityType<?>> forgeEntityTag(String name) {
        return TagKey.create(ForgeRegistries.Keys.ENTITY_TYPES, new ResourceLocation("forge", name));
    }

    private static TagKey<Biome> biomeTag(String name) {
        return TagKey.create(ForgeRegistries.Keys.BIOMES, location(name));
    }

    private static TagKey<Fluid> fluidTag(String name) {
        return TagKey.create(ForgeRegistries.Keys.FLUIDS, location(name));
    }

    public static TagKey<Fluid> forgeFluidTag(String name) {
        return TagKey.create(ForgeRegistries.Keys.FLUIDS, new ResourceLocation("forge", name));
    }

    private GregTechTags() {}
}
