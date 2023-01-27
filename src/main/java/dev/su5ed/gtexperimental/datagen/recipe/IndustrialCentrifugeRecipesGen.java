package dev.su5ed.gtexperimental.datagen.recipe;

import dev.ftb.mods.ftbic.item.FTBICItems;
import dev.ftb.mods.ftbic.world.ResourceElements;
import dev.ftb.mods.ftbic.world.ResourceType;
import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.object.Dust;
import dev.su5ed.gtexperimental.object.Ingot;
import dev.su5ed.gtexperimental.object.ModFluid;
import dev.su5ed.gtexperimental.object.Nugget;
import dev.su5ed.gtexperimental.object.Smalldust;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import ic2.core.ref.Ic2Items;
import io.alwa.mods.myrtrees.common.item.MyrtreesItems;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.api.Reference.location;
import static dev.su5ed.gtexperimental.datagen.RecipeGen.FTBIC_LOADED;
import static dev.su5ed.gtexperimental.datagen.RecipeGen.IC2_LOADED;
import static dev.su5ed.gtexperimental.datagen.RecipeGen.NOT_IC2_LOADED;
import static dev.su5ed.gtexperimental.recipe.gen.ModRecipeBuilders.industrialCentrifuge;
import static dev.su5ed.gtexperimental.util.GtUtil.buckets;

public final class IndustrialCentrifugeRecipesGen implements ModRecipeProvider {
    public static final IndustrialCentrifugeRecipesGen INSTANCE = new IndustrialCentrifugeRecipesGen();

