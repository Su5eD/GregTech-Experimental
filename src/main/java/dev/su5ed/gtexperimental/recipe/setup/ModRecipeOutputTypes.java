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

    public static <T> List<T> fromNetwork(RecipeOutputType<T> outputType, int outputTypeCount, FriendlyByteBuf buffer) {
        int outputCount = buffer.readInt();
        if (outputTypeCount >= outputCount) {
            return StreamEx.generate(() -> outputType.fromNetwork(buffer))
                .limit(outputCount)
                .toList();
        }
        throw new IllegalArgumentException("There are more outputs than known output types");
    }

    public static <T> void toNetwork(RecipeOutputType<T> outputType, int outputCount, List<T> outputs, FriendlyByteBuf buffer) {
        if (outputCount >= outputs.size()) {
            buffer.writeInt(outputs.size());
            for (T output : outputs) {
                outputType.toNetwork(buffer, output);
            }
        }
        throw new IllegalArgumentException("There are more outputs than known output types");
    }

    public static <T> JsonElement toJson(RecipeOutputType<T> outputType, int outputCount, List<T> outputs) {
        if (outputCount >= outputs.size()) {
            JsonArray json = new JsonArray(outputs.size());
            for (T output : outputs) {
                json.add(outputType.toJson(output));
            }
            return json;
        }
        throw new IllegalArgumentException("There are more outputs than known output types");
    }

    private ModRecipeOutputTypes() {}
}
