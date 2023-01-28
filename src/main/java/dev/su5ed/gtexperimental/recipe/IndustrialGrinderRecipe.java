package dev.su5ed.gtexperimental.recipe;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeTypes;
import dev.su5ed.gtexperimental.recipe.type.IFMORecipe;
import dev.su5ed.gtexperimental.recipe.type.RecipePropertyMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class IndustrialGrinderRecipe extends IFMORecipe {

    public IndustrialGrinderRecipe(ResourceLocation id, RecipeIngredient<ItemStack> input, RecipeIngredient<FluidStack> fluid, List<ItemStack> outputs) {
        this(id, input, fluid, outputs, RecipePropertyMap.empty());
    }

    public IndustrialGrinderRecipe(ResourceLocation id, RecipeIngredient<ItemStack> input, RecipeIngredient<FluidStack> fluid, List<ItemStack> outputs, RecipePropertyMap properties) {
        super(ModRecipeTypes.INDUSTRIAL_GRINDER.get(), ModRecipeSerializers.INDUSTRIAL_GRINDER.get(), id, input, fluid, outputs, properties);
    }

    @Override
    public int getDuration() {
        return this.input.getCount() * 100;
    }

    @Override
    public double getEnergyCost() {
        return 128;
    }
}
