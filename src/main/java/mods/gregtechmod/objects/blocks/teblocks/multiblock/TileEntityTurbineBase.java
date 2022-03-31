package mods.gregtechmod.objects.blocks.teblocks.multiblock;

import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.recipe.fuel.IFuel;
import mods.gregtechmod.api.recipe.fuel.IFuelManagerFluid;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class TileEntityTurbineBase extends TileEntityMultiBlockBase<TurbineInstance> {

    public TileEntityTurbineBase(IFuelManagerFluid<IFuel<IRecipeIngredient>> fuelManager) {
        super(fuelManager);
    }

    @Override
    protected List<List<String>> getStructurePattern() {
        return Arrays.asList(
            Arrays.asList(
                "RRR",
                "CHC",
                "CHC",
                "CCC"
            ),
            Arrays.asList(
                "RXR",
                "H H",
                "H H",
                "CDC"
            ),
            Arrays.asList(
                "RRR",
                "CHC",
                "CHC",
                "CCC"
            )
        );
    }

    @Override
    protected TurbineInstance createStructureInstance(EnumFacing facing, Map<Character, Collection<BlockPos>> elements) {
        return new TurbineInstance(facing, this.world, elements, getActive());
    }

    @Override
    protected void onInvalidate(TurbineInstance instance) {
        super.onInvalidate(instance);
        instance.setTurbineProperty(false);
        instance.setRotorProperty(null, false);
    }

    @Override
    public boolean isCorrectMachinePart() {
        return acceptsMachinePart(this.machinePartSlot.get());
    }

    @Override
    public boolean acceptsMachinePart(ItemStack stack) {
        return GregTechAPI.instance().getTurbineRotor(stack).isPresent();
    }

    @Override
    public int getDamageToComponent(ItemStack stack) {
        return GregTechAPI.instance().getTurbineRotor(stack)
            .map(rotor -> rotor.damageToComponent)
            .orElse(0);
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack stack) {
        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack stack) {
        return GregTechAPI.instance().getTurbineRotor(stack)
            .map(rotor -> {
                float efficiency = rotor.efficiency / 100F;
                return (int) (10000 * efficiency);
            })
            .orElse(0);
    }

    @Override
    public IFuel<IRecipeIngredient> getFuel(MultiBlockInstance instance) {
        return getFluidFuel(instance)
            .orElse(null);
    }

    @Override
    public void processFuel(MultiBlockInstance instance, IFuel<IRecipeIngredient> fuel) {
        GregTechAPI.instance().getTurbineRotor(this.machinePartSlot.get())
            .ifPresent(rotor -> this.efficiencyIncrease = this.maxProgress * rotor.efficiencyMultiplier);
    }

    @Override
    protected boolean consumeFluidInput(MultiBlockInstance instance, IRecipeIngredientFluid input) {
        int milliBuckets = input.getMilliBuckets();
        return input.getMatchingFluids()
            .stream()
            .map(fluid -> new FluidStack(fluid, milliBuckets))
            .anyMatch(instance::depleteInput);
    }

    @Override
    public ItemStack getFuelOutput(IFuel<IRecipeIngredient> fuel) {
        return ItemStack.EMPTY;
    }

    @Override
    protected void onStart(TurbineInstance instance) {
        instance.setRotorProperty(getFacing(), true);
    }

    @Override
    protected void onStop(TurbineInstance instance) {
        instance.setRotorProperty(getFacing(), false);
    }
}
