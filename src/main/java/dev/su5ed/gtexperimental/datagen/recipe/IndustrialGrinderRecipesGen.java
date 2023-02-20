package dev.su5ed.gtexperimental.datagen.recipe;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.object.Dust;
import dev.su5ed.gtexperimental.object.Miscellaneous;
import dev.su5ed.gtexperimental.object.ModFluid;
import dev.su5ed.gtexperimental.object.Nugget;
import dev.su5ed.gtexperimental.object.Ore;
import dev.su5ed.gtexperimental.object.Smalldust;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipeName;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.recipe.gen.ModRecipeBuilders.industrialGrinder;
import static dev.su5ed.gtexperimental.recipe.type.RecipeUtil.MERCURY;
import static dev.su5ed.gtexperimental.recipe.type.RecipeUtil.WATER;
import static dev.su5ed.gtexperimental.util.GtUtil.buckets;

public final class IndustrialGrinderRecipesGen implements ModRecipeProvider {
    public static final IndustrialGrinderRecipesGen INSTANCE = new IndustrialGrinderRecipesGen();
    private static final RecipeIngredient<FluidStack> SODIUM_PERSULFATE = ModRecipeIngredientTypes.FLUID.of(ModFluid.SODIUM_PERSULFATE, buckets(1));

    private IndustrialGrinderRecipesGen() {}

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        // TODO Chunk / raw ore processing        
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Tags.Items.NETHERRACK, 16), WATER, new ItemStack(Items.GOLD_NUGGET), Dust.NETHERRACK.getItemStack(16)).build(finishedRecipeConsumer, id("netherrack_water"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Tags.Items.NETHERRACK, 8), MERCURY, new ItemStack(Items.GOLD_NUGGET), Dust.NETHERRACK.getItemStack(8)).build(finishedRecipeConsumer, id("netherrack_mercury"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Tags.Items.ORES_LAPIS), WATER, new ItemStack(Items.LAPIS_LAZULI, 12), Dust.LAZURITE.getItemStack(3)).build(finishedRecipeConsumer, id("lapis_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.SODALITE.getTag()), WATER, Dust.SODALITE.getItemStack(12), Dust.ALUMINIUM.getItemStack(3)).build(finishedRecipeConsumer, id("sodalite_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Tags.Items.ORES_REDSTONE), WATER, new ItemStack(Items.REDSTONE, 15), Smalldust.GLOWSTONE.getItemStack(2)).build(finishedRecipeConsumer, id("redstone_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Tags.Items.ORES_QUARTZ), WATER, new ItemStack(Items.QUARTZ, 4), Dust.NETHERRACK.getItemStack()).build(finishedRecipeConsumer, id("quartz_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ore("silver")), WATER, Dust.SILVER.getItemStack(2), Dust.LEAD.getItemStack(2)).build(finishedRecipeConsumer, id("silver_ore_water"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ore("silver")), MERCURY, Dust.SILVER.getItemStack(3), Dust.LEAD.getItemStack(2)).build(finishedRecipeConsumer, id("silver_ore_mercury"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ore("lead")), WATER, Dust.LEAD.getItemStack(2), Smalldust.SILVER.getItemStack()).build(finishedRecipeConsumer, id("lead_ore_water"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ore("lead")), MERCURY, Dust.LEAD.getItemStack(2), Dust.SILVER.getItemStack()).build(finishedRecipeConsumer, id("lead_ore_mercury"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.GALENA.getTag()), WATER, Dust.GALENA.getItemStack(2), Dust.SULFUR.getItemStack()).build(finishedRecipeConsumer, id("galena_ore_water"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.GALENA.getTag()), MERCURY, Dust.GALENA.getItemStack(2), Dust.SULFUR.getItemStack(), Dust.SILVER.getItemStack()).build(finishedRecipeConsumer, id("galena_ore_mercury"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Tags.Items.ORES_EMERALD), WATER, new ItemStack(Items.EMERALD), Smalldust.EMERALD.getItemStack(6), Smalldust.OLIVINE.getItemStack(2)).build(finishedRecipeConsumer, id("emerald_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.RUBY.getTag()), WATER, Miscellaneous.RUBY.getItemStack(), Smalldust.RUBY.getItemStack(6), Smalldust.RED_GARNET.getItemStack(2)).build(finishedRecipeConsumer, id("ruby_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.SAPPHIRE.getTag()), WATER, Miscellaneous.SAPPHIRE.getItemStack(), Smalldust.SAPPHIRE.getItemStack(6), Smalldust.GREEN_SAPPHIRE.getItemStack(2)).build(finishedRecipeConsumer, id("sapphire_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ore("green_sapphire")), WATER, Miscellaneous.GREEN_SAPPHIRE.getItemStack(), Smalldust.GREEN_SAPPHIRE.getItemStack(6), Smalldust.SAPPHIRE.getItemStack(2)).build(finishedRecipeConsumer, id("green_sapphire_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.OLIVINE.getTag()), WATER, Miscellaneous.OLIVINE.getItemStack(), Smalldust.OLIVINE.getItemStack(6), Smalldust.EMERALD.getItemStack(2)).build(finishedRecipeConsumer, id("olivine_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ore("zinc")), WATER, Dust.ZINC.getItemStack(2), Smalldust.TIN.getItemStack(2)).build(finishedRecipeConsumer, id("zinc_ore_water"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ore("zinc")), SODIUM_PERSULFATE, Dust.ZINC.getItemStack(3), Smalldust.TIN.getItemStack(2)).build(finishedRecipeConsumer, id("zinc_ore_sodium_persulfate"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ore("antimony")), WATER, Dust.ANTIMONY.getItemStack(2), Smalldust.ZINC.getItemStack(2), Smalldust.IRON.getItemStack(2)).build(finishedRecipeConsumer, id("antimony_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.SHELDONITE.getTag()), WATER, Dust.PLATINUM.getItemStack(2), Dust.NICKEL.getItemStack(), Nugget.IRIDIUM.getItemStack(2)).build(finishedRecipeConsumer, id("sheldonite_ore_water"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.SHELDONITE.getTag()), MERCURY, Dust.PLATINUM.getItemStack(3), Dust.NICKEL.getItemStack(), Nugget.IRIDIUM.getItemStack(2)).build(finishedRecipeConsumer, id("sheldonite_ore_mercury"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ore("platinum")), WATER, Dust.PLATINUM.getItemStack(2), Nugget.IRIDIUM.getItemStack(2), Smalldust.NICKEL.getItemStack()).build(finishedRecipeConsumer, id("platinum_ore_water"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ore("platinum")), MERCURY, Dust.PLATINUM.getItemStack(3), Nugget.IRIDIUM.getItemStack(2), Smalldust.NICKEL.getItemStack()).build(finishedRecipeConsumer, id("platinum_ore_mercury"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ore("nickel")), WATER, Dust.NICKEL.getItemStack(3), Smalldust.PLATINUM.getItemStack(), Smalldust.COPPER.getItemStack()).build(finishedRecipeConsumer, id("nickel_ore_water"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ore("nickel")), MERCURY, Dust.NICKEL.getItemStack(3), Dust.PLATINUM.getItemStack()).build(finishedRecipeConsumer, id("nickel_ore_mercury"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.PYRITE.getTag()), WATER, Dust.PYRITE.getItemStack(5), Dust.SULFUR.getItemStack(2)).build(finishedRecipeConsumer, id("pyrite_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.CINNABAR.getTag()), WATER, Dust.CINNABAR.getItemStack(5), Smalldust.REDSTONE.getItemStack(2), Smalldust.GLOWSTONE.getItemStack()).build(finishedRecipeConsumer, id("cinnabar_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.SPHALERITE.getTag()), WATER, Dust.SPHALERITE.getItemStack(5), Dust.ZINC.getItemStack(), Smalldust.YELLOW_GARNET.getItemStack()).build(finishedRecipeConsumer, id("sphalerite_ore_water"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.SPHALERITE.getTag()), SODIUM_PERSULFATE, Dust.SPHALERITE.getItemStack(5), Dust.ZINC.getItemStack(3), Smalldust.YELLOW_GARNET.getItemStack()).build(finishedRecipeConsumer, id("sphalerite_ore_sodium_persulfate"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ore("aluminium")), WATER, Dust.ALUMINIUM.getItemStack(3), Smalldust.TITANIUM.getItemStack()).build(finishedRecipeConsumer, id("aluminium_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ore("steel")), WATER, Dust.STEEL.getItemStack(2), Smalldust.NICKEL.getItemStack(2)).build(finishedRecipeConsumer, id("steel_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ore("titanium")), WATER, Dust.TITANIUM.getItemStack(2), Smalldust.ALUMINIUM.getItemStack(2)).build(finishedRecipeConsumer, id("titanium_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ore("chromium")), WATER, Dust.CHROME.getItemStack(2), Smalldust.RUBY.getItemStack()).build(finishedRecipeConsumer, id("chromium_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ore("electrum")), WATER, Dust.ELECTRUM.getItemStack(2), Smalldust.GOLD.getItemStack(), Smalldust.SILVER.getItemStack()).build(finishedRecipeConsumer, id("electrum_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.TUNGSTATE.getTag()), WATER, Dust.TUNGSTEN.getItemStack(2), Smalldust.IRON.getItemStack(3), Smalldust.MANGANESE.getItemStack(3)).build(finishedRecipeConsumer, id("tungstate_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.BAUXITE.getTag()), WATER, Dust.BAUXITE.getItemStack(4), Dust.ALUMINIUM.getItemStack()).build(finishedRecipeConsumer, id("bauxite_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ore("sulfur")), WATER, Dust.SULFUR.getItemStack(10)).build(finishedRecipeConsumer, id("sulfur_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ore("saltpeter")), WATER, Dust.SALTPETER.getItemStack(7)).build(finishedRecipeConsumer, id("saltpeter_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ore("magnesium")), WATER, Dust.MAGNESIUM.getItemStack(2), Smalldust.IRON.getItemStack(2), Smalldust.NICKEL.getItemStack()).build(finishedRecipeConsumer, id("magnesium_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ore("manganese")), WATER, Dust.MANGANESE.getItemStack(2), Smalldust.IRON.getItemStack(2), Smalldust.NICKEL.getItemStack()).build(finishedRecipeConsumer, id("manganese_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ore("uranium")), WATER, Dust.URANIUM.getItemStack(2), Smalldust.URANIUM.getItemStack(2), Dust.THORIUM.getItemStack()).build(finishedRecipeConsumer, id("uranium_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ore("thorium")), WATER, Dust.THORIUM.getItemStack(2), Smalldust.URANIUM.getItemStack()).build(finishedRecipeConsumer, id("thorium_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ore("plutonium")), WATER, Dust.PLUTONIUM.getItemStack(2), Dust.URANIUM.getItemStack()).build(finishedRecipeConsumer, id("plutonium_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Tags.Items.ORES_IRON), WATER, Dust.IRON.getItemStack(2), Smalldust.TIN.getItemStack(), Smalldust.NICKEL.getItemStack()).build(finishedRecipeConsumer, id("iron_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Tags.Items.ORES_GOLD), WATER, Dust.GOLD.getItemStack(2), Smalldust.COPPER.getItemStack(), Smalldust.NICKEL.getItemStack()).build(finishedRecipeConsumer, id("gold_ore_water"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Tags.Items.ORES_GOLD), MERCURY, Dust.GOLD.getItemStack(3), Smalldust.COPPER.getItemStack(), Smalldust.NICKEL.getItemStack()).build(finishedRecipeConsumer, id("gold_ore_mercury"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Tags.Items.ORES_GOLD), SODIUM_PERSULFATE, Dust.GOLD.getItemStack(2), Dust.COPPER.getItemStack(), Smalldust.NICKEL.getItemStack()).build(finishedRecipeConsumer, id("gold_ore_sodium_persulfate"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Tags.Items.ORES_COAL), WATER, new ItemStack(Items.COAL), Dust.COAL.getItemStack(), Smalldust.THORIUM.getItemStack()).build(finishedRecipeConsumer, id("coal_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Tags.Items.ORES_DIAMOND), WATER, new ItemStack(Items.DIAMOND), Smalldust.DIAMOND.getItemStack(6), Dust.COAL.getItemStack()).build(finishedRecipeConsumer, id("diamond_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Tags.Items.ORES_COPPER), WATER, Dust.COPPER.getItemStack(2), Smalldust.GOLD.getItemStack(), Smalldust.NICKEL.getItemStack()).build(finishedRecipeConsumer, id("copper_ore_water"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Tags.Items.ORES_COPPER), SODIUM_PERSULFATE, Dust.COPPER.getItemStack(3), Smalldust.GOLD.getItemStack(), Smalldust.NICKEL.getItemStack()).build(finishedRecipeConsumer, id("copper_ore_sodium_persulfate"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Tags.Items.ORES_COPPER), MERCURY, Dust.COPPER.getItemStack(2), Dust.GOLD.getItemStack()).build(finishedRecipeConsumer, id("copper_ore_mercury"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ore("tin")), WATER, Dust.TIN.getItemStack(2), Smalldust.IRON.getItemStack(), Smalldust.ZINC.getItemStack()).build(finishedRecipeConsumer, id("tin_ore_water"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ore("tin")), SODIUM_PERSULFATE, Dust.TIN.getItemStack(2), Smalldust.IRON.getItemStack(), Dust.ZINC.getItemStack()).build(finishedRecipeConsumer, id("tin_ore_sodium_persulfate"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.CASSITERITE.getTag()), WATER, Dust.TIN.getItemStack(5)).build(finishedRecipeConsumer, id("cassiterite_ore"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.TETRAHEDRITE.getTag()), WATER, Dust.COPPER.getItemStack(2), Smalldust.ZINC.getItemStack(2), Smalldust.ANTIMONY.getItemStack()).build(finishedRecipeConsumer, id("tetrahedrite_ore_water"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.TETRAHEDRITE.getTag()), SODIUM_PERSULFATE, Dust.COPPER.getItemStack(3), Smalldust.ZINC.getItemStack(2), Smalldust.ANTIMONY.getItemStack()).build(finishedRecipeConsumer, id("tetrahedrite_ore_sodium_persulfate"));

        buildOtherModRecipes(finishedRecipeConsumer);
    }

    private void buildOtherModRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        // Forestry
//        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "apatite")), WATER, new ItemStack(GEM_APATITE, 12), Dust.PHOSPHORUS.getItemStack())
//            .build(finishedRecipeConsumer, id("apatite_ore"));

        // MFFS
//        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "monazite")), WATER, new ItemStack(MFFS_FORCICIUM), Dust.THORIUM.getItemStack(2))
//            .build(finishedRecipeConsumer, id("monazite_ore"));
    }

    public static RecipeName id(String name) {
        return RecipeName.common(Reference.MODID, "industrial_grinder", name);
    }
}
