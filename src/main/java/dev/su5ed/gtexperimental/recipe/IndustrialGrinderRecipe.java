package dev.su5ed.gtexperimental.recipe;

import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeTypes;
import dev.su5ed.gtexperimental.recipe.type.BaseRecipeSerializer;
import dev.su5ed.gtexperimental.recipe.type.ItemFluidRecipe;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class IndustrialGrinderRecipe extends ItemFluidRecipe {

    public IndustrialGrinderRecipe(ResourceLocation id, RecipeIngredient<ItemStack> input, RecipeIngredient<FluidStack> fluid, List<ItemStack> outputs) {
        super(ModRecipeTypes.INDUSTRIAL_GRINDER.get(), ModRecipeSerializers.INDUSTRIAL_GRINDER.get(), id, input, fluid, outputs);
    }

    public static BaseRecipeSerializer<IndustrialGrinderRecipe> createSerializer() {
        return new BaseRecipeSerializer<>(ModRecipeTypes.INDUSTRIAL_GRINDER.get());
    }
}
