package dev.su5ed.gtexperimental.datagen.recipe;

import dev.ftb.mods.ftbic.item.FTBICItems;
import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.object.Dust;
import dev.su5ed.gtexperimental.object.Ingot;
import dev.su5ed.gtexperimental.object.Miscellaneous;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipeName;
import ic2.core.ref.Ic2Items;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.datagen.RecipeGen.*;
import static dev.su5ed.gtexperimental.recipe.gen.ModRecipeBuilders.implosion;

public final class ImplosionRecipesGen implements ModRecipeProvider {
    public static final ImplosionRecipesGen INSTANCE = new ImplosionRecipesGen();

    private ImplosionRecipesGen() {}

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        implosion(ModRecipeIngredientTypes.ITEM.of(Dust.RED_GARNET.getTag(), 4), Miscellaneous.RED_GARNET.getItemStack(3), Dust.DARK_ASHES.getItemStack(8), 16)
            .build(finishedRecipeConsumer, id("red_garnet"));
        implosion(ModRecipeIngredientTypes.ITEM.of(Dust.YELLOW_GARNET.getTag(), 4), Miscellaneous.YELLOW_GARNET.getItemStack(3), Dust.DARK_ASHES.getItemStack(8), 16)
            .build(finishedRecipeConsumer, id("yellow_garnet"));
        implosion(ModRecipeIngredientTypes.ITEM.of(Dust.OLIVINE.getTag(), 4), Miscellaneous.OLIVINE.getItemStack(3), Dust.DARK_ASHES.getItemStack(12), 24)
            .build(finishedRecipeConsumer, id("olivine"));
        implosion(ModRecipeIngredientTypes.ITEM.of(Dust.EMERALD.getTag(), 4), new ItemStack(Items.EMERALD, 3), Dust.DARK_ASHES.getItemStack(12), 24)
            .build(finishedRecipeConsumer, id("emerald"));
        implosion(ModRecipeIngredientTypes.ITEM.of(Dust.RUBY.getTag(), 4), Miscellaneous.RUBY.getItemStack(3), Dust.DARK_ASHES.getItemStack(12), 24)
            .build(finishedRecipeConsumer, id("ruby"));
        implosion(ModRecipeIngredientTypes.ITEM.of(Dust.SAPPHIRE.getTag(), 4), Miscellaneous.SAPPHIRE.getItemStack(3), Dust.DARK_ASHES.getItemStack(12), 24)
            .build(finishedRecipeConsumer, id("sapphire"));
        implosion(ModRecipeIngredientTypes.ITEM.of(Dust.GREEN_SAPPHIRE.getTag(), 4), Miscellaneous.GREEN_SAPPHIRE.getItemStack(3), Dust.DARK_ASHES.getItemStack(12), 24)
            .build(finishedRecipeConsumer, id("green_sapphire"));

        buildOtherModRecipes(finishedRecipeConsumer);
    }

    private void buildOtherModRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        implosion(ModRecipeIngredientTypes.ITEM.of(GregTechTags.COAL_CHUNK), new ItemStack(Items.DIAMOND), Dust.DARK_ASHES.getItemStack(4), 8)
            .build(finishedRecipeConsumer, id("diamond"));

        // IC2
        implosion(ModRecipeIngredientTypes.ITEM.of(Dust.DIAMOND.getTag(), 4), new ItemStack(Ic2Items.INDUTRIAL_DIAMOND, 3), Dust.DARK_ASHES.getItemStack(16), 32)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/industrial_diamond"));
        implosion(ModRecipeIngredientTypes.ITEM.of(Ingot.IRIDIUM_ALLOY.getTag()), new ItemStack(Ic2Items.IRIDIUM), Dust.DARK_ASHES.getItemStack(4), 8)
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/iridium"));

        // FTBIC
        implosion(ModRecipeIngredientTypes.ITEM.of(Dust.DIAMOND.getTag(), 4), new ItemStack(Items.DIAMOND, 3), Dust.DARK_ASHES.getItemStack(16), 32)
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/diamond"));
        implosion(ModRecipeIngredientTypes.ITEM.of(Ingot.IRIDIUM_ALLOY.getTag()), new ItemStack(FTBICItems.IRIDIUM_ALLOY.item.get()), Dust.DARK_ASHES.getItemStack(4), 8)
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/iridium_alloy"));
    }

    private static RecipeName id(String name) {
        return RecipeName.common(Reference.MODID, "implosion", name);
    }
}
