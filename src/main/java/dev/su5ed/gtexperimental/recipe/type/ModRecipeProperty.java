package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import dev.su5ed.gtexperimental.api.recipe.RecipeProperty;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

public final class ModRecipeProperty<T> implements RecipeProperty<T> {
    public static final ModRecipeProperty<Integer> DURATION = new ModRecipeProperty<>("duration", FriendlyByteBuf::writeInt, FriendlyByteBuf::readInt, JsonPrimitive::new, JsonElement::getAsInt, ModRecipeProperty::greaterThanZero);
    public static final ModRecipeProperty<Double> ENERGY_COST = new ModRecipeProperty<>("energy_cost", FriendlyByteBuf::writeDouble, FriendlyByteBuf::readDouble, JsonPrimitive::new, JsonElement::getAsDouble, ModRecipeProperty::greaterThanZero);
    public static final ModRecipeProperty<Double> START_ENERGY = new ModRecipeProperty<>("start_energy", FriendlyByteBuf::writeDouble, FriendlyByteBuf::readDouble, JsonPrimitive::new, JsonElement::getAsDouble, ModRecipeProperty::greaterThanZero);
    public static final ModRecipeProperty<Integer> TNT = new ModRecipeProperty<>("tnt", FriendlyByteBuf::writeInt, FriendlyByteBuf::readInt, JsonPrimitive::new, JsonElement::getAsInt, between(1, 64));

    private final String name;
    private final BiConsumer<FriendlyByteBuf, T> networkSerializer;
    private final Function<FriendlyByteBuf, T> networkDeserializer;
    private final Function<T, JsonElement> jsonSerializer;
    private final Function<JsonElement, T> jsonDeserializer;
    private final Predicate<T> validator;

    public ModRecipeProperty(String name, BiConsumer<FriendlyByteBuf, T> networkSerializer, Function<FriendlyByteBuf, T> networkDeserializer, Function<T, JsonElement> jsonSerializer, Function<JsonElement, T> jsonDeserializer, Predicate<T> validator) {
        this.name = name;
        this.networkSerializer = networkSerializer;
        this.networkDeserializer = networkDeserializer;
        this.jsonSerializer = jsonSerializer;
        this.jsonDeserializer = jsonDeserializer;
        this.validator = validator;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, T value) {
        this.networkSerializer.accept(buffer, value);
    }

    @Override
    public T fromNetwork(FriendlyByteBuf buffer) {
        return this.networkDeserializer.apply(buffer);
    }

    @Override
    public JsonElement toJson(T value) {
        return this.jsonSerializer.apply(value);
    }

    @Override
    public T fromJson(JsonElement element) {
        return this.jsonDeserializer.apply(element);
    }

    @Override
    public void validate(ResourceLocation id, T value) {
        if (!this.validator.test(value)) {
            throw new IllegalStateException("Invalid value " + value + " for property " + this.name + " in recipe " + id);
        }
    }

    private static <T extends Number> boolean greaterThanZero(T number) {
        return number.doubleValue() > 0;
    }
    
    private static <T extends Number> Predicate<T> between(int minInclusive, int maxInclusive) {
        return num -> num.doubleValue() >= minInclusive && num.doubleValue() <= maxInclusive;
    }
}
