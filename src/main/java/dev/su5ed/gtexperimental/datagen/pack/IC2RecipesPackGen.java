package dev.su5ed.gtexperimental.datagen.pack;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.compat.DamagedIC2ReactorComponentIngredient;
import dev.su5ed.gtexperimental.compat.ModHandler;
import dev.su5ed.gtexperimental.datagen.recipe.IndustrialElectrolyzerRecipesGen;
import dev.su5ed.gtexperimental.datagen.recipe.IndustrialGrinderRecipesGen;
import dev.su5ed.gtexperimental.object.Component;
import dev.su5ed.gtexperimental.object.Dust;
import dev.su5ed.gtexperimental.object.Ingot;
import dev.su5ed.gtexperimental.object.Miscellaneous;
import dev.su5ed.gtexperimental.object.ModCoverItem;
import dev.su5ed.gtexperimental.object.ModFluid;
import dev.su5ed.gtexperimental.object.NuclearCoolantPack;
import dev.su5ed.gtexperimental.object.Ore;
import dev.su5ed.gtexperimental.object.Plate;
import dev.su5ed.gtexperimental.object.Smalldust;
import dev.su5ed.gtexperimental.object.Tool;
import dev.su5ed.gtexperimental.object.Wrench;
import dev.su5ed.gtexperimental.recipe.crafting.RemovedRecipeBuilder;
import dev.su5ed.gtexperimental.recipe.gen.compat.TEBottlerRecipeBuilder;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipeName;
import dev.su5ed.gtexperimental.recipe.type.VanillaFluidIngredient;
import ic2.core.ref.Ic2Fluids;
import ic2.core.ref.Ic2Items;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.datagen.RecipeGen.IC2_LOADED;
import static dev.su5ed.gtexperimental.recipe.crafting.ConditionalShapedRecipeBuilder.conditionalShaped;
import static dev.su5ed.gtexperimental.recipe.crafting.ConditionalShapelessRecipeBuilder.conditionalShapeless;
import static dev.su5ed.gtexperimental.recipe.gen.ModRecipeBuilders.*;
import static dev.su5ed.gtexperimental.recipe.gen.compat.CompatRecipeBuilders.ic2Extractor;
import static dev.su5ed.gtexperimental.recipe.type.RecipeUtil.*;
import static dev.su5ed.gtexperimental.util.GtUtil.buckets;

public class IC2RecipesPackGen extends RecipeProvider {
    private final String name;

