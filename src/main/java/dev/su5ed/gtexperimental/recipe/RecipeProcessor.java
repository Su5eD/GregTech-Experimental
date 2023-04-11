package dev.su5ed.gtexperimental.recipe;

import com.google.common.base.Stopwatch;
import com.mojang.datafixers.util.Pair;
import dev.su5ed.gtexperimental.GregTechConfig;
import dev.su5ed.gtexperimental.GregTechMod;
import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.compat.IC2BaseMod;
import dev.su5ed.gtexperimental.compat.ModHandler;
import dev.su5ed.gtexperimental.object.Dust;
import dev.su5ed.gtexperimental.recipe.crafting.ToolCraftingIngredient;
import dev.su5ed.gtexperimental.recipe.crafting.ToolShapedRecipe;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipePropertyMap;
import dev.su5ed.gtexperimental.recipe.type.RecipeUtil;
import dev.su5ed.gtexperimental.recipe.type.VanillaRecipeIngredient;
import dev.su5ed.gtexperimental.util.GtUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.items.ItemHandlerHelper;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static dev.su5ed.gtexperimental.recipe.type.RecipeUtil.WATER;
import static dev.su5ed.gtexperimental.util.GtUtil.location;

public class RecipeProcessor {
    private static final Map<RecipeUtil.Shape, Integer> ARMOR_SHAPES = Map.of(
        new RecipeUtil.Shape("XXX", "X X"), 4,
        new RecipeUtil.Shape("X X", "XXX", "XXX"), 1,
        new RecipeUtil.Shape("XXX", "X X", "X X"), 4,
        new RecipeUtil.Shape("X X", "X X"), 1
    );
    private static final Map<Character, ItemLike> STICK_KEY = Map.of('S', Items.STICK);
    private static final RecipeUtil.Shape SWORD_SHAPE = new RecipeUtil.Shape(STICK_KEY, "X", "X", "S");
    private static final RecipeUtil.Shape PICKAXE_SHAPE = new RecipeUtil.Shape(STICK_KEY, "XXX", " S ", " S ");
    private static final RecipeUtil.Shape SHOVEL_SHAPE = new RecipeUtil.Shape(STICK_KEY, "X", "S", "S");
    private static final RecipeUtil.Shape AXE_SHAPE = new RecipeUtil.Shape(STICK_KEY, "XX", "XS", " S");
    private static final RecipeUtil.Shape HOE_SHAPE = new RecipeUtil.Shape(STICK_KEY, "XX", " S", " S");
    private static final RecipeUtil.Shape STORAGE_BLOCK_SHAPE = new RecipeUtil.Shape("XXX", "XXX", "XXX");

    private final RecipeManager recipeManager;
    private final Map<TagKey<Item>, TagKey<Item>> ingotToPlate;

    public RecipeProcessor(ReloadableServerResources resources) {
        this.recipeManager = resources.getRecipeManager();
        this.ingotToPlate = RecipeUtil.associateTags("ingots", "plates")
            .mapValues(Pair::getFirst)
            .toMap();
    }

