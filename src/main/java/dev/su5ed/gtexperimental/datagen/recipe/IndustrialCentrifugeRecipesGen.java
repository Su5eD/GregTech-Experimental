package dev.su5ed.gtexperimental.datagen.recipe;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.object.Dust;
import dev.su5ed.gtexperimental.object.Ingot;
import dev.su5ed.gtexperimental.object.ModFluid;
import dev.su5ed.gtexperimental.object.Nugget;
import dev.su5ed.gtexperimental.object.Smalldust;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipeName;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.recipe.gen.ModRecipeBuilders.industrialCentrifuge;
import static dev.su5ed.gtexperimental.util.GtUtil.buckets;

public final class IndustrialCentrifugeRecipesGen implements ModRecipeProvider {
    public static final IndustrialCentrifugeRecipesGen INSTANCE = new IndustrialCentrifugeRecipesGen();

    private IndustrialCentrifugeRecipesGen() {}

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.REDROCK.getTag(), 4), Dust.CALCITE.getItemStack(2), Dust.FLINT.getItemStack(), Dust.CLAY.getItemStack(), 100).build(finishedRecipeConsumer, id("redrock_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.MARBLE.getTag(), 8), Dust.MAGNESIUM.getItemStack(), Dust.CALCITE.getItemStack(7), 1055).build(finishedRecipeConsumer, id("marble_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.BASALT.getTag(), 16), Dust.OLIVINE.getItemStack(), Dust.CALCITE.getItemStack(3), Dust.FLINT.getItemStack(8), Dust.DARK_ASHES.getItemStack(4), 2040).build(finishedRecipeConsumer, id("basalt_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Tags.Items.GEMS_LAPIS), Dust.LAZURITE.getItemStack(3), Smalldust.LAZURITE.getItemStack(), Smalldust.CALCITE.getItemStack(), Smalldust.SODALITE.getItemStack(2), 1500).build(finishedRecipeConsumer, id("gems_lapis"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Tags.Items.DUSTS_REDSTONE, 10), ModFluid.SILICON.getBuckets(1), Dust.PYRITE.getItemStack(5), Dust.RUBY.getItemStack(), ModFluid.MERCURY.getItemStack(3), 7000).build(finishedRecipeConsumer, id("redstone_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.BRASS.getTag()), Smalldust.COPPER.getItemStack(3), Smalldust.ZINC.getItemStack(), 1500).build(finishedRecipeConsumer, id("brass_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.COPPER.getTag()), Smalldust.GOLD.getItemStack(), Smalldust.NICKEL.getItemStack(), 2400).build(finishedRecipeConsumer, id("copper_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.GOLD.getTag(), 3), Smalldust.COPPER.getItemStack(), Smalldust.NICKEL.getItemStack(), 2400).build(finishedRecipeConsumer, id("gold_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.NICKEL.getTag(), 3), Smalldust.IRON.getItemStack(), Smalldust.GOLD.getItemStack(), Smalldust.COPPER.getItemStack(), 3450).build(finishedRecipeConsumer, id("nickel_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.IRON.getTag(), 2), Smalldust.TIN.getItemStack(), Smalldust.NICKEL.getItemStack(), 1500).build(finishedRecipeConsumer, id("iron_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.TIN.getTag(), 2), Smalldust.ZINC.getItemStack(), Smalldust.IRON.getItemStack(), 2100).build(finishedRecipeConsumer, id("tin_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.ZINC.getTag()), Smalldust.TIN.getItemStack(), 1050).build(finishedRecipeConsumer, id("zinc_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(GregTechTags.dust("quicksilver")), ModFluid.MERCURY.getBuckets(1), 100).build(finishedRecipeConsumer, id("quicksilver_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.RED_GARNET.getTag(), 16), Dust.PYROPE.getItemStack(3), Dust.ALMANDINE.getItemStack(5), Dust.SPESSARTINE.getItemStack(8), 3000).build(finishedRecipeConsumer, id("red_garnet_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.YELLOW_GARNET.getTag(), 16), Dust.ANDRADITE.getItemStack(5), Dust.GROSSULAR.getItemStack(8), Dust.UVAROVITE.getItemStack(3), 3500).build(finishedRecipeConsumer, id("yellow_garnet_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.PLATINUM.getTag()), Nugget.IRIDIUM.getItemStack(), Smalldust.NICKEL.getItemStack(), 3000).build(finishedRecipeConsumer, id("platinum_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(GregTechTags.dust("iridium")), Nugget.PLATINUM.getItemStack(), Smalldust.NICKEL.getItemStack(), 3000).build(finishedRecipeConsumer, id("iridium_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.LEAD.getTag(), 2), Smalldust.SILVER.getItemStack(), 2400).build(finishedRecipeConsumer, id("lead_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.SILVER.getTag(), 2), Smalldust.LEAD.getItemStack(), 2400).build(finishedRecipeConsumer, id("silver_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.ENDER_EYE.getTag(), 2), Dust.ENDER_PEARL.getItemStack(), new ItemStack(Items.BLAZE_POWDER), 1850).build(finishedRecipeConsumer, id("ender_eye_dust"));
        // TODO Thermal slag output
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.DARK_ASHES.getTag(), 2), Dust.DARK_ASHES.getItemStack(), Dust.ASHES.getItemStack(), 250).build(finishedRecipeConsumer, id("dark_ashes_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.ENDSTONE.getTag(), 16), ModFluid.HELIUM3.getBuckets(1), ModFluid.WOLFRAMIUM.getBuckets(1), Smalldust.TUNGSTEN.getItemStack(), new ItemStack(Items.SAND, 16), 4800).build(finishedRecipeConsumer, id("endstone_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.ELECTRUM.getTag()), Smalldust.GOLD.getItemStack(2), Smalldust.SILVER.getItemStack(2), 975).build(finishedRecipeConsumer, id("electrum_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.ofFluid(ModFluid.HYDROGEN.getTag(), buckets(4)), ModFluid.DEUTERIUM.getBuckets(1), 3000).build(finishedRecipeConsumer, id("hydrogen"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.ofFluid(ModFluid.HELIUM.getTag(), buckets(16)), ModFluid.HELIUM3.getBuckets(1), 10000).build(finishedRecipeConsumer, id("helium"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.ofFluid(ModFluid.HELIUM3.getTag(), buckets(4)), ModFluid.DEUTERIUM.getBuckets(1), 3000).build(finishedRecipeConsumer, id("helium3"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.ofFluid(ModFluid.SULFUR.getTag(), buckets(1)), Dust.SULFUR.getItemStack(1), 40).build(finishedRecipeConsumer, id("sulfur"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.ofFluid(ModFluid.CALCIUM_CARBONATE.getTag(), buckets(1)), Dust.CALCITE.getItemStack(1), 40).build(finishedRecipeConsumer, id("calcium_carbonate"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(GregTechTags.OIL_SAND, 2), ModFluid.OIL.getBuckets(1), new ItemStack(Items.SAND), 1000).build(finishedRecipeConsumer, id("oil_sand"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Items.MAGMA_CREAM), new ItemStack(Items.BLAZE_POWDER), new ItemStack(Items.SLIME_BALL), 500).build(finishedRecipeConsumer, id("magma_cream"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Items.MYCELIUM, 8), new ItemStack(Items.BROWN_MUSHROOM, 2), new ItemStack(Items.RED_MUSHROOM, 2), new ItemStack(Items.CLAY_BALL), new ItemStack(Items.SAND, 4), 1650).build(finishedRecipeConsumer, id("mycelium"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Items.ENCHANTED_GOLDEN_APPLE), ModFluid.METHANE.getBuckets(2), new ItemStack(Items.GOLD_INGOT, 64), 10000).build(finishedRecipeConsumer, id("enchanted_golden_apple"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Items.GOLDEN_APPLE, Items.GOLDEN_CARROT), ModFluid.METHANE.getBuckets(1), new ItemStack(Items.GOLD_NUGGET, 6), 10000).build(finishedRecipeConsumer, id("golden_apple"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(8, Items.GLISTERING_MELON_SLICE, Items.CAKE), ModFluid.METHANE.getBuckets(1), new ItemStack(Items.GOLD_NUGGET, 6), 10000).build(finishedRecipeConsumer, id("glistering_melon_slice"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(32, Items.APPLE, Items.SPIDER_EYE, Items.NETHER_WART), ModFluid.METHANE.getBuckets(1), 5000).build(finishedRecipeConsumer, id("apple"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(GregTechTags.RAW_FOOD, 12), ModFluid.METHANE.getBuckets(1), 5000).build(finishedRecipeConsumer, id("raw_food"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(GregTechTags.COOKED_FOOD, 16), ModFluid.METHANE.getBuckets(1), 5000).build(finishedRecipeConsumer, id("cooked_food"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(64, Items.MELON, Items.COOKIE, Items.BREAD), ModFluid.METHANE.getBuckets(1), 5000).build(finishedRecipeConsumer, id("melon"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(16, Items.PUMPKIN, Items.ROTTEN_FLESH, Items.CARROT, Items.POTATO, Items.MUSHROOM_STEW), ModFluid.METHANE.getBuckets(1), 5000).build(finishedRecipeConsumer, id("pumpkin"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(12, Items.POISONOUS_POTATO, Items.BROWN_MUSHROOM_BLOCK, Items.RED_MUSHROOM_BLOCK), ModFluid.METHANE.getBuckets(1), 5000).build(finishedRecipeConsumer, id("poisonous_potato"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Items.BAKED_POTATO, 24), ModFluid.METHANE.getBuckets(1), 5000).build(finishedRecipeConsumer, id("baked_potato"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.ofFluid(ModFluid.DEUTERIUM.getTag(), buckets(4)), ModFluid.TRITIUM.getBuckets(1), 3000).build(finishedRecipeConsumer, id("deuterium"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.INVAR.getTag(), 3), Dust.IRON.getItemStack(2), Dust.NICKEL.getItemStack(), 1000).build(finishedRecipeConsumer, id("invar_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Tags.Items.DUSTS_GLOWSTONE, 16), new ItemStack(Items.REDSTONE, 8), Dust.GOLD.getItemStack(8), ModFluid.HELIUM.getBuckets(1), 25000).build(finishedRecipeConsumer, id("glowstone_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.NETHERRACK.getTag(), 16), new ItemStack(Items.REDSTONE), Dust.SULFUR.getItemStack(4), Dust.COAL.getItemStack(), new ItemStack(Items.GOLD_NUGGET), 2400).build(finishedRecipeConsumer, id("netherrack_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Items.SOUL_SAND, 16), ModFluid.OIL.getBuckets(1), Dust.SALTPETER.getItemStack(4), Dust.COAL.getItemStack(), new ItemStack(Items.SAND, 10), 2500).build(finishedRecipeConsumer, id("soul_sand"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.ofFluid(FluidTags.LAVA, buckets(16)), Ingot.ELECTRUM.getItemStack(), new ItemStack(Items.COPPER_INGOT, 4), Smalldust.TUNGSTEN.getItemStack(), Ingot.TIN.getItemStack(2), 10000).build(finishedRecipeConsumer, id("lava"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Tags.Items.SEEDS, 64), ModFluid.SEED_OIL.getBuckets(1), 200).build(finishedRecipeConsumer, id("seeds"));
    }

    private static RecipeName id(String name) {
        return RecipeName.common(Reference.MODID, "industrial_centrifuge", name);
    }
}
