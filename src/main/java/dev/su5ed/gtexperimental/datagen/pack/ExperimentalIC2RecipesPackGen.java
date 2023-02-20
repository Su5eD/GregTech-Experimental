package dev.su5ed.gtexperimental.datagen.pack;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.compat.ModHandler;
import dev.su5ed.gtexperimental.datagen.recipe.PulverizerRecipesGen;
import dev.su5ed.gtexperimental.object.Dust;
import dev.su5ed.gtexperimental.object.Miscellaneous;
import dev.su5ed.gtexperimental.object.Smalldust;
import dev.su5ed.gtexperimental.recipe.gen.SmeltingRecipeBuilder;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipeName;
import ic2.core.ref.Ic2Items;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.datagen.RecipeGen.IC2_LOADED;
import static dev.su5ed.gtexperimental.recipe.crafting.ConditionalShapedRecipeBuilder.conditionalShaped;
import static dev.su5ed.gtexperimental.recipe.gen.ModRecipeBuilders.*;
import static dev.su5ed.gtexperimental.recipe.gen.compat.CompatRecipeBuilders.ic2Compressor;
import static dev.su5ed.gtexperimental.recipe.type.RecipeUtil.*;

public class ExperimentalIC2RecipesPackGen extends IC2RecipesPackGen {
    public static final String NAME = "experimental_ic2_compat";
    private static final String NAMESPACE = Reference.MODID + "_" + NAME;

    public ExperimentalIC2RecipesPackGen(DataGenerator generator) {
        super(generator, NAMESPACE);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        super.buildCraftingRecipes(finishedRecipeConsumer);

        // Canning Machine
        canningMachine(ModRecipeIngredientTypes.ITEM.of(Ic2Items.URANIUM), ModRecipeIngredientTypes.ITEM.of(Ic2Items.EMPTY_CELL), new ItemStack(Ic2Items.URANIUM_FUEL_ROD), 100, 2)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, canningMachineId("uranium_fuel_rod"));
//        canningMachine(ModRecipeIngredientTypes.ITEM.of(Ic2Items.GRIN_POWDER), ModRecipeIngredientTypes.ITEM.of(Ic2Items.EMPTY_CELL), WEED_EX_FLUID_CELL, 800, 1)
//            .addConditions(IC2_LOADED, SelectedProfileCondition.EXPERIMENTAL)
//            .build(finishedRecipeConsumer, id("experimental/ic2/weed_ex_cell"));
        // TODO Dynamic Fluid filling recipes
//        canningMachine(ModRecipeIngredientTypes.ITEM.of(Miscellaneous.OIL_BERRY, 4), ModRecipeIngredientTypes.ITEM.of(Ic2Items.EMPTY_CELL), OIL_CELL, 100, 1)
//            .build(finishedRecipeConsumer, id("ic2/oil_fluid_cell"));

        // IC2 Compressor
        ic2Compressor(Dust.URANIUM, 1, new ItemStack(Ic2Items.URANIUM_238), finishedRecipeConsumer, NAMESPACE);

        // Industrial Centrifuge
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Items.DIRT, 16), new ItemStack(Ic2Items.BIO_CHAFF), new ItemStack(Ic2Items.PLANT_BALL), new ItemStack(Items.CLAY_BALL), new ItemStack(Items.SAND, 8), 2500)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, industrialCentrifugeId("dirt"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Items.GRASS, 16), new ItemStack(Ic2Items.BIO_CHAFF, 2), new ItemStack(Ic2Items.PLANT_BALL, 2), new ItemStack(Items.CLAY_BALL), new ItemStack(Items.SAND, 8), 2500)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, industrialCentrifugeId("grass"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Ic2Items.RESIN, 4), new ItemStack(Ic2Items.RUBBER, 14), new ItemStack(Ic2Items.BIO_CHAFF), new ItemStack(Ic2Items.PLANT_BALL), 1300)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, industrialCentrifugeId("resin"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.URANIUM.getTag(), 4), new ItemStack(Ic2Items.SMALL_URANIUM_235), Smalldust.PLUTONIUM.getItemStack(), Dust.THORIUM.getItemStack(2), Dust.TUNGSTEN.getItemStack(), 10000)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, industrialCentrifugeId("uranium_dust"));

        // Implosion
        implosion(ModRecipeIngredientTypes.ITEM.of(Dust.DIAMOND.getTag(), 4), new ItemStack(Items.DIAMOND, 3), Dust.DARK_ASHES.getItemStack(16), 32)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, implosionId("industrial_diamond"));

        // Industrial Grinder
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ore("uranium")), WATER, new ItemStack(Ic2Items.PURIFIED_URANIUM, 2), Smalldust.PLUTONIUM.getItemStack(2), Dust.THORIUM.getItemStack())
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, industrialGrinderId("uranium_ore"));

        // Pulverizer
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Ic2Items.EMPTY_CELL), Dust.TIN.getItemStack(2))
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, pulverizerId("empty_cell"));
        pulverizer(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ore("uranium")), new ItemStack(Ic2Items.CRUSHED_URANIUM, 2), Dust.PLUTONIUM.getItemStack())
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, PulverizerRecipesGen.id("ores_uranium"));

        // Crafting
        conditionalShaped(Ic2Items.ENERGIUM_DUST, 9)
            .define('D', Dust.RUBY.getTag())
            .define('R', Tags.Items.DUSTS_REDSTONE)
            .pattern("RDR")
            .pattern("DRD")
            .pattern("RDR")
            .unlockedBy("has_gems_ruby", has(Miscellaneous.RUBY.getTag()))
            .addCondition(IC2_LOADED)
            .save(finishedRecipeConsumer, shapedId("component/energium_dust"));
        conditionalShaped(Ic2Items.GLASS_FIBRE_CABLE, 6)
            .define('G', Items.GLASS)
            .define('R', tagsIngredient(Dust.SILVER.getTag(), Dust.ELECTRUM.getTag()))
            .define('D', tagsIngredient(Tags.Items.GEMS_DIAMOND, Dust.DIAMOND.getTag()))
            .pattern("GGG")
            .pattern("DRD")
            .pattern("GGG")
            .unlockedBy("has_gems_diamond", hasTags(Tags.Items.GEMS_DIAMOND, Dust.DIAMOND.getTag()))
            .addCondition(IC2_LOADED)
            .save(finishedRecipeConsumer, new ResourceLocation(ModHandler.IC2_MODID, "shaped/glass_fibre_cable"));

        // Smelting
        new SmeltingRecipeBuilder(Ingredient.of(Ic2Items.MACHINE), new ItemStack(Items.IRON_INGOT, 8), 0, 200)
            .unlockedBy("has_machine", has(Ic2Items.MACHINE))
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, RecipeName.common(NAME, "smelting", "machine"));
    }
}
