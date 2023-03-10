package dev.su5ed.gtexperimental.datagen.recipe;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.object.Dust;
import dev.su5ed.gtexperimental.object.Ingot;
import dev.su5ed.gtexperimental.object.ModFluid;
import dev.su5ed.gtexperimental.object.Ore;
import dev.su5ed.gtexperimental.object.Plate;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipeName;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.recipe.gen.ModRecipeBuilders.blastFurnace;

public final class BlastFurnaceRecipesGen implements ModRecipeProvider {
    public static final BlastFurnaceRecipesGen INSTANCE = new BlastFurnaceRecipesGen();

    private BlastFurnaceRecipesGen() {}

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        blastFurnace(ModRecipeIngredientTypes.ITEM.of(GregTechTags.UNIVERSAL_IRON_INGOT), ModRecipeIngredientTypes.ITEM.of(Dust.COAL.getTag(), 2), Ingot.STEEL.getItemStack(), Dust.DARK_ASHES.getItemStack(2), 500, 1000).build(finishedRecipeConsumer, id("steel_ingot_from_iron"));
        blastFurnace(ModRecipeIngredientTypes.ITEM.of(Dust.STEEL.getTag()), Ingot.STEEL.getItemStack(), 100, 1000).build(finishedRecipeConsumer, id("steel_ingot_from_dust"), true);
        blastFurnace(ModRecipeIngredientTypes.ITEM.of(Dust.CHROME.getTag()), Ingot.CHROME.getItemStack(), 800, 1700).build(finishedRecipeConsumer, id("chrome_ingot"));
        blastFurnace(ModRecipeIngredientTypes.ITEM.of(Dust.TITANIUM.getTag()), Ingot.TITANIUM.getItemStack(), 1000, 1500).build(finishedRecipeConsumer, id("titanium_ingot"));
        blastFurnace(ModRecipeIngredientTypes.ITEM.of(Dust.ALUMINIUM.getTag()), Ingot.ALUMINIUM.getItemStack(), 200, 1700).build(finishedRecipeConsumer, id("aluminium_ingot"), true);
        blastFurnace(ModRecipeIngredientTypes.ITEM.of(Dust.TUNGSTEN.getTag()), Ingot.TUNGSTEN.getItemStack(), 2000, 2500).build(finishedRecipeConsumer, id("tungsten_ingot"), true);
        blastFurnace(ModRecipeIngredientTypes.ITEM.of(Dust.GALENA.getTag(), 2), Ingot.SILVER.getItemStack(), Ingot.LEAD.getItemStack(), 20, 1500).build(finishedRecipeConsumer, id("silver_ingot"));
        blastFurnace(ModRecipeIngredientTypes.ITEM.of(Ingot.TUNGSTEN.getTag()), ModRecipeIngredientTypes.ITEM.of(Ingot.STEEL.getTag()), Ingot.HOT_TUNGSTEN_STEEL.getItemStack(2), Dust.DARK_ASHES.getItemStack(4), 2000, 3000).build(finishedRecipeConsumer, id("hot_tungsten_steel_ingot"));
        blastFurnace(ModRecipeIngredientTypes.ITEM.ofFluidBuckets(ModFluid.SILICON.getTag(), 2), Plate.SILICON.getItemStack(), 1000, 1500).build(finishedRecipeConsumer, id("silicon_plate"));
        blastFurnace(ModRecipeIngredientTypes.ITEM.of(Tags.Items.ORES_IRON), ModRecipeIngredientTypes.ITEM.of(Dust.CALCITE.getTag()), new ItemStack(Items.IRON_INGOT, 3), Dust.DARK_ASHES.getItemStack(), 500, 1500).build(finishedRecipeConsumer, id("iron_ingot_from_iron_ore"));
        blastFurnace(ModRecipeIngredientTypes.ITEM.of(Ore.PYRITE.getTag()), ModRecipeIngredientTypes.ITEM.of(Dust.CALCITE.getTag()), new ItemStack(Items.IRON_INGOT, 2), Dust.DARK_ASHES.getItemStack(), 100, 1500).build(finishedRecipeConsumer, id("iron_ingot_from_pyrite_ore"));
    }

    public static RecipeName id(String name) {
        return RecipeName.common(Reference.MODID, "blast_furnace", name);
    }
}
