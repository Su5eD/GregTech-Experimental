package dev.su5ed.gtexperimental.datagen.recipe;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.compat.ModHandler;
import dev.su5ed.gtexperimental.object.Component;
import dev.su5ed.gtexperimental.object.Dust;
import dev.su5ed.gtexperimental.object.Ingot;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipeName;
import dev.su5ed.gtexperimental.recipe.type.SelectedProfileCondition;
import ic2.core.ref.Ic2Items;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.datagen.RecipeGen.IC2_LOADED;
import static dev.su5ed.gtexperimental.recipe.gen.ModRecipeBuilders.alloySmelter;

public final class AlloySmelterRecipesGen implements ModRecipeProvider {
    public static final AlloySmelterRecipesGen INSTANCE = new AlloySmelterRecipesGen();

    private AlloySmelterRecipesGen() {}

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Tags.Items.INGOTS_GOLD), ModRecipeIngredientTypes.ITEM.of(Ingot.SILVER.getTag()), Ingot.ELECTRUM.getItemStack(2), 100, 16)
            .build(finishedRecipeConsumer, id("electrum_ingot"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Tags.Items.INGOTS_COPPER, 3), ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ingots", "tin")), new ItemStack(ModHandler.getModItem("bronze_ingot"), 2), 100, 16)
            .build(finishedRecipeConsumer, id("bronze_ingot"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Tags.Items.INGOTS_COPPER, 3), ModRecipeIngredientTypes.ITEM.of(Ingot.ZINC.getTag()), Ingot.BRASS.getItemStack(4), 200, 16)
            .build(finishedRecipeConsumer, id("brass_ingot"));
        //        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Tags.Items.INGOTS_COPPER), ModRecipeIngredientTypes.ITEM.ofTags(Tags.Items.INGOTS_COPPER, Ingot.NICKEL.getTag()), Ingot.CUPRONICKEL.getItemStack(2), 100, 16)
        //            .build(finishedRecipeConsumer, location("alloy_smelter/cupronickel_ingot"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ingots", "tin"), 9), ModRecipeIngredientTypes.ITEM.of(Ingot.ANTIMONY.getTag()), Ingot.SOLDERING_ALLOY.getItemStack(10), 500, 16)
            .build(finishedRecipeConsumer, id("soldering_alloy_ingot"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Ingot.LEAD.getTag(), 4), ModRecipeIngredientTypes.ITEM.of(Ingot.ANTIMONY.getTag()), Ingot.BATTERY_ALLOY.getItemStack(5), 250, 16)
            .build(finishedRecipeConsumer, id("battery_alloy_ingot"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Tags.Items.INGOTS_IRON, 2), ModRecipeIngredientTypes.ITEM.of(Ingot.NICKEL.getTag()), Ingot.INVAR.getItemStack(3), 150, 16)
            .build(finishedRecipeConsumer, id("invar_ingot"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Ingot.ALUMINIUM.getTag(), 2), ModRecipeIngredientTypes.ITEM.ofTags(Ingot.MAGNALIUM.getTag(), Dust.MAGNESIUM.getTag()), Ingot.MAGNALIUM.getItemStack(3), 150, 16)
            .build(finishedRecipeConsumer, id("magnalium_ingot"));
        //        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Ingot.CHROME.getTag()), ModRecipeIngredientTypes.ITEM.of(Ingot.NICKEL.getTag(), 4), Ingot.NICHROME.getItemStack(5), 250, 16)
        //            .build(finishedRecipeConsumer, location("alloy_smelter/nichrome_ingot"));

        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.CLOCK), new ItemStack(Items.GOLD_INGOT, 4), 130, 3)
            .build(finishedRecipeConsumer, id("gold_ingot_from_clock"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.COMPASS), new ItemStack(Items.IRON_INGOT, 4), 130, 3)
            .build(finishedRecipeConsumer, id("iron_ingot_from_clock"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.SHEARS), new ItemStack(Items.IRON_INGOT, 2), 130, 3)
            .build(finishedRecipeConsumer, id("iron_ingot_from_compass"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.MINECART), new ItemStack(Items.IRON_INGOT, 5), 130, 3)
            .build(finishedRecipeConsumer, id("iron_ingot_from_minecart"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.BUCKET), new ItemStack(Items.IRON_INGOT, 3), 130, 3)
            .build(finishedRecipeConsumer, id("iron_ingot_from_bucket"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.IRON_DOOR), new ItemStack(Items.IRON_INGOT, 6), 130, 3)
            .build(finishedRecipeConsumer, id("iron_ingot_from_iron_door"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.CAULDRON), new ItemStack(Items.IRON_INGOT, 7), 130, 3)
            .build(finishedRecipeConsumer, id("iron_ingot_from_cauldron"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.ANVIL), new ItemStack(Items.IRON_INGOT, 31), 130, 3)
            .build(finishedRecipeConsumer, id("iron_ingot_from_anvil"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.CHIPPED_ANVIL), new ItemStack(Items.IRON_INGOT, 20), 130, 3)
            .build(finishedRecipeConsumer, id("iron_ingot_from_chipped_anvil"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.DAMAGED_ANVIL), new ItemStack(Items.IRON_INGOT, 10), 130, 3)
            .build(finishedRecipeConsumer, id("iron_ingot_from_damaged_anvil"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.LIGHT_WEIGHTED_PRESSURE_PLATE), new ItemStack(Items.GOLD_INGOT, 2), 130, 3)
            .build(finishedRecipeConsumer, id("gold_ingot_from_light_weighted_pressure_plate"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.HEAVY_WEIGHTED_PRESSURE_PLATE), new ItemStack(Items.IRON_INGOT, 2), 130, 3)
            .build(finishedRecipeConsumer, id("iron_ingot_from_heavy_weighted_pressure_plate"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.HOPPER), new ItemStack(Items.IRON_INGOT, 5), 130, 3)
            .build(finishedRecipeConsumer, id("iron_ingot_from_hopper"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Component.ALUMINIUM_HULL.getItem()), Ingot.ALUMINIUM.getItemStack(6), 130, 3)
            .build(finishedRecipeConsumer, id("aluminium_ingot_from_aluminium_hull"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Component.BRONZE_HULL.getItem()), new ItemStack(ModHandler.getModItem("bronze_ingot"), 6), 130, 3)
            .build(finishedRecipeConsumer, id("bronze_ingot_from_bronze_hull"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Component.BRASS_HULL.getItem()), Ingot.BRASS.getItemStack(6), 130, 3)
            .build(finishedRecipeConsumer, id("brass_ingot_from_brass_hull"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Component.STEEL_HULL.getItem()), Ingot.STEEL.getItemStack(6), 130, 3)
            .build(finishedRecipeConsumer, id("steel_ingot_from_steel_hull"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Component.TITANIUM_HULL.getItem()), Ingot.TITANIUM.getItemStack(6), 130, 3)
            .build(finishedRecipeConsumer, id("titanium_ingot_from_titanium_hull"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Component.BRONZE_GEAR.getItem()), new ItemStack(ModHandler.getModItem("bronze_ingot"), 6), 130, 3)
            .build(finishedRecipeConsumer, id("bronze_ingot_from_bronze_gear"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Component.STEEL_GEAR.getItem()), Ingot.STEEL.getItemStack(6), 130, 3)
            .build(finishedRecipeConsumer, id("steel_ingot_from_steel_gear"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Component.TITANIUM_GEAR.getItem()), Ingot.TITANIUM.getItemStack(6), 130, 3)
            .build(finishedRecipeConsumer, id("titanium_ingot_from_titanium_gear"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Component.TUNGSTEN_STEEL_GEAR.getItem()), Ingot.TUNGSTEN_STEEL.getItemStack(6), 130, 3)
            .build(finishedRecipeConsumer, id("tungsten_steel_ingot_from_tungsten_steel_gear"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Component.IRIDIUM_GEAR.getItem()), Ingot.IRIDIUM.getItemStack(6), 130, 3)
            .build(finishedRecipeConsumer, id("iridium_ingot_from_iridium_gear"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.MILK_BUCKET, Items.WATER_BUCKET), new ItemStack(Items.BUCKET), 100, 1)
            .build(finishedRecipeConsumer, id("bucket_from_filled_bucket"));

        // Experimental
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Component.IRON_GEAR.getItem()), new ItemStack(Items.IRON_INGOT, 6), 130, 3)
            .addConditions(SelectedProfileCondition.EXPERIMENTAL)
            .build(finishedRecipeConsumer, profileId("experimental", "iron_ingot_from_iron_gear"), true);

        buildOtherModRecipes(finishedRecipeConsumer);
    }

    private void buildOtherModRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Ic2Items.IRON_FURNACE), new ItemStack(Items.IRON_INGOT, 5), 130, 3)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/iron_ingot_from_iron_furnace"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Ic2Items.EMPTY_CELL), new ItemStack(Ic2Items.TIN_INGOT), 130, 3)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/tin_ingot_from_empty_cell"), true);

        // Classic
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Ic2Items.EMPTY_FUEL_CAN), new ItemStack(Ic2Items.TIN_INGOT, 7), 130, 3)
            .addConditions(IC2_LOADED, SelectedProfileCondition.CLASSIC)
            .build(finishedRecipeConsumer, profileId("classic", "ic2/tin_ingot_from_empty_fuel_can"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Component.IRON_GEAR.getItem()), new ItemStack(Ic2Items.REFINED_IRON_INGOT, 6), 130, 3)
            .addConditions(IC2_LOADED, SelectedProfileCondition.CLASSIC)
            .build(finishedRecipeConsumer, profileId("classic", "ic2/refined_iron_ingot_from_iron_gear"), true);
    }

    private static RecipeName id(String name) {
        return profileId(null, name);
    }

    private static RecipeName profileId(String profile, String name) {
        return RecipeName.profile(Reference.MODID, "alloy_smelter", profile, name);
    }
}
