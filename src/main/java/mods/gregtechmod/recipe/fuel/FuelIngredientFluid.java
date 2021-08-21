package mods.gregtechmod.recipe.fuel;

import com.google.common.base.MoreObjects;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.recipe.ingredient.RecipeIngredient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FuelIngredientFluid extends RecipeIngredient<Ingredient> implements IRecipeIngredientFluid {
    public static final FuelIngredientFluid EMPTY = new FuelIngredientFluid(Collections.emptyList(), 0);
    
    private final List<Fluid> matchingFluids;
    private final int milliBuckets;

    private FuelIngredientFluid(List<Fluid> fluids, int milliBuckets) {
        super(Ingredient.EMPTY, 0);
        this.matchingFluids = fluids;
        this.milliBuckets = milliBuckets;
    }
    
    public static FuelIngredientFluid fromName(String name, int buckets) {
        return fromNames(Collections.singletonList(name), buckets);
    }
    
    public static FuelIngredientFluid fromNames(List<String> names, int milliBuckets) {
        List<Fluid> fluids = names.stream()
                .map(FluidRegistry::getFluid)
                .collect(Collectors.toList());
        if (fluids.isEmpty()) {
            GregTechMod.logger.error("Tried to a create an IRecipeIngredientFluid with no matching inputs");
        } else if (fluids.contains(null)) {
            GregTechMod.logger.error("Tried to create an IRecipeIngredientfluid with an invalid fluid among its matching fluids: " + String.join(", ", names));
        } else return new FuelIngredientFluid(fluids, milliBuckets);
    
        return EMPTY;
    }

    @Override
    public int getCount() {
        throw new UnsupportedOperationException("Use getMilliBuckets instead");
    }

    @Override
    public boolean isEmpty() {
        return this.matchingFluids.isEmpty();
    }

    @Override
    public boolean apply(@Nullable FluidStack input) {
        return input != null && this.matchingFluids.stream()
                .map(fluid -> new FluidStack(fluid, this.getMilliBuckets()))
                .anyMatch(input::containsFluid);
    }

    @Override
    public boolean apply(Fluid fluid) {
        return this.matchingFluids.stream()
                .map(Fluid::getName)
                .anyMatch(name -> name.equals(fluid.getName()));
    }

    @Override
    public boolean apply(IRecipeIngredient ingredient) {
        if (ingredient instanceof IRecipeIngredientFluid) {
            return this.matchingFluids.stream()
                    .anyMatch(firstFluid -> ((IRecipeIngredientFluid) ingredient).getMatchingFluids().stream()
                            .anyMatch(secondFluid -> firstFluid.getName().equals(secondFluid.getName()))) && this.milliBuckets <= ((IRecipeIngredientFluid) ingredient).getMilliBuckets();
        }
        return super.apply(ingredient);
    }

    @Override
    public boolean apply(ItemStack input, boolean checkSize) {
        return false;
    }

    @Override
    public int getMilliBuckets() {
        return this.milliBuckets;
    }

    @Override
    public List<Fluid> getMatchingFluids() {
        return this.matchingFluids;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("matchingFluids", this.matchingFluids)
                .add("milliBuckets", this.milliBuckets)
                .toString();
    }
}
