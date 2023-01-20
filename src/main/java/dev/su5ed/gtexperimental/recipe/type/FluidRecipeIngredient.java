package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import one.util.streamex.StreamEx;

import java.util.List;
import java.util.function.Predicate;

public class FluidRecipeIngredient implements RecipeIngredient<FluidStack> {
    private final Value value;
    
    public FluidRecipeIngredient(FluidStack stack) {
        this(new FluidStackValue(stack));
    }

    public FluidRecipeIngredient(List<Fluid> fluids, int amount) {
        this(new FluidRecipeIngredient.FluidsValue(fluids, amount));
    }
    
    public FluidRecipeIngredient(TagKey<Fluid> tag, int amount) {
        this(new FluidTagValue(tag, amount));
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
    public Ingredient asIngredient() {
        return Ingredient.EMPTY;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        this.value.toNetwork(buffer);
    }

    @Override
    public JsonElement toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", this.value.name());
        json.add("value", this.value.toJson());
        return json;
    }

    @Override
    public boolean test(FluidStack fluidStack) {
        return this.value.test(fluidStack);
    }

    public interface Value extends Predicate<FluidStack> {
        String name();
        
        int amount();
        
        boolean isEmpty();
        
        void toNetwork(FriendlyByteBuf buffer);
        
        JsonElement toJson();
    }

    public record FluidStackValue(FluidStack fluidStack) implements Value {
        public static final String NAME = "fluid";
        
        @Override
        public String name() {
            return NAME;
        }

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
            buffer.writeUtf(name());
            buffer.writeFluidStack(this.fluidStack);
        }

        @Override
        public JsonElement toJson() {
            JsonObject json = new JsonObject();
            json.addProperty("fluid", ForgeRegistries.FLUIDS.getKey(this.fluidStack.getFluid()).toString());
            json.addProperty("amount", this.fluidStack.getAmount());
            return json;
        }
    }

    public record FluidsValue(List<Fluid> list, int amount) implements Value {
        public static final String NAME = "fluids";
        
        @Override
        public String name() {
            return NAME;
        }

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
            buffer.writeUtf(name());
            buffer.writeInt(this.list.size());
            for (Fluid fluid : this.list) {
                buffer.writeRegistryId(ForgeRegistries.FLUIDS, fluid);
            }
            buffer.writeInt(this.amount);
        }

        @Override
        public JsonElement toJson() {
            JsonObject json = new JsonObject();
            JsonArray fluids = new JsonArray();
            for (Fluid fluid : this.list) {
                fluids.add(ForgeRegistries.FLUIDS.getKey(fluid).toString());
            }
            json.add("fluids", fluids);
            json.addProperty("amount", this.amount);
            return json;
        }
    }

    public record FluidTagValue(TagKey<Fluid> tag, int amount) implements Value {
        public static final String NAME = "tag";
        
        @Override
        public String name() {
            return NAME;
        }

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
            buffer.writeUtf(name());
            buffer.writeResourceLocation(this.tag.location());
        }

        @Override
        public JsonElement toJson() {
            JsonObject json = new JsonObject();
            json.addProperty("tag", this.tag.location().toString());
            json.addProperty("amount", this.amount);
            return json;
        }
    }
}
