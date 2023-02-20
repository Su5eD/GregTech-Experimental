package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import dev.su5ed.gtexperimental.api.recipe.RecipeOutputType;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeOutputTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class HybridRecipeOutputType implements RecipeOutputType<Either<ItemStack, FluidStack>> {
    
    @Override
    public void toNetwork(FriendlyByteBuf buffer, Either<ItemStack, FluidStack> value) {
        buffer.writeEither(value, ModRecipeOutputTypes.ITEM::toNetwork, ModRecipeOutputTypes.FLUID::toNetwork);
    }

    @Override
    public JsonObject toJson(Either<ItemStack, FluidStack> value) {
        return value.map(ModRecipeOutputTypes.ITEM::toJson, ModRecipeOutputTypes.FLUID::toJson);
    }

    @Override
    public Either<ItemStack, FluidStack> fromNetwork(FriendlyByteBuf buffer) {
        return buffer.readEither(ModRecipeOutputTypes.ITEM::fromNetwork, ModRecipeOutputTypes.FLUID::fromNetwork);
    }

    @Override
    public Either<ItemStack, FluidStack> fromJson(JsonObject json) {
        return json.has("item")
            ? Either.left(ModRecipeOutputTypes.ITEM.fromJson(json))
            : Either.right(ModRecipeOutputTypes.FLUID.fromJson(json));
    }

    @Override
    public void validate(ResourceLocation id, String name, Either<ItemStack, FluidStack> value, boolean allowEmpty) {
        value.ifLeft(item -> ModRecipeOutputTypes.ITEM.validate(id, name, item, allowEmpty))
            .ifRight(fluid -> ModRecipeOutputTypes.FLUID.validate(id, name, fluid, allowEmpty));
    }
}
