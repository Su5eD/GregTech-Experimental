package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.api.recipe.RecipeOutputType;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

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
    public FluidStack fromJson(JsonElement json) {
        JsonObject obj = json.getAsJsonObject();
        String name = GsonHelper.getAsString(obj, "fluid");
        Fluid fluid = RecipeUtil.deserializeFluid(name);
        int amount = GsonHelper.getAsInt(obj, "amount");
        return new FluidStack(fluid, amount);
    }

    @Override
    public Tag toNBT(FluidStack value) {
        return FluidStack.CODEC.encodeStart(NbtOps.INSTANCE, value).getOrThrow(false, s -> {});
    }

    @Override
    public FluidStack fromNBT(Tag tag) {
        return FluidStack.CODEC.decode(NbtOps.INSTANCE, tag)
            .getOrThrow(false, s -> {})
            .getFirst();
    }

    @Override
    public FluidStack copy(FluidStack value) {
        return value.copy();
    }

    @Override
    public void validate(ResourceLocation id, String name, FluidStack value, boolean allowEmpty) {
        if (value == null) {
            throw new RuntimeException("Null " + name + " fluid in recipe " + id);
        }
        else if (!allowEmpty && value.isEmpty()) {
            throw new RuntimeException("Empty " + name + " fluid in recipe " + id);
        }
    }

    @Override
    public RecipeOutputType<List<FluidStack>> listOf(int count) {
        return new ListRecipeOutputType<>(this, count);
    }
}
