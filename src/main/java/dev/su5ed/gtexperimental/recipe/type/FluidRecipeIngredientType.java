package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.util.FluidProvider;
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
            int amount = GsonHelper.getAsInt(obj, "amount");
            if (obj.has(FluidRecipeIngredient.FluidStackValue.NAME)) {
                String fluidName = GsonHelper.getAsString(obj, FluidRecipeIngredient.FluidStackValue.NAME);
                Fluid fluid = deserializeFluid(fluidName);
                return of(new FluidStack(fluid, amount));
            }
            else if (obj.has(FluidRecipeIngredient.FluidsValue.NAME)) {
                JsonArray fluidsJson = GsonHelper.getAsJsonArray(obj, FluidRecipeIngredient.FluidsValue.NAME);
                List<Fluid> fluids = StreamEx.of(fluidsJson.iterator())
                    .map(element -> {
                        ResourceLocation location = new ResourceLocation(element.getAsString());
                        return ForgeRegistries.FLUIDS.getValue(location);
                    })
                    .toList();
                return of(fluids, amount);
            }
            else if (obj.has(FluidRecipeIngredient.FluidTagValue.NAME)) {
                String tagName = GsonHelper.getAsString(obj, FluidRecipeIngredient.FluidTagValue.NAME);
                TagKey<Fluid> tag = ForgeRegistries.FLUIDS.tags().createTagKey(new ResourceLocation(tagName));
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

    public static Fluid deserializeFluid(String name) {
        ResourceLocation resourceLocation = new ResourceLocation(name);
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(resourceLocation);
        if (fluid == null || fluid == Fluids.EMPTY) {
            throw new IllegalArgumentException("Fluid '" + resourceLocation + "' not found");
        }
        return fluid;
    }
}
