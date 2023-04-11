package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.api.recipe.RecipeProperties;
import dev.su5ed.gtexperimental.api.recipe.RecipeProperty;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import one.util.streamex.StreamEx;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class RecipePropertyMap implements RecipeProperties {
    private static final RecipePropertyMap EMPTY = new RecipePropertyMap(Map.of());

    private final Map<RecipeProperty<?>, Object> properties;
    private final Map<RecipeProperty<?>, Object> transientProperties;

    public RecipePropertyMap(Map<RecipeProperty<?>, Object> properties) {
        this(properties, Map.of());
    }

    public RecipePropertyMap(Map<RecipeProperty<?>, Object> properties, Map<RecipeProperty<?>, Object> transientProperties) {
        this.properties = Collections.unmodifiableMap(properties);
        this.transientProperties = Collections.unmodifiableMap(transientProperties);
    }

    @Override
    public <T> T get(RecipeProperty<T> property) {
        return getOptional(property)
            .orElseThrow(() -> new IllegalArgumentException("Missing value for property " + property.getName()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Optional<T> getOptional(RecipeProperty<T> property) {
        return (Optional<T>) Optional.<Object>ofNullable(this.transientProperties.get(property))
            .or(() -> Optional.ofNullable(this.properties.get(property)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeInt(this.properties.size());
        this.properties.forEach((property, value) -> {
            buffer.writeUtf(property.getName());
            ((RecipeProperty<Object>) property).toNetwork(buffer, value);
        });
        buffer.writeInt(this.transientProperties.size());
        this.transientProperties.forEach((property, value) -> {
            buffer.writeUtf(property.getName());
            ((RecipeProperty<Object>) property).toNetwork(buffer, value);
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public void toJson(JsonObject json) {
        this.properties.forEach((property, value) -> {
            JsonElement element = ((RecipeProperty<Object>) property).toJson(value);
            json.add(property.getName(), element);
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();
        this.properties.forEach((property, value) -> {
            Tag nbt = ((RecipeProperty<Object>) property).toNBT(value);
            tag.put(property.getName(), nbt);
        });
        return tag;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void validate(ResourceLocation id, List<RecipeProperty<?>> expected) {
        for (RecipeProperty<?> property : expected) {
            if (!this.properties.containsKey(property) && !this.transientProperties.containsKey(property)) {
                throw new IllegalStateException("Recipe " + id + " missing property " + property.getName());
            }
        }
        this.properties.forEach((property, value) -> ((RecipeProperty<Object>) property).validate(id, value));
        this.transientProperties.forEach((property, value) -> ((RecipeProperty<Object>) property).validate(id, value));
    }

    public static RecipePropertyMap fromNBT(ResourceLocation id, List<RecipeProperty<?>> properties, CompoundTag tag) {
        return deserialize(id, properties, tag, CompoundTag::get, RecipeProperty::fromNBT);
    }

    public static RecipePropertyMap fromJson(ResourceLocation id, List<RecipeProperty<?>> properties, JsonObject json) {
        return deserialize(id, properties, json, JsonObject::get, RecipeProperty::fromJson);
    }

    private static <T, U> RecipePropertyMap deserialize(ResourceLocation id, List<RecipeProperty<?>> properties, T serialized, BiFunction<T, String, U> getter, BiFunction<RecipeProperty<?>, U, Object> mapper) {
        Map<RecipeProperty<?>, Object> map = StreamEx.of(properties)
            .mapToEntry(property -> {
                U element = getter.apply(serialized, property.getName());
                if (element == null) {
                    throw new IllegalStateException("Recipe " + id + " missing property " + property.getName());
                }
                return mapper.apply(property, element);
            })
            .toMap();
        return new RecipePropertyMap(map);
    }

    public static RecipePropertyMap fromNetwork(ResourceLocation id, List<RecipeProperty<?>> propertyKeys, FriendlyByteBuf buffer) {
        int size = buffer.readInt();
        if (size >= propertyKeys.size()) {
            Map<RecipeProperty<?>, Object> properties = readNetworkProps(buffer, size);
            Map<RecipeProperty<?>, Object> transientProperties = readNetworkProps(buffer, buffer.readInt());
            RecipePropertyMap propertyMap = new RecipePropertyMap(properties, transientProperties);
            propertyMap.validate(id, propertyKeys);
            return propertyMap;
        }
        throw new IllegalArgumentException("Expected " + propertyKeys.size() + " properties, received " + size);
    }

    private static Map<RecipeProperty<?>, Object> readNetworkProps(FriendlyByteBuf buf, int size) {
        Map<RecipeProperty<?>, Object> map = new HashMap<>();
        for (int i = 0; i < size; i++) {
            String name = buf.readUtf();
            RecipeProperty<?> property = ModRecipeProperty.getByName(name);
            map.put(property, property.fromNetwork(buf));
        }
        return map;
    }

    public static RecipePropertyMap empty() {
        return EMPTY;
    }

    public static Builder builder() {
        return new Builder();
    }

    public RecipePropertyMap withTransient(Consumer<Builder> builderConsumer) {
        Builder builder = new Builder(this.transientProperties);
        builderConsumer.accept(builder);
        return builder.buildTransient(this.properties);
    }

    public static class Builder {
        protected final Map<RecipeProperty<?>, Object> properties;

        public Builder() {
            this.properties = new HashMap<>();
        }

        public Builder(Map<RecipeProperty<?>, Object> existing) {
            this.properties = new HashMap<>(existing);
        }

        public Builder duration(int duration) {
            return put(ModRecipeProperty.DURATION, duration);
        }

        public Builder energyCost(double energyCost) {
            return put(ModRecipeProperty.ENERGY_COST, energyCost);
        }

        public Builder startEnergy(double startEnergy) {
            return put(ModRecipeProperty.START_ENERGY, startEnergy);
        }

        public Builder tnt(int tnt) {
            return put(ModRecipeProperty.TNT, tnt);
        }

        public Builder heat(int heat) {
            return put(ModRecipeProperty.HEAT, heat);
        }

        public Builder chance(int chance) {
            return put(ModRecipeProperty.CHANCE, chance);
        }

        public Builder energy(double energy) {
            return put(ModRecipeProperty.ENERGY, energy);
        }

        private <T> Builder put(RecipeProperty<T> property, T value) {
            this.properties.put(property, value);
            return this;
        }

        public RecipePropertyMap build() {
            return new RecipePropertyMap(this.properties);
        }

        public RecipePropertyMap buildTransient(Map<RecipeProperty<?>, Object> existing) {
            return new RecipePropertyMap(existing, this.properties);
        }
    }
}