    public IC2RecipesPackGen(DataGenerator generator, String name) {
        super(generator);
        this.name = name;
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        // Alloy Smelter
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Ic2Items.IRON_FURNACE), new ItemStack(Items.IRON_INGOT, 5), 130, 3)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, alloySmelterId("iron_ingot_from_iron_furnace"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Ic2Items.EMPTY_CELL), Ingot.TIN.getItemStack(), 130, 3)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, alloySmelterId("tin_ingot_from_empty_cell"), true);

        // Assembler
        assembler(ModRecipeIngredientTypes.ITEM.of(GregTechTags.UNIVERSAL_IRON_PLATE, 8), ModRecipeIngredientTypes.ITEM.of(Component.MACHINE_PARTS), new ItemStack(Ic2Items.MACHINE), 400, 8)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, assemblerId("machine"));
        assembler(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ingots", "tin"), 2), new ItemStack(Ic2Items.EMPTY_CELL), 400, 1)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, assemblerId("empty_cell"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Plate.TIN.getTag()), new ItemStack(Ic2Items.TIN_CAN), 400, 1)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, assemblerId("tin_can"));
        assembler(ModRecipeIngredientTypes.ITEM.of(GregTechTags.CRAFTING_RAW_MACHINE_TIER_1), ModRecipeIngredientTypes.ITEM.of(ModCoverItem.SOLAR_PANEL.getTag()), new ItemStack(Ic2Items.SOLAR_GENERATOR), 1600, 2)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, assemblerId("solar_generator"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Component.BASIC_CIRCUIT_BOARD.getTag()), ModRecipeIngredientTypes.ITEM.of(GregTechTags.INSULATED_COPPER_CABLE, 3), new ItemStack(Ic2Items.CIRCUIT), 800, 1)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, assemblerId("circuit"));
        assembler(ModRecipeIngredientTypes.ITEM.of(GregTechTags.COMPRESSED_COAL_BALL, 8), ModRecipeIngredientTypes.ITEM.ofValues(new Ingredient.ItemValue(new ItemStack(Items.BRICKS)), new Ingredient.TagValue(Tags.Items.STORAGE_BLOCKS_IRON), new Ingredient.TagValue(Tags.Items.OBSIDIAN)), new ItemStack(Ic2Items.COAL_CHUNK), 400, 4)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, assemblerId("coal_chunk_from_bricks"));
        assembler(ModRecipeIngredientTypes.ITEM.of(GregTechTags.dust("coal"), 8), ModRecipeIngredientTypes.ITEM.of(Items.FLINT), new ItemStack(Ic2Items.COAL_BLOCK), 400, 4)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, assemblerId("compressed_coal_ball"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Component.ADVANCED_CIRCUIT_BOARD.getTag()), ModRecipeIngredientTypes.ITEM.of(Component.ADVANCED_CIRCUIT_PARTS, 2), new ItemStack(Ic2Items.ADVANCED_CIRCUIT), 1600, 2)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, assemblerId("advanced_circuit"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Ic2Items.COPPER_CABLE), ModRecipeIngredientTypes.ITEM.of(GregTechTags.RUBBER), new ItemStack(Ic2Items.INSULATED_COPPER_CABLE), 100, 2)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, assemblerId("insulated_copper_cable"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Ic2Items.GOLD_CABLE), ModRecipeIngredientTypes.ITEM.of(GregTechTags.RUBBER, 2), new ItemStack(Ic2Items.DOUBLE_INSULATED_GOLD_CABLE), 200, 2)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, assemblerId("double_insulated_gold_cable_from_raw"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Ic2Items.IRON_CABLE), ModRecipeIngredientTypes.ITEM.of(GregTechTags.RUBBER, 3), new ItemStack(Ic2Items.TRIPLE_INSULATED_IRON_CABLE), 300, 2)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, assemblerId("triple_insulated_iron_cable_from_raw"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Ic2Items.INSULATED_GOLD_CABLE), ModRecipeIngredientTypes.ITEM.of(GregTechTags.RUBBER), new ItemStack(Ic2Items.DOUBLE_INSULATED_GOLD_CABLE), 200, 2)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, assemblerId("double_insulated_gold_cable_from_insulated"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Ic2Items.INSULATED_IRON_CABLE), ModRecipeIngredientTypes.ITEM.of(GregTechTags.RUBBER, 2), new ItemStack(Ic2Items.TRIPLE_INSULATED_IRON_CABLE), 200, 2)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, assemblerId("triple_insulated_iron_cable_from_insulated"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Ic2Items.DOUBLE_INSULATED_IRON_CABLE), ModRecipeIngredientTypes.ITEM.of(GregTechTags.RUBBER), new ItemStack(Ic2Items.TRIPLE_INSULATED_IRON_CABLE), 100, 2)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, assemblerId("triple_insulated_iron_cable_from_double_insulated"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Dust.FLINT.getTag(), 5), ModRecipeIngredientTypes.ITEM.of(Items.TNT, 3), new ItemStack(Ic2Items.ITNT), 800, 2)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, assemblerId("itnt"));
        assembler(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ingot("tin")), new ItemStack(Ic2Items.TIN_CABLE, 4), 150, 1)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, assemblerId("tin_cable"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Ic2Items.WATER_GENERATOR, 2), new ItemStack(Ic2Items.GENERATOR), 6400, 8)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, assemblerId("generator"));
        assembler(ModRecipeIngredientTypes.ITEM.of(GregTechTags.GENERATOR), ModRecipeIngredientTypes.ITEM.of(Plate.ALUMINIUM.getTag(), 4), new ItemStack(Ic2Items.WATER_GENERATOR), 6400, 8)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, assemblerId("water_generator"));
        assembler(ModRecipeIngredientTypes.ITEM.of(GregTechTags.GENERATOR), ModRecipeIngredientTypes.ITEM.of(GregTechTags.CARBON_PLATE, 4), new ItemStack(Ic2Items.WIND_GENERATOR), 6400, 8)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, assemblerId("wind_generator_from_carbon"));
        assembler(ModRecipeIngredientTypes.ITEM.of(GregTechTags.GENERATOR), ModRecipeIngredientTypes.ITEM.of(Plate.MAGNALIUM.getTag(), 2), new ItemStack(Ic2Items.WIND_GENERATOR), 6400, 8)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, assemblerId("wind_generator_from_magnalium"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Ic2Items.CARBON_FIBRE, 2), new ItemStack(Ic2Items.CARBON_MESH), 800, 2)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, assemblerId("carbon_mesh"));
        assembler(ModRecipeIngredientTypes.ITEM.of(Ic2Items.ALLOY, 2), ModRecipeIngredientTypes.ITEM.of(Items.GLASS, 7), new ItemStack(Ic2Items.REINFORCED_GLASS, 7), 400, 4)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, assemblerId("reinforced_glass"));
