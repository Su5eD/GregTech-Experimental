package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import dev.su5ed.gtexperimental.api.recipe.RecipeProperty;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

public final class ModRecipeProperty<T> implements RecipeProperty<T> {
    private static final Map<String, RecipeProperty<?>> PROPERTIES = new HashMap<>();
    public static final Codec<RecipeProperty<?>> CODEC = Codec.STRING.comapFlatMap(name -> {
        RecipeProperty<?> property = PROPERTIES.get(name);
        return property != null ? DataResult.success(property) : DataResult.error("Unknown recipe property " + name);
    }, RecipeProperty::getName);

    public static final ModRecipeProperty<Integer> DURATION = intProperty("duration", ModRecipeProperty::greaterThanZero);
    public static final ModRecipeProperty<Double> ENERGY_COST = doubleProperty("energy_cost", ModRecipeProperty::greaterThanZero);
    public static final ModRecipeProperty<Double> START_ENERGY = doubleProperty("start_energy", ModRecipeProperty::greaterThanZero);
    public static final ModRecipeProperty<Integer> TNT = intProperty("tnt", between(1, 64));
    public static final ModRecipeProperty<Integer> HEAT = intProperty("heat", num -> true);
    public static final ModRecipeProperty<Integer> CHANCE = intProperty("chance", num -> num >= 0);
    public static final ModRecipeProperty<Double> ENERGY = doubleProperty("energy", ModRecipeProperty::greaterThanZero);

    private final String name;
    private final BiConsumer<FriendlyByteBuf, T> networkSerializer;
    private final Function<FriendlyByteBuf, T> networkDeserializer;
    private final Codec<T> codec;
    private final Predicate<T> validator;

    public ModRecipeProperty(String name, BiConsumer<FriendlyByteBuf, T> networkSerializer, Function<FriendlyByteBuf, T> networkDeserializer, Codec<T> codec, Predicate<T> validator) {
        this.name = name;
        this.networkSerializer = networkSerializer;
        this.networkDeserializer = networkDeserializer;
        this.codec = codec;
        this.validator = validator;

        PROPERTIES.put(this.name, this);
    }

    public static RecipeProperty<?> getByName(String name) {
        RecipeProperty<?> property = PROPERTIES.get(name);
        if (property == null) {
            throw new IllegalArgumentException("Unknown recipe property " + name);
        }
        return property;
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
        return this.codec.encodeStart(JsonOps.INSTANCE, value).getOrThrow(false, s -> {});
    }

    @Override
    public T fromJson(JsonElement element) {
        return this.codec.decode(JsonOps.INSTANCE, element).map(Pair::getFirst).getOrThrow(false, s -> {});
    }

    @Override
    public Tag toNBT(T value) {
        return this.codec.encodeStart(NbtOps.INSTANCE, value).getOrThrow(false, s -> {});
    }

    @Override
    public T fromNBT(Tag tag) {
        return this.codec.decode(NbtOps.INSTANCE, tag).map(Pair::getFirst).getOrThrow(false, s -> {});
    }

    @Override
    public void validate(ResourceLocation id, T value) {
        if (!this.validator.test(value)) {
            throw new IllegalStateException("Invalid value " + value + " for property " + this.name + " in recipe " + id);
        }
    }

    @Override
    public String toString() {
        return "ModRecipeProperty{%s}".formatted(this.name);
    }

    private static ModRecipeProperty<Integer> intProperty(String name, Predicate<Integer> validator) {
        return new ModRecipeProperty<>(name, FriendlyByteBuf::writeInt, FriendlyByteBuf::readInt, Codec.INT, validator);
    }

    private static ModRecipeProperty<Double> doubleProperty(String name, Predicate<Double> validator) {
        return new ModRecipeProperty<>(name, FriendlyByteBuf::writeDouble, FriendlyByteBuf::readDouble, Codec.DOUBLE, validator);
    }

    private static <T extends Number> boolean greaterThanZero(T number) {
        return number.doubleValue() > 0;
    }

    private static <T extends Number> Predicate<T> between(int minInclusive, int maxInclusive) {
        return num -> num.doubleValue() >= minInclusive && num.doubleValue() <= maxInclusive;
    }

}
