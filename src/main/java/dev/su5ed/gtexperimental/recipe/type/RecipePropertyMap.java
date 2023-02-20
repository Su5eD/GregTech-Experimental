package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.api.recipe.RecipeProperty;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import one.util.streamex.StreamEx;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class RecipePropertyMap {
    private static final RecipePropertyMap EMPTY = new RecipePropertyMap(Map.of());

    private final Map<RecipeProperty<?>, ?> properties;

    public RecipePropertyMap(Map<RecipeProperty<?>, ?> properties) {
        this.properties = Collections.unmodifiableMap(properties);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(RecipeProperty<T> property) {
        T value = (T) this.properties.get(property);
        if (value == null) {
            throw new NullPointerException("Missing value for property " + property.getName());
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeInt(this.properties.size());
        this.properties.forEach((property, value) -> {
            buffer.writeUtf(property.getName());
            ((RecipeProperty<Object>) property).toNetwork(buffer, value);
        });
    }

    @SuppressWarnings("unchecked")
    public void toJson(JsonObject json) {
        this.properties.forEach((property, value) -> {
            JsonElement element = ((RecipeProperty<Object>) property).toJson(value);
            json.add(property.getName(), element);
        });
    }

    @SuppressWarnings("unchecked")
    public void validate(ResourceLocation id, List<RecipeProperty<?>> expected) {
        for (RecipeProperty<?> property : expected) {
            if (!this.properties.containsKey(property)) {
                throw new IllegalStateException("Recipe " + id + " missing property " + property.getName());
            }
        }
        this.properties.forEach((property, value) -> ((RecipeProperty<Object>) property).validate(id, value));
    }

    public static RecipePropertyMap fromJson(ResourceLocation id, List<RecipeProperty<?>> properties, JsonObject json) {
        Map<RecipeProperty<?>, Object> map = StreamEx.of(properties)
            .<Object>mapToEntry(property -> {
                JsonElement element = json.get(property.getName());
                if (element == null) {
                    throw new IllegalStateException("Recipe " + id + " missing property " + property.getName());
                }
                return property.fromJson(element);
            })
            .toMap();
        return new RecipePropertyMap(map);
    }

    public static RecipePropertyMap fromNetwork(List<RecipeProperty<?>> properties, FriendlyByteBuf buffer) {
        int size = buffer.readInt();
        if (properties.size() >= size) {
            Map<String, RecipeProperty<?>> nameToProp = StreamEx.of(properties)
                .mapToEntry(RecipeProperty::getName, Function.identity())
                .toMap();
            Map<RecipeProperty<?>, Object> propToValue = new HashMap<>();
            for (int i = 0; i < size; i++) {
                String name = buffer.readUtf();
                RecipeProperty<?> property = nameToProp.get(name);
                if (property == null) {
                    throw new IllegalArgumentException("Received unknown property " + name);
                }
                propToValue.put(property, property.fromNetwork(buffer));
            }
            return new RecipePropertyMap(propToValue);
        }
        throw new IllegalArgumentException("Expected " + properties.size() + " properties, received " + size);
    }

    public static RecipePropertyMap empty() {
        return EMPTY;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Map<RecipeProperty<?>, Object> properties;

        private Builder() {
            this.properties = new HashMap<>();
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

        private <T> Builder put(RecipeProperty<T> property, T value) {
            this.properties.put(property, value);
            return this;
        }

        public RecipePropertyMap build() {
            return new RecipePropertyMap(this.properties);
        }
    }
}
