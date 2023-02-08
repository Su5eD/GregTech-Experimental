package dev.su5ed.gtexperimental.datagen.recipe;

import dev.ftb.mods.ftbic.item.FTBICItems;
import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.object.Component;
import dev.su5ed.gtexperimental.object.Dust;
import dev.su5ed.gtexperimental.object.Ingot;
import dev.su5ed.gtexperimental.object.Miscellaneous;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipeName;
import dev.su5ed.gtexperimental.recipe.type.SelectedProfileCondition;
import ic2.core.ref.Ic2Items;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.datagen.RecipeGen.*;
import static dev.su5ed.gtexperimental.recipe.gen.ModRecipeBuilders.wiremill;

public final class WiremillRecipesGen implements ModRecipeProvider {
    public static final WiremillRecipesGen INSTANCE = new WiremillRecipesGen();

    private WiremillRecipesGen() {}

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        wiremill(ModRecipeIngredientTypes.ITEM.of(Ingot.SOLDERING_ALLOY.getTag()), Miscellaneous.SOLDERING_TIN.getItemStack(), 100, 4)
            .build(finishedRecipeConsumer, id("soldering_tin"));
        wiremill(ModRecipeIngredientTypes.ITEM.of(Ingot.LEAD.getTag()), Miscellaneous.SOLDERING_LEAD.getItemStack(), 100, 4)
            .build(finishedRecipeConsumer, id("soldering_lead"));
        wiremill(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ingot("kanthal"), 4), Component.KANTHAL_COIL.getItemStack(), 450, 12)
            .build(finishedRecipeConsumer, id("kanthal_coil"));
        wiremill(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ingot("nichrome"), 5), Component.NICHROME_COIL.getItemStack(), 600, 16)
            .build(finishedRecipeConsumer, id("nichrome_coil"));
        wiremill(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ingot("cupronickel"), 3), Component.CUPRONICKEL_COIL.getItemStack(), 300, 8)
            .build(finishedRecipeConsumer, id("cupronickel_coil"));

        buildOtherModRecipes(finishedRecipeConsumer);
    }

    private void buildOtherModRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        // IC2
        wiremill(ModRecipeIngredientTypes.ITEM.of(Dust.COAL.getTag(), 4), new ItemStack(Ic2Items.CARBON_FIBRE), 400, 2)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/carbon_fibre"));
        wiremill(ModRecipeIngredientTypes.ITEM.of(Tags.Items.INGOTS_COPPER), new ItemStack(Ic2Items.COPPER_CABLE, 3), 100, 2)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/copper_cable"));
        wiremill(ModRecipeIngredientTypes.ITEM.of(Tags.Items.INGOTS_GOLD), new ItemStack(Ic2Items.GOLD_CABLE, 6), 200, 1)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/gold_cable"));

        // Regular Iron
        wiremill(ModRecipeIngredientTypes.ITEM.of(Tags.Items.INGOTS_IRON), new ItemStack(Ic2Items.IRON_CABLE, 6), 200, 2)
            .addConditions(IC2_LOADED, SelectedProfileCondition.REGULAR_IRON)
            .build(finishedRecipeConsumer, profileId("regular_iron", "iron_cable"));

        // Refined Iron
        wiremill(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ingot("refined_iron")), new ItemStack(Ic2Items.IRON_CABLE, 6), 200, 2)
            .addConditions(IC2_LOADED, SelectedProfileCondition.REFINED_IRON)
            .build(finishedRecipeConsumer, profileId("refined_iron", "iron_cable"));

        // FTBIC
        wiremill(ModRecipeIngredientTypes.ITEM.of(Dust.COAL.getTag(), 4), new ItemStack(FTBICItems.CARBON_FIBERS.item.get()), 400, 2)
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/carbon_fibre"));
    }

    private static RecipeName id(String name) {
        return profileId(null, name);
    }

    private static RecipeName profileId(String profile, String name) {
        return RecipeName.profile(Reference.MODID, "wiremill", profile, name);
    }
}
