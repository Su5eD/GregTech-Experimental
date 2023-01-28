package dev.su5ed.gtexperimental.datagen.recipe;

import dev.ftb.mods.ftbic.item.FTBICItems;
import dev.ftb.mods.ftbic.world.ResourceElements;
import dev.ftb.mods.ftbic.world.ResourceType;
import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.object.Dust;
import dev.su5ed.gtexperimental.object.ModFluid;
import dev.su5ed.gtexperimental.object.Smalldust;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import ic2.core.ref.Ic2Fluids;
import ic2.core.ref.Ic2Items;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.api.Reference.location;
import static dev.su5ed.gtexperimental.datagen.RecipeGen.FTBIC_LOADED;
import static dev.su5ed.gtexperimental.datagen.RecipeGen.IC2_LOADED;
import static dev.su5ed.gtexperimental.datagen.RecipeGen.NOT_IC2_LOADED;
import static dev.su5ed.gtexperimental.recipe.gen.ModRecipeBuilders.industrialElectrolyzer;
import static dev.su5ed.gtexperimental.util.GtUtil.buckets;

public final class IndustrialElectrolyzerRecipesGen implements ModRecipeProvider {
    public static final IndustrialElectrolyzerRecipesGen INSTANCE = new IndustrialElectrolyzerRecipesGen();

