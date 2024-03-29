package mods.gregtechmod.objects.blocks.teblocks.base;

import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerBasic;
import mods.gregtechmod.gui.GuiAutoElectricFurnace;
import mods.gregtechmod.objects.blocks.teblocks.component.CoilHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public abstract class TileEntityElectricFurnaceBase<RI, I, R extends IMachineRecipe<RI, List<ItemStack>>> extends TileEntityBasicMachine<R, RI, I, IGtRecipeManagerBasic<RI, I, R>> {
    private final CoilHandler coilHandler;

    protected TileEntityElectricFurnaceBase(IGtRecipeManagerBasic<RI, I, R> recipeManager) {
        super(recipeManager);

        this.coilHandler = addComponent(new CoilHandler(this, 1));
    }

    @Override
    protected void relocateStacks() {
        moveStack(this.queueInputSlot, this.inputSlot);
        moveStack(this.queueOutputSlot, this.outputSlot);
    }

    @Override
    protected void prepareRecipeForProcessing(R recipe) {
        super.prepareRecipeForProcessing(recipe);
        this.maxProgress = this.coilHandler.getMaxProgress(this.maxProgress);
    }

    @Override
    protected List<ItemStack> getAuxDrops(int fortune) {
        return this.coilHandler.addDrops(super.getAuxDrops(fortune));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiAutoElectricFurnace(getGuiContainer(player));
    }
}
