package dev.su5ed.gtexperimental.datagen.recipe;

import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.object.Component;
import dev.su5ed.gtexperimental.object.Dust;
import dev.su5ed.gtexperimental.object.Ingot;
import dev.su5ed.gtexperimental.object.Nugget;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipeName;
import dev.su5ed.gtexperimental.util.GtUtil;
import dev.su5ed.gtexperimental.util.JavaUtil;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import one.util.streamex.StreamEx;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.recipe.gen.ModRecipeBuilders.alloySmelter;

public final class AlloySmelterRecipesGen implements ModRecipeProvider {
    public static final AlloySmelterRecipesGen INSTANCE = new AlloySmelterRecipesGen();

    private AlloySmelterRecipesGen() {}

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Tags.Items.INGOTS_GOLD), ModRecipeIngredientTypes.ITEM.of(Ingot.SILVER.getTag()), Ingot.ELECTRUM.getItemStack(2), 100, 16).build(finishedRecipeConsumer, id("electrum_ingot"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Tags.Items.INGOTS_COPPER, 3), ModRecipeIngredientTypes.ITEM.of(Ingot.TIN.getTag()), new ItemStack(Ingot.BRONZE, 2), 100, 16).build(finishedRecipeConsumer, id("bronze_ingot"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Tags.Items.INGOTS_COPPER, 3), ModRecipeIngredientTypes.ITEM.of(Ingot.ZINC.getTag()), Ingot.BRASS.getItemStack(4), 200, 16).build(finishedRecipeConsumer, id("brass_ingot"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Ingot.TIN.getTag(), 9), ModRecipeIngredientTypes.ITEM.of(Ingot.ANTIMONY.getTag()), Ingot.SOLDERING_ALLOY.getItemStack(10), 500, 16).build(finishedRecipeConsumer, id("soldering_alloy_ingot"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Ingot.LEAD.getTag(), 4), ModRecipeIngredientTypes.ITEM.of(Ingot.ANTIMONY.getTag()), Ingot.BATTERY_ALLOY.getItemStack(5), 250, 16).build(finishedRecipeConsumer, id("battery_alloy_ingot"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Tags.Items.INGOTS_IRON, 2), ModRecipeIngredientTypes.ITEM.of(Ingot.NICKEL.getTag()), Ingot.INVAR.getItemStack(3), 150, 16).build(finishedRecipeConsumer, id("invar_ingot"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Ingot.ALUMINIUM.getTag(), 2), ModRecipeIngredientTypes.ITEM.ofTags(Ingot.MAGNALIUM.getTag(), Dust.MAGNESIUM.getTag()), Ingot.MAGNALIUM.getItemStack(3), 150, 16).build(finishedRecipeConsumer, id("magnalium_ingot"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.CLOCK), new ItemStack(Items.GOLD_INGOT, 4), 130, 3).build(finishedRecipeConsumer, id("gold_ingot_from_clock"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.COMPASS), new ItemStack(Items.IRON_INGOT, 4), 130, 3).build(finishedRecipeConsumer, id("iron_ingot_from_clock"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.SHEARS), new ItemStack(Items.IRON_INGOT, 2), 130, 3).build(finishedRecipeConsumer, id("iron_ingot_from_compass"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.MINECART), new ItemStack(Items.IRON_INGOT, 5), 130, 3).build(finishedRecipeConsumer, id("iron_ingot_from_minecart"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.BUCKET), new ItemStack(Items.IRON_INGOT, 3), 130, 3).build(finishedRecipeConsumer, id("iron_ingot_from_bucket"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.IRON_DOOR), new ItemStack(Items.IRON_INGOT, 6), 130, 3).build(finishedRecipeConsumer, id("iron_ingot_from_iron_door"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.CAULDRON), new ItemStack(Items.IRON_INGOT, 7), 130, 3).build(finishedRecipeConsumer, id("iron_ingot_from_cauldron"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.ANVIL), new ItemStack(Items.IRON_INGOT, 31), 130, 3).build(finishedRecipeConsumer, id("iron_ingot_from_anvil"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.CHIPPED_ANVIL), new ItemStack(Items.IRON_INGOT, 20), 130, 3).build(finishedRecipeConsumer, id("iron_ingot_from_chipped_anvil"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.DAMAGED_ANVIL), new ItemStack(Items.IRON_INGOT, 10), 130, 3).build(finishedRecipeConsumer, id("iron_ingot_from_damaged_anvil"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.LIGHT_WEIGHTED_PRESSURE_PLATE), new ItemStack(Items.GOLD_INGOT, 2), 130, 3).build(finishedRecipeConsumer, id("gold_ingot_from_light_weighted_pressure_plate"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.HEAVY_WEIGHTED_PRESSURE_PLATE), new ItemStack(Items.IRON_INGOT, 2), 130, 3).build(finishedRecipeConsumer, id("iron_ingot_from_heavy_weighted_pressure_plate"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.HOPPER), new ItemStack(Items.IRON_INGOT, 5), 130, 3).build(finishedRecipeConsumer, id("iron_ingot_from_hopper"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Component.ALUMINIUM_HULL.getItem()), Ingot.ALUMINIUM.getItemStack(6), 130, 3).build(finishedRecipeConsumer, id("aluminium_ingot_from_aluminium_hull"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Component.BRONZE_HULL.getItem()), Ingot.BRONZE.getItemStack(6), 130, 3).build(finishedRecipeConsumer, id("bronze_ingot_from_bronze_hull"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Component.BRASS_HULL.getItem()), Ingot.BRASS.getItemStack(6), 130, 3).build(finishedRecipeConsumer, id("brass_ingot_from_brass_hull"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Component.STEEL_HULL.getItem()), Ingot.STEEL.getItemStack(6), 130, 3).build(finishedRecipeConsumer, id("steel_ingot_from_steel_hull"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Component.TITANIUM_HULL.getItem()), Ingot.TITANIUM.getItemStack(6), 130, 3).build(finishedRecipeConsumer, id("titanium_ingot_from_titanium_hull"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Component.BRONZE_GEAR.getItem()), Ingot.BRONZE.getItemStack(6), 130, 3).build(finishedRecipeConsumer, id("bronze_ingot_from_bronze_gear"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Component.STEEL_GEAR.getItem()), Ingot.STEEL.getItemStack(6), 130, 3).build(finishedRecipeConsumer, id("steel_ingot_from_steel_gear"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Component.TITANIUM_GEAR.getItem()), Ingot.TITANIUM.getItemStack(6), 130, 3).build(finishedRecipeConsumer, id("titanium_ingot_from_titanium_gear"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Component.TUNGSTEN_STEEL_GEAR.getItem()), Ingot.TUNGSTEN_STEEL.getItemStack(6), 130, 3).build(finishedRecipeConsumer, id("tungsten_steel_ingot_from_tungsten_steel_gear"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Component.IRIDIUM_GEAR.getItem()), Ingot.IRIDIUM.getItemStack(6), 130, 3).build(finishedRecipeConsumer, id("iridium_ingot_from_iridium_gear"), true);
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Items.MILK_BUCKET, Items.WATER_BUCKET), new ItemStack(Items.BUCKET), 100, 1).build(finishedRecipeConsumer, id("bucket_from_filled_bucket"));
        alloySmelter(ModRecipeIngredientTypes.ITEM.of(Component.IRON_GEAR.getItem()), new ItemStack(Items.IRON_INGOT, 6), 130, 3).build(finishedRecipeConsumer, id("iron_ingot_from_iron_gear"), true);
        StreamEx.of(Nugget.values())
            .without(Nugget.IRIDIUM, Nugget.OSMIUM)
            .mapToEntry(nugget -> JavaUtil.getEnumConstantSafely(Ingot.class, nugget.name()))
            .nonNullValues()
            .forKeyValue((nugget, ingot) -> alloySmelter(ModRecipeIngredientTypes.ITEM.of(nugget.getTag(), 9), ingot.getItemStack(), 200, 1).build(finishedRecipeConsumer, id(ingot.getRegistryName() + "_from_" + GtUtil.tagName(nugget.getTag()))));
        // TODO Thermal Obsidian glass recipe
    }

    public static RecipeName id(String name) {
        return RecipeName.common(Reference.MODID, "alloy_smelter", name);
    }
}
