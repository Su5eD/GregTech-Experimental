package dev.su5ed.gtexperimental.datagen.recipe;

import dev.ftb.mods.ftbic.item.FTBICItems;
import dev.ftb.mods.ftbic.world.ResourceElements;
import dev.ftb.mods.ftbic.world.ResourceType;
import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.object.Dust;
import dev.su5ed.gtexperimental.object.Miscellaneous;
import dev.su5ed.gtexperimental.object.ModFluid;
import dev.su5ed.gtexperimental.object.Nugget;
import dev.su5ed.gtexperimental.object.Ore;
import dev.su5ed.gtexperimental.object.Smalldust;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import ic2.core.ref.Ic2Items;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.api.Reference.location;
import static dev.su5ed.gtexperimental.datagen.RecipeGen.FTBIC_LOADED;
import static dev.su5ed.gtexperimental.datagen.RecipeGen.IC2_LOADED;
import static dev.su5ed.gtexperimental.datagen.RecipeGen.NOT_IC2_LOADED;
import static dev.su5ed.gtexperimental.recipe.gen.ModRecipeBuilders.industrialGrinder;
import static dev.su5ed.gtexperimental.util.GtUtil.buckets;

public final class IndustrialGrinderRecipesGen implements ModRecipeProvider {
    public static final IndustrialGrinderRecipesGen INSTANCE = new IndustrialGrinderRecipesGen();
    private static final RecipeIngredient<FluidStack> WATER = ModRecipeIngredientTypes.FLUID.of(Fluids.WATER, buckets(1));
    private static final RecipeIngredient<FluidStack> MERCURY = ModRecipeIngredientTypes.FLUID.of(ModFluid.MERCURY, buckets(1));
    private static final RecipeIngredient<FluidStack> SODIUM_PERSULFATE = ModRecipeIngredientTypes.FLUID.of(ModFluid.SODIUM_PERSULFATE, buckets(1));

