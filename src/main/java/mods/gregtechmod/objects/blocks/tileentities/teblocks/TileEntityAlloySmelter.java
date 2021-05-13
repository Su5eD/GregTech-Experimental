package mods.gregtechmod.objects.blocks.tileentities.teblocks;

import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IRecipeAlloySmelter;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.gui.GuiAlloySmelter;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.base.TileEntityBasicMachineMultiInput;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.base.TileEntityElectricFurnaceBase;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.container.ContainerBasicMachine;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;

public class TileEntityAlloySmelter extends TileEntityElectricFurnaceBase<List<IRecipeIngredient>, List<ItemStack>, IRecipeAlloySmelter> {

    public TileEntityAlloySmelter() {
        super("alloy_smelter", GtRecipes.alloySmelter);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiAlloySmelter(new ContainerBasicMachine<>(player, this));
    }

    @Override
    protected List<ItemStack> getInput() {
        return Arrays.asList(this.queueInputSlot.get(), this.inputSlot.get());
    }

    @Override
    protected void relocateStacks() {
        moveStack(this.queueOutputSlot, this.outputSlot);
    }

    @Override
    public void consumeInput(IRecipeAlloySmelter recipe, boolean consumeContainers) {
        TileEntityBasicMachineMultiInput.consumeMultiInput(this, recipe, consumeContainers);
    }
}
