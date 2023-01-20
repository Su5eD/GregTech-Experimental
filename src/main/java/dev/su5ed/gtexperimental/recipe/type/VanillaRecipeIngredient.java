package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Predicate;

public class VanillaRecipeIngredient implements RecipeIngredient<ItemStack> {
    private final Value value;
    private final int count;

    public VanillaRecipeIngredient(Ingredient ingredient) {
        this(ingredient, 1);
    }

    public VanillaRecipeIngredient(Ingredient ingredient, int count) {
        this(new IngredientValue(ingredient), count);
    }
    
    public VanillaRecipeIngredient(RecipeIngredient<FluidStack> ingredient) {
        this(new FluidIngredientValue(ingredient), 1);
    }

    public VanillaRecipeIngredient(Value value, int count) {
        this.value = value;
        this.count = count;
    }

    @Override
    public RecipeIngredientType<?> getType() {
        return ModRecipeIngredientTypes.ITEM;
    }

    @Override
    public int getCount() {
        return this.count;
    }

    @Override
    public boolean isEmpty() {
        return this.value.isEmpty() || this.count <= 0;
    }

    @Override
    public Ingredient asIngredient() {
        return this.value.asIngredient();
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.value.name());
        buffer.writeInt(this.count);
        this.value.toNetwork(buffer);
    }

    @Override
    public JsonElement toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", this.value.name());
        json.add("value", this.value.toJson());
        if (this.count > 1) {
            json.addProperty("count", this.count);
        }
        return json;
    }

    @Override
    public boolean test(ItemStack stack) {
        return this.value.test(stack) && stack.getCount() >= getCount();
    }

    private interface Value extends Predicate<ItemStack> {
        String name();

        boolean isEmpty();

        void toNetwork(FriendlyByteBuf buffer);

        JsonElement toJson();

        Ingredient asIngredient();
    }

    public record IngredientValue(Ingredient ingredient) implements Value {
        public static final String NAME = "ingredient";
        
        @Override
        public String name() {
            return NAME;
        }

        @Override
        public boolean isEmpty() {
            return this.ingredient.isEmpty();
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer) {
            this.ingredient.toNetwork(buffer);
        }

        @Override
        public JsonElement toJson() {
            return this.ingredient.toJson();
        }

        @Override
        public Ingredient asIngredient() {
            return this.ingredient;
        }

        @Override
        public boolean test(ItemStack stack) {
            return this.ingredient.test(stack);
        }
    }
    
    public record FluidIngredientValue(RecipeIngredient<FluidStack> ingredient) implements Value {
        public static final String NAME = "fluid_ingredient";
        
        @Override
        public String name() {
            return NAME;
        }

        @Override
        public boolean isEmpty() {
            return this.ingredient.isEmpty();
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer) {
            this.ingredient.toNetwork(buffer);
        }

        @Override
        public JsonElement toJson() {
            return this.ingredient.toJson();
        }

        @Override
        public Ingredient asIngredient() {
            return this.ingredient.asIngredient();
        }

        @Override
        public boolean test(ItemStack stack) {
            return stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM)
                .map(handler -> {
                    for (int i = 0; i < handler.getTanks(); i++) {
                        FluidStack fluid = handler.getFluidInTank(i);
                        if (this.ingredient.test(fluid)) {
                            return true;
                        }
                    }
                    return false;
                })
                .orElse(false);
        }
    }
}