    private IndustrialElectrolyzerRecipesGen() {}

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Items.SUGAR, 32), ModFluid.CARBON.getBuckets(2), new FluidStack(Fluids.WATER, buckets(5)), 210, 32)
            .build(finishedRecipeConsumer, id("sugar"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Items.CLAY, 8), ModFluid.LITHIUM.getBuckets(1), ModFluid.SILICON.getBuckets(2), Dust.ALUMINIUM.getItemStack(2), ModFluid.SODIUM.getBuckets(2), 200, 50)
            .build(finishedRecipeConsumer, id("clay"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.SPHALERITE.getTag(), 2), Dust.ZINC.getItemStack(), Dust.SULFUR.getItemStack(), 150, 100)
            .build(finishedRecipeConsumer, id("sphalerite_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.CINNABAR.getTag(), 2), ModFluid.MERCURY.getBuckets(1), Dust.SULFUR.getItemStack(), 100, 128)
            .build(finishedRecipeConsumer, id("cinnabar_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.GALENA.getTag(), 2), Smalldust.SILVER.getItemStack(3), Smalldust.LEAD.getItemStack(3), Smalldust.SULFUR.getItemStack(2), 1000, 120)
            .build(finishedRecipeConsumer, id("galena_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.ENDER_PEARL.getTag(), 16), ModFluid.NITROGEN.getBuckets(5), ModFluid.BERYLIUM.getBuckets(1), ModFluid.POTASSIUM.getBuckets(4), ModFluid.CHLORITE.getBuckets(6), 1300, 50)
            .build(finishedRecipeConsumer, id("ender_pearl_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.GREEN_SAPPHIRE.getTag()), Dust.SAPPHIRE.getItemStack(), 100, 50)
            .build(finishedRecipeConsumer, id("green_sapphire_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.ASHES.getTag(), 2), ModFluid.CARBON.getBuckets(1), 25, 50)
            .build(finishedRecipeConsumer, id("ashes_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(GregTechTags.dust("coal")), ModFluid.CARBON.getBuckets(2), 40, 50)
            .build(finishedRecipeConsumer, id("coal_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.CHARCOAL.getTag()), ModFluid.CARBON.getBuckets(1), 20, 50)
            .build(finishedRecipeConsumer, id("charcoal_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.LAZURITE.getTag(), 29), Dust.ALUMINIUM.getItemStack(3), ModFluid.SILICON.getBuckets(3), ModFluid.CALCIUM.getBuckets(4), ModFluid.SODIUM.getBuckets(4), 1475, 100)
            .build(finishedRecipeConsumer, id("lazurite_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.SODALITE.getTag(), 23), ModFluid.SODIUM.getBuckets(4), Dust.ALUMINIUM.getItemStack(3), ModFluid.SILICON.getBuckets(3), ModFluid.CHLORITE.getBuckets(1), 1350, 90)
            .build(finishedRecipeConsumer, id("sodalite_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.ofFluid(ModFluid.METHANE.getTag(), buckets(5)), ModFluid.HYDROGEN.getBuckets(4), ModFluid.CARBON.getBuckets(1), 150, 50)
            .build(finishedRecipeConsumer, id("methane"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Items.BONE_MEAL, 3), ModFluid.CALCIUM.getBuckets(1), 24, 106)
            .build(finishedRecipeConsumer, id("bone_meal"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Items.BLAZE_POWDER, 4), Dust.DARK_ASHES.getItemStack(), Dust.SULFUR.getItemStack(), 300, 25)
            .build(finishedRecipeConsumer, id("blaze_powder"));

        buildOtherModRecipes(finishedRecipeConsumer);
    }

    private void buildOtherModRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        // IC2
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.SALTPETER.getTag(), 10), ModFluid.POTASSIUM.getBuckets(2), ModFluid.NITROGEN.getBuckets(2), new FluidStack(Ic2Fluids.AIR.still, buckets(3)), 50, 110)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/saltpeter_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.OBSIDIAN.getTag(), 4), Smalldust.MAGNESIUM.getItemStack(2), Smalldust.IRON.getItemStack(2), ModFluid.SILICON.getBuckets(2), new FluidStack(Ic2Fluids.AIR.still, buckets(2)), 500, 5)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/obsidian_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.PYROPE.getTag(), 20), Dust.MAGNESIUM.getItemStack(3), Dust.ALUMINIUM.getItemStack(2), ModFluid.SILICON.getBuckets(3), new FluidStack(Ic2Fluids.AIR.still, buckets(6)), 1790, 50)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/pyrope_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.ALMANDINE.getTag(), 20), new ItemStack(Ic2Items.IRON_DUST, 3), Dust.ALUMINIUM.getItemStack(2), ModFluid.SILICON.getBuckets(3), new FluidStack(Ic2Fluids.AIR.still, buckets(6)), 1640, 50)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/almandine_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.SPESSARTINE.getTag(), 20), Dust.ALUMINIUM.getItemStack(2), Dust.MANGANESE.getItemStack(3), ModFluid.SILICON.getBuckets(3), new FluidStack(Ic2Fluids.AIR.still, buckets(6)), 1810, 50)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/spessartine_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.ANDRADITE.getTag(), 20), ModFluid.CALCIUM.getBuckets(3), new ItemStack(Ic2Items.IRON_DUST, 2), ModFluid.SILICON.getBuckets(3), new FluidStack(Ic2Fluids.AIR.still, 6), 1280, 50)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/andradite_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.GROSSULAR.getTag(), 20), ModFluid.CALCIUM.getBuckets(3), new ItemStack(Ic2Items.IRON_DUST, 2), ModFluid.SILICON.getBuckets(3), new FluidStack(Ic2Fluids.AIR.still, 6), 2050, 50)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/grossular_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.UVAROVITE.getTag(), 20), ModFluid.CALCIUM.getBuckets(3), Dust.CHROME.getItemStack(2), ModFluid.SILICON.getBuckets(3), new FluidStack(Ic2Fluids.AIR.still, 6), 2200, 50)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/uvarovite_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.OLIVINE.getTag(), 9), Dust.MAGNESIUM.getItemStack(2), new ItemStack(Ic2Items.IRON_DUST, 2), ModFluid.SILICON.getBuckets(1), new FluidStack(Ic2Fluids.AIR.still, buckets(1)), 600, 60)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/olivine_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.EMERALD.getTag(), 29), Dust.ALUMINIUM.getItemStack(2), ModFluid.BERYLIUM.getBuckets(3), ModFluid.SILICON.getBuckets(6), new FluidStack(Ic2Fluids.AIR.still, buckets(9)), 600, 50)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/emerald_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.RUBY.getTag(), 9), Dust.ALUMINIUM.getItemStack(2), Dust.CHROME.getItemStack(), new FluidStack(Ic2Fluids.AIR.still, buckets(3)), 500, 50)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/ruby_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.SAPPHIRE.getTag(), 8), Dust.ALUMINIUM.getItemStack(2), new FluidStack(Ic2Fluids.AIR.still, buckets(3)), 400, 50)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/sapphire_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.PYRITE.getTag(), 3), new ItemStack(Ic2Items.IRON_DUST), Dust.SULFUR.getItemStack(2), 120, 128)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/pyrite_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.CALCITE.getTag(), 10), ModFluid.CALCIUM.getBuckets(2), ModFluid.CARBON.getBuckets(2), new FluidStack(Ic2Fluids.AIR.still, buckets(3)), 700, 80)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/calcite_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.FLINT.getTag(), 8), ModFluid.SILICON.getBuckets(1), new FluidStack(Ic2Fluids.AIR.still, buckets(1)), 1000, 5)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/flint_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.BAUXITE.getTag(), 12), Dust.ALUMINIUM.getItemStack(8), Smalldust.TITANIUM.getItemStack(2), ModFluid.HYDROGEN.getBuckets(5), new FluidStack(Ic2Fluids.AIR.still, buckets(3)), 2000, 128)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/bauxite_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.ofFluid(ModFluid.SULFURIC_ACID.getTag(), 7), ModFluid.HYDROGEN.getBuckets(2), ModFluid.SULFUR.getBuckets(1), new FluidStack(Ic2Fluids.AIR.still, buckets(2)), 40, 100)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/sulfuric_acid"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.ofFluid(FluidTags.WATER, 6), ModFluid.HYDROGEN.getBuckets(4), new FluidStack(Ic2Fluids.AIR.still, buckets(1)), 775, 120)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/water"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Items.SAND, 16), ModFluid.SILICON.getBuckets(1), new FluidStack(Ic2Fluids.AIR.still, buckets(1)), 1000, 25)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/sand"));

        // FTBIC
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.SALTPETER.getTag(), 10), ModFluid.POTASSIUM.getBuckets(2), ModFluid.NITROGEN.getBuckets(2), 50, 110)
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/saltpeter_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.OBSIDIAN.getTag(), 4), Smalldust.MAGNESIUM.getItemStack(2), Smalldust.IRON.getItemStack(2), ModFluid.SILICON.getBuckets(2), 500, 5)
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/obsidian_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.PYROPE.getTag(), 20), Dust.MAGNESIUM.getItemStack(3), Dust.ALUMINIUM.getItemStack(2), ModFluid.SILICON.getBuckets(3), 1790, 50)
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/pyrope_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.ALMANDINE.getTag(), 20), new ItemStack(FTBICItems.getResourceFromType(ResourceElements.IRON, ResourceType.DUST).orElseThrow().get(), 3), Dust.ALUMINIUM.getItemStack(2), ModFluid.SILICON.getBuckets(3), 1640, 50)
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/almandine_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.SPESSARTINE.getTag(), 20), Dust.ALUMINIUM.getItemStack(2), Dust.MANGANESE.getItemStack(3), ModFluid.SILICON.getBuckets(3), 1810, 50)
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/spessartine_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.ANDRADITE.getTag(), 20), ModFluid.CALCIUM.getBuckets(3), new ItemStack(FTBICItems.getResourceFromType(ResourceElements.IRON, ResourceType.DUST).orElseThrow().get(), 2), ModFluid.SILICON.getBuckets(3), 1280, 50)
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/andradite_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.GROSSULAR.getTag(), 20), ModFluid.CALCIUM.getBuckets(3), new ItemStack(FTBICItems.getResourceFromType(ResourceElements.IRON, ResourceType.DUST).orElseThrow().get(), 2), ModFluid.SILICON.getBuckets(3), 2050, 50)
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/grossular_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.UVAROVITE.getTag(), 20), ModFluid.CALCIUM.getBuckets(3), Dust.CHROME.getItemStack(2), ModFluid.SILICON.getBuckets(3), 2200, 50)
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/uvarovite_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.OLIVINE.getTag(), 9), Dust.MAGNESIUM.getItemStack(2), new ItemStack(FTBICItems.getResourceFromType(ResourceElements.IRON, ResourceType.DUST).orElseThrow().get(), 2), ModFluid.SILICON.getBuckets(1), 600, 60)
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/olivine_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.EMERALD.getTag(), 29), Dust.ALUMINIUM.getItemStack(2), ModFluid.BERYLIUM.getBuckets(3), ModFluid.SILICON.getBuckets(6), 600, 50)
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/emerald_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.RUBY.getTag(), 9), Dust.ALUMINIUM.getItemStack(2), Dust.CHROME.getItemStack(), 500, 50)
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/ruby_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.SAPPHIRE.getTag(), 8), Dust.ALUMINIUM.getItemStack(2), 400, 50)
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/sapphire_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.PYRITE.getTag(), 3), new ItemStack(FTBICItems.getResourceFromType(ResourceElements.IRON, ResourceType.DUST).orElseThrow().get()), Dust.SULFUR.getItemStack(2), 120, 128)
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/pyrite_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.FLINT.getTag(), 8), ModFluid.SILICON.getBuckets(1), 1000, 5)
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/flint_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Dust.BAUXITE.getTag(), 12), Dust.ALUMINIUM.getItemStack(8), Smalldust.TITANIUM.getItemStack(2), ModFluid.HYDROGEN.getBuckets(5), 2000, 128)
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/bauxite_dust"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.ofFluid(ModFluid.SULFURIC_ACID.getTag(), 7), ModFluid.HYDROGEN.getBuckets(2), ModFluid.SULFUR.getBuckets(1), 40, 100)
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/sulfuric_acid"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.ofFluid(FluidTags.WATER, 6), ModFluid.HYDROGEN.getBuckets(4), new FluidStack(Ic2Fluids.AIR.still, buckets(1)), 775, 120)
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/water"));
        industrialElectrolyzer(ModRecipeIngredientTypes.HYBRID.of(Items.SAND, 16), ModFluid.SILICON.getBuckets(1), new FluidStack(Ic2Fluids.AIR.still, buckets(1)), 1000, 25)
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/sand"));
    }

    private static ResourceLocation id(String name) {
        return location("industrial_electrolyzer/" + name);
    }
}
