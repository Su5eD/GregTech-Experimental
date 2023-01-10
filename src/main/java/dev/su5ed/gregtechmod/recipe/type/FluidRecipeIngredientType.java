package dev.su5ed.gregtechmod.recipe.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import one.util.streamex.StreamEx;

import java.util.ArrayList;
import java.util.List;

public class FluidRecipeIngredientType implements RecipeIngredientType<FluidRecipeIngredient> {
    @Override
    public FluidRecipeIngredient create(JsonElement json) {
        if (json.isJsonObject()) {
            JsonObject obj = json.getAsJsonObject();
            if (obj.has("fluid")) {
                JsonObject fluid = GsonHelper.getAsJsonObject(obj, "fluid");
                FluidStack fluidStack = deserializeFluid(fluid);
                return FluidRecipeIngredient.fluidStack(fluidStack);
            }
            else if (obj.has("fluids")) {
                JsonObject fluidsJson = GsonHelper.getAsJsonObject(obj, "fluids");
                List<Fluid> fluids = StreamEx.of(GsonHelper.getAsJsonArray(fluidsJson, "names").iterator())
                    .map(element -> {
                        ResourceLocation location = new ResourceLocation(element.getAsString());
                        return ForgeRegistries.FLUIDS.getValue(location);
                    })
                    .toList();
                int amount = GsonHelper.getAsInt(fluidsJson, "amount");
                return FluidRecipeIngredient.fluids(fluids, amount);
            }
            // TODO other types
        }
        throw new IllegalArgumentException();
    }

    @Override
    public FluidRecipeIngredient create(FriendlyByteBuf buffer) {
        String type = buffer.readUtf();
        if (type.equals("fluid")) {
            FluidStack fluidStack = buffer.readFluidStack();
            return FluidRecipeIngredient.fluidStack(fluidStack);
        }
        else if (type.equals("fluids")) {
            int fluidCount = buffer.readInt();
            List<Fluid> fluids = new ArrayList<>(fluidCount);
            for (int i = 0; i < fluidCount; i++) {
                fluids.add(buffer.readRegistryId());
            }
            int amount = buffer.readInt();
            return FluidRecipeIngredient.fluids(fluids, amount);
        }
        throw new IllegalArgumentException();
    }

    public static FluidStack deserializeFluid(JsonObject json) {
        JsonElement count = json.get("amount");
        int amount = count.getAsJsonPrimitive().getAsInt();
        ResourceLocation resourceLocation = new ResourceLocation(GsonHelper.getAsString(json, "fluid"));
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(resourceLocation);
        if (fluid == null || fluid == Fluids.EMPTY) {
            throw new IllegalArgumentException("Fluid '" + resourceLocation + "' not found");
        }
        return new FluidStack(fluid, amount);
    }
}
