package mods.gregtechmod.recipe.ingredient;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RecipeIngredientFluid extends RecipeIngredientBase<Ingredient> implements IRecipeIngredientFluid {
    private final int amount;
    private final List<Fluid> matchingFluids;

    public RecipeIngredientFluid(int amount, Fluid... fluids) {
        this(amount, Arrays.asList(fluids));
    }

    public RecipeIngredientFluid(int amount, List<Fluid> fluids) {
        super(Ingredient.fromStacks(getContainersForFluids(fluids).toArray(new ItemStack[0])), 1);
        this.amount = amount;
        this.matchingFluids = fluids;
    }

    @Override
    public int compareTo(IRecipeIngredient other) {
        if (other instanceof IRecipeIngredientFluid) {
            int diff = 0;
            for (Fluid firstFluid : this.matchingFluids) {
                for (Fluid secondFluid : ((IRecipeIngredientFluid) other).getMatchingFluids()) {
                    diff += firstFluid.getName().compareTo(secondFluid.getName());
                }
            }
            diff += ((IRecipeIngredientFluid) other).getFluidAmount() - this.amount;
            return diff;
        } else return super.compareTo(other);
    }

    @Override
    public boolean apply(FluidStack input) {
        for (Fluid fluid : this.matchingFluids) {
            if (input.containsFluid(new FluidStack(fluid, this.amount))) return true;
        }
        return false;
    }

    @Override
    public int getFluidAmount() {
        return this.amount;
    }

    @Override
    public boolean apply(@Nullable ItemStack itemStack) {
        if (itemStack == null || itemStack.isEmpty()) return false;

        FluidStack fluidStack = FluidUtil.getFluidContained(itemStack);
        if (fluidStack != null) return apply(fluidStack);

        return super.apply(itemStack);
    }

    @Override
    public List<Fluid> getMatchingFluids() {
        return this.matchingFluids;
    }

    public static List<ItemStack> getContainersForFluids(List<Fluid> fluids) {
        return fluids.stream()
                .map(fluid -> FluidUtil.getFilledBucket(new FluidStack(fluid, Fluid.BUCKET_VOLUME)))
                .filter(stack -> !stack.isEmpty())
                .collect(Collectors.toList());
    }
}
