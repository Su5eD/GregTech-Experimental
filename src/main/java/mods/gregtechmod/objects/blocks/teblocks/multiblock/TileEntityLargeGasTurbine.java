package mods.gregtechmod.objects.blocks.teblocks.multiblock;

import mods.gregtechmod.api.recipe.fuel.GtFuels;
import mods.gregtechmod.api.recipe.fuel.IFuel;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.util.struct.StructureElement;
import mods.gregtechmod.util.struct.StructureElementGatherer;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class TileEntityLargeGasTurbine extends TileEntityTurbineBase {

    public TileEntityLargeGasTurbine() {
        super(GtFuels.gas);
    }

    @Override
    protected Map<Character, Collection<StructureElement>> getStructureElements() {
        Block casing = BlockItems.Block.REINFORCED_MACHINE_CASING.getBlockInstance();
        return new StructureElementGatherer(this::getWorld)
                .block('C', casing)
                .block('R', casing)
                .id('H', builder -> builder
                        .block(casing)
                        .tileEntity(TileEntityHatchInput.class, 1)
                        .tileEntity(TileEntityHatchOutput.class)
                        .tileEntity(TileEntityHatchMaintenance.class, 1)
                        .tileEntity(TileEntityHatchMuffler.class, 1))
                .tileEntity('D', TileEntityHatchDynamo.class, 1)
                .gather();
    }

    @Override
    public void processFuel(MultiBlockInstance instance, IFuel<IRecipeIngredient> fuel) {
        this.fuelEnergy = 1000;
        this.maxProgress = (int) fuel.getEnergy();
        super.processFuel(instance, fuel);
    }

    @Override
    public ItemStack getFuelOutput(IFuel<IRecipeIngredient> fuel) {
        List<ItemStack> output = fuel.getOutput();
        return output.size() > 0 ? output.get(0) : super.getFuelOutput(fuel);
    }

    @Override
    public int getPollutionPerTick(ItemStack stack) {
        return 1;
    }
}
