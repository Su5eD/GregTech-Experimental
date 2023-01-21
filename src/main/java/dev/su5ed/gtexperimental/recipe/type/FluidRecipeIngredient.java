package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredientType;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import one.util.streamex.StreamEx;

import java.util.List;
import java.util.function.Predicate;

public class FluidRecipeIngredient implements RecipeIngredient<FluidStack> {
    private final Value value;
    private final int amount;

    public FluidRecipeIngredient(FluidStack stack) {
        this(stack.getFluid(), stack.getAmount());
    }

    public FluidRecipeIngredient(Fluid fluid, int amount) {
        this(new FluidValue(fluid), amount);
    }

    public FluidRecipeIngredient(List<Fluid> fluids, int amount) {
        this(new FluidsValue(fluids), amount);
    }

    public FluidRecipeIngredient(TagKey<Fluid> tag, int amount) {
        this(new FluidTagValue(tag), amount);
    }

    private FluidRecipeIngredient(Value value, int amount) {
        this.value = value;
        this.amount = amount;
    }

    @Override
    public RecipeIngredientType<?> getType() {
        return ModRecipeIngredientTypes.FLUID;
    }

    @Override
    public int getCount() {
        return this.amount; // TODO Bucket count?
    }

    @Override
    public boolean isEmpty() {
        return this.value.isEmpty() || getCount() <= 0;
    }

    @Override
    public Ingredient asIngredient() {
        return Ingredient.EMPTY;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.value.name());
        this.value.toNetwork(buffer);
        buffer.writeInt(this.amount);
    }

    @Override
    public JsonElement toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", this.value.name());
        json.add(this.value.name(), this.value.toJson());
        json.addProperty("amount", this.amount);
        return json;
    }

    @Override
    public boolean test(FluidStack fluidStack) {
        return this.value.test(fluidStack) && fluidStack.getAmount() >= this.amount;
    }

    public interface Value extends Predicate<FluidStack> {
        String name();

        boolean isEmpty();

        void toNetwork(FriendlyByteBuf buffer);

        JsonElement toJson();
    }

    public record FluidValue(Fluid fluid) implements Value {
        public static final String NAME = "fluid";

        @Override
        public String name() {
            return NAME;
        }

        @Override
        public boolean test(FluidStack fluid) {
            return fluid.getFluid() == this.fluid;
        }

        @Override
        public boolean isEmpty() {
            return this.fluid == Fluids.EMPTY;
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer) {
            buffer.writeRegistryId(ForgeRegistries.FLUIDS, fluid);
        }

        @Override
        public JsonElement toJson() {
            return new JsonPrimitive(ForgeRegistries.FLUIDS.getKey(this.fluid).toString());
        }
    }

    public record FluidsValue(List<Fluid> list) implements Value {
        public static final String NAME = "fluids";

        @Override
        public String name() {
            return NAME;
        }

        @Override
        public boolean test(FluidStack fluidStack) {
            return StreamEx.of(this.list).allMatch(fluid -> fluidStack.getFluid() == fluid);
        }

        @Override
        public boolean isEmpty() {
            return this.list.isEmpty();
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer) {
            buffer.writeInt(this.list.size());
            for (Fluid fluid : this.list) {
                buffer.writeRegistryId(ForgeRegistries.FLUIDS, fluid);
            }
        }

        @Override
        public JsonElement toJson() {
            JsonArray fluids = new JsonArray();
            for (Fluid fluid : this.list) {
                fluids.add(ForgeRegistries.FLUIDS.getKey(fluid).toString());
            }
            return fluids;
        }
    }

    public record FluidTagValue(TagKey<Fluid> tag) implements Value {
        public static final String NAME = "tag";

        @Override
        public String name() {
            return NAME;
        }

        @Override
        public boolean test(FluidStack fluidStack) {
            return fluidStack.getFluid().is(this.tag);
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer) {
            buffer.writeResourceLocation(this.tag.location());
        }

        @Override
        public JsonElement toJson() {
            return new JsonPrimitive(this.tag.location().toString());
        }
    }
}