    private IndustrialCentrifugeRecipesGen() {}

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.REDROCK.getTag(), 4), Dust.CALCITE.getItemStack(2), Dust.FLINT.getItemStack(), Dust.CLAY.getItemStack(), 100)
            .build(finishedRecipeConsumer, id("redrock_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.MARBLE.getTag(), 8), Dust.MAGNESIUM.getItemStack(), Dust.CALCITE.getItemStack(7), 1055)
            .build(finishedRecipeConsumer, id("marble_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.BASALT.getTag(), 16), Dust.OLIVINE.getItemStack(), Dust.CALCITE.getItemStack(3), Dust.FLINT.getItemStack(8), Dust.DARK_ASHES.getItemStack(4), 2040)
            .build(finishedRecipeConsumer, id("basalt_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Tags.Items.GEMS_LAPIS), Dust.LAZURITE.getItemStack(3), Smalldust.LAZURITE.getItemStack(), Smalldust.CALCITE.getItemStack(), Smalldust.SODALITE.getItemStack(2), 1500)
            .build(finishedRecipeConsumer, id("gems_lapis"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Tags.Items.DUSTS_REDSTONE, 10), ModFluid.SILICON.getFluidStack(buckets(1)), Dust.PYRITE.getItemStack(5), Dust.RUBY.getItemStack(), ModFluid.MERCURY.getItemStack(3), 7000)
            .build(finishedRecipeConsumer, id("redstone_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.BRASS.getTag()), Smalldust.COPPER.getItemStack(3), Smalldust.ZINC.getItemStack(), 1500)
            .build(finishedRecipeConsumer, id("brass_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(GregTechTags.dust("copper")), Smalldust.GOLD.getItemStack(), Smalldust.NICKEL.getItemStack(), 2400)
            .build(finishedRecipeConsumer, id("copper_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(GregTechTags.dust("gold"), 3), Smalldust.COPPER.getItemStack(), Smalldust.NICKEL.getItemStack(), 2400)
            .build(finishedRecipeConsumer, id("gold_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.NICKEL.getTag(), 3), Smalldust.IRON.getItemStack(), Smalldust.GOLD.getItemStack(), Smalldust.COPPER.getItemStack(), 3450)
            .build(finishedRecipeConsumer, id("nickel_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(GregTechTags.dust("iron"), 2), Smalldust.TIN.getItemStack(), Smalldust.NICKEL.getItemStack(), 1500)
            .build(finishedRecipeConsumer, id("iron_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(GregTechTags.dust("tin"), 2), Smalldust.ZINC.getItemStack(), Smalldust.IRON.getItemStack(), 2100)
            .build(finishedRecipeConsumer, id("tin_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.ZINC.getTag()), Smalldust.TIN.getItemStack(), 1050)
            .build(finishedRecipeConsumer, id("zinc_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(GregTechTags.dust("quicksilver")), ModFluid.MERCURY.getFluidStack(buckets(1)), 100)
            .build(finishedRecipeConsumer, id("quicksilver_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.RED_GARNET.getTag(), 16), Dust.PYROPE.getItemStack(3), Dust.ALMANDINE.getItemStack(5), Dust.SPESSARTINE.getItemStack(8), 3000)
            .build(finishedRecipeConsumer, id("red_garnet_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.YELLOW_GARNET.getTag(), 16), Dust.ANDRADITE.getItemStack(5), Dust.GROSSULAR.getItemStack(8), Dust.UVAROVITE.getItemStack(3), 3500)
            .build(finishedRecipeConsumer, id("yellow_garnet_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.PLATINUM.getTag()), Nugget.IRIDIUM.getItemStack(), Smalldust.NICKEL.getItemStack(), 3000)
            .build(finishedRecipeConsumer, id("platinum_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(GregTechTags.dust("iridium")), Nugget.PLATINUM.getItemStack(), Smalldust.NICKEL.getItemStack(), 3000)
            .build(finishedRecipeConsumer, id("iridium_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.LEAD.getTag(), 2), Smalldust.SILVER.getItemStack(), 2400)
            .build(finishedRecipeConsumer, id("lead_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.SILVER.getTag(), 2), Smalldust.LEAD.getItemStack(), 2400)
            .build(finishedRecipeConsumer, id("silver_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.ENDER_EYE.getTag(), 2), Dust.ENDER_PEARL.getItemStack(), new ItemStack(Items.BLAZE_POWDER), 1850)
            .build(finishedRecipeConsumer, id("ender_eye_dust"));
        // TODO Thermal slag output
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.DARK_ASHES.getTag(), 2), Dust.DARK_ASHES.getItemStack(), Dust.ASHES.getItemStack(), 250)
            .build(finishedRecipeConsumer, id("dark_ashes_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.ENDSTONE.getTag(), 16), ModFluid.HELIUM3.getFluidStack(buckets(1)), ModFluid.WOLFRAMIUM.getFluidStack(buckets(1)), Smalldust.TUNGSTEN.getItemStack(), new ItemStack(Items.SAND, 16), 4800)
            .build(finishedRecipeConsumer, id("endstone_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.ELECTRUM.getTag()), Smalldust.GOLD.getItemStack(2), Smalldust.SILVER.getItemStack(2), 975)
            .build(finishedRecipeConsumer, id("electrum_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.ofFluid(ModFluid.HYDROGEN.getTag(), buckets(4)), ModFluid.DEUTERIUM.getFluidStack(buckets(1)), 3000)
            .build(finishedRecipeConsumer, id("hydrogen"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.ofFluid(ModFluid.HELIUM.getTag(), buckets(16)), ModFluid.HELIUM3.getFluidStack(buckets(1)), 10000)
            .build(finishedRecipeConsumer, id("helium"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.ofFluid(ModFluid.HELIUM3.getTag(), buckets(4)), ModFluid.DEUTERIUM.getFluidStack(buckets(1)), 3000)
            .build(finishedRecipeConsumer, id("helium3"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.ofFluid(ModFluid.SULFUR.getTag(), buckets(1)), Dust.SULFUR.getItemStack(1), 40)
            .build(finishedRecipeConsumer, id("sulfur"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.ofFluid(ModFluid.CALCIUM_CARBONATE.getTag(), buckets(1)), Dust.CALCITE.getItemStack(1), 40)
            .build(finishedRecipeConsumer, id("calcium_carbonate"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(GregTechTags.OIL_SAND, 2), ModFluid.OIL.getFluidStack(buckets(1)), new ItemStack(Items.SAND), 1000)
            .build(finishedRecipeConsumer, id("oil_sand"));
        // TODO Remove Ice cell        
//        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Cell.ICE), new ItemStack(Items.ICE), new ItemStack(Ic2Items.EMPTY_CELL), 40)
//            .build(finishedRecipeConsumer, id("ice_cell"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Items.MAGMA_CREAM), new ItemStack(Items.BLAZE_POWDER), new ItemStack(Items.SLIME_BALL), 500)
            .build(finishedRecipeConsumer, id("magma_cream"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Items.MYCELIUM, 8), new ItemStack(Items.BROWN_MUSHROOM, 2), new ItemStack(Items.RED_MUSHROOM, 2), new ItemStack(Items.CLAY_BALL), new ItemStack(Items.SAND, 4), 1650)
            .build(finishedRecipeConsumer, id("mycelium"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Items.ENCHANTED_GOLDEN_APPLE), ModFluid.METHANE.getFluidStack(buckets(2)), new ItemStack(Items.GOLD_INGOT, 64), 10000)
            .build(finishedRecipeConsumer, id("enchanted_golden_apple"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Items.GOLDEN_APPLE, Items.GOLDEN_CARROT), ModFluid.METHANE.getFluidStack(buckets(1)), new ItemStack(Items.GOLD_NUGGET, 6), 10000)
            .build(finishedRecipeConsumer, id("golden_apple"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(8, Items.GLISTERING_MELON_SLICE, Items.CAKE), ModFluid.METHANE.getFluidStack(buckets(1)), new ItemStack(Items.GOLD_NUGGET, 6), 10000)
            .build(finishedRecipeConsumer, id("glistering_melon_slice"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(32, Items.APPLE, Items.SPIDER_EYE, Items.NETHER_WART), ModFluid.METHANE.getFluidStack(buckets(1)), 5000)
            .build(finishedRecipeConsumer, id("apple"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(GregTechTags.RAW_FOOD, 12), ModFluid.METHANE.getFluidStack(buckets(1)), 5000)
            .build(finishedRecipeConsumer, id("raw_food"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(GregTechTags.COOKED_FOOD, 16), ModFluid.METHANE.getFluidStack(buckets(1)), 5000)
            .build(finishedRecipeConsumer, id("cooked_food"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(64, Items.MELON, Items.COOKIE, Items.BREAD), ModFluid.METHANE.getFluidStack(buckets(1)), 5000)
            .build(finishedRecipeConsumer, id("melon"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(16, Items.PUMPKIN, Items.ROTTEN_FLESH, Items.CARROT, Items.POTATO, Items.MUSHROOM_STEW), ModFluid.METHANE.getFluidStack(buckets(1)), 5000)
            .build(finishedRecipeConsumer, id("pumpkin"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(12, Items.POISONOUS_POTATO, Items.BROWN_MUSHROOM_BLOCK, Items.RED_MUSHROOM_BLOCK), ModFluid.METHANE.getFluidStack(buckets(1)), 5000)
            .build(finishedRecipeConsumer, id("poisonous_potato"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Items.BAKED_POTATO, 24), ModFluid.METHANE.getFluidStack(buckets(1)), 5000)
            .build(finishedRecipeConsumer, id("baked_potato"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.ofFluid(ModFluid.DEUTERIUM.getTag(), buckets(4)), ModFluid.TRITIUM.getFluidStack(buckets(1)), 3000)
            .build(finishedRecipeConsumer, id("deuterium"));

        buildOtherModRecipes(finishedRecipeConsumer);
    }

    private void buildOtherModRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        // IC2
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.INVAR.getTag(), 3), new ItemStack(Ic2Items.IRON_DUST, 2), Dust.NICKEL.getItemStack(), 1000)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/invar_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Tags.Items.DUSTS_GLOWSTONE, 16), new ItemStack(Items.REDSTONE, 8), new ItemStack(Ic2Items.GOLD_DUST, 8), ModFluid.HELIUM.getFluidStack(buckets(1)), 25000)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/glowstone_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.NETHERRACK.getTag(), 16), new ItemStack(Items.REDSTONE), Dust.SULFUR.getItemStack(4), new ItemStack(Ic2Items.COAL_DUST), new ItemStack(Items.GOLD_NUGGET), 2400)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/netherrack_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(GregTechTags.material("logs", "rubber"), 16), new ItemStack(Ic2Items.RESIN, 8), new ItemStack(Ic2Items.PLANT_BALL, 6), ModFluid.METHANE.getFluidStack(buckets(1)), ModFluid.CARBON.getFluidStack(buckets(4)), 5000)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/rubber_log"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Ic2Items.TERRA_WART, 16), ModFluid.METHANE.getFluidStack(buckets(1)), 5000)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/terra_wart"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Items.SOUL_SAND, 16), ModFluid.OIL.getFluidStack(buckets(1)), Dust.SALTPETER.getItemStack(4), new ItemStack(Ic2Items.COAL_DUST), new ItemStack(Items.SAND, 10), 2500)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/soul_sand"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.ofFluid(FluidTags.LAVA, buckets(16)), Ingot.ELECTRUM.getItemStack(), new ItemStack(Items.COPPER_INGOT, 4), Smalldust.TUNGSTEN.getItemStack(), new ItemStack(Ic2Items.TIN_INGOT, 2), 10000)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/lava"));

        // FTBIC
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.INVAR.getTag(), 3), new ItemStack(FTBICItems.getResourceFromType(ResourceElements.IRON, ResourceType.DUST).orElseThrow().get(), 2), Dust.NICKEL.getItemStack(), 1000)
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/invar_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Tags.Items.DUSTS_GLOWSTONE, 16), new ItemStack(Items.REDSTONE, 8), new ItemStack(FTBICItems.getResourceFromType(ResourceElements.GOLD, ResourceType.DUST).orElseThrow().get(), 8), ModFluid.HELIUM.getFluidStack(buckets(1)), 25000)
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/glowstone_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Dust.NETHERRACK.getTag(), 16), new ItemStack(Items.REDSTONE), Dust.SULFUR.getItemStack(4), new ItemStack(FTBICItems.getResourceFromType(ResourceElements.COAL, ResourceType.DUST).orElseThrow().get()), new ItemStack(Items.GOLD_NUGGET), 2400)
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/netherrack_dust"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(GregTechTags.material("logs", "rubber"), 16), new ItemStack(MyrtreesItems.LATEX.get(), 8), new ItemStack(Items.VINE, 6), ModFluid.METHANE.getFluidStack(buckets(1)), ModFluid.CARBON.getFluidStack(buckets(4)), 5000)
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/rubber_log"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(Items.SOUL_SAND, 16), ModFluid.OIL.getFluidStack(buckets(1)), Dust.SALTPETER.getItemStack(4), new ItemStack(FTBICItems.getResourceFromType(ResourceElements.COAL, ResourceType.DUST).orElseThrow().get()), new ItemStack(Items.SAND, 10), 2500)
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/soul_sand"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.ofFluid(FluidTags.LAVA, buckets(16)), Ingot.ELECTRUM.getItemStack(), new ItemStack(Items.COPPER_INGOT, 4), Smalldust.TUNGSTEN.getItemStack(), new ItemStack(FTBICItems.getResourceFromType(ResourceElements.TIN, ResourceType.INGOT).orElseThrow().get(), 2), 10000)
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/lava"));
    }

    private static ResourceLocation id(String name) {
        return location("industrial_centrifuge/" + name);
    }
}