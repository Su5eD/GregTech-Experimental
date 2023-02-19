package dev.su5ed.gtexperimental.recipe.crafting;

import com.google.common.base.Stopwatch;
import com.mojang.datafixers.util.Pair;
import dev.su5ed.gtexperimental.GregTechConfig;
import dev.su5ed.gtexperimental.GregTechMod;
import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.recipe.type.RecipeUtil;
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

import static dev.su5ed.gtexperimental.api.Reference.location;

public class RecipeReplacer {
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

    private final ReloadableServerResources resources;
    private final Map<TagKey<Item>, TagKey<Item>> ingotToPlate;

    public RecipeReplacer(ReloadableServerResources resources) {
        this.resources = resources;
        this.ingotToPlate = RecipeUtil.associateTags("ingots", "plates")
            .mapValues(Pair::getFirst)
            .toMap();
    }

    public void run() {
        GregTechMod.LOGGER.info("Processing recipes...");
        RecipeManager manager = this.resources.getRecipeManager();
        Stopwatch stopwatch = Stopwatch.createStarted();
        Collection<Recipe<?>> modifiedRecipes = StreamEx.of(manager.getRecipes())
            .flatMap(this::mapCraftingRecipe)
            .append(getAdditionalRecipes())
            .toList();
        manager.replaceRecipes(modifiedRecipes);
        stopwatch.stop();
        GregTechMod.LOGGER.info("Finished processing recipes in {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    public Stream<Recipe<?>> mapCraftingRecipe(Recipe<?> recipe) {
        if (recipe instanceof IShapedRecipe<?> shapedRecipe) {
            // Replace plank -> stick recipes
            if (shapedRecipe.getRecipeWidth() == 1 && shapedRecipe.getRecipeHeight() == 2
                && recipe.getResultItem().is(Tags.Items.RODS_WOODEN)
                && recipe.getIngredients().size() == 2 && StreamEx.of(recipe.getIngredients()).flatMap(i -> Stream.of(i.getItems())).allMatch(stack -> stack.is(ItemTags.PLANKS))) {
                // Sawing
                ResourceLocation sawingId = replacedId(recipe.getId(), "sawing");
                NonNullList<Ingredient> ingredients = NonNullList.createWithCapacity(3);
                ingredients.add(ToolCraftingIngredient.of(GregTechTags.SAW, 1));
                ingredients.addAll(recipe.getIngredients());
                ItemStack result = recipe.getResultItem();
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
                if (SWORD_SHAPE.matches(shapedRecipe) && recipe.getResultItem().is(Tags.Items.TOOLS_SWORDS)) {
                    NonNullList<Ingredient> ingredients = toPlates(recipe.getIngredients(), true);
                    if (ingredients != null) {
                        NonNullList<Ingredient> allIngredients = NonNullList.withSize(9, Ingredient.EMPTY);
                        allIngredients.set(1, ingredients.get(0));
                        allIngredients.set(3, Ingredient.of(GregTechTags.FILE));
                        allIngredients.set(4, ingredients.get(1));
                        allIngredients.set(5, Ingredient.of(GregTechTags.HARD_HAMMER));
                        allIngredients.set(7, ingredients.get(2));
                        return Stream.of(new ShapedRecipe(replacedId(recipe.getId()), recipe.getGroup(), 3, 3, allIngredients, recipe.getResultItem()));
                    }
                }
                else if (PICKAXE_SHAPE.matches(shapedRecipe) && recipe.getResultItem().is(Tags.Items.TOOLS_PICKAXES)) {
                    NonNullList<Ingredient> ingredients = recipe.getIngredients();
                    Ingredient plate = toPlate(ingredients.get(0));
                    if (plate != null) {
                        ingredients.set(0, plate);
                        ingredients.set(3, Ingredient.of(GregTechTags.FILE));
                        ingredients.set(5, Ingredient.of(GregTechTags.HARD_HAMMER));
                        return Stream.of(new ShapedRecipe(replacedId(recipe.getId()), recipe.getGroup(), shapedRecipe.getRecipeWidth(), shapedRecipe.getRecipeHeight(), ingredients, recipe.getResultItem()));
                    }
                }
                else if (SHOVEL_SHAPE.matches(shapedRecipe) && recipe.getResultItem().is(Tags.Items.TOOLS_SHOVELS)) {
                    NonNullList<Ingredient> ingredients = toPlates(recipe.getIngredients(), true);
                    if (ingredients != null) {
                        NonNullList<Ingredient> allIngredients = NonNullList.withSize(9, Ingredient.EMPTY);
                        allIngredients.set(0, Ingredient.of(GregTechTags.FILE));
                        allIngredients.set(1, ingredients.get(0));
                        allIngredients.set(2, Ingredient.of(GregTechTags.HARD_HAMMER));
                        allIngredients.set(4, ingredients.get(1));
                        allIngredients.set(7, ingredients.get(2));
                        return Stream.of(new ShapedRecipe(replacedId(recipe.getId()), recipe.getGroup(), 3, 3, allIngredients, recipe.getResultItem()));
                    }
                }
                else if (AXE_SHAPE.matches(shapedRecipe) && recipe.getResultItem().is(Tags.Items.TOOLS_AXES)) {
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
                        return Stream.of(new ShapedRecipe(replacedId(recipe.getId()), recipe.getGroup(), 3, 3, allIngredients, recipe.getResultItem()));
                    }
                }
                else if (HOE_SHAPE.matches(shapedRecipe) && recipe.getResultItem().is(Tags.Items.TOOLS_HOES)) {
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
                        return Stream.of(new ShapedRecipe(replacedId(recipe.getId()), recipe.getGroup(), 3, 3, allIngredients, recipe.getResultItem()));
                    }
                }
            }
        }
        return Stream.of(recipe);
    }

    public static Collection<Recipe<?>> getAdditionalRecipes() {
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

        return recipes;
    }

    private Stream<Recipe<?>> replaceArmorRecipe(IShapedRecipe<?> recipe, int hammerIndex) {
        ResourceLocation id = replacedId(recipe.getId());
        NonNullList<Ingredient> ingredients = toPlates(recipe.getIngredients(), false);
        if (ingredients != null) {
            ingredients.set(hammerIndex, Ingredient.of(GregTechTags.HARD_HAMMER));
            return Stream.of(new ShapedRecipe(id, recipe.getGroup(), recipe.getRecipeWidth(), recipe.getRecipeHeight(), ingredients, recipe.getResultItem()));
        }
        return Stream.of(recipe);
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
