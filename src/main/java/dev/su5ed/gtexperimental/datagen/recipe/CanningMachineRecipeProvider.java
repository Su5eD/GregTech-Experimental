package dev.su5ed.gtexperimental.datagen.recipe;

import dev.ftb.mods.ftbic.item.FTBICItems;
import dev.ftb.mods.ftbic.item.FluidCellItem;
import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.compat.ModHandler;
import dev.su5ed.gtexperimental.object.ColorSpray;
import dev.su5ed.gtexperimental.object.Dust;
import dev.su5ed.gtexperimental.object.Ingot;
import dev.su5ed.gtexperimental.object.ModFluid;
import dev.su5ed.gtexperimental.object.NuclearFuelRod;
import dev.su5ed.gtexperimental.object.Tool;
import dev.su5ed.gtexperimental.recipe.gen.ModRecipeBuilders;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipeName;
import dev.su5ed.gtexperimental.util.FluidProvider;
import dev.su5ed.gtexperimental.util.ItemProvider;
import ic2.core.ref.Ic2Items;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidType;

import java.util.List;
import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.datagen.RecipeGen.FTBIC_LOADED;
import static dev.su5ed.gtexperimental.datagen.RecipeGen.IC2_LOADED;
import static dev.su5ed.gtexperimental.recipe.gen.ModRecipeBuilders.canningMachine;

public final class CanningMachineRecipeProvider implements ModRecipeProvider {
    public static final CanningMachineRecipeProvider INSTANCE = new CanningMachineRecipeProvider();

    private CanningMachineRecipeProvider() {}

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        fluidCellFilling(ModRecipeIngredientTypes.ITEM.of(Dust.TUNGSTEN.getTag()), ModFluid.WOLFRAMIUM, finishedRecipeConsumer);
        fluidCellFilling(ModRecipeIngredientTypes.ITEM.of(Dust.CALCITE.getTag()), ModFluid.CALCIUM_CARBONATE, finishedRecipeConsumer);
        cellFilling(ModRecipeIngredientTypes.ITEM.of(Ingot.PLUTONIUM.getTag()), NuclearFuelRod.PLUTONIUM, finishedRecipeConsumer);
        cellFilling(ModRecipeIngredientTypes.ITEM.of(Ingot.THORIUM.getTag()), NuclearFuelRod.THORIUM, finishedRecipeConsumer);
        canningMachine(ModRecipeIngredientTypes.ITEM.ofFluid(FluidTags.WATER, 16 * FluidType.BUCKET_VOLUME), ModRecipeIngredientTypes.ITEM.of(GregTechTags.CRAFTING_SPRAY_CAN), Tool.HYDRATION_SPRAY.getItemStack(), 1600, 2).build(finishedRecipeConsumer, id("hydration_spray"));
        canningMachine(ModRecipeIngredientTypes.ITEM.of(Tags.Items.SAND, 16), ModRecipeIngredientTypes.ITEM.of(GregTechTags.CRAFTING_SPRAY_CAN), Tool.HARDENER_SPRAY.getItemStack(), 1600, 2).build(finishedRecipeConsumer, id("hardener_spray"));
        canningMachine(ModRecipeIngredientTypes.ITEM.ofFluid(ModFluid.NITROGEN, 16 * FluidType.BUCKET_VOLUME), ModRecipeIngredientTypes.ITEM.of(GregTechTags.CRAFTING_SPRAY_CAN), Tool.ICE_SPRAY.getItemStack(), new ItemStack(Ic2Items.EMPTY_CELL, 16), 1600, 2).build(finishedRecipeConsumer, id("ice_spray"));
        for (ColorSpray value : ColorSpray.values()) {
            canningMachine(ModRecipeIngredientTypes.ITEM.of(value.getColor().getTag(), 16), ModRecipeIngredientTypes.ITEM.of(GregTechTags.CRAFTING_SPRAY_CAN), value.getItemStack(), 800, 1).build(finishedRecipeConsumer, id(value.getRegistryName()));
        }
        canningMachine(ModRecipeIngredientTypes.ITEM.of(Items.POISONOUS_POTATO, 16), ModRecipeIngredientTypes.ITEM.of(GregTechTags.CRAFTING_SPRAY_CAN), Tool.BUG_SPRAY.getItemStack(), 800, 1).build(finishedRecipeConsumer, id("bug_spray"));

