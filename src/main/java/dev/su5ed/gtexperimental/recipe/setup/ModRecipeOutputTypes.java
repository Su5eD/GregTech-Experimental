package dev.su5ed.gtexperimental.recipe.setup;

import com.mojang.datafixers.util.Either;
import dev.su5ed.gtexperimental.api.recipe.RecipeOutputType;
import dev.su5ed.gtexperimental.recipe.type.FluidRecipeOutputType;
import dev.su5ed.gtexperimental.recipe.type.HybridRecipeOutputType;
import dev.su5ed.gtexperimental.recipe.type.ItemRecipeOutputType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public final class ModRecipeOutputTypes {
    public static final RecipeOutputType<ItemStack> ITEM = new ItemRecipeOutputType();
    public static final RecipeOutputType<FluidStack> FLUID = new FluidRecipeOutputType();
    public static final RecipeOutputType<Either<ItemStack, FluidStack>> HYBRID = new HybridRecipeOutputType();

    private ModRecipeOutputTypes() {}
}
