package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Pair;
import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.api.recipe.BaseRecipe;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredientType;
import dev.su5ed.gtexperimental.compat.ModHandler;
import dev.su5ed.gtexperimental.object.ModFluid;
import dev.su5ed.gtexperimental.recipe.crafting.FluidItemPredicate;
import dev.su5ed.gtexperimental.recipe.crafting.ToolCraftingIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.util.GtUtil;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static dev.su5ed.gtexperimental.util.GtUtil.buckets;

public final class RecipeUtil {
    public static final RecipeIngredient<FluidStack> WATER = ModRecipeIngredientTypes.FLUID.of(Fluids.WATER, buckets(1));
    public static final RecipeIngredient<FluidStack> MERCURY = ModRecipeIngredientTypes.FLUID.of(ModFluid.MERCURY, buckets(1));
    private static final List<String> MOD_PRIORITY = List.of(ModHandler.FTBIC_MODID, ModHandler.IC2_MODID, Reference.MODID);
    private static final ResourceLocation EMPTY = new ResourceLocation("empty");

    public static <T> List<? extends RecipeIngredient<T>> parseInputs(RecipeIngredientType<? extends RecipeIngredient<T>> inputType, int inputCount, JsonArray json) {
        if (!json.isEmpty() && inputCount >= json.size()) {
            return StreamEx.of(json.getAsJsonArray().iterator())
                .map(JsonElement::getAsJsonObject)
                .map(inputType::create)
                .toList();
        }
        throw new IllegalArgumentException("Expected " + inputCount + " inputs, got " + json.size());
    }

    public static ItemStack parseItemStack(JsonElement json) {
        if (json.isJsonObject()) {
            return CraftingHelper.getItemStack(json.getAsJsonObject(), true, false);
        }
        else {
            String resultJson = json.getAsString();
            ResourceLocation name = new ResourceLocation(resultJson);
            return new ItemStack(Optional.ofNullable(ForgeRegistries.ITEMS.getValue(name)).orElseThrow(() -> new IllegalStateException("Item: " + resultJson + " does not exist")));
        }
    }

    public static void validateInput(ResourceLocation id, String name, RecipeIngredient<?> ingredient) {
        if (ingredient.isEmpty()) {
            throw new RuntimeException("Empty " + name + " ingredient in recipe " + id);
        }
    }

    public static void validateInputList(ResourceLocation id, String name, List<? extends RecipeIngredient<?>> ingredients, int maxSize) {
        if (ingredients.isEmpty()) {
            throw new RuntimeException("Empty " + name + " for recipe " + id);
        }
        else if (ingredients.size() > maxSize) {
            throw new RuntimeException(name + " exceeded max size of " + maxSize + " for recipe " + id);
        }
        else if (StreamEx.of(ingredients).allMatch(RecipeIngredient::isEmpty)) {
            throw new RuntimeException(name + " contained no ingredients for recipe " + id);
        }
    }

    public static <R extends BaseRecipe<?, ?, ?, ? super R>> int compareCount(R first, R second) {
        return second.compareInputCount(first);
    }

    public static JsonArray serializeConditions(Collection<ICondition> conditions) {
        JsonArray jsonConditions = new JsonArray();
        for (ICondition condition : conditions) {
            jsonConditions.add(CraftingHelper.serialize(condition));
        }
        return jsonConditions;
    }

    public static Fluid deserializeFluid(String name) {
        ResourceLocation location = new ResourceLocation(name);
        if (location.equals(EMPTY)) {
            return Fluids.EMPTY;
        }
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(location);
        if (fluid == null || fluid == Fluids.EMPTY) {
            throw new IllegalArgumentException("Fluid '" + location + "' not found");
        }
        return fluid;
    }

    public static String createName(ItemLike input, ItemStack output) {
        return "%s_to_%s".formatted(GtUtil.itemName(input), GtUtil.itemName(output));
    }

    public static String createName(TagKey<Item> input, ItemStack output) {
        return "%s_to_%s".formatted(GtUtil.tagName(input), GtUtil.itemName(output));
    }

    public static EntryStream<TagKey<Item>, Pair<TagKey<Item>, Item>> associateTags(String source, String dest) {
        ITagManager<Item> tags = ForgeRegistries.ITEMS.tags();
        return StreamEx.of(tags.getTagNames())
            .filter(key -> key.location().getNamespace().equals("forge") && key.location().getPath().startsWith(source + "/"))
            .mapToEntry(key -> findAssociatedTag(key, source, dest).orElse(null))
            .nonNullValues();
    }

    public static Optional<Pair<TagKey<Item>, Item>> findAssociatedTag(TagKey<Item> tag, String material, String replacement) {
        ITagManager<Item> tags = ForgeRegistries.ITEMS.tags();
        TagKey<Item> key = tags.createTagKey(new ResourceLocation(tag.location().toString().replaceFirst(material, replacement)));
        return tags.isKnownTagName(key) ? findFirstItem(key).map(item -> Pair.of(key, item)) : Optional.empty();
    }

    public static Optional<Item> findFirstItem(TagKey<Item> tag) {
        ITagManager<Item> tags = ForgeRegistries.ITEMS.tags();
        return tags.getTag(tag).stream()
            .min(Comparator.comparingInt(item -> {
                String namespace = ForgeRegistries.ITEMS.getKey(item).getNamespace();
                return MOD_PRIORITY.indexOf(namespace);
            }));
    }

