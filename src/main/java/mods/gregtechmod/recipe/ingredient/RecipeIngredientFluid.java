package mods.gregtechmod.recipe.ingredient;

import com.google.common.base.MoreObjects;
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
import one.util.streamex.StreamEx;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class RecipeIngredientFluid extends RecipeIngredient<Ingredient> implements IRecipeIngredientFluid {
    public static final RecipeIngredientFluid EMPTY = new RecipeIngredientFluid(Collections.emptyList(), 0);
    private final List<Fluid> matchingFluids;

    private RecipeIngredientFluid(List<Fluid> fluids, int buckets) {
        super(Ingredient.fromStacks(getContainersForFluids(fluids)), buckets);
        this.matchingFluids = fluids;
    }

    public static RecipeIngredientFluid fromName(String name, int buckets) {
        return fromNames(Collections.singletonList(name), buckets);
    }

    public static RecipeIngredientFluid fromNames(List<String> names, int buckets) {
        List<Fluid> fluids = StreamEx.of(names)
            .map(FluidRegistry::getFluid)
            .toImmutableList();
        if (fluids.isEmpty()) {
            GregTechMod.LOGGER.error("Tried to a create an IRecipeIngredientFluid with no matching inputs");
        }
        else if (fluids.contains(null)) {
            GregTechMod.LOGGER.error("Tried to create an IRecipeIngredientfluid with an invalid fluid among its matching fluids: " + String.join(", ", names));
        }
        else return fromFluids(fluids, buckets);

        return EMPTY;
    }

    public static RecipeIngredientFluid fromFluid(Fluid fluid, int buckets) {
        return new RecipeIngredientFluid(Collections.singletonList(fluid), buckets);
    }

    public static RecipeIngredientFluid fromFluids(List<Fluid> fluids, int buckets) {
        return new RecipeIngredientFluid(fluids, buckets);
    }

    @Override
    public boolean apply(@Nullable FluidStack input) {
        return input != null && this.matchingFluids.stream()
            .map(fluid -> new FluidStack(fluid, this.getMilliBuckets()))
            .anyMatch(input::containsFluid);
    }

    @Override
    public boolean apply(Fluid fluid) {
        String name = fluid.getName();
        return this.matchingFluids.stream()
            .map(Fluid::getName)
            .anyMatch(name::equals);
    }

    @Override
    public int getMilliBuckets() {
        return this.count * Fluid.BUCKET_VOLUME;
    }

    @Override
    public boolean apply(@Nullable ItemStack input, boolean checkCount) {
        if (input != null) {
            FluidStack fluidStack = FluidUtil.getFluidContained(input);
            if (fluidStack != null && fluidStack.amount >= 1000) {
                if (checkCount) {
                    fluidStack.amount *= input.getCount();
                    return apply(fluidStack);
                }
                return apply(fluidStack.getFluid());
            }
            else return super.apply(input, checkCount);
        }

        return false;
    }

    @Override
    public boolean apply(IRecipeIngredient ingredient) {
        return ingredient instanceof IRecipeIngredientFluid
            ? this.matchingFluids.stream()
            .anyMatch(firstFluid -> ((IRecipeIngredientFluid) ingredient).getMatchingFluids().stream()
                .anyMatch(secondFluid -> firstFluid.getName().equals(secondFluid.getName()))) && this.count <= ingredient.getCount()
            : super.apply(ingredient);
    }

    @Override
    public List<Fluid> getMatchingFluids() {
        return this.matchingFluids;
    }

    @Override
    public List<FluidStack> getFluidStacks() {
        return StreamEx.of(this.matchingFluids)
            .map(fluid -> new FluidStack(fluid, getMilliBuckets()))
            .toList();
    }

    @Override
    public boolean isEmpty() {
        return this.matchingFluids.isEmpty();
    }

    @Override
    public String toString() {
        List<String> fluids = StreamEx.of(this.matchingFluids)
            .map(Fluid::getName)
            .toList();
        return MoreObjects.toStringHelper(this)
            .add("ingredient", ingredient)
            .add("matchingFluids", fluids)
            .toString();
    }

    public static ItemStack[] getContainersForFluids(List<Fluid> fluids) {
        return StreamEx.of(fluids)
            .map(fluid -> FluidUtil.getFilledBucket(new FluidStack(fluid, Fluid.BUCKET_VOLUME)))
            .append(StreamEx.of(fluids)
                .map(Fluid::getName)
                .map(ProfileDelegate::getCell))
            .remove(ItemStack::isEmpty)
            .toArray(ItemStack[]::new);
    }
}
