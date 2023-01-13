package dev.su5ed.gregtechmod.datagen;

import dev.su5ed.gregtechmod.GregTechTags;
import dev.su5ed.gregtechmod.api.Reference;
import dev.su5ed.gregtechmod.compat.ModHandler;
import dev.su5ed.gregtechmod.object.Component;
import dev.su5ed.gregtechmod.object.Dust;
import dev.su5ed.gregtechmod.object.Ingot;
import dev.su5ed.gregtechmod.recipe.setup.ModRecipeIngredientTypes;
import ic2.core.ref.Ic2Items;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;

import java.util.function.Consumer;

import static dev.su5ed.gregtechmod.api.Reference.location;
import static dev.su5ed.gregtechmod.recipe.gen.ModRecipeBuilders.alloySmelter;

public class RecipeGen extends RecipeProvider {

    public RecipeGen(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Tags.Items.INGOTS_GOLD), ModRecipeIngredientTypes.ITEM.of(Ingot.SILVER.getTag()), Ingot.ELECTRUM.getItemStack(2), 100, 16)
            .build(finishedRecipeConsumer, location("alloy_smelter/electrum_ingot"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Tags.Items.INGOTS_COPPER, 3), ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ingots", "tin")), new ItemStack(ModHandler.getModItem("bronze_ingot"), 2), 100, 16)
            .build(finishedRecipeConsumer, location("alloy_smelter/bronze_ingot"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Tags.Items.INGOTS_COPPER, 3), ModRecipeIngredientTypes.ITEM.of(Ingot.ZINC.getTag()), Ingot.BRASS.getItemStack(4), 200, 16)
            .build(finishedRecipeConsumer, location("alloy_smelter/brass_ingot"));
//        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Tags.Items.INGOTS_COPPER), ModRecipeIngredientTypes.ITEM.ofTags(Tags.Items.INGOTS_COPPER, Ingot.NICKEL.getTag()), Ingot.CUPRONICKEL.getItemStack(2), 100, 16)
//            .build(finishedRecipeConsumer, location("alloy_smelter/cupronickel_ingot"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ingots", "tin"), 9), ModRecipeIngredientTypes.ITEM.of(Ingot.ANTIMONY.getTag()), Ingot.SOLDERING_ALLOY.getItemStack(10), 500, 16)
            .build(finishedRecipeConsumer, location("alloy_smelter/soldering_alloy_ingot"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Ingot.LEAD.getTag(), 4), ModRecipeIngredientTypes.ITEM.of(Ingot.ANTIMONY.getTag()), Ingot.BATTERY_ALLOY.getItemStack(5), 250, 16)
            .build(finishedRecipeConsumer, location("alloy_smelter/battery_alloy_ingot"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Tags.Items.INGOTS_IRON, 2), ModRecipeIngredientTypes.ITEM.of(Ingot.NICKEL.getTag()), Ingot.INVAR.getItemStack(3), 150, 16)
            .build(finishedRecipeConsumer, location("alloy_smelter/invar_ingot"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Ingot.ALUMINIUM.getTag(), 2), ModRecipeIngredientTypes.ITEM.ofTags(Ingot.MAGNALIUM.getTag(), Dust.MAGNESIUM.getTag()), Ingot.MAGNALIUM.getItemStack(3), 150, 16)
            .build(finishedRecipeConsumer, location("alloy_smelter/magnalium_ingot"));
//        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Ingot.CHROME.getTag()), ModRecipeIngredientTypes.ITEM.of(Ingot.NICKEL.getTag(), 4), Ingot.NICHROME.getItemStack(5), 250, 16)
//            .build(finishedRecipeConsumer, location("alloy_smelter/nichrome_ingot"));

        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.CLOCK), new ItemStack(Items.GOLD_INGOT, 4), 130, 3)
            .build(finishedRecipeConsumer, location("alloy_smelter/gold_ingot_from_clock"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.COMPASS), new ItemStack(Items.IRON_INGOT, 4), 130, 3)
            .build(finishedRecipeConsumer, location("alloy_smelter/iron_ingot_from_clock"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.SHEARS), new ItemStack(Items.IRON_INGOT, 2), 130, 3)
            .build(finishedRecipeConsumer, location("alloy_smelter/iron_ingot_from_compass"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.MINECART), new ItemStack(Items.IRON_INGOT, 5), 130, 3)
            .build(finishedRecipeConsumer, location("alloy_smelter/iron_ingot_from_minecart"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.BUCKET), new ItemStack(Items.IRON_INGOT, 3), 130, 3)
            .build(finishedRecipeConsumer, location("alloy_smelter/iron_ingot_from_bucket"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.IRON_DOOR), new ItemStack(Items.IRON_INGOT, 6), 130, 3)
            .build(finishedRecipeConsumer, location("alloy_smelter/iron_ingot_from_iron_door"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.CAULDRON), new ItemStack(Items.IRON_INGOT, 7), 130, 3)
            .build(finishedRecipeConsumer, location("alloy_smelter/iron_ingot_from_cauldron"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.ANVIL), new ItemStack(Items.IRON_INGOT, 31), 130, 3)
            .build(finishedRecipeConsumer, location("alloy_smelter/iron_ingot_from_anvil"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.CHIPPED_ANVIL), new ItemStack(Items.IRON_INGOT, 20), 130, 3)
            .build(finishedRecipeConsumer, location("alloy_smelter/iron_ingot_from_chipped_anvil"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.DAMAGED_ANVIL), new ItemStack(Items.IRON_INGOT, 10), 130, 3)
            .build(finishedRecipeConsumer, location("alloy_smelter/iron_ingot_from_damaged_anvil"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.LIGHT_WEIGHTED_PRESSURE_PLATE), new ItemStack(Items.GOLD_INGOT, 2), 130, 3)
            .build(finishedRecipeConsumer, location("alloy_smelter/gold_ingot_from_light_weighted_pressure_plate"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.HEAVY_WEIGHTED_PRESSURE_PLATE), new ItemStack(Items.IRON_INGOT, 2), 130, 3)
            .build(finishedRecipeConsumer, location("alloy_smelter/iron_ingot_from_heavy_weighted_pressure_plate"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.HOPPER), new ItemStack(Items.IRON_INGOT, 5), 130, 3)
            .build(finishedRecipeConsumer, location("alloy_smelter/iron_ingot_from_hopper"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Component.ALUMINIUM_HULL.getItem()), Ingot.ALUMINIUM.getItemStack(6), 130, 3)
            .build(finishedRecipeConsumer, location("alloy_smelter/aluminium_ingot_from_aluminium_hull"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Component.BRONZE_HULL.getItem()), new ItemStack(ModHandler.getModItem("bronze_ingot"), 6), 130, 3)
            .build(finishedRecipeConsumer, location("alloy_smelter/bronze_ingot_from_bronze_hull"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Component.BRASS_HULL.getItem()), Ingot.BRASS.getItemStack(6), 130, 3)
            .build(finishedRecipeConsumer, location("alloy_smelter/brass_ingot_from_brass_hull"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Component.STEEL_HULL.getItem()), Ingot.STEEL.getItemStack(6), 130, 3)
            .build(finishedRecipeConsumer, location("alloy_smelter/steel_ingot_from_steel_hull"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Component.TITANIUM_HULL.getItem()), Ingot.TITANIUM.getItemStack(6), 130, 3)
            .build(finishedRecipeConsumer, location("alloy_smelter/titanium_ingot_from_titanium_hull"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Component.BRONZE_GEAR.getItem()), new ItemStack(ModHandler.getModItem("bronze_ingot"), 6), 130, 3)
            .build(finishedRecipeConsumer, location("alloy_smelter/bronze_ingot_from_bronze_gear"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Component.STEEL_GEAR.getItem()), Ingot.STEEL.getItemStack(6), 130, 3)
            .build(finishedRecipeConsumer, location("alloy_smelter/steel_ingot_from_steel_gear"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Component.TITANIUM_GEAR.getItem()), Ingot.TITANIUM.getItemStack(6), 130, 3)
            .build(finishedRecipeConsumer, location("alloy_smelter/titanium_ingot_from_titanium_gear"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Component.TUNGSTEN_STEEL_GEAR.getItem()), Ingot.TUNGSTEN_STEEL.getItemStack(6), 130, 3)
            .build(finishedRecipeConsumer, location("alloy_smelter/tungsten_steel_ingot_from_tungsten_steel_gear"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Component.IRIDIUM_GEAR.getItem()), Ingot.IRIDIUM.getItemStack(6), 130, 3)
            .build(finishedRecipeConsumer, location("alloy_smelter/iridium_ingot_from_iridium_gear"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.MILK_BUCKET, Items.WATER_BUCKET), new ItemStack(Items.BUCKET), 100, 1)
            .build(finishedRecipeConsumer, location("alloy_smelter/bucket_from_filled_bucket"));

        buildOtherModRecipes(finishedRecipeConsumer);
    }

    private void buildOtherModRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        ICondition ic2Loaded = new ModLoadedCondition(ModHandler.IC2_MODID);

        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Ic2Items.IRON_FURNACE), new ItemStack(Items.IRON_INGOT, 5), 130, 3)
            .addCondition(ic2Loaded)
            .build(finishedRecipeConsumer, location("alloy_smelter/iron_ingot_from_iron_furnace"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Ic2Items.EMPTY_CELL), new ItemStack(Ic2Items.TIN_INGOT), 130, 3)
            .addCondition(ic2Loaded)
            .build(finishedRecipeConsumer, location("alloy_smelter/tin_ingot_from_empty_cell"));
    }

    @Override
    public String getName() {
        return Reference.NAME + super.getName();
    }
}
