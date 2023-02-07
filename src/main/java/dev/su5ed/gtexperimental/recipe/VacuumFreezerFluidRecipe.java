package dev.su5ed.gtexperimental.recipe;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeSerializers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipePropertyMap;
import dev.su5ed.gtexperimental.recipe.type.SISORecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class VacuumFreezerFluidRecipe extends SISORecipe<FluidStack> {
    
    public VacuumFreezerFluidRecipe(ResourceLocation id, RecipeIngredient<FluidStack> input, FluidStack output, int duration) {
        this(id, input, output, RecipePropertyMap.builder()
            .duration(duration)
            .build());
    }

    public VacuumFreezerFluidRecipe(ResourceLocation id, RecipeIngredient<FluidStack> input, FluidStack output, RecipePropertyMap properties) {
        super(ModRecipeTypes.VACUUM_FREEZER_FLUID.get(), ModRecipeSerializers.VACUUM_FREEZER_FLUID.get(), id, input, output, properties);
    }

    @Override
    public double getEnergyCost() {
        return 128;
    }
}
