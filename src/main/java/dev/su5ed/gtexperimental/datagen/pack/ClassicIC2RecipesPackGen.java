package dev.su5ed.gtexperimental.datagen.pack;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.compat.IC2BaseMod;
import dev.su5ed.gtexperimental.datagen.recipe.BlastFurnaceRecipesGen;
import dev.su5ed.gtexperimental.object.Component;
import dev.su5ed.gtexperimental.object.Dust;
import dev.su5ed.gtexperimental.object.Ingot;
import dev.su5ed.gtexperimental.object.Miscellaneous;
import dev.su5ed.gtexperimental.object.ModFluid;
import dev.su5ed.gtexperimental.object.Ore;
import dev.su5ed.gtexperimental.object.Smalldust;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import ic2.core.ref.Ic2Items;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.datagen.RecipeGen.IC2_LOADED;
import static dev.su5ed.gtexperimental.recipe.crafting.ConditionalShapedRecipeBuilder.conditionalShaped;
import static dev.su5ed.gtexperimental.recipe.gen.ModRecipeBuilders.*;
import static dev.su5ed.gtexperimental.recipe.gen.compat.CompatRecipeBuilders.ic2Compressor;
import static dev.su5ed.gtexperimental.recipe.type.RecipeUtil.WATER;
import static dev.su5ed.gtexperimental.util.GtUtil.buckets;

public class ClassicIC2RecipesPackGen extends IC2RecipesPackGen {
    public static final String NAME = "classic_ic2_compat";
    private static final String NAMESPACE = Reference.MODID + "_" + NAME;

    public ClassicIC2RecipesPackGen(DataGenerator generator) {
        super(generator, NAMESPACE);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        super.buildCraftingRecipes(finishedRecipeConsumer);

        // Alloy Smelter
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Ic2Items.EMPTY_FUEL_CAN), Ingot.TIN.getItemStack(7), 130, 3)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, alloySmelterId("tin_ingot_from_empty_fuel_can"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Component.IRON_GEAR.getItem()), new ItemStack(Ic2Items.REFINED_IRON_INGOT, 6), 130, 3)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, alloySmelterId("refined_iron_ingot_from_iron_gear"), true);

        // Assembler
        assembler(ModRecipeIngredientTypes.ITEM.of(Ic2Items.ALLOY), ModRecipeIngredientTypes.ITEM.of(Tags.Items.STONE, 8), new ItemStack(Ic2Items.REINFORCED_STONE, 8), 400, 4)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, assemblerId("reinforced_stone"));
//        assembler(ModRecipeIngredientTypes.ITEM.ofTags(Plate.REFINED_IRON.getTag(), Plate.ALUMINIUM.getTag()), ModRecipeIngredientTypes.ITEM.of(Ic2Items.METER), ModCoverItem.ENERGY_METER.getItemStack(), 800, 16)
//            .addConditions(IC2_LOADED)
//            .build(finishedRecipeConsumer, assemblerId("energy_meter"));

        // Blast Furnace
        // TODO Use calcite dust?
        blastFurnace(ModRecipeIngredientTypes.ITEM.of(Tags.Items.ORES_IRON), ModRecipeIngredientTypes.ITEM.ofFluid(ModFluid.CALCIUM_CARBONATE, buckets(1)), new ItemStack(Ic2Items.REFINED_IRON_INGOT, 3), 100, 1000)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, BlastFurnaceRecipesGen.id("iron_ingot_from_iron_ore"));
        blastFurnace(ModRecipeIngredientTypes.ITEM.of(Ore.PYRITE.getTag()), ModRecipeIngredientTypes.ITEM.ofFluid(ModFluid.CALCIUM_CARBONATE, buckets(1)), new ItemStack(Ic2Items.REFINED_IRON_INGOT, 2), 100, 1500)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, BlastFurnaceRecipesGen.id("iron_ingot_from_pyrite_ore"));

        // Canning Machine
        canningMachine(ModRecipeIngredientTypes.ITEM.of(Ic2Items.GRIN_POWDER), ModRecipeIngredientTypes.ITEM.of(GregTechTags.CRAFTING_SPRAY_CAN), new ItemStack(Ic2Items.WEED_EX_CELL), 800, 1)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, canningMachineId("weed_ex_cell"));
        canningMachine(ModRecipeIngredientTypes.ITEM.of(Ic2Items.COMPRESSED_PLANTS), ModRecipeIngredientTypes.ITEM.of(GregTechTags.EMPTY_FLUID_CELL), new ItemStack(Ic2Items.BIO_CELL), 100, 1)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, canningMachineId("bio_cell"));
        canningMachine(ModRecipeIngredientTypes.ITEM.of(Ic2Items.COMPRESSED_HYDRATED_COAL), ModRecipeIngredientTypes.ITEM.of(GregTechTags.EMPTY_FLUID_CELL), new ItemStack(Ic2Items.HYDRATED_COAL_CELL), 100, 1)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, canningMachineId("hydrated_coal_cell"));
        canningMachine(ModRecipeIngredientTypes.ITEM.of(Ic2Items.BIOFUEL_CELL, 6), ModRecipeIngredientTypes.ITEM.of(GregTechTags.EMPTY_FUEL_CAN), IC2BaseMod.getFilledFuelCan(5208), new ItemStack(Ic2Items.EMPTY_CELL), 600, 1)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, canningMachineId("filled_fuel_can_bioduel"));
        canningMachine(ModRecipeIngredientTypes.ITEM.of(Ic2Items.COALFUEL_CELL, 6), ModRecipeIngredientTypes.ITEM.of(GregTechTags.EMPTY_FUEL_CAN), IC2BaseMod.getFilledFuelCan(15288), new ItemStack(Ic2Items.EMPTY_CELL), 600, 1)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, canningMachineId("filled_fuel_can_coalfuel"));
        canningMachine(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ingot("uranium")), ModRecipeIngredientTypes.ITEM.of(Ic2Items.EMPTY_CELL), new ItemStack(Ic2Items.URANIUM_FUEL_ROD), 100, 2)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, canningMachineId("uranium_fuel_rod"));

        // Chemical Reactor
        // TODO ItemFluid recipes
