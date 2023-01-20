package dev.su5ed.gtexperimental.datagen.recipe;

import dev.ftb.mods.ftbic.item.FTBICItems;
import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.compat.ModHandler;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import ic2.core.ref.Ic2Items;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.crafting.conditions.NotCondition;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.api.Reference.location;
import static dev.su5ed.gtexperimental.recipe.gen.ModRecipeBuilders.bender;

public final class BenderRecipeProvider implements ModRecipeProvider {
    public static final BenderRecipeProvider INSTANCE = new BenderRecipeProvider();

    private BenderRecipeProvider() {}

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        buildOtherModRecipes(finishedRecipeConsumer);
    }

    private void buildOtherModRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        ICondition ic2Loaded = new ModLoadedCondition(ModHandler.IC2_MODID);
        ICondition ftbicLoaded = new ModLoadedCondition(ModHandler.FTBIC_MODID);
//        ICondition railcraftLoaded = new ModLoadedCondition(ModHandler.RAILCRAFT_MODID);

        // IC2
        bender(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ingots", "mixed_metal")), new ItemStack(Ic2Items.ALLOY), 100, 8)
            .addConditions(ic2Loaded)
            .build(finishedRecipeConsumer, id("ic2/alloy"));

        // FTBIC
        bender(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ingots", "mixed_metal")), new ItemStack(FTBICItems.ADVANCED_ALLOY.item.get()), 100, 8)
            .addConditions(new NotCondition(ic2Loaded), ftbicLoaded)
            .build(finishedRecipeConsumer, id("ftbic/alloy"));

        // Railcraft
//        bender(ModRecipeIngredientTypes.ITEM.of(Rod.ALUMINIUM.getTag(), 3), new ItemStack(RAILCRAFT_RAIL), 150, 10)
//            .addConditions(railcraftLoaded)
//            .build(finishedRecipeConsumer, id("rail_from_aluminium"));
//        bender(ModRecipeIngredientTypes.ITEM.of(Rod.IRON.getTag(), 3), new ItemStack(RAILCRAFT_RAIL, 2), 150, 10)
//            .addConditions(railcraftLoaded)
//            .build(finishedRecipeConsumer, id("rail_from_iron"));
//        bender(ModRecipeIngredientTypes.ITEM.of(Rod.BRONZE.getTag(), 2), new ItemStack(RAILCRAFT_RAIL), 50, 20)
//            .addConditions(railcraftLoaded)
//            .build(finishedRecipeConsumer, id("rail_from_bronze"));
//        bender(ModRecipeIngredientTypes.ITEM.of(Rod.STEEL.getTag(), 3), new ItemStack(RAILCRAFT_RAIL, 4), 150, 30)
//            .addConditions(railcraftLoaded)
//            .build(finishedRecipeConsumer, id("rail_from_steel"));
//        bender(ModRecipeIngredientTypes.ITEM.ofTags(3, Rod.TITANIUM.getTag(), Rod.TUNGSTEN.getTag()), new ItemStack(RAILCRAFT_RAIL, 8), 150, 30)
//            .addConditions(railcraftLoaded)
//            .build(finishedRecipeConsumer, id("rail_from_titanium"));
//        bender(ModRecipeIngredientTypes.ITEM.of(Rod.TUNGSTEN_STEEL.getTag()), new ItemStack(RAILCRAFT_RAIL, 4), 100, 30)
//            .addConditions(railcraftLoaded)
//            .build(finishedRecipeConsumer, id("rail_from_tungsten_steel"));
//        bender(ModRecipeIngredientTypes.ITEM.of(Rod.REFINED_IRON.getTag(), 6), new ItemStack(RAILCRAFT_RAIL, 5), 150, 20)
//            .addConditions(railcraftLoaded, SelectedProfileCondition.REFINED_IRON)
//            .build(finishedRecipeConsumer, id("rail_from_refined_iron"));
    }

    private static ResourceLocation id(String name) {
        return location("bender/" + name);
    }
}