    public void run() {
        GregTechMod.LOGGER.info("Processing recipes...");
        Stopwatch stopwatch = Stopwatch.createStarted();
        Collection<Recipe<?>> modifiedRecipes = StreamEx.of(this.recipeManager.getRecipes())
            .flatMap(this::mapCraftingRecipe)
            .append(getAdditionalRecipes())
            .toList();
        this.recipeManager.replaceRecipes(modifiedRecipes);
        stopwatch.stop();
        GregTechMod.LOGGER.info("Finished processing recipes in {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    public Stream<Recipe<?>> mapCraftingRecipe(Recipe<?> recipe) {
        ItemStack result = recipe.getResultItem();
        if (recipe instanceof IShapedRecipe<?> shapedRecipe) {
            // Replace plank -> stick recipes
            if (shapedRecipe.getRecipeWidth() == 1 && shapedRecipe.getRecipeHeight() == 2
                && result.is(Tags.Items.RODS_WOODEN)
                && recipe.getIngredients().size() == 2 && testIngredient(recipe, ItemTags.PLANKS)) {
                // Sawing
                ResourceLocation sawingId = replacedId(recipe.getId(), "sawing");
                NonNullList<Ingredient> ingredients = NonNullList.createWithCapacity(3);
                ingredients.add(ToolCraftingIngredient.of(GregTechTags.SAW, 1));
                ingredients.addAll(recipe.getIngredients());
                // Non-sawing
                ResourceLocation rawId = replacedId(recipe.getId());
                ItemStack rawOutput = GregTechConfig.COMMON.woodNeedsSawForCrafting.get() ? ItemHandlerHelper.copyStackWithSize(result, result.getCount() / 2) : result;

                return Stream.of(
                    new ToolShapedRecipe(sawingId, recipe.getGroup(), 1, 3, ingredients, result.copy()),
                    new ShapedRecipe(rawId, recipe.getGroup(), shapedRecipe.getRecipeWidth(), shapedRecipe.getRecipeHeight(), recipe.getIngredients(), rawOutput)
                );
            }
            // Plate armor recipes
            if (GregTechConfig.COMMON.armorPlateCrafting.get()) {
                for (Map.Entry<RecipeUtil.Shape, Integer> entry : ARMOR_SHAPES.entrySet()) {
                    if (entry.getKey().matches(shapedRecipe)) {
                        return replaceArmorRecipe(shapedRecipe, entry.getValue());
                    }
                }
            }
            // Plate tool recipes
            if (GregTechConfig.COMMON.toolPlateCrafting.get()) {
                if (SWORD_SHAPE.matches(shapedRecipe) && result.is(Tags.Items.TOOLS_SWORDS)) {
                    NonNullList<Ingredient> ingredients = toPlates(recipe.getIngredients(), true);
                    if (ingredients != null) {
                        NonNullList<Ingredient> allIngredients = NonNullList.withSize(9, Ingredient.EMPTY);
                        allIngredients.set(1, ingredients.get(0));
                        allIngredients.set(3, Ingredient.of(GregTechTags.FILE));
                        allIngredients.set(4, ingredients.get(1));
                        allIngredients.set(5, Ingredient.of(GregTechTags.HARD_HAMMER));
                        allIngredients.set(7, ingredients.get(2));
                        return Stream.of(new ShapedRecipe(replacedId(recipe.getId()), recipe.getGroup(), 3, 3, allIngredients, result));
                    }
                }
                else if (PICKAXE_SHAPE.matches(shapedRecipe) && result.is(Tags.Items.TOOLS_PICKAXES)) {
                    NonNullList<Ingredient> ingredients = recipe.getIngredients();
                    Ingredient plate = toPlate(ingredients.get(0));
                    if (plate != null) {
                        ingredients.set(0, plate);
                        ingredients.set(3, Ingredient.of(GregTechTags.FILE));
                        ingredients.set(5, Ingredient.of(GregTechTags.HARD_HAMMER));
                        return Stream.of(new ShapedRecipe(replacedId(recipe.getId()), recipe.getGroup(), shapedRecipe.getRecipeWidth(), shapedRecipe.getRecipeHeight(), ingredients, result));
                    }
                }
                else if (SHOVEL_SHAPE.matches(shapedRecipe) && result.is(Tags.Items.TOOLS_SHOVELS)) {
                    NonNullList<Ingredient> ingredients = toPlates(recipe.getIngredients(), true);
                    if (ingredients != null) {
                        NonNullList<Ingredient> allIngredients = NonNullList.withSize(9, Ingredient.EMPTY);
                        allIngredients.set(0, Ingredient.of(GregTechTags.FILE));
                        allIngredients.set(1, ingredients.get(0));
                        allIngredients.set(2, Ingredient.of(GregTechTags.HARD_HAMMER));
                        allIngredients.set(4, ingredients.get(1));
                        allIngredients.set(7, ingredients.get(2));
                        return Stream.of(new ShapedRecipe(replacedId(recipe.getId()), recipe.getGroup(), 3, 3, allIngredients, result));
                    }
                }
                else if (AXE_SHAPE.matches(shapedRecipe) && result.is(Tags.Items.TOOLS_AXES)) {
                    NonNullList<Ingredient> ingredients = recipe.getIngredients();
                    Ingredient plate = toPlate(ingredients.get(0));
                    if (plate != null) {
                        NonNullList<Ingredient> allIngredients = NonNullList.withSize(9, Ingredient.EMPTY);
                        allIngredients.set(0, plate);
                        allIngredients.set(1, ingredients.get(1));
                        allIngredients.set(2, Ingredient.of(GregTechTags.HARD_HAMMER));
                        allIngredients.set(3, plate);
                        allIngredients.set(4, ingredients.get(3));
                        allIngredients.set(6, Ingredient.of(GregTechTags.FILE));
                        allIngredients.set(7, ingredients.get(5));
                        return Stream.of(new ShapedRecipe(replacedId(recipe.getId()), recipe.getGroup(), 3, 3, allIngredients, result));
                    }
                }
                else if (HOE_SHAPE.matches(shapedRecipe) && result.is(Tags.Items.TOOLS_HOES)) {
                    NonNullList<Ingredient> ingredients = recipe.getIngredients();
                    Ingredient plate = toPlate(ingredients.get(0));
                    if (plate != null) {
                        NonNullList<Ingredient> allIngredients = NonNullList.withSize(9, Ingredient.EMPTY);
                        allIngredients.set(0, plate);
                        allIngredients.set(1, ingredients.get(1));
                        allIngredients.set(2, Ingredient.of(GregTechTags.HARD_HAMMER));
                        allIngredients.set(3, Ingredient.of(GregTechTags.FILE));
                        allIngredients.set(4, ingredients.get(3));
                        allIngredients.set(7, ingredients.get(5));
                        return Stream.of(new ShapedRecipe(replacedId(recipe.getId()), recipe.getGroup(), 3, 3, allIngredients, result));
                    }
                }
            }
            // Storage block crafting
            if (STORAGE_BLOCK_SHAPE.matches(shapedRecipe) && result.is(Tags.Items.STORAGE_BLOCKS)) {
                Ingredient ingredient = shapedRecipe.getIngredients().get(0);
                if (ModHandler.ic2Loaded && !ingredient.isEmpty() && !IC2BaseMod.compressorRecipeExists(this.recipeManager, ingredient.getItems()[0])) {
                    Recipe<?> compressorRecipe = IC2BaseMod.generateCompressorRecipe(recipe.getId().getPath(), recipe.getIngredients().get(0), 9, result);
                    return GregTechConfig.COMMON.storageBlockCrafting.get() ? Stream.of(recipe, compressorRecipe) : Stream.of(compressorRecipe);
                }
                return GregTechConfig.COMMON.storageBlockCrafting.get() ? Stream.of(recipe) : Stream.empty();
            }
            // Storage block decrafting
            if (shapedRecipe.getRecipeWidth() == 1 && shapedRecipe.getRecipeHeight() == 1
                && testIngredient(recipe, Tags.Items.STORAGE_BLOCKS)
                && result.getCount() == 9 && !GregTechConfig.COMMON.storageBlockDecrafting.get() && !result.is(Tags.Items.GEMS)) {
                return Stream.empty();
            }
        }
        else if (recipe instanceof ShapelessRecipe) {
            // Storage block decrafting
            if (recipe.getIngredients().size() == 1
                && testIngredient(recipe, Tags.Items.STORAGE_BLOCKS)
                && result.getCount() == 9 && !GregTechConfig.COMMON.storageBlockDecrafting.get() && !result.is(Tags.Items.GEMS)) {
                return Stream.empty();
            }
            // Log to planks
            if (recipe.getIngredients().size() == 1
                && testIngredient(recipe, ItemTags.LOGS)
                && result.getCount() == 4 && result.is(ItemTags.PLANKS)) {
                Ingredient ingredient = recipe.getIngredients().get(0);
                ItemStack planks = ItemHandlerHelper.copyStackWithSize(result, result.getCount() * 3 / 2);
                String name = GtUtil.itemName(ingredient.getItems()[0]);
                Recipe<?> sawmillRecipe = IFMORecipe.industrialSawmill(location("generated", "industrial_sawmill", name + "_to_planks"), new VanillaRecipeIngredient(ingredient), WATER, List.of(planks, Dust.WOOD.getItemStack()), RecipePropertyMap.empty());
                if (GregTechConfig.COMMON.woodNeedsSawForCrafting.get()) {
                    return Stream.of(
                        new ShapelessRecipe(replacedId(recipe.getId()), recipe.getGroup(), ItemHandlerHelper.copyStackWithSize(result, result.getCount() / 2), recipe.getIngredients()),
                        new ShapedRecipe(location("generated", "shaped", name + "_sawing"), recipe.getGroup(), 1, 2, NonNullList.of(Ingredient.EMPTY, Ingredient.of(GregTechTags.SAW), ingredient), result.copy()),
                        sawmillRecipe
                    );
                }
                return Stream.of(recipe, sawmillRecipe);
            }
        }
        return Stream.of(recipe);
    }

    private static boolean testIngredient(Recipe<?> recipe, TagKey<Item> tag) {
        for (Ingredient ingredient : recipe.getIngredients()) {
            if (!ingredient.isEmpty()) {
                for (ItemStack item : ingredient.getItems()) {
                    if (item.is(tag)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Collection<Recipe<?>> getAdditionalRecipes() {
        List<Recipe<?>> recipes = new ArrayList<>();

        // Ore crushing recipes
        // TODO: Raw Materials
        RecipeUtil.associateTags("ores", "dusts")
            .mapKeyValue((key, dustPair) -> {
                ResourceLocation id = location("generated/shaped/crushing/" + GtUtil.tagName(key));
                NonNullList<Ingredient> ingredients = NonNullList.of(Ingredient.EMPTY, Ingredient.of(GregTechTags.HARD_HAMMER), Ingredient.of(key));
                return new ShapedRecipe(id, "", 1, 2, ingredients, new ItemStack(dustPair.getSecond()));
            })
            .forEach(recipes::add);

        // Dust <-> Smalldust
        RecipeUtil.associateTags("dusts", "small_dusts")
            .flatMapKeyValue((key, smallDustPair) -> {
                Item dustItem = RecipeUtil.findFirstItem(key).orElseThrow();
                ResourceLocation toSmallDustId = location("generated/shapeless/" + GtUtil.tagName(key) + "_to_" + GtUtil.itemName(smallDustPair.getSecond()));
                ResourceLocation toDustId = location("generated/shapeless/" + GtUtil.tagName(smallDustPair.getFirst()) + "_to_" + GtUtil.itemName(dustItem));
                return StreamEx.of(
                    new ShapelessRecipe(toSmallDustId, "", new ItemStack(smallDustPair.getSecond(), 4), NonNullList.of(Ingredient.EMPTY, Ingredient.of(key))),
                    new ShapelessRecipe(toDustId, "", new ItemStack(dustItem), NonNullList.withSize(4, Ingredient.of(smallDustPair.getFirst())))
                );
            })
            .forEach(recipes::add);

        // Ingot -> Rod
        RecipeUtil.associateTags("ingots", "rods")
            .flatMapKeyValue((key, rodPair) -> {
                Pair<Item, Integer> output = RecipeUtil.findAssociatedTag(rodPair.getFirst(), "rods", "small_dusts")
                    .map(smallDustPair -> Pair.of(smallDustPair.getSecond(), 50))
                    .orElseGet(() -> Pair.of(rodPair.getSecond(), 150));
                Item outputItem = output.getFirst();
                ResourceLocation id = location("lathe", "generated", GtUtil.tagName(key) + "_to_" + GtUtil.itemName(outputItem));
                ResourceLocation filingId = location("generated", "shaped", "filing_" + GtUtil.tagName(key));
                return Stream.of(
                    new ShapedRecipe(filingId, "", 1, 2, NonNullList.of(Ingredient.EMPTY, Ingredient.of(GregTechTags.FILE), Ingredient.of(key)), new ItemStack(rodPair.getSecond())),
                    SIMORecipe.lathe(id, ModRecipeIngredientTypes.ITEM.of(key), List.of(new ItemStack(rodPair.getSecond()), new ItemStack(outputItem)), RecipePropertyMap.builder().duration(output.getSecond()).energyCost(16).build())
                );
            })
            .forEach(recipes::add);

        // Ingot -> Plate
        RecipeUtil.associateTags("ingots", "plates")
            .mapValues(Pair::getSecond)
            .mapKeyValue((key, output) -> {
                ResourceLocation id = location("generated", "bender", GtUtil.tagName(key) + "_to_" + GtUtil.itemName(output));
                return SISORecipe.bender(id, ModRecipeIngredientTypes.ITEM.of(key), new ItemStack(output), RecipePropertyMap.builder().duration(50).energyCost(20).build());
            })
            .forEach(recipes::add);

        // Storage block macerating
        RecipeUtil.associateTags("storage_blocks", "dusts")
            .flatMapKeyValue((key, dustPair) -> {
                if (ModHandler.ic2Loaded && !IC2BaseMod.maceratorRecipeExists(this.recipeManager, key)) {
                    return Stream.of(IC2BaseMod.generateMaceratorRecipe(key, 1, new ItemStack(dustPair.getSecond(), 9)));
                }
                return Stream.empty();
            })
            .forEach(recipes::add);

        return recipes;
    }

    private Stream<Recipe<?>> replaceArmorRecipe(IShapedRecipe<?> recipe, int hammerIndex) {
        ResourceLocation id = replacedId(recipe.getId());
        NonNullList<Ingredient> ingredients = toPlates(recipe.getIngredients(), false);
        if (ingredients != null) {
            ingredients.set(hammerIndex, Ingredient.of(GregTechTags.HARD_HAMMER));
            int size = recipe.getRecipeWidth() * recipe.getRecipeHeight();
            return Stream.of(new ShapedRecipe(id, recipe.getGroup(), recipe.getRecipeWidth(), recipe.getRecipeHeight(), adjustListSize(ingredients, size), recipe.getResultItem()));
        }
        return Stream.of(recipe);
    }

    // Workaround for IC2 AdvRecipe having more ingredients than its size allows
    private static NonNullList<Ingredient> adjustListSize(NonNullList<Ingredient> list, int maxSize) {
        NonNullList<Ingredient> adjusted = NonNullList.withSize(maxSize, Ingredient.EMPTY);
        for (int i = 0; i < maxSize; i++) {
            adjusted.set(i, list.get(i));
        }
        return adjusted;
    }

    @Nullable
    private NonNullList<Ingredient> toPlates(NonNullList<Ingredient> ingredients, boolean allowPartial) {
        NonNullList<Ingredient> list = NonNullList.withSize(ingredients.size(), Ingredient.EMPTY);
        boolean applied = false;
        for (int i = 0; i < ingredients.size(); i++) {
            Ingredient ingredient = ingredients.get(i);
            if (!ingredient.isEmpty()) {
                Ingredient plate = toPlate(ingredient);
                if (plate != null) {
                    list.set(i, plate);
                    applied = true;
                }
                else if (!allowPartial) {
                    return null;
                }
                else {
                    list.set(i, ingredient);
                }
            }
        }
        return applied ? list : null;
    }

    @Nullable
    private Ingredient toPlate(Ingredient ingredient) {
        Set<Ingredient.TagValue> tagValues = StreamEx.of(ingredient.getItems())
            .map(this::getPlateForIngot)
            .nonNull()
            .distinct()
            .map(Ingredient.TagValue::new)
            .toSet();
        return !tagValues.isEmpty() ? Ingredient.fromValues(StreamEx.of(tagValues)) : null;
    }

    private TagKey<Item> getPlateForIngot(ItemStack stack) {
        return EntryStream.of(this.ingotToPlate)
            .findFirst(e -> stack.is(e.getKey()))
            .map(Map.Entry::getValue)
            .orElse(null);
    }

    private static ResourceLocation replacedId(ResourceLocation id, String... subpath) {
        String prefix = StreamEx.of("replaced")
            .append(subpath)
            .joining("/");
        return location(prefix + "/" + id.getNamespace() + "/" + id.getPath());
    }
}
