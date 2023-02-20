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
import dev.su5ed.gtexperimental.recipe.gen.compat.TEInductionSmelterRecipeBuilder;
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
import net.minecraft.world.level.ItemLike;
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
        simple(Ore.TUNGSTATE.getTag(), Dust.TUNGSTEN, 1, finishedRecipeConsumer);
        simple(Ore.SHELDONITE.getTag(), Dust.PLATINUM, 1, finishedRecipeConsumer);
        simple(GregTechTags.ore("sulfur"), Dust.SULFUR, 3, finishedRecipeConsumer);
        simple(GregTechTags.ore("saltpeter"), Dust.SALTPETER, 3, finishedRecipeConsumer);
        simple(GregTechTags.ore("phosphorus"), Dust.PHOSPHORUS, 2, finishedRecipeConsumer);
        simple(Miscellaneous.FLOUR.getTag(), Items.BREAD, 1, finishedRecipeConsumer);

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

        StreamEx.of(Dust.values())
            .without(Dust.STEEL, Dust.CHROME, Dust.TUNGSTEN, Dust.ALUMINIUM)
            .mapToEntry(dust -> JavaUtil.getEnumConstantSafely(Ingot.class, dust.name()))
            .nonNullValues()
            .mapValues(Ingot::getItem)
            .append(Dust.IRON, Items.IRON_INGOT)
            .append(Dust.GOLD, Items.GOLD_INGOT)
            .append(Dust.COPPER, Items.COPPER_INGOT)
            .mapKeys(Dust::getTag)
            .forKeyValue((dust, ingot) -> simple(dust, ingot, 1, finishedRecipeConsumer, GtUtil.tagName(dust) + "_to_ingot"));

        new TEInductionSmelterRecipeBuilder(Ingredient.of(Ore.PYRITE.getTag()), List.of(new TEInductionSmelterRecipeBuilder.Result(new ItemStack(Items.IRON_INGOT), 95)), 3000)
            .build(finishedRecipeConsumer, RecipeName.foreign(TEInductionSmelterRecipeBuilder.TYPE, "iron_ingot"));
    }
    
    private static void simple(TagKey<Item> input, ItemLike output, int outputCount, Consumer<FinishedRecipe> finishedRecipeConsumer) {
        simple(input, output, outputCount, finishedRecipeConsumer, GtUtil.tagName(input) + "_to_" + GtUtil.itemName(output));
    }

    private static void simple(TagKey<Item> input, ItemLike output, int outputCount, Consumer<FinishedRecipe> finishedRecipeConsumer, String name) {
        new SmeltingRecipeBuilder(Ingredient.of(input), new ItemStack(output, outputCount), 0, 200)
            .unlockedBy("has_" + GtUtil.tagName(input), hasTags(input))
            .build(finishedRecipeConsumer, id(name));
    }

    public static RecipeName id(String name) {
        return RecipeName.common(Reference.MODID, "smelting", name);
    }
}
