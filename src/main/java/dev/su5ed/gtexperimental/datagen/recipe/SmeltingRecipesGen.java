package dev.su5ed.gtexperimental.datagen.recipe;

import com.mojang.datafixers.util.Pair;
import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.object.Dust;
import dev.su5ed.gtexperimental.object.Ingot;
import dev.su5ed.gtexperimental.object.Miscellaneous;
import dev.su5ed.gtexperimental.object.Ore;
import dev.su5ed.gtexperimental.object.Plate;
import dev.su5ed.gtexperimental.recipe.gen.SmeltingRecipeBuilder;
import dev.su5ed.gtexperimental.recipe.gen.compat.InductionSmelterRecipeBuilder;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipeName;
import dev.su5ed.gtexperimental.util.GtUtil;
import dev.su5ed.gtexperimental.util.JavaUtil;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import one.util.streamex.StreamEx;

import java.util.List;
import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.recipe.gen.ModRecipeBuilders.alloySmelter;
import static dev.su5ed.gtexperimental.recipe.gen.ModRecipeBuilders.pulverizer;
import static dev.su5ed.gtexperimental.recipe.type.RecipeUtil.hasTags;

public final class SmeltingRecipesGen implements ModRecipeProvider {
    public static final SmeltingRecipesGen INSTANCE = new SmeltingRecipesGen();

    private SmeltingRecipesGen() {}

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        new SmeltingRecipeBuilder(Ingredient.of(GregTechTags.ore("sulfur")), Dust.SULFUR.getItemStack(3), 0, 200)
            .unlockedBy("has_ores_sulfur", hasTags(GregTechTags.ore("sulfur")))
            .build(finishedRecipeConsumer, id("sulfur_dust"));
        new SmeltingRecipeBuilder(Ingredient.of(GregTechTags.ore("saltpeter")), Dust.SALTPETER.getItemStack(3), 0, 200)
            .unlockedBy("has_ores_saltpeter", hasTags(GregTechTags.ore("saltpeter")))
            .build(finishedRecipeConsumer, id("saltpeter_dust"));
        new SmeltingRecipeBuilder(Ingredient.of(GregTechTags.ore("phosphorus")), Dust.PHOSPHORUS.getItemStack(2), 0, 200)
            .unlockedBy("has_ores_phosphorus", hasTags(GregTechTags.ore("phosphorus")))
            .build(finishedRecipeConsumer, id("phosphorus_dust"));
        new SmeltingRecipeBuilder(Ingredient.of(Miscellaneous.FLOUR.getTag()), new ItemStack(Items.BREAD), 0, 200)
            .unlockedBy("has_flour", hasTags(Miscellaneous.FLOUR.getTag()))
            .build(finishedRecipeConsumer, id("bread"));

        StreamEx.of(Plate.values())
            .mapToEntry(plate -> JavaUtil.getEnumConstantSafely(Ingot.class, plate.name()))
            .nonNullValues()
            .mapValues(ingot -> Pair.of(ingot.getTag(), ingot.getItem()))
            .append(Plate.IRON, Pair.of(Tags.Items.INGOTS_IRON, Items.IRON_INGOT))
            .append(Plate.GOLD, Pair.of(Tags.Items.INGOTS_GOLD, Items.GOLD_INGOT))
            .append(Plate.COPPER, Pair.of(Tags.Items.INGOTS_COPPER, Items.COPPER_INGOT))
            .forKeyValue((plate, ingotPair) -> {
                Item ingot = ingotPair.getSecond();
                TagKey<Item> ingotTag = ingotPair.getFirst();
                String name = GtUtil.tagName(plate.getTag()) + "_to_ingot";
                // Plate Smelting
                new SmeltingRecipeBuilder(Ingredient.of(plate.getTag()), new ItemStack(ingot), 0, 200)
                    .unlockedBy("has_" + GtUtil.tagName(plate.getTag()), hasTags(plate.getTag()))
                    .build(finishedRecipeConsumer, id(name));
                alloySmelter(ModRecipeIngredientTypes.ITEM.of(plate.getTag()), new ItemStack(ingot), 130, 3)
                    .build(finishedRecipeConsumer, AlloySmelterRecipesGen.id(name));
                // Plate pulverizing
                Dust dust = JavaUtil.getEnumConstantSafely(Dust.class, plate.name());
                if (dust != null) {
                    pulverizer(ModRecipeIngredientTypes.ITEM.of(plate.getTag()), dust.getItemStack())
                        .build(finishedRecipeConsumer, PulverizerRecipesGen.id(GtUtil.tagName(plate.getTag()) + "_to_dust"));
                }
                // Plate hammering
                ShapedRecipeBuilder.shaped(plate.getItem())
                    .define('H', GregTechTags.HARD_HAMMER)
                    .define('I', ingotTag)
                    .pattern("H")
                    .pattern("I")
                    .unlockedBy("has_hard_hammer", hasTags(GregTechTags.HARD_HAMMER))
                    .save(finishedRecipeConsumer, CraftingRecipesGen.shapedId(GtUtil.tagName(ingotTag) + "_to_plate"));
            });

        new InductionSmelterRecipeBuilder(Ingredient.of(Ore.PYRITE.getTag()), List.of(new InductionSmelterRecipeBuilder.Result(new ItemStack(Items.IRON_INGOT), 95)), 3000)
            .build(finishedRecipeConsumer, RecipeName.foreign(InductionSmelterRecipeBuilder.TYPE, "iron_ingot"));
    }

    public static RecipeName id(String name) {
        return RecipeName.common(Reference.MODID, "smelting", name);
    }
}
