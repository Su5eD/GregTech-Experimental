package mods.gregtechmod.objects.blocks.teblocks.multiblock;

import mods.gregtechmod.api.recipe.fuel.GtFuels;
import mods.gregtechmod.api.recipe.fuel.IFuel;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

public class TileEntityThermalBoiler extends TileEntityMultiBlockBase {

    public TileEntityThermalBoiler() {
        super("thermal_boiler", GtFuels.hot);
    }

    @Override
    protected List<List<String>> getStructurePattern() {
        return Arrays.asList(
                Arrays.asList(
                        "CCC",
                        "CHC",
                        "CCC"
                ),
                Arrays.asList(
                        "CHC",
                        "HAH",
                        "CXC"
                ),
                Arrays.asList(
                        "CCC",
                        "CHC",
                        "CCC"
                )
        );
    }

    @Override
    protected void getStructureElements(Map<Character, Predicate<BlockPos>> map) {
        map.put('C', pos -> GtUtil.findBlocks(world, pos, BlockItems.Block.REINFORCED_MACHINE_CASING.getInstance()));
        map.put('H', pos -> GtUtil.findTileEntities(world, pos, TileEntityHatchIO.class, TileEntityHatchDynamo.class, TileEntityHatchMuffler.class, TileEntityHatchMaintenance.class));
        map.put('A', pos -> GtUtil.findBlocks(world, pos, Blocks.AIR));
    }
    
    @Override
    public IFuel<IRecipeIngredient> getFuel(MultiBlockInstance instance) {
        return instance.getInputFluids().stream()
                .filter(Objects::nonNull)
                .map(FluidStack::getFluid)
                .map(this.fuelManager::getFuel)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void processFuel(MultiBlockInstance instance, IFuel<IRecipeIngredient> fuel) {
        IRecipeIngredient input = fuel.getInput();
        boolean drain;
        if (input instanceof IRecipeIngredientFluid) {
            drain = ((IRecipeIngredientFluid) input).getMatchingFluids()
                    .stream()
                    .map(fluid -> new FluidStack(fluid, Fluid.BUCKET_VOLUME))
                    .anyMatch(instance::depleteInput); 
        } else {
            drain = input.getMatchingInputs()
                    .stream()
                    .anyMatch(instance::depleteInput); 
        }
        
        if (drain) {
            this.fuelEnergy = 400;
            this.maxProgress = (int) (fuel.getEnergy() * 2 / 5);
            this.efficiencyIncrease = this.maxProgress * 30;
        }
    }

    @Override
    public ItemStack getFuelOutput(IFuel<IRecipeIngredient> fuel) {
        List<ItemStack> output = fuel.getOutput();
        int size = output.size();
        
        if (acceptsMachinePart(this.machinePartSlot.get())) {
            if (size > 1 && this.world.rand.nextInt(1000) < 100) return output.get(1);
            else if (size > 2 && this.world.rand.nextInt(900) < 50) return output.get(2);
            else if (size > 3 && this.world.rand.nextInt(850) < 25) return output.get(3);
        }
        
        return size > 0 ? output.get(0) : ItemStack.EMPTY; 
    }

    @Override
    public void onRunningTick(MultiBlockInstance instance) {
        if (this.fuelEnergy > 0) {
            int generatedEnergy = (int) (this.fuelEnergy * 2L * this.efficiency / 10000L);
            if (generatedEnergy > 0 && instance.depleteInput(new FluidStack(FluidRegistry.WATER, (generatedEnergy + 160) / 160))) {
                FluidStack steam = FluidRegistry.getFluidStack("steam", generatedEnergy);
                instance.addOutput(steam);
            }
        }
    }

    @Override
    public boolean isCorrectMachinePart() {
        return true;
    }

    @Override
    public boolean acceptsMachinePart(ItemStack stack) {
        return stack.getItem() == BlockItems.Miscellaneous.LAVA_FILTER.getInstance();
    }

    @Override
    public int getDamageToComponent(ItemStack stack) {
        return acceptsMachinePart(stack) ? 1 : 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack stack) {
        return false;
    }

    @Override
    public int getPollutionPerTick(ItemStack stack) {
        return 0;
    }

    @Override
    public int getMaxEfficiency(ItemStack stack) {
        return 10000;
    }
}
