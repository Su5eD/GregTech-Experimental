package dev.su5ed.gtexperimental.datagen.recipe;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.object.Dust;
import dev.su5ed.gtexperimental.object.Miscellaneous;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipeName;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.recipe.gen.ModRecipeBuilders.implosion;

public final class ImplosionRecipesGen implements ModRecipeProvider {
    public static final ImplosionRecipesGen INSTANCE = new ImplosionRecipesGen();

    private ImplosionRecipesGen() {}

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        implosion(ModRecipeIngredientTypes.ITEM.of(Dust.RED_GARNET.getTag(), 4), Miscellaneous.RED_GARNET.getItemStack(3), Dust.DARK_ASHES.getItemStack(8), 16).build(finishedRecipeConsumer, id("red_garnet"));
        implosion(ModRecipeIngredientTypes.ITEM.of(Dust.YELLOW_GARNET.getTag(), 4), Miscellaneous.YELLOW_GARNET.getItemStack(3), Dust.DARK_ASHES.getItemStack(8), 16).build(finishedRecipeConsumer, id("yellow_garnet"));
        implosion(ModRecipeIngredientTypes.ITEM.of(Dust.OLIVINE.getTag(), 4), Miscellaneous.OLIVINE.getItemStack(3), Dust.DARK_ASHES.getItemStack(12), 24).build(finishedRecipeConsumer, id("olivine"));
        implosion(ModRecipeIngredientTypes.ITEM.of(Dust.EMERALD.getTag(), 4), new ItemStack(Items.EMERALD, 3), Dust.DARK_ASHES.getItemStack(12), 24).build(finishedRecipeConsumer, id("emerald"));
        implosion(ModRecipeIngredientTypes.ITEM.of(Dust.RUBY.getTag(), 4), Miscellaneous.RUBY.getItemStack(3), Dust.DARK_ASHES.getItemStack(12), 24).build(finishedRecipeConsumer, id("ruby"));
        implosion(ModRecipeIngredientTypes.ITEM.of(Dust.SAPPHIRE.getTag(), 4), Miscellaneous.SAPPHIRE.getItemStack(3), Dust.DARK_ASHES.getItemStack(12), 24).build(finishedRecipeConsumer, id("sapphire"));
        implosion(ModRecipeIngredientTypes.ITEM.of(Dust.GREEN_SAPPHIRE.getTag(), 4), Miscellaneous.GREEN_SAPPHIRE.getItemStack(3), Dust.DARK_ASHES.getItemStack(12), 24).build(finishedRecipeConsumer, id("green_sapphire"));

        implosion(ModRecipeIngredientTypes.ITEM.of(GregTechTags.COAL_CHUNK), new ItemStack(Items.DIAMOND), Dust.DARK_ASHES.getItemStack(4), 8).build(finishedRecipeConsumer, id("diamond"));
    }

    private static RecipeName id(String name) {
        return RecipeName.common(Reference.MODID, "implosion", name);
    }
}
