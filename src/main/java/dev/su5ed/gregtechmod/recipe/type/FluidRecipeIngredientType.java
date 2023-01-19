package dev.su5ed.gregtechmod.recipe.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.su5ed.gregtechmod.util.FluidProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import one.util.streamex.StreamEx;

import java.util.ArrayList;
import java.util.List;

public class FluidRecipeIngredientType implements RecipeIngredientType<FluidRecipeIngredient> {
    public FluidRecipeIngredient of(FluidProvider provider, int amount) {
        return of(new FluidStack(provider.getSourceFluid(), amount));
    }
    
    public FluidRecipeIngredient of(FluidStack stack) {
        return new FluidRecipeIngredient(stack);
    }

    public FluidRecipeIngredient of(List<Fluid> fluids, int amount) {
        return new FluidRecipeIngredient(fluids, amount);
    }

    public FluidRecipeIngredient of(TagKey<Fluid> tag, int amount) {
        return new FluidRecipeIngredient(tag, amount);
    }

    @Override
    public FluidRecipeIngredient create(JsonElement json) {
        if (json.isJsonObject()) {
            JsonObject obj = json.getAsJsonObject();
            if (obj.has(FluidRecipeIngredient.FluidStackValue.NAME)) {
                JsonObject fluid = GsonHelper.getAsJsonObject(obj, FluidRecipeIngredient.FluidStackValue.NAME);
                FluidStack fluidStack = deserializeFluid(fluid);
                return of(fluidStack);
            }
            else if (obj.has(FluidRecipeIngredient.FluidsValue.NAME)) {
                JsonObject fluidsJson = GsonHelper.getAsJsonObject(obj, FluidRecipeIngredient.FluidsValue.NAME);
                List<Fluid> fluids = StreamEx.of(GsonHelper.getAsJsonArray(fluidsJson, "names").iterator())
                    .map(element -> {
                        ResourceLocation location = new ResourceLocation(element.getAsString());
                        return ForgeRegistries.FLUIDS.getValue(location);
                    })
                    .toList();
                int amount = GsonHelper.getAsInt(fluidsJson, "amount");
                return of(fluids, amount);
            }
            else if (obj.has(FluidRecipeIngredient.FluidTagValue.NAME)) {
                JsonObject fluidTagJson = GsonHelper.getAsJsonObject(obj, FluidRecipeIngredient.FluidTagValue.NAME);
                String tagName = GsonHelper.getAsString(fluidTagJson, "tag");
                TagKey<Fluid> tag = ForgeRegistries.FLUIDS.tags().createTagKey(new ResourceLocation(tagName));
                int amount = GsonHelper.getAsInt(fluidTagJson, "amount");
                return of(tag, amount);
            }
        }
        throw new IllegalArgumentException("Element must be a JSON object");
    }

    @Override
    public FluidRecipeIngredient create(FriendlyByteBuf buffer) {
        String type = buffer.readUtf();
        return switch (type) {
            case FluidRecipeIngredient.FluidStackValue.NAME -> {
                FluidStack fluidStack = buffer.readFluidStack();
                yield of(fluidStack);
            }
            case FluidRecipeIngredient.FluidsValue.NAME -> {
                int fluidCount = buffer.readInt();
                List<Fluid> fluids = new ArrayList<>(fluidCount);
                for (int i = 0; i < fluidCount; i++) {
                    fluids.add(buffer.readRegistryId());
                }
                int amount = buffer.readInt();
                yield of(fluids, amount);
            }
            case FluidRecipeIngredient.FluidTagValue.NAME -> {
                ResourceLocation tagName = buffer.readResourceLocation();
                TagKey<Fluid> tag = ForgeRegistries.FLUIDS.tags().createTagKey(tagName);
                int amount = buffer.readInt();
                yield of(tag, amount);
            }
            default -> throw new IllegalArgumentException("Unknown fluid recipe ingredient type '" + type + "'");
        };
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