    private IndustrialGrinderRecipesGen() {}

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Tags.Items.NETHERRACK, 16), WATER, new ItemStack(Items.GOLD_NUGGET), Dust.NETHERRACK.getItemStack(16))
            .build(finishedRecipeConsumer, id("netherrack_water"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Tags.Items.NETHERRACK, 8), MERCURY, new ItemStack(Items.GOLD_NUGGET), Dust.NETHERRACK.getItemStack(8))
            .build(finishedRecipeConsumer, id("netherrack_mercury"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Tags.Items.ORES_LAPIS), WATER, new ItemStack(Items.LAPIS_LAZULI, 12), Dust.LAZURITE.getItemStack(3))
            .build(finishedRecipeConsumer, id("lapis_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.SODALITE.getTag()), WATER, Dust.SODALITE.getItemStack(12), Dust.ALUMINIUM.getItemStack(3))
            .build(finishedRecipeConsumer, id("sodalite_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Tags.Items.ORES_REDSTONE), WATER, new ItemStack(Items.REDSTONE, 15), Smalldust.GLOWSTONE.getItemStack(2))
            .build(finishedRecipeConsumer, id("redstone_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Tags.Items.ORES_QUARTZ), WATER, new ItemStack(Items.QUARTZ, 4), Dust.NETHERRACK.getItemStack())
            .build(finishedRecipeConsumer, id("quartz_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "silver")), WATER, Dust.SILVER.getItemStack(2), Dust.LEAD.getItemStack(2))
            .build(finishedRecipeConsumer, id("silver_ore_water"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "silver")), MERCURY, Dust.SILVER.getItemStack(3), Dust.LEAD.getItemStack(2))
            .build(finishedRecipeConsumer, id("silver_ore_mercury"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "lead")), WATER, Dust.LEAD.getItemStack(2), Smalldust.SILVER.getItemStack())
            .build(finishedRecipeConsumer, id("lead_ore_water"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "lead")), MERCURY, Dust.LEAD.getItemStack(2), Dust.SILVER.getItemStack())
            .build(finishedRecipeConsumer, id("lead_ore_mercury"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.GALENA.getTag()), WATER, Dust.GALENA.getItemStack(2), Dust.SULFUR.getItemStack())
            .build(finishedRecipeConsumer, id("galena_ore_water"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.GALENA.getTag()), MERCURY, Dust.GALENA.getItemStack(2), Dust.SULFUR.getItemStack(), Dust.SILVER.getItemStack())
            .build(finishedRecipeConsumer, id("galena_ore_mercury"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Tags.Items.ORES_EMERALD), WATER, new ItemStack(Items.EMERALD), Smalldust.EMERALD.getItemStack(6), Smalldust.OLIVINE.getItemStack(2))
            .build(finishedRecipeConsumer, id("emerald_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.RUBY.getTag()), WATER, Miscellaneous.RUBY.getItemStack(), Smalldust.RUBY.getItemStack(6), Smalldust.RED_GARNET.getItemStack(2))
            .build(finishedRecipeConsumer, id("ruby_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.SAPPHIRE.getTag()), WATER, Miscellaneous.SAPPHIRE.getItemStack(), Smalldust.SAPPHIRE.getItemStack(6), Smalldust.GREEN_SAPPHIRE.getItemStack(2))
            .build(finishedRecipeConsumer, id("sapphire_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "green_sapphire")), WATER, Miscellaneous.GREEN_SAPPHIRE.getItemStack(), Smalldust.GREEN_SAPPHIRE.getItemStack(6), Smalldust.SAPPHIRE.getItemStack(2))
            .build(finishedRecipeConsumer, id("green_sapphire_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.OLIVINE.getTag()), WATER, Miscellaneous.OLIVINE.getItemStack(), Smalldust.OLIVINE.getItemStack(6), Smalldust.EMERALD.getItemStack(2))
            .build(finishedRecipeConsumer, id("olivine_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "zinc")), WATER, Dust.ZINC.getItemStack(2), Smalldust.TIN.getItemStack(2))
            .build(finishedRecipeConsumer, id("zinc_ore_water"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "zinc")), SODIUM_PERSULFATE, Dust.ZINC.getItemStack(3), Smalldust.TIN.getItemStack(2))
            .build(finishedRecipeConsumer, id("zinc_ore_sodium_persulfate"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "antimony")), WATER, Dust.ANTIMONY.getItemStack(2), Smalldust.ZINC.getItemStack(2), Smalldust.IRON.getItemStack(2))
            .build(finishedRecipeConsumer, id("antimony_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.SHELDONITE.getTag()), WATER, Dust.PLATINUM.getItemStack(2), Dust.NICKEL.getItemStack(), Nugget.IRIDIUM.getItemStack(2))
            .build(finishedRecipeConsumer, id("sheldonite_ore_water"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.SHELDONITE.getTag()), MERCURY, Dust.PLATINUM.getItemStack(3), Dust.NICKEL.getItemStack(), Nugget.IRIDIUM.getItemStack(2))
            .build(finishedRecipeConsumer, id("sheldonite_ore_mercury"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "platinum")), WATER, Dust.PLATINUM.getItemStack(2), Nugget.IRIDIUM.getItemStack(2), Smalldust.NICKEL.getItemStack())
            .build(finishedRecipeConsumer, id("platinum_ore_water"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "platinum")), MERCURY, Dust.PLATINUM.getItemStack(3), Nugget.IRIDIUM.getItemStack(2), Smalldust.NICKEL.getItemStack())
            .build(finishedRecipeConsumer, id("platinum_ore_mercury"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "nickel")), WATER, Dust.NICKEL.getItemStack(3), Smalldust.PLATINUM.getItemStack(), Smalldust.COPPER.getItemStack())
            .build(finishedRecipeConsumer, id("nickel_ore_water"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "nickel")), MERCURY, Dust.NICKEL.getItemStack(3), Dust.PLATINUM.getItemStack())
            .build(finishedRecipeConsumer, id("nickel_ore_mercury"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.PYRITE.getTag()), WATER, Dust.PYRITE.getItemStack(5), Dust.SULFUR.getItemStack(2))
            .build(finishedRecipeConsumer, id("pyrite_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.CINNABAR.getTag()), WATER, Dust.CINNABAR.getItemStack(5), Smalldust.REDSTONE.getItemStack(2), Smalldust.GLOWSTONE.getItemStack())
            .build(finishedRecipeConsumer, id("cinnabar_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.SPHALERITE.getTag()), WATER, Dust.SPHALERITE.getItemStack(5), Dust.ZINC.getItemStack(), Smalldust.YELLOW_GARNET.getItemStack())
            .build(finishedRecipeConsumer, id("sphalerite_ore_water"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.SPHALERITE.getTag()), SODIUM_PERSULFATE, Dust.SPHALERITE.getItemStack(5), Dust.ZINC.getItemStack(3), Smalldust.YELLOW_GARNET.getItemStack())
            .build(finishedRecipeConsumer, id("sphalerite_ore_sodium_persulfate"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "aluminium")), WATER, Dust.ALUMINIUM.getItemStack(3), Smalldust.TITANIUM.getItemStack())
            .build(finishedRecipeConsumer, id("aluminium_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "steel")), WATER, Dust.STEEL.getItemStack(2), Smalldust.NICKEL.getItemStack(2))
            .build(finishedRecipeConsumer, id("steel_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "titanium")), WATER, Dust.TITANIUM.getItemStack(2), Smalldust.ALUMINIUM.getItemStack(2))
            .build(finishedRecipeConsumer, id("titanium_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "chromium")), WATER, Dust.CHROME.getItemStack(2), Smalldust.RUBY.getItemStack())
            .build(finishedRecipeConsumer, id("chromium_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "electrum")), WATER, Dust.ELECTRUM.getItemStack(2), Smalldust.GOLD.getItemStack(), Smalldust.SILVER.getItemStack())
            .build(finishedRecipeConsumer, id("electrum_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.TUNGSTATE.getTag()), WATER, Dust.TUNGSTEN.getItemStack(2), Smalldust.IRON.getItemStack(3), Smalldust.MANGANESE.getItemStack(3))
            .build(finishedRecipeConsumer, id("tungstate_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.BAUXITE.getTag()), WATER, Dust.BAUXITE.getItemStack(4), Dust.ALUMINIUM.getItemStack())
            .build(finishedRecipeConsumer, id("bauxite_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "sulfur")), WATER, Dust.SULFUR.getItemStack(10))
            .build(finishedRecipeConsumer, id("sulfur_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "saltpeter")), WATER, Dust.SALTPETER.getItemStack(7))
            .build(finishedRecipeConsumer, id("saltpeter_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "magnesium")), WATER, Dust.MAGNESIUM.getItemStack(2), Smalldust.IRON.getItemStack(2), Smalldust.NICKEL.getItemStack())
            .build(finishedRecipeConsumer, id("magnesium_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "manganese")), WATER, Dust.MANGANESE.getItemStack(2), Smalldust.IRON.getItemStack(2), Smalldust.NICKEL.getItemStack())
            .build(finishedRecipeConsumer, id("manganese_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "uranium")), WATER, Dust.URANIUM.getItemStack(2), Smalldust.URANIUM.getItemStack(2), Dust.THORIUM.getItemStack())
            .build(finishedRecipeConsumer, id("uranium_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "thorium")), WATER, Dust.THORIUM.getItemStack(2), Smalldust.URANIUM.getItemStack())
            .build(finishedRecipeConsumer, id("thorium_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "plutonium")), WATER, Dust.PLUTONIUM.getItemStack(2), Dust.URANIUM.getItemStack())
            .build(finishedRecipeConsumer, id("plutonium_ore"));

        buildOtherModRecipes(finishedRecipeConsumer);
    }

    private void buildOtherModRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        // TODO Chunk / raw ore processing
        
        // IC2
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Tags.Items.ORES_IRON), WATER, new ItemStack(Ic2Items.IRON_DUST, 2), Smalldust.TIN.getItemStack(), Smalldust.NICKEL.getItemStack())
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/iron_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Tags.Items.ORES_GOLD), WATER, new ItemStack(Ic2Items.GOLD_DUST, 2), Smalldust.COPPER.getItemStack(), Smalldust.NICKEL.getItemStack())
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/gold_ore_water"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Tags.Items.ORES_GOLD), MERCURY, new ItemStack(Ic2Items.GOLD_DUST, 3), Smalldust.COPPER.getItemStack(), Smalldust.NICKEL.getItemStack())
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/gold_ore_mercury"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Tags.Items.ORES_GOLD), SODIUM_PERSULFATE, new ItemStack(Ic2Items.GOLD_DUST, 2), new ItemStack(Ic2Items.COPPER_DUST), Smalldust.NICKEL.getItemStack())
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/gold_ore_sodium_persulfate"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Tags.Items.ORES_DIAMOND), WATER, new ItemStack(Items.DIAMOND), Smalldust.DIAMOND.getItemStack(6), new ItemStack(Ic2Items.COAL_FUEL_DUST))
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/diamond_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Tags.Items.ORES_COAL), WATER, new ItemStack(Items.COAL), new ItemStack(Ic2Items.COAL_DUST), Smalldust.THORIUM.getItemStack())
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/coal_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "copper")), WATER, new ItemStack(Ic2Items.COPPER_DUST, 2), Smalldust.GOLD.getItemStack(), Smalldust.NICKEL.getItemStack())
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/copper_ore_water"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "copper")), SODIUM_PERSULFATE, new ItemStack(Ic2Items.COPPER_DUST, 3), Smalldust.GOLD.getItemStack(), Smalldust.NICKEL.getItemStack())
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/copper_ore_sodium_persulfate"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "copper")), MERCURY, new ItemStack(Ic2Items.COPPER_DUST, 2), new ItemStack(Ic2Items.GOLD_DUST))
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/copper_ore_mercury"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "tin")), WATER, new ItemStack(Ic2Items.TIN_DUST, 2), Smalldust.IRON.getItemStack(), Smalldust.ZINC.getItemStack())
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/tin_ore_water"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "tin")), SODIUM_PERSULFATE, new ItemStack(Ic2Items.TIN_DUST, 2), Smalldust.IRON.getItemStack(), Dust.ZINC.getItemStack())
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/tin_ore_sodium_persulfate"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.CASSITERITE.getTag()), WATER, new ItemStack(Ic2Items.TIN_DUST, 5))
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/cassiterite_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.TETRAHEDRITE.getTag()), WATER, new ItemStack(Ic2Items.COPPER_DUST, 2), Smalldust.ZINC.getItemStack(2), Smalldust.ANTIMONY.getItemStack())
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/tetrahedrite_ore_water"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.TETRAHEDRITE.getTag()), SODIUM_PERSULFATE, new ItemStack(Ic2Items.COPPER_DUST, 3), Smalldust.ZINC.getItemStack(2), Smalldust.ANTIMONY.getItemStack())
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/tetrahedrite_ore_sodium_persulfate"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.IRIDIUM.getTag()), WATER, new ItemStack(Ic2Items.IRIDIUM_ORE, 2), Smalldust.PLATINUM.getItemStack(2))
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/iridium_ore_water"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.IRIDIUM.getTag()), MERCURY, new ItemStack(Ic2Items.IRIDIUM_ORE, 2), Dust.PLATINUM.getItemStack(2))
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/iridium_ore_mercury"));

        // FTBIC
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Tags.Items.ORES_IRON), WATER, new ItemStack(FTBICItems.getResourceFromType(ResourceElements.IRON, ResourceType.DUST).orElseThrow().get(), 2), Smalldust.TIN.getItemStack(), Smalldust.NICKEL.getItemStack())
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/iron_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Tags.Items.ORES_GOLD), WATER, new ItemStack(FTBICItems.getResourceFromType(ResourceElements.GOLD, ResourceType.DUST).orElseThrow().get(), 2), Smalldust.COPPER.getItemStack(), Smalldust.NICKEL.getItemStack())
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/gold_ore_water"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Tags.Items.ORES_GOLD), MERCURY, new ItemStack(FTBICItems.getResourceFromType(ResourceElements.GOLD, ResourceType.DUST).orElseThrow().get(), 3), Smalldust.COPPER.getItemStack(), Smalldust.NICKEL.getItemStack())
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/gold_ore_mercury"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Tags.Items.ORES_GOLD), SODIUM_PERSULFATE, new ItemStack(FTBICItems.getResourceFromType(ResourceElements.GOLD, ResourceType.DUST).orElseThrow().get(), 2), new ItemStack(Ic2Items.COPPER_DUST), Smalldust.NICKEL.getItemStack())
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/gold_ore_sodium_persulfate"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Tags.Items.ORES_DIAMOND), WATER, new ItemStack(Items.DIAMOND), Smalldust.DIAMOND.getItemStack(6), new ItemStack(FTBICItems.getResourceFromType(ResourceElements.COAL, ResourceType.DUST).orElseThrow().get()))
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/diamond_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Tags.Items.ORES_COAL), WATER, new ItemStack(Items.COAL), new ItemStack(FTBICItems.getResourceFromType(ResourceElements.COAL, ResourceType.DUST).orElseThrow().get()), Smalldust.THORIUM.getItemStack())
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/coal_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "copper")), WATER, new ItemStack(FTBICItems.getResourceFromType(ResourceElements.COPPER, ResourceType.DUST).orElseThrow().get(), 2), Smalldust.GOLD.getItemStack(), Smalldust.NICKEL.getItemStack())
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/copper_ore_water"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "copper")), SODIUM_PERSULFATE, new ItemStack(FTBICItems.getResourceFromType(ResourceElements.COPPER, ResourceType.DUST).orElseThrow().get(), 3), Smalldust.GOLD.getItemStack(), Smalldust.NICKEL.getItemStack())
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/copper_ore_sodium_persulfate"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "copper")), MERCURY, new ItemStack(FTBICItems.getResourceFromType(ResourceElements.COPPER, ResourceType.DUST).orElseThrow().get(), 2), new ItemStack(FTBICItems.getResourceFromType(ResourceElements.GOLD, ResourceType.DUST).orElseThrow().get()))
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/copper_ore_mercury"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "tin")), WATER, new ItemStack(FTBICItems.getResourceFromType(ResourceElements.TIN, ResourceType.DUST).orElseThrow().get(), 2), Smalldust.IRON.getItemStack(), Smalldust.ZINC.getItemStack())
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/tin_ore_water"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "tin")), SODIUM_PERSULFATE, new ItemStack(FTBICItems.getResourceFromType(ResourceElements.TIN, ResourceType.DUST).orElseThrow().get(), 2), Smalldust.IRON.getItemStack(), Dust.ZINC.getItemStack())
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/tin_ore_sodium_persulfate"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.CASSITERITE.getTag()), WATER, new ItemStack(FTBICItems.getResourceFromType(ResourceElements.TIN, ResourceType.DUST).orElseThrow().get(), 5))
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/cassiterite_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.TETRAHEDRITE.getTag()), WATER, new ItemStack(FTBICItems.getResourceFromType(ResourceElements.COPPER, ResourceType.DUST).orElseThrow().get(), 2), Smalldust.ZINC.getItemStack(2), Smalldust.ANTIMONY.getItemStack())
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/tetrahedrite_ore_water"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.TETRAHEDRITE.getTag()), SODIUM_PERSULFATE, new ItemStack(FTBICItems.getResourceFromType(ResourceElements.COPPER, ResourceType.DUST).orElseThrow().get(), 3), Smalldust.ZINC.getItemStack(2), Smalldust.ANTIMONY.getItemStack())
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/tetrahedrite_ore_sodium_persulfate"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.IRIDIUM.getTag()), WATER, new ItemStack(FTBICItems.getResourceFromType(ResourceElements.IRIDIUM, ResourceType.CHUNK).orElseThrow().get(), 2), Smalldust.PLATINUM.getItemStack(2))
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/iridium_ore_water"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.IRIDIUM.getTag()), MERCURY, new ItemStack(FTBICItems.getResourceFromType(ResourceElements.IRIDIUM, ResourceType.CHUNK).orElseThrow().get(), 2), Dust.PLATINUM.getItemStack(2))
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/iridium_ore_mercury"));

        // Forestry
//        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "apatite")), WATER, new ItemStack(GEM_APATITE, 12), Dust.PHOSPHORUS.getItemStack())
//            .build(finishedRecipeConsumer, id("apatite_ore"));

        // MFFS
//        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "monazite")), WATER, new ItemStack(MFFS_FORCICIUM), Dust.THORIUM.getItemStack(2))
//            .build(finishedRecipeConsumer, id("monazite_ore"));
    }

    private static ResourceLocation id(String name) {
        return location("industrial_grinder/" + name);
    }
}