        buildOtherModRecipes(finishedRecipeConsumer);
    }

    private void buildOtherModRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        cannedFood(ModRecipeIngredientTypes.ITEM.of(Items.SPIDER_EYE, Items.POISONOUS_POTATO, Items.COD, Items.SALMON, Items.TROPICAL_FISH, Items.COOKIE, Items.MELON, Items.CHICKEN, Items.POTATO), 1, finishedRecipeConsumer, "filled_tin_can_from_spider_eye");
        cannedFood(ModRecipeIngredientTypes.ITEM.of(Items.ROTTEN_FLESH, Items.APPLE, Items.PORKCHOP, Items.GOLDEN_APPLE, Items.BEEF, Items.CARROT), 2, finishedRecipeConsumer, "filled_tin_can_from_rotten_flesh");
        cannedFood(ModRecipeIngredientTypes.ITEM.of(Items.BREAD, Items.COOKED_COD, Items.COOKED_SALMON, Items.COOKED_CHICKEN, Items.BAKED_POTATO, Items.GOLDEN_CARROT), 3, finishedRecipeConsumer, "filled_tin_can_from_bread");
        cannedFood(ModRecipeIngredientTypes.ITEM.of(Items.MUSHROOM_STEW, Items.BEETROOT_SOUP), new ItemStack(Items.BOWL), 3, finishedRecipeConsumer, "filled_tin_can_from_soup");
        cannedFood(ModRecipeIngredientTypes.ITEM.of(Items.COOKED_PORKCHOP, Items.COOKED_BEEF, Items.PUMPKIN_PIE), 4, finishedRecipeConsumer, "filled_tin_can_from_cooked");
        cannedFood(ModRecipeIngredientTypes.ITEM.of(Items.RABBIT_STEW), new ItemStack(Items.BOWL), 5, finishedRecipeConsumer, "filled_tin_can_from_stew");
        cannedFood(ModRecipeIngredientTypes.ITEM.of(Items.CAKE), 6, finishedRecipeConsumer, "filled_tin_can_from_cake");

        // IC2
        canningMachine(ModRecipeIngredientTypes.ITEM.of(Ic2Items.PELLET, 16), ModRecipeIngredientTypes.ITEM.of(GregTechTags.CRAFTING_SPRAY_CAN), Tool.FOAM_SPRAY.getItemStack(), 1600, 2)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("foam_spray"));
    }

    private static void cannedFood(RecipeIngredient<ItemStack> input, int canCount, Consumer<FinishedRecipe> finishedRecipeConsumer, String name) {
        cannedFood(input, ItemStack.EMPTY, canCount, finishedRecipeConsumer, name);
    }

    private static void cannedFood(RecipeIngredient<ItemStack> input, ItemStack output, int canCount, Consumer<FinishedRecipe> finishedRecipeConsumer, String name) {
        int duration = canCount * 100;
        ItemStack ic2Can = new ItemStack(Ic2Items.FILLED_TIN_CAN, canCount);
        canningMachine(List.of(input, ModRecipeIngredientTypes.ITEM.of(Ic2Items.TIN_CAN, canCount)), output.isEmpty() ? List.of(ic2Can) : List.of(ic2Can, output), duration, 1)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id(ModHandler.IC2_MODID + "/" + name));

        ItemStack ftbicCan = new ItemStack(FTBICItems.CANNED_FOOD.get(), canCount);
        canningMachine(List.of(input, ModRecipeIngredientTypes.ITEM.of(FTBICItems.EMPTY_CAN.item.get(), canCount)), output.isEmpty() ? List.of(ftbicCan) : List.of(ftbicCan, output), duration, 1)
            .addConditions(FTBIC_LOADED)
            .build(finishedRecipeConsumer, id(ModHandler.FTBIC_MODID + "/" + name));
    }

    private static void fluidCellFilling(RecipeIngredient<ItemStack> input, FluidProvider output, Consumer<FinishedRecipe> finishedRecipeConsumer) {
        fluidCellFilling(input, output.getSourceFluid(), output.getFluidRegistryName(), finishedRecipeConsumer);
    }

    private static void fluidCellFilling(RecipeIngredient<ItemStack> input, Fluid output, String fluidName, Consumer<FinishedRecipe> finishedRecipeConsumer) {
        ItemStack cell = FluidCellItem.setFluid(new ItemStack(FTBICItems.FLUID_CELL.get()), output);
        canningMachine(input, ModRecipeIngredientTypes.ITEM.of(GregTechTags.EMPTY_FLUID_CELL), cell, 100, 1)
            .addConditions(FTBIC_LOADED)
            .build(finishedRecipeConsumer, id(fluidName + "_fluid_cell"));
    }

    private static void cellFilling(RecipeIngredient<ItemStack> primary, ItemProvider output, Consumer<FinishedRecipe> finishedRecipeConsumer) {
        ModRecipeBuilders.canningMachine(primary, ModRecipeIngredientTypes.ITEM.of(GregTechTags.EMPTY_FLUID_CELL), output.getItemStack(), 100, 2)
            .build(finishedRecipeConsumer, id(output.getRegistryName()));
    }

    private static RecipeName id(String name) {
        return RecipeName.common(Reference.MODID, "canning_machine", name);
    }
}
