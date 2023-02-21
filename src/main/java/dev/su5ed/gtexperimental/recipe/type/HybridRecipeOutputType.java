package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import dev.su5ed.gtexperimental.api.recipe.RecipeOutputType;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeOutputTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class HybridRecipeOutputType implements RecipeOutputType<Either<ItemStack, FluidStack>> {

    @Override
    public void toNetwork(FriendlyByteBuf buffer, Either<ItemStack, FluidStack> value) {
        buffer.writeEither(value, ModRecipeOutputTypes.ITEM::toNetwork, ModRecipeOutputTypes.FLUID::toNetwork);
    }

    @Override
    public JsonElement toJson(Either<ItemStack, FluidStack> value) {
        return value.map(ModRecipeOutputTypes.ITEM::toJson, ModRecipeOutputTypes.FLUID::toJson);
    }

    @Override
    public Either<ItemStack, FluidStack> fromNetwork(FriendlyByteBuf buffer) {
        return buffer.readEither(ModRecipeOutputTypes.ITEM::fromNetwork, ModRecipeOutputTypes.FLUID::fromNetwork);
    }

    @Override
    public Either<ItemStack, FluidStack> fromJson(JsonElement json) {
        return json.getAsJsonObject().has("item")
            ? Either.left(ModRecipeOutputTypes.ITEM.fromJson(json))
            : Either.right(ModRecipeOutputTypes.FLUID.fromJson(json));
    }

    @Override
    public Tag toNBT(Either<ItemStack, FluidStack> value) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("left", value.left().isPresent());
        tag.put("data", value.map(ModRecipeOutputTypes.ITEM::toNBT, ModRecipeOutputTypes.FLUID::toNBT));
        return tag;
    }

    @Override
    public Either<ItemStack, FluidStack> fromNBT(Tag tag) {
        CompoundTag compound = (CompoundTag) tag;
        boolean left = compound.getBoolean("left");
        Tag data = compound.get("data");
        return left ? Either.left(ModRecipeOutputTypes.ITEM.fromNBT(data)) : Either.right(ModRecipeOutputTypes.FLUID.fromNBT(data));
    }

    @Override
    public Either<ItemStack, FluidStack> copy(Either<ItemStack, FluidStack> value) {
        return value.mapBoth(ModRecipeOutputTypes.ITEM::copy, ModRecipeOutputTypes.FLUID::copy);
    }

    @Override
    public void validate(ResourceLocation id, String name, Either<ItemStack, FluidStack> value, boolean allowEmpty) {
        value.ifLeft(item -> ModRecipeOutputTypes.ITEM.validate(id, name, item, allowEmpty))
            .ifRight(fluid -> ModRecipeOutputTypes.FLUID.validate(id, name, fluid, allowEmpty));
    }

    @Override
    public RecipeOutputType<List<Either<ItemStack, FluidStack>>> listOf(int count) {
        return new ListRecipeOutputType<>(this, count);
    }
}
