package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import dev.su5ed.gtexperimental.api.recipe.RecipeOutputType;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import one.util.streamex.StreamEx;

import java.util.List;

public class ListRecipeOutputType<T> implements RecipeOutputType<List<T>> {
    private final RecipeOutputType<T> outputType;
    private final int count;

    public ListRecipeOutputType(RecipeOutputType<T> outputType, int count) {
        this.outputType = outputType;
        this.count = count;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, List<T> value) {
        if (this.count >= value.size()) {
            buffer.writeInt(value.size());
            for (T output : value) {
                this.outputType.toNetwork(buffer, output);
            }
        }
        else {
            throw new IllegalArgumentException("There are more outputs than known output types");
        }
    }

    @Override
    public List<T> fromNetwork(FriendlyByteBuf buffer) {
        int outputCount = buffer.readInt();
        if (this.count >= outputCount) {
            return StreamEx.generate(() -> this.outputType.fromNetwork(buffer))
                .limit(outputCount)
                .toList();
        }
        throw new IllegalArgumentException("There are more outputs than known output types");
    }

    @Override
    public JsonArray toJson(List<T> value) {
        if (this.count >= value.size()) {
            JsonArray json = new JsonArray(value.size());
            for (T output : value) {
                json.add(this.outputType.toJson(output));
            }
            return json;
        }
        throw new IllegalArgumentException("There are more outputs than known output types");
    }

    @Override
    public List<T> fromJson(JsonElement json) {
        JsonArray array = json.getAsJsonArray();
        if (!array.isEmpty() && this.count >= array.size()) {
            return StreamEx.of(json.getAsJsonArray().iterator())
                .map(this.outputType::fromJson)
                .toList();
        }
        throw new IllegalArgumentException("Output type and json sizes differ");
    }

    @Override
    public Tag toNBT(List<T> value) {
        if (this.count >= value.size()) {
            ListTag list = new ListTag();
            for (T output : value) {
                list.add(this.outputType.toNBT(output));
            }
            return list;
        }
        throw new IllegalArgumentException("There are more outputs than known output types");
    }

    @Override
    public List<T> fromNBT(Tag tag) {
        ListTag list = (ListTag) tag;
        if (!list.isEmpty() && this.count >= list.size()) {
            return StreamEx.of(list.iterator())
                .map(this.outputType::fromNBT)
                .toList();
        }
        throw new IllegalArgumentException("Output type and tag list sizes differ");
    }

    @Override
    public List<T> copy(List<T> value) {
        return StreamEx.of(value)
            .map(this.outputType::copy)
            .toList();
    }

    @Override
    public void validate(ResourceLocation id, String name, List<T> value, boolean allowEmpty) {
        if (value.isEmpty()) {
            throw new RuntimeException("Empty " + name + " for recipe " + id);
        }
        else if (value.size() > this.count) {
            throw new RuntimeException(name + " exceeded max size of " + this.count + " for recipe " + id);
        }
        value.forEach(output -> this.outputType.validate(id, name, output, allowEmpty));
    }

    @Override
    public RecipeOutputType<List<List<T>>> listOf(int count) {
        return new ListRecipeOutputType<>(this, count);
    }
}