//        assembler(ModRecipeIngredientTypes.ITEM.of(GregTechTags.CRAFTING_LI_BATTERY, 8), ModRecipeIngredientTypes.ITEM.of(Ic2Items.CROPNALYZER), Tool.SCANNER.getItemStack(), 12800, 16)
//            .addConditions(IC2_LOADED)
//            .build(finishedRecipeConsumer, id("scanner"));
//        assembler(ModRecipeIngredientTypes.ITEM.of(Ic2Items.CIRCUIT), ModRecipeIngredientTypes.ITEM.of(GregTechTags.CRAFTING_WIRE_COPPER), new ItemStack(Ic2Items.FREQUENCY_TRANSMITTER), 800, 1)
//            .addConditions(IC2_LOADED)
//            .build(finishedRecipeConsumer, id("circuit"));
//        assembler(ModRecipeIngredientTypes.ITEM.of(Ic2Items.BATPACK), new ItemStack(Ic2Items.RE_BATTERY, 6), 1600, 2)
//            .addConditions(IC2_LOADED)
//            .build(finishedRecipeConsumer, id("re_battery"));

        // Bender
        bender(ModRecipeIngredientTypes.ITEM.of(Ic2Items.MIXED_METAL_INGOT), new ItemStack(Ic2Items.ALLOY), 100, 8)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, benderId("alloy"));

        // IC2 Extractor
        ic2Extractor(Items.SLIME_BALL, 1, new ItemStack(Ic2Items.RESIN, 2), finishedRecipeConsumer, this.name);

        // Fusion Reactor
        fusionSolid(ModRecipeIngredientTypes.FLUID.of(ModFluid.WOLFRAMIUM.getTag()), ModRecipeIngredientTypes.FLUID.of(ModFluid.LITHIUM.getTag()), new ItemStack(Ic2Items.IRIDIUM_ORE), 512, 32768, 150000000)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, fluidSolidId("iridium_ore"));

        // Implosion
        implosion(ModRecipeIngredientTypes.ITEM.of(Ingot.IRIDIUM_ALLOY.getTag()), new ItemStack(Ic2Items.IRIDIUM), Dust.DARK_ASHES.getItemStack(4), 8)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, implosionId("iridium"));

        // Industrial Centrifuge
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(GregTechTags.material("logs", "rubber"), 16), new ItemStack(Ic2Items.RESIN, 8), new ItemStack(Ic2Items.PLANT_BALL, 6), ModFluid.METHANE.getBuckets(1), ModFluid.CARBON.getBuckets(4), 5000)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, industrialCentrifugeId("rubber_log"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Ic2Items.TERRA_WART, 16), ModFluid.METHANE.getBuckets(1), 5000)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, industrialCentrifugeId("terra_wart"));

        // Industrial Electrolyzer
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.SALTPETER.getTag(), 10), ModFluid.POTASSIUM.getBuckets(2), ModFluid.NITROGEN.getBuckets(2), new FluidStack(Ic2Fluids.AIR.still, buckets(3)), 50, 110)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, IndustrialElectrolyzerRecipesGen.id("saltpeter_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.OBSIDIAN.getTag(), 4), Smalldust.MAGNESIUM.getItemStack(2), Smalldust.IRON.getItemStack(2), ModFluid.SILICON.getBuckets(2), new FluidStack(Ic2Fluids.AIR.still, buckets(2)), 500, 5)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, IndustrialElectrolyzerRecipesGen.id("obsidian_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.PYROPE.getTag(), 20), Dust.MAGNESIUM.getItemStack(3), Dust.ALUMINIUM.getItemStack(2), ModFluid.SILICON.getBuckets(3), new FluidStack(Ic2Fluids.AIR.still, buckets(6)), 1790, 50)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, IndustrialElectrolyzerRecipesGen.id("pyrope_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.ALMANDINE.getTag(), 20), Dust.IRON.getItemStack(3), Dust.ALUMINIUM.getItemStack(2), ModFluid.SILICON.getBuckets(3), new FluidStack(Ic2Fluids.AIR.still, buckets(6)), 1640, 50)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, IndustrialElectrolyzerRecipesGen.id("almandine_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.SPESSARTINE.getTag(), 20), Dust.ALUMINIUM.getItemStack(2), Dust.MANGANESE.getItemStack(3), ModFluid.SILICON.getBuckets(3), new FluidStack(Ic2Fluids.AIR.still, buckets(6)), 1810, 50)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, IndustrialElectrolyzerRecipesGen.id("spessartine_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.ANDRADITE.getTag(), 20), ModFluid.CALCIUM.getBuckets(3), Dust.IRON.getItemStack(2), ModFluid.SILICON.getBuckets(3), new FluidStack(Ic2Fluids.AIR.still, 6), 1280, 50)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, IndustrialElectrolyzerRecipesGen.id("andradite_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.GROSSULAR.getTag(), 20), ModFluid.CALCIUM.getBuckets(3), Dust.IRON.getItemStack(2), ModFluid.SILICON.getBuckets(3), new FluidStack(Ic2Fluids.AIR.still, 6), 2050, 50)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, IndustrialElectrolyzerRecipesGen.id("grossular_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.UVAROVITE.getTag(), 20), ModFluid.CALCIUM.getBuckets(3), Dust.CHROME.getItemStack(2), ModFluid.SILICON.getBuckets(3), new FluidStack(Ic2Fluids.AIR.still, 6), 2200, 50)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, IndustrialElectrolyzerRecipesGen.id("uvarovite_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.OLIVINE.getTag(), 9), Dust.MAGNESIUM.getItemStack(2), Dust.IRON.getItemStack(2), ModFluid.SILICON.getBuckets(1), new FluidStack(Ic2Fluids.AIR.still, buckets(1)), 600, 60)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, IndustrialElectrolyzerRecipesGen.id("olivine_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.EMERALD.getTag(), 29), Dust.ALUMINIUM.getItemStack(2), ModFluid.BERYLIUM.getBuckets(3), ModFluid.SILICON.getBuckets(6), new FluidStack(Ic2Fluids.AIR.still, buckets(9)), 600, 50)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, IndustrialElectrolyzerRecipesGen.id("emerald_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.RUBY.getTag(), 9), Dust.ALUMINIUM.getItemStack(2), Dust.CHROME.getItemStack(), new FluidStack(Ic2Fluids.AIR.still, buckets(3)), 500, 50)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, IndustrialElectrolyzerRecipesGen.id("ruby_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.SAPPHIRE.getTag(), 8), Dust.ALUMINIUM.getItemStack(2), new FluidStack(Ic2Fluids.AIR.still, buckets(3)), 400, 50)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, IndustrialElectrolyzerRecipesGen.id("sapphire_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.PYRITE.getTag(), 3), Dust.IRON.getItemStack(), Dust.SULFUR.getItemStack(2), 120, 128)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, IndustrialElectrolyzerRecipesGen.id("pyrite_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.CALCITE.getTag(), 10), ModFluid.CALCIUM.getBuckets(2), ModFluid.CARBON.getBuckets(2), new FluidStack(Ic2Fluids.AIR.still, buckets(3)), 700, 80)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, IndustrialElectrolyzerRecipesGen.id("calcite_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.FLINT.getTag(), 8), ModFluid.SILICON.getBuckets(1), new FluidStack(Ic2Fluids.AIR.still, buckets(1)), 1000, 5)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, IndustrialElectrolyzerRecipesGen.id("flint_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.BAUXITE.getTag(), 12), Dust.ALUMINIUM.getItemStack(8), Smalldust.TITANIUM.getItemStack(2), ModFluid.HYDROGEN.getBuckets(5), new FluidStack(Ic2Fluids.AIR.still, buckets(3)), 2000, 128)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, IndustrialElectrolyzerRecipesGen.id("bauxite_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.ofFluid(ModFluid.SULFURIC_ACID.getTag(), 7), ModFluid.HYDROGEN.getBuckets(2), ModFluid.SULFUR.getBuckets(1), new FluidStack(Ic2Fluids.AIR.still, buckets(2)), 40, 100)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, IndustrialElectrolyzerRecipesGen.id("sulfuric_acid"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.ofFluid(FluidTags.WATER, 6), ModFluid.HYDROGEN.getBuckets(4), new FluidStack(Ic2Fluids.AIR.still, buckets(1)), 775, 120)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, IndustrialElectrolyzerRecipesGen.id("water"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Items.SAND, 16), ModFluid.SILICON.getBuckets(1), new FluidStack(Ic2Fluids.AIR.still, buckets(1)), 1000, 25)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, IndustrialElectrolyzerRecipesGen.id("sand"));

        // Industrial Grinder
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Tags.Items.ORES_DIAMOND), WATER, new ItemStack(Items.DIAMOND), Smalldust.DIAMOND.getItemStack(6), new ItemStack(Ic2Items.COAL_FUEL_DUST))
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, IndustrialGrinderRecipesGen.id("diamond_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.IRIDIUM.getTag()), WATER, new ItemStack(Ic2Items.IRIDIUM_ORE, 2), Smalldust.PLATINUM.getItemStack(2))
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, industrialGrinderId("iridium_ore_water"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.IRIDIUM.getTag()), MERCURY, new ItemStack(Ic2Items.IRIDIUM_ORE, 2), Dust.PLATINUM.getItemStack(2))
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, industrialGrinderId("iridium_ore_mercury"));

        // Pulverizer
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Ore.IRIDIUM), new ItemStack(Ic2Items.IRIDIUM_ORE, 2), Dust.PLATINUM.getItemStack())
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, pulverizerId("ores_iridium"));
        pulverizer(ModRecipeIngredientTypes.ITEM.ofTags(Ore.SHELDONITE.getTag(), GregTechTags.ore("platinum")), Dust.PLATINUM.getItemStack(2), new ItemStack(Ic2Items.IRIDIUM_ORE))
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, pulverizerId("ores_sheldonite"));
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Ic2Items.IRON_FENCE, 2), Smalldust.IRON.getItemStack(3))
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, pulverizerId("iron_fence"));
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Ic2Items.IRON_FURNACE), Dust.TIN.getItemStack(5))
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, pulverizerId("iron_furnace"));
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Ic2Items.CROP_STICK), Dust.WOOD.getItemStack())
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, pulverizerId("crop_stick"));
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Ic2Items.MACHINE), Dust.IRON.getItemStack(8))
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, pulverizerId("machine"));

        // Vacuum Freezer
        vacuumFreezerSolid(DamagedIC2ReactorComponentIngredient.recipeIngredient(Ic2Items.REACTOR_COOLANT_CELL), new ItemStack(Ic2Items.REACTOR_COOLANT_CELL), 100)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, vacuumFreezerId("reactor_coolant_cell"));
        vacuumFreezerSolid(DamagedIC2ReactorComponentIngredient.recipeIngredient(Ic2Items.TRIPLE_REACTOR_COOLANT_CELL), new ItemStack(Ic2Items.TRIPLE_REACTOR_COOLANT_CELL), 300)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, vacuumFreezerId("triple_reactor_coolant_cell"));
        vacuumFreezerSolid(DamagedIC2ReactorComponentIngredient.recipeIngredient(Ic2Items.SEXTUPLE_REACTOR_COOLANT_CELL), new ItemStack(Ic2Items.SEXTUPLE_REACTOR_COOLANT_CELL), 600)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, vacuumFreezerId("sextuple_reactor_coolant_cell"));

        // Wiremill
        wiremill(ModRecipeIngredientTypes.ITEM.of(Dust.COAL.getTag(), 4), new ItemStack(Ic2Items.CARBON_FIBRE), 400, 2)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, wiremillId("carbon_fibre"));
        wiremill(ModRecipeIngredientTypes.ITEM.of(Tags.Items.INGOTS_COPPER), new ItemStack(Ic2Items.COPPER_CABLE, 3), 100, 2)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, wiremillId("copper_cable"));
        wiremill(ModRecipeIngredientTypes.ITEM.of(Tags.Items.INGOTS_GOLD), new ItemStack(Ic2Items.GOLD_CABLE, 6), 200, 1)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, wiremillId("gold_cable"));
        wiremill(ModRecipeIngredientTypes.ITEM.of(GregTechTags.UNIVERSAL_IRON_INGOT), new ItemStack(Ic2Items.IRON_CABLE, 6), 200, 2)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, wiremillId("iron_cable"));

        // Crafting
        conditionalShaped(Ic2Items.OVERCLOCKER_UPGRADE, 2).define('H', Ingredient.of(NuclearCoolantPack.HELIUM_60K, NuclearCoolantPack.NAK_60K)).define('W', GregTechTags.INSULATED_COPPER_CABLE).define('C', GregTechTags.ADVANCED_CIRCUIT).pattern(" H ").pattern("WCW").unlockedBy("has_advanced_circuit", has(GregTechTags.ADVANCED_CIRCUIT)).addCondition(IC2_LOADED).save(finishedRecipeConsumer, shapedId("component/overclocker_upgrade"));
        conditionalShaped(Ic2Items.LAPOTRON_CRYSTAL).define('C', GregTechTags.ADVANCED_CIRCUIT).define('S', Miscellaneous.SAPPHIRE.getTag()).define('L', Dust.LAZURITE.getTag()).pattern("LCL").pattern("LSL").pattern("LCL").unlockedBy("has_advanced_circuit", has(GregTechTags.ADVANCED_CIRCUIT)).addCondition(IC2_LOADED).save(finishedRecipeConsumer, shapedId("component/lapotron_crystal_sapphire"));
        conditionalShaped(Ic2Items.ADVANCED_CIRCUIT).define('C', GregTechTags.CIRCUIT).define('S', Tags.Items.DUSTS_REDSTONE).define('G', Tags.Items.DUSTS_GLOWSTONE).define('L', Dust.LAZURITE.getTag()).pattern("SGS").pattern("LCL").pattern("SGS").unlockedBy("has_circuit", has(GregTechTags.CIRCUIT)).addCondition(IC2_LOADED).save(finishedRecipeConsumer, new ResourceLocation(ModHandler.IC2_MODID, "shaped/advanced_circuit"));
        conditionalShaped(Ic2Items.ADVANCED_CIRCUIT).define('C', GregTechTags.CIRCUIT).define('S', Tags.Items.DUSTS_REDSTONE).define('G', Tags.Items.DUSTS_GLOWSTONE).define('L', Dust.LAZURITE.getTag()).pattern("SLS").pattern("GCG").pattern("SLS").unlockedBy("has_circuit", has(GregTechTags.CIRCUIT)).addCondition(IC2_LOADED).save(finishedRecipeConsumer, new ResourceLocation(ModHandler.IC2_MODID, "shaped/advanced_circuit_vertical"));
        conditionalShaped(Ic2Items.CIRCUIT).define('C', Ic2Items.INSULATED_COPPER_CABLE).define('I', GregTechTags.UNIVERSAL_IRON_PLATE).define('R', Tags.Items.DUSTS_REDSTONE).pattern("CCC").pattern("RIR").pattern("CCC").unlockedBy("has_insulated_copper_cable", has(Ic2Items.INSULATED_COPPER_CABLE)).addCondition(IC2_LOADED).save(finishedRecipeConsumer, new ResourceLocation(ModHandler.IC2_MODID, "shaped/circuit"));
        conditionalShaped(Ic2Items.CIRCUIT).define('I', GregTechTags.UNIVERSAL_IRON_PLATE).define('R', Tags.Items.DUSTS_REDSTONE).define('C', Ic2Items.INSULATED_COPPER_CABLE).pattern("CRC").pattern("CIC").pattern("CRC").unlockedBy("has_insulated_copper_cable", has(Ic2Items.INSULATED_COPPER_CABLE)).addCondition(IC2_LOADED).save(finishedRecipeConsumer, new ResourceLocation(ModHandler.IC2_MODID, "shaped/circuit_vertical"));
        conditionalShaped(Ic2Items.RE_BATTERY, 2).define('S', VanillaFluidIngredient.of(ModFluid.SULFURIC_ACID.getTag(), buckets(1))).define('L', Dust.LEAD.getTag()).define('C', GregTechTags.INSULATED_COPPER_CABLE).define('T', Plate.TIN.getTag()).pattern(" C ").pattern("TLT").pattern("TST").unlockedBy("has_insulated_copper_cable", has(GregTechTags.INSULATED_COPPER_CABLE)).addCondition(IC2_LOADED).save(finishedRecipeConsumer, shapedId("component/re_battery_sulfur"));
        conditionalShaped(Ic2Items.RE_BATTERY, 2).define('R', Tags.Items.DUSTS_REDSTONE).define('C', GregTechTags.INSULATED_COPPER_CABLE).define('T', Plate.BATTERY_ALLOY.getTag()).pattern(" C ").pattern("TRT").pattern("TRT").unlockedBy("has_insulated_copper_cable", has(GregTechTags.INSULATED_COPPER_CABLE)).addCondition(IC2_LOADED).save(finishedRecipeConsumer, shapedId("component/re_battery_alloy"));
        conditionalShaped(Ic2Items.RE_BATTERY, 3).define('S', VanillaFluidIngredient.of(ModFluid.SULFURIC_ACID.getTag(), buckets(1))).define('L', Dust.LEAD.getTag()).define('C', GregTechTags.INSULATED_COPPER_CABLE).define('T', Plate.BATTERY_ALLOY.getTag()).pattern(" C ").pattern("TLT").pattern("TST").unlockedBy("has_insulated_copper_cable", has(GregTechTags.INSULATED_COPPER_CABLE)).addCondition(IC2_LOADED).save(finishedRecipeConsumer, shapedId("component/re_battery_sulfur_alloy"));
        conditionalShaped(Ic2Items.SINGLE_USE_BATTERY, 32).define('Q', VanillaFluidIngredient.of(ModFluid.MERCURY.getTag(), buckets(1))).define('R', Tags.Items.DUSTS_REDSTONE).define('C', GregTechTags.INSULATED_COPPER_CABLE).pattern("C").pattern("Q").pattern("R").unlockedBy("has_insulated_copper_cable", has(GregTechTags.INSULATED_COPPER_CABLE)).addCondition(IC2_LOADED).save(finishedRecipeConsumer, shapedId("component/single_use_battery"));
        conditionalShaped(Ic2Items.SINGLE_USE_BATTERY, 32).define('S', VanillaFluidIngredient.of(ModFluid.SULFURIC_ACID.getTag(), buckets(1))).define('R', Dust.LEAD.getTag()).define('C', GregTechTags.INSULATED_COPPER_CABLE).pattern("C").pattern("S").pattern("R").unlockedBy("has_insulated_copper_cable", has(GregTechTags.INSULATED_COPPER_CABLE)).addCondition(IC2_LOADED).save(finishedRecipeConsumer, shapedId("component/single_use_battery_sulfur"));
        conditionalShaped(Ic2Items.MIXED_METAL_INGOT, 6).define('X', Plate.TUNGSTEN_STEEL.getTag()).define('Y', tagsIngredient(Plate.BRONZE.getTag(), Plate.BRASS.getTag())).define('Z', Plate.ALUMINIUM.getTag()).pattern("X").pattern("Y").pattern("Z").unlockedBy("has_tungsten_steel_plate", has(Plate.TUNGSTEN_STEEL.getTag())).addCondition(IC2_LOADED).save(finishedRecipeConsumer, new ResourceLocation(ModHandler.IC2_MODID, "shaped/mixed_metal_ingot"));
        conditionalShaped(Ic2Items.MIXED_METAL_INGOT, 5).define('X', Plate.TUNGSTEN_STEEL.getTag()).define('Y', tagsIngredient(Plate.BRONZE.getTag(), Plate.BRASS.getTag())).define('Z', tagsIngredient(Plate.TIN.getTag(), Plate.ZINC.getTag())).pattern("X").pattern("Y").pattern("Z").unlockedBy("has_tungsten_steel_plate", has(Plate.TUNGSTEN_STEEL.getTag())).addCondition(IC2_LOADED).save(finishedRecipeConsumer, shapedId("mixed_metal_ingot_2"));
        conditionalShaped(Ic2Items.MIXED_METAL_INGOT, 4).define('X', tagsIngredient(Plate.TITANIUM.getTag(), Plate.TUNGSTEN.getTag())).define('Y', tagsIngredient(Plate.BRONZE.getTag(), Plate.BRASS.getTag())).define('Z', Plate.ALUMINIUM.getTag()).pattern("X").pattern("Y").pattern("Z").unlockedBy("has_titanium_plate", hasTags(Plate.TITANIUM.getTag(), Plate.TUNGSTEN.getTag())).addCondition(IC2_LOADED).save(finishedRecipeConsumer, shapedId("mixed_metal_ingot_3"));
        conditionalShaped(Ic2Items.MIXED_METAL_INGOT, 3).define('X', tagsIngredient(Plate.TITANIUM.getTag(), Plate.TUNGSTEN.getTag())).define('Y', tagsIngredient(Plate.BRONZE.getTag(), Plate.BRASS.getTag())).define('Z', tagsIngredient(Plate.TIN.getTag(), Plate.ZINC.getTag())).pattern("X").pattern("Y").pattern("Z").unlockedBy("has_titanium_plate", hasTags(Plate.TITANIUM.getTag(), Plate.TUNGSTEN.getTag())).addCondition(IC2_LOADED).save(finishedRecipeConsumer, shapedId("mixed_metal_ingot_4"));
        conditionalShaped(Ic2Items.MIXED_METAL_INGOT, 3).define('X', tagsIngredient(Plate.INVAR.getTag(), Plate.STEEL.getTag())).define('Y', tagsIngredient(Plate.BRONZE.getTag(), Plate.BRASS.getTag())).define('Z', Plate.ALUMINIUM.getTag()).pattern("X").pattern("Y").pattern("Z").unlockedBy("has_invar_plate", hasTags(Plate.INVAR.getTag(), Plate.STEEL.getTag())).addCondition(IC2_LOADED).save(finishedRecipeConsumer, shapedId("mixed_metal_ingot_5"));
        conditionalShaped(Ic2Items.MIXED_METAL_INGOT, 2).define('X', tagsIngredient(Plate.INVAR.getTag(), Plate.STEEL.getTag())).define('Y', tagsIngredient(Plate.BRONZE.getTag(), Plate.BRASS.getTag())).define('Z', tagsIngredient(Plate.TIN.getTag(), Plate.ZINC.getTag())).pattern("X").pattern("Y").pattern("Z").unlockedBy("has_invar_plate", hasTags(Plate.INVAR.getTag(), Plate.STEEL.getTag())).addCondition(IC2_LOADED).save(finishedRecipeConsumer, shapedId("mixed_metal_ingot_6"));
        conditionalShaped(Ic2Items.MIXED_METAL_INGOT).define('X', tagsIngredient(GregTechTags.UNIVERSAL_IRON_PLATE, Plate.NICKEL.getTag())).define('Y', tagsIngredient(Plate.BRONZE.getTag(), Plate.BRASS.getTag())).define('Z', tagsIngredient(Plate.TIN.getTag(), Plate.ZINC.getTag(), Plate.ALUMINIUM.getTag())).pattern("X").pattern("Y").pattern("Z").unlockedBy("has_bronze_plate", hasTags(Plate.BRONZE.getTag(), Plate.BRASS.getTag())).addCondition(IC2_LOADED).save(finishedRecipeConsumer, shapedId("mixed_metal_ingot_7"));
