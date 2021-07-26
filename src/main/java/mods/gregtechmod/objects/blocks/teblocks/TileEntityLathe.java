package mods.gregtechmod.objects.blocks.teblocks;

import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.gui.GuiLathe;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityBasicMachineSingleInput;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TileEntityLathe extends TileEntityBasicMachineSingleInput<IMachineRecipe<IRecipeIngredient, List<ItemStack>>> {

    public TileEntityLathe() {
        super("lathe", GtRecipes.lathe);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiLathe(getGuiContainer(player));
    }

    @Override
    protected void relocateStacks() {
        moveStack(this.queueInputSlot, this.inputSlot);
    }

    @Override
    protected IMachineRecipe<IRecipeIngredient, List<ItemStack>> fitRecipe(IMachineRecipe<IRecipeIngredient, List<ItemStack>> recipe) {
        if (recipe != null) {
            List<ItemStack> output = recipe.getOutput();
            if (this.queueOutputSlot.canAdd(output.get(0)) && (output.size() <= 1 || this.outputSlot.canAdd(output.get(1)))) {
                this.outputBlocked = false;
                return recipe;
            } else this.outputBlocked = true;
        }
        return null;
    }

    @Override
    public void addOutput(List<ItemStack> output) {
        this.queueOutputSlot.add(output.get(0));
        if (output.size() > 1) this.outputSlot.add(output.get(1));

        dumpOutput();
    }
}
