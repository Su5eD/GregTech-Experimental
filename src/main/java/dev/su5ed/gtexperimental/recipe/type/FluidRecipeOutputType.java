package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.api.recipe.RecipeOutputType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

public class FluidRecipeOutputType implements RecipeOutputType<FluidStack> {
    @Override
    public void toNetwork(FriendlyByteBuf buffer, FluidStack value) {
        buffer.writeFluidStack(value);
    }

    @Override
    public FluidStack fromNetwork(FriendlyByteBuf buffer) {
        return buffer.readFluidStack();
    }

    @Override
    public JsonElement toJson(FluidStack value) {
        JsonObject json = new JsonObject();
        json.addProperty("fluid", ForgeRegistries.FLUIDS.getKey(value.getFluid()).toString());
        json.addProperty("amount", value.getAmount());
        return json;
    }

    @Override
    public FluidStack fromJson(JsonElement element) {
        JsonObject json = element.getAsJsonObject();
        String name = GsonHelper.getAsString(json, "fluid");
        Fluid fluid = RecipeUtil.deserializeFluid(name);
        int amount = GsonHelper.getAsInt(json, "amount");
        return new FluidStack(fluid, amount);
    }

    @Override
    public void validate(ResourceLocation id, String name, FluidStack value) {
        if (value.isEmpty()) {
            throw new RuntimeException("Empty " + name + " fluid in recipe " + id);
        }
    }
}