//        shaped(Ic2Items.LAPPACK).define('C', GregTechTags.CIRCUIT_TIER_4).define('B', GregTechTags.BATPACK).define('L', GregTechTags.LAZURITE_CHUNK).pattern("LCL").pattern("LBL").pattern("L L").unlockedBy("has_circuit_tier_4", has(GregTechTags.CIRCUIT_TIER_4)).save(finishedRecipeConsumer, shapedId("lappack"));
        conditionalShapeless(Ic2Items.COAL_FUEL_DUST).requires(Tool.HYDRATION_SPRAY).requires(Dust.COAL.getTag()).unlockedBy("has_hydration_spray", has(Tool.HYDRATION_SPRAY)).addCondition(IC2_LOADED).save(finishedRecipeConsumer, shapelessId("material/coal_fuel_dust"));
        conditionalShapeless(Ic2Items.COAL_FUEL_DUST).requires(GregTechTags.MORTAR).requires(Dust.COAL.getTag()).unlockedBy("has_mortar", has(GregTechTags.MORTAR)).addCondition(IC2_LOADED).save(finishedRecipeConsumer, shapelessId("material/coal_fuel_dust_crushing"));
        conditionalShapeless(Ic2Items.CONTAINMENT_REACTOR_PLATING).requires(Ic2Items.REACTOR_PLATING).requires(Plate.LEAD).unlockedBy("has_reactor_plating", has(Ic2Items.REACTOR_PLATING)).addCondition(IC2_LOADED).save(finishedRecipeConsumer, shapelessId("nuclear/containment_reactor_plating"));
        conditionalShaped(Ic2Items.DUAL_URANIUM_FUEL_ROD).define('C', Ic2Items.URANIUM_FUEL_ROD).define('P', tagsIngredient(Plate.COPPER.getTag(), Plate.LEAD.getTag())).pattern("CPC").unlockedBy("has_uranium_fuel_rod", has(Ic2Items.URANIUM_FUEL_ROD)).addCondition(IC2_LOADED).save(finishedRecipeConsumer, shapedId("nuclear/dual_uranium_fuel_rod"));
        conditionalShaped(Ic2Items.QUAD_URANIUM_FUEL_ROD).define('C', Ic2Items.DUAL_URANIUM_FUEL_ROD).define('P', tagsIngredient(Plate.COPPER.getTag(), Plate.LEAD.getTag())).pattern(" C ").pattern("PPP").pattern(" C ").unlockedBy("has_dual_uranium_fuel_rod", has(Ic2Items.DUAL_URANIUM_FUEL_ROD)).addCondition(IC2_LOADED).save(finishedRecipeConsumer, shapedId("nuclear/quad_uranium_fuel_rod"));
        conditionalShaped(Ic2Items.REACTOR_HEAT_VENT).define('I', Items.IRON_BARS).define('A', Plate.ALUMINIUM.getTag()).pattern("AIA").pattern("I I").pattern("AIA").unlockedBy("has_aluminium_plate", has(Plate.ALUMINIUM.getTag())).addCondition(IC2_LOADED).save(finishedRecipeConsumer, shapedId("nuclear/reactor_heat_vent"));
