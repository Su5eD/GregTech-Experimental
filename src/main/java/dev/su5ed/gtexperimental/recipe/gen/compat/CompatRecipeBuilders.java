package dev.su5ed.gtexperimental.recipe.gen.compat;

import com.mojang.datafixers.util.Either;
import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.datagen.RecipeGen;
import dev.su5ed.gtexperimental.recipe.type.RecipeName;
import dev.su5ed.gtexperimental.recipe.type.RecipeUtil;
import dev.su5ed.gtexperimental.recipe.type.SelectedProfileCondition;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.ICondition;

import java.util.Set;
import java.util.function.Consumer;

public final class CompatRecipeBuilders {

    public static void ic2Compressor(ItemLike item, int count, ItemStack output, Consumer<FinishedRecipe> finishedRecipeConsumer, SelectedProfileCondition profile) {
        compressor(Either.left(item), count, output, finishedRecipeConsumer, RecipeName.common(Reference.MODID, "type", RecipeUtil.createName(item, output)), Set.of(RecipeGen.IC2_LOADED, profile));
    }

    public static void ic2Compressor(TagKey<Item> tag, int count, ItemStack output, Consumer<FinishedRecipe> finishedRecipeConsumer, SelectedProfileCondition profile) {
        compressor(Either.right(tag), count, output, finishedRecipeConsumer, RecipeName.common(Reference.MODID, "type", RecipeUtil.createName(tag, output)), Set.of(RecipeGen.IC2_LOADED, profile));
    }

    public static void compressor(ItemLike item, int count, ItemStack output, Consumer<FinishedRecipe> finishedRecipeConsumer) {
        compressor(Either.left(item), count, output, finishedRecipeConsumer, recipeName(item, output), Set.of());
    }

    public static void compressor(TagKey<Item> tag, int count, ItemStack output, Consumer<FinishedRecipe> finishedRecipeConsumer) {
        compressor(Either.right(tag), count, output, finishedRecipeConsumer, recipeName(tag, output), Set.of());
    }

    private static void compressor(Either<ItemLike, TagKey<Item>> input, int count, ItemStack output, Consumer<FinishedRecipe> finishedRecipeConsumer, RecipeName id, Set<ICondition> conditions) {
        machineRecipe(IC2MachineRecipeBuilder.COMPRESSOR, FTBICMachineRecipeBuilder.COMPRESSING, input, count, output, finishedRecipeConsumer, id, conditions);
    }

    public static void ic2Extractor(ItemLike item, int count, ItemStack output, Consumer<FinishedRecipe> finishedRecipeConsumer) {
        extractor(Either.left(item), count, output, finishedRecipeConsumer, recipeName(item, output), Set.of(RecipeGen.IC2_LOADED));
    }

    public static void extractor(ItemLike item, int count, ItemStack output, Consumer<FinishedRecipe> finishedRecipeConsumer) {
        extractor(Either.left(item), count, output, finishedRecipeConsumer, recipeName(item, output), Set.of());
    }

    public static void extractor(TagKey<Item> tag, int count, ItemStack output, Consumer<FinishedRecipe> finishedRecipeConsumer) {
        extractor(Either.right(tag), count, output, finishedRecipeConsumer, recipeName(tag, output), Set.of());
    }

    private static void extractor(Either<ItemLike, TagKey<Item>> input, int count, ItemStack output, Consumer<FinishedRecipe> finishedRecipeConsumer, RecipeName id, Set<ICondition> conditions) {
        machineRecipe(IC2MachineRecipeBuilder.EXTRACTOR, FTBICMachineRecipeBuilder.SEPARATING, input, count, output, finishedRecipeConsumer, id, conditions);
    }

    private static void machineRecipe(ResourceLocation ic2Type, ResourceLocation ftbicType, Either<ItemLike, TagKey<Item>> input, int count, ItemStack output, Consumer<FinishedRecipe> finishedRecipeConsumer, RecipeName id, Set<ICondition> conditions) {
        Ingredient ingredient = input.map(Ingredient::of, Ingredient::of);
        new IC2MachineRecipeBuilder(ic2Type, ingredient, count, output)
            .addConditions(conditions)
            .build(finishedRecipeConsumer, id.toForeign(ic2Type));

        new FTBICMachineRecipeBuilder(ftbicType, ingredient, count, output)
            .addConditions(conditions)
            .build(finishedRecipeConsumer, id.toForeign(ftbicType));
    }

    private static RecipeName recipeName(ItemLike item, ItemStack output) {
        return RecipeName.common(Reference.MODID, "type", RecipeUtil.createName(item, output));
    }

    private static RecipeName recipeName(TagKey<Item> tag, ItemStack output) {
        return RecipeName.common(Reference.MODID, "type", RecipeUtil.createName(tag, output));
    }

    private CompatRecipeBuilders() {}
}
