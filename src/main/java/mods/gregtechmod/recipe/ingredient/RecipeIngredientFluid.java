package mods.gregtechmod.recipe.ingredient;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.util.ProfileDelegate;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RecipeIngredientFluid extends RecipeIngredient<Ingredient> implements IRecipeIngredientFluid {
    public static final RecipeIngredientFluid EMPTY = new RecipeIngredientFluid(Collections.emptyList(), 0);
    private final List<Fluid> matchingFluids;

    private RecipeIngredientFluid(List<Fluid> fluids, int buckets) {
        super(Ingredient.fromStacks(getContainersForFluids(fluids).toArray(new ItemStack[0])), buckets);
        this.matchingFluids = fluids;
    }

    public static RecipeIngredientFluid fromName(String name, int buckets) {
        return fromNames(Collections.singletonList(name), buckets);
    }

    public static RecipeIngredientFluid fromNames(List<String> names, int buckets) {
        List<Fluid> fluids = names.stream()
                .map(FluidRegistry::getFluid)
                .collect(Collectors.toList());
        if (fluids.isEmpty()) {
            GregTechMod.logger.error("Tried to a create an IRecipeIngredientFluid with no matching inputs");
        } else if (fluids.contains(null)) {
            GregTechMod.logger.error("Tried to create an IRecipeIngredientfluid with an invalid fluid among its matching fluids: " + String.join(", ", names));
        } else return fromFluids(fluids, buckets);

        return EMPTY;
    }

    public static RecipeIngredientFluid fromFluid(Fluid fluid) {
        return new RecipeIngredientFluid(Collections.singletonList(fluid), 1);
    }

    public static RecipeIngredientFluid fromFluid(Fluid fluid, int buckets) {
        return new RecipeIngredientFluid(Collections.singletonList(fluid), buckets);
    }

    public static RecipeIngredientFluid fromFluids(List<Fluid> fluids, int buckets) {
        return new RecipeIngredientFluid(fluids, buckets);
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
        return this.count * 1000;
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
    public boolean apply(IRecipeIngredient ingredient) {
        if (ingredient instanceof IRecipeIngredientFluid) {
            return this.matchingFluids.stream()
                    .anyMatch(firstFluid -> ((IRecipeIngredientFluid) ingredient).getMatchingFluids().stream()
                        .anyMatch(secondFluid -> firstFluid.getName().equals(secondFluid.getName()))) && this.count <= ingredient.getCount();
        }
        return super.apply(ingredient);
    }

    @Override
    public List<Fluid> getMatchingFluids() {
        return this.matchingFluids;
    }

    @Override
    public boolean isEmpty() {
        return this.matchingFluids.isEmpty();
    }

    @Override
    public String toString() {
        List<String> fluids = this.matchingFluids.stream()
                .map(Fluid::getName)
                .collect(Collectors.toList());
        return "RecipeIngredientFluid{fluids="+fluids+",count="+this.count+"}";
    }

    public static List<ItemStack> getContainersForFluids(List<Fluid> fluids) {
        List<ItemStack> list = fluids.stream()
                .map(fluid -> FluidUtil.getFilledBucket(new FluidStack(fluid, Fluid.BUCKET_VOLUME)))
                .filter(stack -> !stack.isEmpty())
                .collect(Collectors.toList());
        
        fluids.forEach(fluid -> {
            ItemStack cell = ProfileDelegate.getCell(fluid.getName());
            if (!cell.isEmpty()) list.add(cell);
        });
        
        return list;
    }
}