//        conditionalShaped(Ic2Items.MINING_LASER).define('C', GregTechTags.ADVANCED_CIRCUIT).define('H', NuclearCoolantPack.HELIUM_360K.getTag()).define('R', Miscellaneous.RUBY.getTag()).define('T', Plate.TITANIUM.getTag()).define(SelectedProfileCondition.CLASSIC, 'E', GregTechTags.CRAFTING_100K_EU_STORE).define(SelectedProfileCondition.EXPERIMENTAL, 'E', GregTechTags.CRAFTING_1KK_EU_STORE).define('A', GregTechTags.ADVANCED_ALLOY).pattern("RHE").pattern("TTC").pattern(" AA").addCondition(IC2_LOADED).save(finishedRecipeConsumer, new ResourceLocation(ModHandler.IC2_MODID, "shaped/mining_laser"));
        conditionalShaped(Wrench.BRONZE).define('B', GregTechTags.ingot("bronze")).pattern("B B").pattern("BBB").pattern(" B ").unlockedBy("has_bronze_ingot", has(GregTechTags.ingot("bronze"))).addCondition(IC2_LOADED).save(finishedRecipeConsumer, new ResourceLocation(ModHandler.IC2_MODID, "shaped/wrench"));
        conditionalShaped(Wrench.BRONZE).define('B', GregTechTags.ingot("bronze")).pattern(" B ").pattern("BBB").pattern("B B").unlockedBy("has_bronze_ingot", has(GregTechTags.ingot("bronze"))).addCondition(IC2_LOADED).save(finishedRecipeConsumer, new ResourceLocation(ModHandler.IC2_MODID, "shaped/wrench_down"));
        conditionalShapeless(Ic2Items.COIN, 8).requires(Miscellaneous.SILVER_CREDIT).unlockedBy("has_silver_credit", has(Miscellaneous.SILVER_CREDIT)).addCondition(IC2_LOADED).save(finishedRecipeConsumer, shapelessId("coin"));
        conditionalShapeless(Ic2Items.COIN).requires(Miscellaneous.COPPER_CREDIT, 8).unlockedBy("has_copper_credit", has(Miscellaneous.COPPER_CREDIT)).addCondition(IC2_LOADED).save(finishedRecipeConsumer, shapelessId("coin_from_copper"));
        conditionalShapeless(Miscellaneous.COPPER_CREDIT, 8).requires(Ic2Items.COIN).unlockedBy("has_coin", has(Ic2Items.COIN)).addCondition(IC2_LOADED).save(finishedRecipeConsumer, shapelessId("copper_credit"));
        conditionalShapeless(Miscellaneous.SILVER_CREDIT).requires(Ic2Items.COIN, 8).unlockedBy("has_coin", has(Ic2Items.COIN)).addCondition(IC2_LOADED).save(finishedRecipeConsumer, shapelessId("silver_credit_from_coins"));
        conditionalShapeless(Ic2Items.FERTILIZER, 4).requires(Ic2Items.FERTILIZER).requires(Dust.PHOSPHORUS.getTag()).unlockedBy("has_phosphorus_dust", has(Dust.PHOSPHORUS.getTag())).addCondition(IC2_LOADED).save(finishedRecipeConsumer, shapelessId("fertilizer"));
        conditionalShapeless(Ic2Items.FERTILIZER, 2).requires(Ic2Items.FERTILIZER).requires(Items.BONE_MEAL).unlockedBy("has_bone_meal", has(Items.BONE_MEAL)).addCondition(IC2_LOADED).save(finishedRecipeConsumer, shapelessId("fetilizer_bone_meal"));
        conditionalShapeless(Ic2Items.FERTILIZER, 3).requires(Ic2Items.FERTILIZER).requires(Dust.SULFUR.getTag()).requires(VanillaFluidIngredient.of(ModFluid.CALCIUM.getTag(), buckets(1))).unlockedBy("has_sulfur_dust", has(Dust.SULFUR.getTag())).addCondition(IC2_LOADED).save(finishedRecipeConsumer, shapelessId("fertilizer_sulfur_dust"));
        conditionalShapeless(Ic2Items.FERTILIZER, 3).requires(Ic2Items.FERTILIZER).requires(VanillaFluidIngredient.of(ModFluid.SULFUR.getTag(), buckets(1))).requires(VanillaFluidIngredient.of(ModFluid.CALCIUM.getTag(), buckets(1))).unlockedBy("has_sulfur_fluid", hasFluid(ModFluid.SULFUR.getTag())).addCondition(IC2_LOADED).save(finishedRecipeConsumer, shapelessId("fertilizer_sulfur_fluid"));
        conditionalShapeless(Ic2Items.FERTILIZER, 2).requires(Ic2Items.FERTILIZER).requires(Dust.ASHES.getTag(), 3).unlockedBy("has_ashes_dust", has(Dust.ASHES.getTag())).addCondition(IC2_LOADED).save(finishedRecipeConsumer, shapelessId("fertilizer_ashes"));
        conditionalShapeless(Ic2Items.FERTILIZER, 2).requires(Ic2Items.FERTILIZER).requires(Dust.DARK_ASHES.getTag()).unlockedBy("has_dark_ashes_dust", has(Dust.DARK_ASHES.getTag())).addCondition(IC2_LOADED).save(finishedRecipeConsumer, shapelessId("fertilizer_dark_ashes"));

        // Removed (replaced) recipes
        RemovedRecipeBuilder.build(finishedRecipeConsumer, new ResourceLocation(ModHandler.IC2_MODID, "shaped/plant_ball_7"));
        RemovedRecipeBuilder.build(finishedRecipeConsumer, new ResourceLocation(ModHandler.IC2_MODID, "shaped/solar_generator"));
        RemovedRecipeBuilder.build(finishedRecipeConsumer, new ResourceLocation(ModHandler.IC2_MODID, "shaped/wind_generator"));
        RemovedRecipeBuilder.build(finishedRecipeConsumer, new ResourceLocation(ModHandler.IC2_MODID, "shaped/water_generator"));

        // Smelting
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(Tags.Items.SLIMEBALLS), Ic2Items.RESIN, 0, 200)
            .unlockedBy("has_slimeball", has(Tags.Items.SLIMEBALLS))
            .save(finishedRecipeConsumer, smeltingId("resin"));

        // Fluid Encapsulator
        new TEBottlerRecipeBuilder(Ingredient.of(Dust.COAL.getTag()), FluidTags.WATER, 125, new ItemStack(Ic2Items.COAL_FUEL_DUST), 1250)
            .build(finishedRecipeConsumer, RecipeName.foreign(TEBottlerRecipeBuilder.TYPE, "coal_fuel_dust"));
    }

    protected RecipeName alloySmelterId(String name) {
        return RecipeName.common(this.name, "alloy_smelter", name);
    }

    protected RecipeName assemblerId(String name) {
        return RecipeName.common(this.name, "assembler", name);
    }

    protected RecipeName benderId(String name) {
        return RecipeName.common(this.name, "bender", name);
    }

    protected RecipeName canningMachineId(String name) {
        return RecipeName.common(this.name, "canning_machine", name);
    }

    protected RecipeName fluidSolidId(String name) {
        return RecipeName.common(this.name, "fusion_solid", name);
    }

    protected RecipeName implosionId(String name) {
        return RecipeName.common(this.name, "implosion", name);
    }

    protected RecipeName industrialCentrifugeId(String name) {
        return RecipeName.common(this.name, "industrial_centrifuge", name);
    }

    protected RecipeName industrialGrinderId(String name) {
        return RecipeName.common(this.name, "industrial_grinder", name);
    }

    protected RecipeName pulverizerId(String name) {
        return RecipeName.common(this.name, "pulverizer", name);
    }

    protected RecipeName vacuumFreezerId(String name) {
        return RecipeName.common(this.name, "vacuum_freezer", name);
    }

    protected RecipeName wiremillId(String name) {
        return RecipeName.common(this.name, "wiremill", name);
    }

    protected ResourceLocation shapedId(String name) {
        return new ResourceLocation(this.name, "shaped/" + name);
    }

    protected ResourceLocation shapelessId(String name) {
        return new ResourceLocation(this.name, "shapeless/" + name);
    }

    protected ResourceLocation smeltingId(String name) {
        return new ResourceLocation(this.name, "smelting/" + name);
    }
}