//        chemical(ModRecipeIngredientTypes.FLUID.of(ModFluid.GLYCERYL.getTag()), Ic2Items.COALFUEL_CELL, )

        // IC2 Compressor
        ic2Compressor(Ic2Items.IRIDIUM_ORE, 1, Ingot.IRIDIUM.getItemStack(), finishedRecipeConsumer, NAMESPACE);
        ic2Compressor(GregTechTags.material("nuggets", "uranium"), 1, new ItemStack(Ic2Items.URANIUM_INGOT), finishedRecipeConsumer, NAMESPACE);
        ic2Compressor(ItemTags.SAPLINGS, 4, new ItemStack(Ic2Items.COMPRESSED_PLANTS), finishedRecipeConsumer, NAMESPACE);
        ic2Compressor(ItemTags.LEAVES, 8, new ItemStack(Ic2Items.COMPRESSED_PLANTS), finishedRecipeConsumer, NAMESPACE);
        ic2Compressor(Tags.Items.CROPS_WHEAT, 8, new ItemStack(Ic2Items.COMPRESSED_PLANTS), finishedRecipeConsumer, NAMESPACE);
        ic2Compressor(Items.SUGAR_CANE, 8, new ItemStack(Ic2Items.COMPRESSED_PLANTS), finishedRecipeConsumer, NAMESPACE);
        ic2Compressor(Items.CACTUS, 8, new ItemStack(Ic2Items.COMPRESSED_PLANTS), finishedRecipeConsumer, NAMESPACE);
        ic2Compressor(Miscellaneous.INDIGO_BLOSSOM, 8, new ItemStack(Ic2Items.COMPRESSED_PLANTS), finishedRecipeConsumer, NAMESPACE);
        ic2Compressor(Dust.URANIUM, 8, new ItemStack(Ic2Items.URANIUM_INGOT), finishedRecipeConsumer, NAMESPACE);

        // Industrial Centrifuge
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Ic2Items.NEAR_DEPLETED_URANIUM), Dust.THORIUM.getItemStack(), new ItemStack(Ic2Items.EMPTY_CELL), 500)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, industrialCentrifugeId("near_depleted_uranium"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Ic2Items.RE_ENRICHED_URANIUM, 8), new ItemStack(Ic2Items.NEAR_DEPLETED_URANIUM, 3), Dust.PLUTONIUM.getItemStack(), Dust.THORIUM.getItemStack(4), new ItemStack(Ic2Items.EMPTY_CELL, 5), 20000)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, industrialCentrifugeId("re_enriched_uranium"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Items.DIRT, 16), new ItemStack(Ic2Items.COMPRESSED_PLANTS), new ItemStack(Ic2Items.PLANT_BALL), new ItemStack(Items.CLAY_BALL), new ItemStack(Items.SAND, 8), 2500)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, industrialCentrifugeId("dirt"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Items.GRASS, 16), new ItemStack(Ic2Items.COMPRESSED_PLANTS, 2), new ItemStack(Ic2Items.PLANT_BALL, 2), new ItemStack(Items.CLAY_BALL), new ItemStack(Items.SAND, 8), 2500)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, industrialCentrifugeId("grass"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Ic2Items.RESIN, 4), new ItemStack(Ic2Items.RUBBER, 14), new ItemStack(Ic2Items.COMPRESSED_PLANTS), new ItemStack(Ic2Items.PLANT_BALL), 1300)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, industrialCentrifugeId("resin"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.URANIUM.getTag(), 4), new ItemStack(Ic2Items.URANIUM_FUEL_ROD, 4), Smalldust.PLUTONIUM.getItemStack(), Dust.THORIUM.getItemStack(2), Dust.TUNGSTEN.getItemStack(), 10000)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, industrialCentrifugeId("uranium_dust"));

        // Industrial Grinder
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ore("uranium")), WATER, Dust.URANIUM.getItemStack(2), Smalldust.PLUTONIUM.getItemStack(2), Dust.THORIUM.getItemStack())
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, industrialGrinderId("uranium_ore"));

        // Pulverizer
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Ic2Items.EMPTY_FUEL_CAN), Dust.TIN.getItemStack(7))
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, pulverizerId("empty_fuel_can"));
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Ic2Items.EMPTY_CELL), Dust.TIN.getItemStack())
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, pulverizerId("empty_cell"));
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Ic2Items.PLANT_BALL), new ItemStack(Items.DIRT))
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, pulverizerId("plant_ball"));

        // Crafting
        conditionalShaped(Ic2Items.ENERGY_CRYSTAL)
            .define('D', Tags.Items.DUSTS_REDSTONE)
            .define('R', Miscellaneous.RUBY.getTag())
            .pattern("DDD")
            .pattern("DRD")
            .pattern("DDD")
            .unlockedBy("has_gems_ruby", has(Miscellaneous.RUBY.getTag()))
            .addCondition(IC2_LOADED)
            .save(finishedRecipeConsumer, shapedId("component/energy_crystal"));
    }
}
