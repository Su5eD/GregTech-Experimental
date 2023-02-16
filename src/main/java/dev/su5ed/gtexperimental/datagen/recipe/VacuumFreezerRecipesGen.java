package dev.su5ed.gtexperimental.datagen.recipe;

import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.object.Ingot;
import dev.su5ed.gtexperimental.object.ModFluid;
import dev.su5ed.gtexperimental.object.NuclearCoolantPack;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipeName;
import dev.su5ed.gtexperimental.util.GtUtil;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.recipe.gen.ModRecipeBuilders.vacuumFreezerFluid;
import static dev.su5ed.gtexperimental.recipe.gen.ModRecipeBuilders.vacuumFreezerSolid;

public final class VacuumFreezerRecipesGen implements ModRecipeProvider {
    public static final VacuumFreezerRecipesGen INSTANCE = new VacuumFreezerRecipesGen();

    private VacuumFreezerRecipesGen() {}

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        vacuumFreezerFluid(ModRecipeIngredientTypes.FLUID.of(ModFluid.HELIUM_PLASMA.getTag()), ModFluid.HELIUM.getBuckets(1), 100).build(finishedRecipeConsumer, fluidId("helium"));
        componentCooling(NuclearCoolantPack.HELIUM_60K, 700, finishedRecipeConsumer);
        componentCooling(NuclearCoolantPack.HELIUM_180K, 2000, finishedRecipeConsumer);
        componentCooling(NuclearCoolantPack.HELIUM_360K, 3900, finishedRecipeConsumer);
        componentCooling(NuclearCoolantPack.NAK_60K, 500, finishedRecipeConsumer);
        componentCooling(NuclearCoolantPack.NAK_180K, 1500, finishedRecipeConsumer);
        componentCooling(NuclearCoolantPack.NAK_360K, 3000, finishedRecipeConsumer);
        vacuumFreezerSolid(ModRecipeIngredientTypes.ITEM.of(Ingot.HOT_TUNGSTEN_STEEL), Ingot.TUNGSTEN_STEEL.getItemStack(), 450).build(finishedRecipeConsumer, solidId("tungsten_steel_ingot"));
    }

    private static void componentCooling(ItemLike item, int duration, Consumer<FinishedRecipe> finishedRecipeConsumer) {
        vacuumFreezerSolid(ModRecipeIngredientTypes.ITEM.ofDamaged(item), new ItemStack(item), duration)
            .build(finishedRecipeConsumer, solidId(GtUtil.itemName(item)));
    }

    private static RecipeName solidId(String name) {
        return RecipeName.common(Reference.MODID, "vacuum_freezer_solid", name);
    }

    private static RecipeName fluidId(String name) {
        return RecipeName.common(Reference.MODID, "vacuum_freezer_fluid", name);
    }
}
