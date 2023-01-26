package dev.su5ed.gtexperimental.recipe.setup;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import dev.su5ed.gtexperimental.api.recipe.RecipeOutputType;
import dev.su5ed.gtexperimental.recipe.type.FluidRecipeOutputType;
import dev.su5ed.gtexperimental.recipe.type.ItemRecipeOutputType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import one.util.streamex.StreamEx;

import java.util.List;

public final class ModRecipeOutputTypes {
    public static final RecipeOutputType<ItemStack> ITEM = new ItemRecipeOutputType();
    public static final RecipeOutputType<FluidStack> FLUID = new FluidRecipeOutputType();

    public static <T> List<T> fromNetwork(List<? extends RecipeOutputType<T>> outputTypes, FriendlyByteBuf buffer) {
        int outputCount = buffer.readInt();
        if (outputTypes.size() >= outputCount) {
            return StreamEx.of(outputTypes)
                .limit(outputCount)
                .map(type -> type.fromNetwork(buffer))
                .toList();
        }
        throw new IllegalArgumentException("There are more outputs than known output types");
    }

    public static <T> void toNetwork(List<? extends RecipeOutputType<T>> outputTypes, List<T> outputs, FriendlyByteBuf buffer) {
        if (outputTypes.size() >= outputs.size()) {
            buffer.writeInt(outputs.size());
            for (int i = 0; i < outputs.size(); i++) {
                RecipeOutputType<T> outputType = outputTypes.get(i);
                outputType.toNetwork(buffer, outputs.get(i));
            }
        }
        throw new IllegalArgumentException("There are more outputs than known output types");
    }
    
    public static <T> JsonElement toJson(List<? extends RecipeOutputType<T>> outputTypes, List<T> outputs) {
        if (outputTypes.size() >= outputs.size()) {
            JsonArray json = new JsonArray(outputs.size());
            for (int i = 0; i < outputs.size(); i++) {
                json.add(outputTypes.get(i).toJson(outputs.get(i)));
            }
            return json;
        }
        throw new IllegalArgumentException("There are more outputs than known output types");
    }

    private ModRecipeOutputTypes() {}
}
