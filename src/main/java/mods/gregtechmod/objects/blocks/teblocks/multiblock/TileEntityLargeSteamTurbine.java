package mods.gregtechmod.objects.blocks.teblocks.multiblock;

import mods.gregtechmod.api.recipe.fuel.GtFuels;
import mods.gregtechmod.api.recipe.fuel.IFuel;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.util.struct.StructureElement;
import mods.gregtechmod.util.struct.StructureElementGatherer;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class TileEntityLargeSteamTurbine extends TileEntityTurbineBase {

    public TileEntityLargeSteamTurbine() {
        super("large_steam_turbine", GtFuels.steam);
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
    protected Map<Character, Collection<StructureElement>> getStructureElements() {
        Block casing = BlockItems.Block.STANDARD_MACHINE_CASING.getInstance();
        return new StructureElementGatherer(this::getWorld)
                .block('C', casing)
                .block('R', casing)
                .id('H', builder -> builder
                        .block(casing)
                        .tileEntity(TileEntityHatchInput.class, 1)
                        .tileEntity(TileEntityHatchOutput.class)
                        .tileEntity(TileEntityHatchMaintenance.class, 1))
                .tileEntity('D', TileEntityHatchDynamo.class, 1)
                .gather();
    }
    
    @Override
    public void processFuel(MultiBlockInstance instance, IFuel<IRecipeIngredient> fuel) {
        this.fuelEnergy = fuel.getEnergy();
        this.maxProgress = 1;
        super.processFuel(instance, fuel);
    }
    
    @Override
    public int getPollutionPerTick(ItemStack stack) {
        return 0;
    }

    @Override
    protected void addOutput(MultiBlockInstance instance) {
        super.addOutput(instance);
        instance.addOutput(new FluidStack(FluidRegistry.WATER, 10));
    }
}