    public static Stream<? extends Ingredient.Value> ingredientFromNetwork(FriendlyByteBuf buffer) {
        var size = buffer.readVarInt();
        return Stream.generate(() -> new Ingredient.ItemValue(buffer.readItem())).limit(size);
    }

    public static Stream<? extends Ingredient.Value> ingredientFromJson(@Nullable JsonElement json) {
        if (json != null && !json.isJsonNull()) {
            if (json.isJsonObject()) {
                return Stream.of(Ingredient.valueFromJson(json.getAsJsonObject()));
            }
            else if (json.isJsonArray()) {
                JsonArray array = json.getAsJsonArray();
                if (array.size() == 0) {
                    throw new JsonSyntaxException("Item array cannot be empty, at least one item must be defined");
                }
                else {
                    return StreamSupport.stream(array.spliterator(), false)
                        .map(element -> Ingredient.valueFromJson(GsonHelper.convertToJsonObject(element, "item")));
                }
            }
            else {
                throw new JsonSyntaxException("Expected item to be object or array of objects");
            }
        }
        else {
            throw new JsonSyntaxException("Item cannot be null");
        }
    }

    public static NonNullList<ItemStack> getRemainingToolItems(NonNullList<Ingredient> ingredients, CraftingContainer container) {
        return getRemainingItems(ingredients, container, (ingredient, stack) -> {
            if (ingredient instanceof ToolCraftingIngredient tool && tool.test(stack)) {
                return Optional.of(tool.damageItem(stack));
            }
            return Optional.empty();
        });
    }

    public static NonNullList<ItemStack> getRemainingFluidItems(NonNullList<Ingredient> ingredients, CraftingContainer container) {
        return getRemainingItems(ingredients, container, (ingredient, stack) -> {
            if (ingredient instanceof VanillaFluidIngredient fluid && fluid.test(stack)) {
                fluid.drainFluid(stack);
                return Optional.of(stack);
            }
            return Optional.empty();
        });
    }

    public static NonNullList<ItemStack> getRemainingItems(NonNullList<Ingredient> ingredients, CraftingContainer container, BiFunction<Ingredient, ItemStack, Optional<ItemStack>> processor) {
        NonNullList<ItemStack> list = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
        outer:
        for (int i = 0; i < list.size(); i++) {
            ItemStack stack = container.getItem(i);
            for (Ingredient ingredient : ingredients) {
                ItemStack processed = processor.apply(ingredient, stack).orElse(ItemStack.EMPTY);
                if (!processed.isEmpty()) {
                    list.set(i, stack.copy());
                    continue outer;
                }
            }
            if (stack.hasCraftingRemainingItem()) {
                list.set(i, stack.getCraftingRemainingItem());
            }
        }
        return list;
    }

    @SafeVarargs
    public static Ingredient tagsIngredient(TagKey<Item>... tags) {
        return Ingredient.fromValues(Stream.of(tags).map(Ingredient.TagValue::new));
    }

    @SafeVarargs
    public static InventoryChangeTrigger.TriggerInstance hasTags(TagKey<Item>... tags) {
        ItemPredicate.Builder builder = ItemPredicate.Builder.item();
        for (TagKey<Item> tag : tags) {
            builder.of(tag);
        }
        return inventoryTrigger(builder.build());
    }

    public static InventoryChangeTrigger.TriggerInstance hasFluid(TagKey<Fluid> tag) {
        return inventoryTrigger(FluidItemPredicate.of(tag));
    }

    public static InventoryChangeTrigger.TriggerInstance inventoryTrigger(ItemPredicate... predicates) {
        return new InventoryChangeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, predicates);
    }

    public static class Shape {
        private final Map<Character, ItemStack> keys;
        private final String pattern;
        private final int width;
        private final int height;

        public Shape(String... pattern) {
            this(Map.of(), pattern);
        }
        
        public Shape(Map<Character, ItemLike> keys, String... pattern) {
            this.keys = EntryStream.of(keys).mapValues(ItemStack::new).toMap();
            this.pattern = String.join("", pattern);
            this.width = StreamEx.of(pattern).mapToInt(String::length).max().orElseThrow();
            this.height = pattern.length;
        }

        public boolean matches(IShapedRecipe<?> recipe) {
            try {
                Map<Character, Ingredient> patternIngredient = new HashMap<>();
                NonNullList<Ingredient> ingredients = recipe.getIngredients();
                if (ingredients.size() >= this.pattern.length() && recipe.getRecipeWidth() == this.width && recipe.getRecipeHeight() == this.height) {
                    for (int i = 0; i < this.pattern.length(); i++) {
                        char ch = this.pattern.charAt(i);
                        boolean empty = ch == ' ';
                        final int finalIndex = i;
                        Ingredient ingredient = ingredients.get(i);
                        Ingredient matchingIngredient = patternIngredient.computeIfAbsent(ch, c -> ingredients.get(finalIndex));
                        if (ingredient.isEmpty() != empty || !empty && ingredient != matchingIngredient || this.keys.containsKey(ch) && !matchingIngredient.test(this.keys.get(ch))) {
                            return false;
                        }
                    }
                    for (int i = this.pattern.length(); i < ingredients.size(); i++) {
                        if (!ingredients.get(i).isEmpty()) {
                            return false;
                        }
                    }
                    return true;
                }
            } catch (Throwable ignored) {

            }
            return false;
        }
    }

    private RecipeUtil() {}
}
