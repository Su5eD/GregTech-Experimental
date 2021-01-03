package mods.gregtechmod.recipe.ingredient;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class RecipeIngredientFluid extends RecipeIngredientBase<Ingredient> implements IRecipeIngredientFluid {
    private final int buckets;
    private final List<Fluid> matchingFluids;

    public RecipeIngredientFluid(List<Fluid> fluids, int buckets) {
        super(Ingredient.fromStacks(getContainersForFluids(fluids).toArray(new ItemStack[0])), buckets);
        this.buckets = buckets;
        this.matchingFluids = fluids;
    }

    @Override
    public int compareTo(IRecipeIngredient other) {
        if (other instanceof IRecipeIngredientFluid) {
            int diff = 0;
            for (Fluid firstFluid : this.matchingFluids) {
                for (Fluid secondFluid : ((IRecipeIngredientFluid) other).getMatchingFluids()) {
                    if (firstFluid.getName().equals(secondFluid.getName())) {
                        diff = 0;
                        break;
                    }
                    else diff += firstFluid.getName().compareTo(secondFluid.getName());
                }
            }
            diff += ((IRecipeIngredientFluid) other).getMilliBuckets() - this.getMilliBuckets();
            return diff;
        } else return super.compareTo(other);
    }

    @Override
    public boolean apply(@Nullable FluidStack input) {
        if (input != null) {
            for (Fluid fluid : this.matchingFluids) {
                FluidStack stack = new FluidStack(fluid, this.getMilliBuckets());
                if (input.containsFluid(stack)) return true;
            }
        }
        return false;
    }

    @Override
    public boolean apply(Fluid fluid) {
        return this.matchingFluids.stream()
                .map(Fluid::getName)
                .anyMatch(name -> name.equals(fluid.getName()));
    }

    @Override
    public int getMilliBuckets() {
        return this.buckets * 1000;
    }

    @Override
    public boolean apply(@Nullable ItemStack input, boolean checkCount) {
        if (input != null) {
            FluidStack fluidStack = FluidUtil.getFluidContained(input);
            if (fluidStack != null && fluidStack.amount >= 1000) {
                if (checkCount) {
                    fluidStack.amount *= input.getCount();
                    return apply(fluidStack);
                } else return apply(fluidStack.getFluid());
            } else return super.apply(input, checkCount);
        }

        return false;
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
