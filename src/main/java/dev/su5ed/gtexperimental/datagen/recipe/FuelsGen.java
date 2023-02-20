package dev.su5ed.gtexperimental.datagen.recipe;

import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.object.Ingot;
import dev.su5ed.gtexperimental.object.ModFluid;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipeName;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import twilightforest.init.TFItems;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.datagen.RecipeGen.TWILIGHT_FOREST_LOADED;
import static dev.su5ed.gtexperimental.recipe.gen.ModFuelBuilders.*;
import static dev.su5ed.gtexperimental.util.GtUtil.buckets;

public final class FuelsGen implements ModRecipeProvider {
    public static final FuelsGen INSTANCE = new FuelsGen();

    private FuelsGen() {}

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        // Dense Liquid
        denseLiquid(ModRecipeIngredientTypes.FLUID.of(ModFluid.SEED_OIL.getTag(), buckets(1)), 2).build(finishedRecipeConsumer, denseLiquidId("seed_oil"));
        denseLiquid(ModRecipeIngredientTypes.FLUID.of(ModFluid.SODIUM.getTag(), buckets(1)), 30).build(finishedRecipeConsumer, denseLiquidId("sodium"));
        denseLiquid(ModRecipeIngredientTypes.FLUID.of(ModFluid.LITHIUM.getTag(), buckets(1)), 60).build(finishedRecipeConsumer, denseLiquidId("lithium"));
        denseLiquid(ModRecipeIngredientTypes.FLUID.of(ModFluid.OIL.getTag(), buckets(1)), 64).build(finishedRecipeConsumer, denseLiquidId("oil"));

        // Diesel
        diesel(ModRecipeIngredientTypes.HYBRID.ofFluid(ModFluid.DIESEL.getTag(), buckets(1)), 384).build(finishedRecipeConsumer, dieselId("diesel"));
        diesel(ModRecipeIngredientTypes.HYBRID.ofFluid(ModFluid.NITRO_DIESEL.getTag(), buckets(1)), 400).build(finishedRecipeConsumer, dieselId("nitro_diesel"));
        diesel(ModRecipeIngredientTypes.HYBRID.ofFluid(ModFluid.NITRO_COALFUEL.getTag(), buckets(1)), 48).build(finishedRecipeConsumer, dieselId("nitro_coalfuel"));

        // Gas
        gas(ModRecipeIngredientTypes.FLUID.of(ModFluid.HYDROGEN.getTag(), buckets(1)), new FluidStack(Fluids.WATER, buckets(1)), 15).build(finishedRecipeConsumer, gasId("hydrogen"));
        gas(ModRecipeIngredientTypes.FLUID.of(ModFluid.METHANE.getTag(), buckets(1)), new FluidStack(Fluids.WATER, buckets(1)), 45).build(finishedRecipeConsumer, gasId("methane"));

        // Hot
        hot(ModRecipeIngredientTypes.HYBRID.ofFluid(FluidTags.LAVA, buckets(1)), new ItemStack(Items.OBSIDIAN), new ItemStack(Items.COPPER_INGOT), Ingot.TIN.getItemStack(), Ingot.ELECTRUM.getItemStack(), 30).build(finishedRecipeConsumer, hotId("lava"));

        // Magic
        magic(ModRecipeIngredientTypes.HYBRID.of(Items.EXPERIENCE_BOTTLE), 10).build(finishedRecipeConsumer, magicId("experience_bottle"));
        magic(ModRecipeIngredientTypes.HYBRID.of(Items.ENDER_EYE), 20).build(finishedRecipeConsumer, magicId("ender_eye"));
        magic(ModRecipeIngredientTypes.HYBRID.of(Items.GHAST_TEAR), 500).build(finishedRecipeConsumer, magicId("ghast_tear"));
        magic(ModRecipeIngredientTypes.HYBRID.of(Items.NETHER_STAR), 100000).build(finishedRecipeConsumer, magicId("nether_star"));
        magic(ModRecipeIngredientTypes.HYBRID.of(Items.BEACON), 100000).build(finishedRecipeConsumer, magicId("beacon"));
        magic(ModRecipeIngredientTypes.HYBRID.ofFluid(ModFluid.MERCURY.getTag(), buckets(1)), 8).build(finishedRecipeConsumer, magicId("mercury"));

        // Plasma
        plasma(ModRecipeIngredientTypes.FLUID.of(ModFluid.HELIUM_PLASMA.getTag(), buckets(1)), 8192).build(finishedRecipeConsumer, plasmaId("helium_plasma"));

        // Steam
        steam(ModRecipeIngredientTypes.FLUID.of(ModFluid.STEAM.getTag(), buckets(1)), 500).build(finishedRecipeConsumer, steamId("steam"));

        buildOtherModRecipes(finishedRecipeConsumer);
    }

    private void buildOtherModRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        // Twilight Forest
        magic(ModRecipeIngredientTypes.HYBRID.of(TFItems.LIVEROOT.get()), new ItemStack(Items.STICK, 2), 16)
            .addConditions(TWILIGHT_FOREST_LOADED)
            .build(finishedRecipeConsumer, magicId("liveroot"));
        magic(ModRecipeIngredientTypes.HYBRID.of(TFItems.FIERY_INGOT.get()), Ingot.STEEL.getItemStack(), 2048)
            .addConditions(TWILIGHT_FOREST_LOADED)
            .build(finishedRecipeConsumer, magicId("fiery_ingot"));

        // Forestry
//        denseLiquid(ModRecipeIngredientTypes.FLUID.of(ModFluid.BIOMASS, buckets(1)), 8)
//            .addConditions(FORESTRY_LOADED)
//            .build(finishedRecipeConsumer, id("biomass"));
//        diesel(ModRecipeIngredientTypes.FLUID.of(ModFluid.ETHANOL.getTag(), buckets(1)), 128)
//            .addConditions(FORESTRY_LOADED)
//            .build(finishedRecipeConsumer, dieselId("ethanol"));

        // RailCraft
//        denseLiquid(ModRecipeIngredientTypes.FLUID.of(ModFluid.CREOSOTE, buckets(1)), 3)
//            .addConditions(RAILCRAFT_LOADED)
//            .build(finishedRecipeConsumer, id("creosote"));
    }

    private static RecipeName denseLiquidId(String name) {
        return RecipeName.common(Reference.MODID, "fuels/dense_liquid", name);
    }

    private static RecipeName dieselId(String name) {
        return RecipeName.common(Reference.MODID, "fuels/diesel", name);
    }

    private static RecipeName gasId(String name) {
        return RecipeName.common(Reference.MODID, "fuels/gas", name);
    }

    private static RecipeName hotId(String name) {
        return RecipeName.common(Reference.MODID, "fuels/hot", name);
    }

    private static RecipeName magicId(String name) {
        return RecipeName.common(Reference.MODID, "fuels/magic", name);
    }

    private static RecipeName plasmaId(String name) {
        return RecipeName.common(Reference.MODID, "fuels/plasma", name);
    }

    private static RecipeName steamId(String name) {
        return RecipeName.common(Reference.MODID, "fuels/steam", name);
    }
}
