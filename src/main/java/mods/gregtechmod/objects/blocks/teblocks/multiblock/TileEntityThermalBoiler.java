package mods.gregtechmod.objects.blocks.teblocks.multiblock;

import mods.gregtechmod.api.recipe.fuel.GtFuels;
import mods.gregtechmod.api.recipe.fuel.IFuel;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.util.struct.StructureElement;
import mods.gregtechmod.util.struct.StructureElementGatherer;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.*;

public class TileEntityThermalBoiler extends TileEntityMultiBlockBase<TileEntityMultiBlockBase.MultiBlockInstance> {

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
                        "CXC",
                        "HAH",
                        "CHC"
                ),
                Arrays.asList(
                        "CCC",
                        "CHC",
                        "CCC"
                )
        );
    }

    @Override
    protected Map<Character, Collection<StructureElement>> getStructureElements() {
        Block casing = BlockItems.Block.REINFORCED_MACHINE_CASING.getInstance();
        return new StructureElementGatherer(this::getWorld)
                .block('C', casing)
                .id('H', builder -> builder
                        .block(casing)
                        .tileEntity(TileEntityHatchInput.class, 1)
                        .tileEntity(TileEntityHatchOutput.class)
                        .tileEntity(TileEntityHatchMaintenance.class, 1))
                .block('A', Blocks.AIR)
                .gather();
    }

    @Override
    protected MultiBlockInstance createStructureInstance(EnumFacing facing, Map<Character, Collection<BlockPos>> elements) {
        return new MultiBlockInstance(this.world, elements);
    }

    @Override
    public IFuel<IRecipeIngredient> getFuel(MultiBlockInstance instance) {
        return getFluidFuel(instance)
                .orElseGet(() -> instance.getInputItems().stream()
                        .map(this.fuelManager::getFuel)
                        .filter(Objects::nonNull)
                        .findFirst()
                        .orElse(null));
    }

    @Override
    public void processFuel(MultiBlockInstance instance, IFuel<IRecipeIngredient> fuel) {
        boolean consume = consumeInput(instance, fuel);
        
        if (consume) {
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
