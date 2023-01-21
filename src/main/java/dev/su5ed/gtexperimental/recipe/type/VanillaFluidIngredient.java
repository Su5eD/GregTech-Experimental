package dev.su5ed.gtexperimental.recipe.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.crafting.AbstractIngredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

import static dev.su5ed.gtexperimental.api.Reference.location;

public class VanillaFluidIngredient extends AbstractIngredient {
    private final RecipeIngredient<FluidStack> fluidIngredient;

    public VanillaFluidIngredient(RecipeIngredient<FluidStack> fluidIngredient) {
        this.fluidIngredient = fluidIngredient;
    }

    @Override
    public boolean isEmpty() {
        return this.fluidIngredient.isEmpty();
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public boolean test(@Nullable ItemStack stack) {
        return stack != null && stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM)
            .map(handler -> {
                for (int i = 0; i < handler.getTanks(); i++) {
                    FluidStack fluid = handler.getFluidInTank(i);
                    if (this.fluidIngredient.test(fluid)) {
                        return true;
                    }
                }
                return false;
            })
            .orElse(false);
    }

    @Override
    public JsonElement toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", CraftingHelper.getID(Serializer.INSTANCE).toString());
        json.add("fluid_ingredient", this.fluidIngredient.toJson());
        return json;
    }

    public static class Serializer implements IIngredientSerializer<VanillaFluidIngredient> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation NAME = location("fluid_item_ingredient");

        @Override
        public VanillaFluidIngredient parse(FriendlyByteBuf buffer) {
            RecipeIngredient<FluidStack> fluidIngredient = ModRecipeIngredientTypes.FLUID.create(buffer);
            return new VanillaFluidIngredient(fluidIngredient);
        }

        @Override
        public VanillaFluidIngredient parse(JsonObject json) {
            RecipeIngredient<FluidStack> fluidIngredient = ModRecipeIngredientTypes.FLUID.create(json);
            return new VanillaFluidIngredient(fluidIngredient);
        }

        @Override
        public void write(FriendlyByteBuf buffer, VanillaFluidIngredient ingredient) {
            ingredient.fluidIngredient.toNetwork(buffer);
        }
    }
}
