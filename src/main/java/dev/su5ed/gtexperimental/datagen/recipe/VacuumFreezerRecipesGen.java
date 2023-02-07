package dev.su5ed.gtexperimental.datagen.recipe;

import dev.ftb.mods.ftbic.item.FTBICItems;
import dev.su5ed.gtexperimental.compat.DamagedIC2ReactorComponentIngredient;
import dev.su5ed.gtexperimental.object.Ingot;
import dev.su5ed.gtexperimental.object.ModFluid;
import dev.su5ed.gtexperimental.object.NuclearCoolantPack;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.util.GtUtil;
import ic2.core.ref.Ic2Items;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.api.Reference.location;
import static dev.su5ed.gtexperimental.datagen.RecipeGen.FTBIC_LOADED;
import static dev.su5ed.gtexperimental.datagen.RecipeGen.IC2_LOADED;
import static dev.su5ed.gtexperimental.recipe.gen.ModRecipeBuilders.vacuumFreezerFluid;
import static dev.su5ed.gtexperimental.recipe.gen.ModRecipeBuilders.vacuumFreezerSolid;

public final class VacuumFreezerRecipesGen implements ModRecipeProvider {
    public static final VacuumFreezerRecipesGen INSTANCE = new VacuumFreezerRecipesGen();

    private VacuumFreezerRecipesGen() {}

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        vacuumFreezerFluid(ModRecipeIngredientTypes.FLUID.of(ModFluid.HELIUM_PLASMA.getTag()), ModFluid.HELIUM.getBuckets(1), 100)
            .build(finishedRecipeConsumer, fluidId("helium"));

        componentCooling(NuclearCoolantPack.HELIUM_60K, 700, finishedRecipeConsumer);
        componentCooling(NuclearCoolantPack.HELIUM_180K, 2000, finishedRecipeConsumer);
        componentCooling(NuclearCoolantPack.HELIUM_360K, 3900, finishedRecipeConsumer);
        componentCooling(NuclearCoolantPack.NAK_60K, 500, finishedRecipeConsumer);
        componentCooling(NuclearCoolantPack.NAK_180K, 1500, finishedRecipeConsumer);
        componentCooling(NuclearCoolantPack.NAK_360K, 3000, finishedRecipeConsumer);

        vacuumFreezerSolid(ModRecipeIngredientTypes.ITEM.of(Ingot.HOT_TUNGSTEN_STEEL), Ingot.TUNGSTEN_STEEL.getItemStack(), 450)
            .build(finishedRecipeConsumer, solidId("tungsten_steel_ingot"));

        buildOtherModRecipes(finishedRecipeConsumer);
    }

    private void buildOtherModRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        // IC2
        vacuumFreezerSolid(DamagedIC2ReactorComponentIngredient.recipeIngredient(Ic2Items.REACTOR_COOLANT_CELL), new ItemStack(Ic2Items.REACTOR_COOLANT_CELL), 100)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, solidId("ic2/reactor_coolant_cell"));
        vacuumFreezerSolid(DamagedIC2ReactorComponentIngredient.recipeIngredient(Ic2Items.TRIPLE_REACTOR_COOLANT_CELL), new ItemStack(Ic2Items.TRIPLE_REACTOR_COOLANT_CELL), 300)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, solidId("ic2/triple_reactor_coolant_cell"));
        vacuumFreezerSolid(DamagedIC2ReactorComponentIngredient.recipeIngredient(Ic2Items.SEXTUPLE_REACTOR_COOLANT_CELL), new ItemStack(Ic2Items.SEXTUPLE_REACTOR_COOLANT_CELL), 600)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, solidId("ic2/sextuple_reactor_coolant_cell"));

        // FTBIC
        vacuumFreezerSolid(ModRecipeIngredientTypes.ITEM.ofDamaged(FTBICItems.SMALL_COOLANT_CELL.get()), new ItemStack(FTBICItems.SMALL_COOLANT_CELL.get()), 100)
            .addConditions(FTBIC_LOADED)
            .build(finishedRecipeConsumer, solidId("ftbic/small_coolant_cell"));
        vacuumFreezerSolid(ModRecipeIngredientTypes.ITEM.ofDamaged(FTBICItems.MEDIUM_COOLANT_CELL.get()), new ItemStack(FTBICItems.MEDIUM_COOLANT_CELL.get()), 300)
            .addConditions(FTBIC_LOADED)
            .build(finishedRecipeConsumer, solidId("ftbic/medium_coolant_cell"));
        vacuumFreezerSolid(ModRecipeIngredientTypes.ITEM.ofDamaged(FTBICItems.LARGE_COOLANT_CELL.get()), new ItemStack(FTBICItems.LARGE_COOLANT_CELL.get()), 600)
            .addConditions(FTBIC_LOADED)
            .build(finishedRecipeConsumer, solidId("ftbic/large_coolant_cell"));
    }

    private static void componentCooling(ItemLike item, int duration, Consumer<FinishedRecipe> finishedRecipeConsumer) {
        vacuumFreezerSolid(ModRecipeIngredientTypes.ITEM.ofDamaged(item), new ItemStack(item), duration)
            .build(finishedRecipeConsumer, solidId(GtUtil.itemName(item)));
    }

    private static ResourceLocation solidId(String name) {
        return location("vacuum_freezer_solid/" + name);
    }

    private static ResourceLocation fluidId(String name) {
        return location("vacuum_freezer_fluid/" + name);
    }
}
