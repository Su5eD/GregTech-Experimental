package dev.su5ed.gtexperimental.recipe.gen.compat;

import dev.su5ed.gtexperimental.compat.ModHandler;
import dev.su5ed.gtexperimental.datagen.RecipeGen;
import dev.su5ed.gtexperimental.recipe.type.RecipeUtil;
import dev.su5ed.gtexperimental.recipe.type.SelectedProfileCondition;
import dev.su5ed.gtexperimental.util.GtUtil;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

public final class CompatRecipeBuilders {

    public static void compressor(ItemLike item, int count, ItemStack output, Consumer<FinishedRecipe> finishedRecipeConsumer) {
        compressor(item, count, output, finishedRecipeConsumer, RecipeUtil.createId(item, output));
    }

    public static void compressor(ItemLike item, int count, ItemStack output, Consumer<FinishedRecipe> finishedRecipeConsumer, ResourceLocation id) {
        new IC2MachineRecipeBuilder(IC2MachineRecipeBuilder.COMPRESSOR, item, count, output)
            .build(finishedRecipeConsumer, GtUtil.nestedId(id, "compressor/" + ModHandler.IC2_MODID));

        new FTBICMachineRecipeBuilder(FTBICMachineRecipeBuilder.COMPRESSOR, Ingredient.of(item), count, output)
            .build(finishedRecipeConsumer, GtUtil.nestedId(id, "compressor/" + ModHandler.FTBIC_MODID));
    }

    public static void compressor(TagKey<Item> tag, int count, ItemStack output, Consumer<FinishedRecipe> finishedRecipeConsumer) {
        compressor(tag, count, output, finishedRecipeConsumer, RecipeUtil.createId(tag, output));
    }

    public static void compressor(TagKey<Item> tag, int count, ItemStack output, Consumer<FinishedRecipe> finishedRecipeConsumer, ResourceLocation id) {
        new IC2MachineRecipeBuilder(IC2MachineRecipeBuilder.COMPRESSOR, tag, count, output)
            .build(finishedRecipeConsumer, GtUtil.nestedId(id, "compressor/" + ModHandler.IC2_MODID));

        new FTBICMachineRecipeBuilder(FTBICMachineRecipeBuilder.COMPRESSOR, Ingredient.of(tag), count, output)
            .build(finishedRecipeConsumer, GtUtil.nestedId(id, "compressor/" + ModHandler.FTBIC_MODID));
    }

    public static void ic2Compressor(TagKey<Item> tag, int count, ItemStack output, Consumer<FinishedRecipe> finishedRecipeConsumer, SelectedProfileCondition profile) {
        ic2Compressor(tag, count, output, finishedRecipeConsumer, RecipeUtil.createId(tag, output), profile);
    }

    public static void ic2Compressor(TagKey<Item> tag, int count, ItemStack output, Consumer<FinishedRecipe> finishedRecipeConsumer, ResourceLocation id, SelectedProfileCondition profile) {
        new IC2MachineRecipeBuilder(IC2MachineRecipeBuilder.COMPRESSOR, tag, count, output)
            .addConditions(profile)
            .build(finishedRecipeConsumer, GtUtil.nestedId(id, "compressor/" + ModHandler.IC2_MODID));

        new FTBICMachineRecipeBuilder(FTBICMachineRecipeBuilder.COMPRESSOR, Ingredient.of(tag), count, output)
            .addConditions(RecipeGen.IC2_LOADED, profile)
            .build(finishedRecipeConsumer, GtUtil.nestedId(id, "compressor/" + ModHandler.FTBIC_MODID));
    }
    
    public static void ic2Compressor(ItemLike item, int count, ItemStack output, Consumer<FinishedRecipe> finishedRecipeConsumer, SelectedProfileCondition profile) {
        ic2Compressor(item, count, output, finishedRecipeConsumer, RecipeUtil.createId(item, output), profile);
    }

    public static void ic2Compressor(ItemLike item, int count, ItemStack output, Consumer<FinishedRecipe> finishedRecipeConsumer, ResourceLocation id, SelectedProfileCondition profile) {
        new IC2MachineRecipeBuilder(IC2MachineRecipeBuilder.COMPRESSOR, item, count, output)
            .addConditions(profile)
            .build(finishedRecipeConsumer, GtUtil.nestedId(id, "compressor/" + ModHandler.IC2_MODID));

        new FTBICMachineRecipeBuilder(FTBICMachineRecipeBuilder.COMPRESSOR, Ingredient.of(item), count, output)
            .addConditions(RecipeGen.IC2_LOADED, profile)
            .build(finishedRecipeConsumer, GtUtil.nestedId(id, "compressor/" + ModHandler.FTBIC_MODID));
    }

    private CompatRecipeBuilders() {}
}
