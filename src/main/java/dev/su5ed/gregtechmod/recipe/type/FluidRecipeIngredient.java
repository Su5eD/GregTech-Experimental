package dev.su5ed.gregtechmod.recipe.type;

import com.google.gson.JsonElement;
import dev.su5ed.gregtechmod.recipe.setup.ModRecipeIngredientTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import one.util.streamex.StreamEx;

import java.util.List;
import java.util.function.Predicate;

public class FluidRecipeIngredient implements RecipeIngredient<FluidStack> {
    private final Value value;

    public static FluidRecipeIngredient fluidStack(FluidStack stack) {
        Value value = new FluidStackValue(stack);
        return new FluidRecipeIngredient(value);
    }

    public static FluidRecipeIngredient fluids(List<Fluid> fluids, int amount) {
        Value value = new FluidsValue(fluids, amount);
        return new FluidRecipeIngredient(value);
    }

    public static FluidRecipeIngredient tag(TagKey<Fluid> tag, int amount) {
        Value value = new FluidTagValue(tag, amount);
        return new FluidRecipeIngredient(value);
    }

    private FluidRecipeIngredient(Value value) {
        this.value = value;
    }

    @Override
    public RecipeIngredientType<?> getType() {
        return ModRecipeIngredientTypes.FLUID;
    }

    @Override
    public int getCount() {
        return this.value.amount(); // TODO Bucket count?
    }

    @Override
    public boolean isEmpty() {
        return this.value.isEmpty() || getCount() <= 0;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        this.value.toNetwork(buffer);
    }

    @Override
    public JsonElement toJson() {
        return null; // TODO
    }

    @Override
    public boolean test(FluidStack fluidStack) {
        return this.value.test(fluidStack);
    }

    public interface Value extends Predicate<FluidStack> {
        int amount();
        
        boolean isEmpty();
        
        void toNetwork(FriendlyByteBuf buffer);
    }

    private record FluidStackValue(FluidStack fluidStack) implements Value {
        @Override
        public boolean test(FluidStack fluid) {
            return fluid.containsFluid(this.fluidStack);
        }

        @Override
        public int amount() {
            return this.fluidStack.getAmount();
        }

        @Override
        public boolean isEmpty() {
            return this.fluidStack.isEmpty();
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer) {
            buffer.writeUtf("fluid");
            buffer.writeFluidStack(this.fluidStack);
        }
    }

    private record FluidsValue(List<Fluid> list, int amount) implements Value {
        @Override
        public boolean test(FluidStack fluidStack) {
            return StreamEx.of(this.list).allMatch(fluid -> fluidStack.getFluid() == fluid) && fluidStack.getAmount() >= this.amount;
        }

        @Override
        public boolean isEmpty() {
            return this.list.isEmpty();
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer) {
            buffer.writeUtf("fluids");
            buffer.writeInt(this.list.size());
            for (Fluid fluid : this.list) {
                buffer.writeRegistryId(ForgeRegistries.FLUIDS, fluid);
            }
            buffer.writeInt(this.amount);
        }
    }

    private record FluidTagValue(TagKey<Fluid> tag, int amount) implements Value {
        @Override
        public boolean test(FluidStack fluidStack) {
            return fluidStack.getFluid().is(this.tag) && fluidStack.getAmount() >= this.amount;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer) {
            buffer.writeUtf("fluidTag");
        }
    }
}
