package mods.gregtechmod.objects.blocks.tileentities.teblocks;

import ic2.core.block.machine.tileentity.TileEntityRecycler;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.gui.GuiAutoRecycler;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.base.TileEntityBasicMachine;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.container.ContainerBasicMachine;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class TileEntityAutoRecycler extends TileEntityBasicMachine {

    public TileEntityAutoRecycler() {
        super("auto_recycler", null);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiAutoRecycler(new ContainerBasicMachine<>(player, this));
    }

    @Override
    protected boolean canOperate(IMachineRecipe<IRecipeIngredient, List<ItemStack>> recipe) {
        boolean canWork = this.maxProgress > 0 || !this.pendingRecipe.isEmpty();
        return canWork || enableWorking && !this.inputSlot.isEmpty();
    }

    @Override
    public IMachineRecipe<IRecipeIngredient, List<ItemStack>> getRecipe() {
        moveStack(this.queueInputSlot, this.inputSlot);
        moveStack(this.queueOutputSlot, this.outputSlot);
        return null;
    }

    @Override
    protected void updateEnergyConsume(@Nullable IMachineRecipe<IRecipeIngredient, List<ItemStack>> recipe) {
        this.energyConsume = this.baseEnergyConsume = 1;
        overclockEnergyConsume();
    }

    @Override
    protected void prepareRecipeForProcessing(IMachineRecipe<IRecipeIngredient, List<ItemStack>> recipe) {
        ItemStack stack = this.inputSlot.consume(1, false, true);
        if (!TileEntityRecycler.getIsItemBlacklisted(stack) && GtUtil.RANDOM.nextInt(8) == 0) this.pendingRecipe.add(ModHandler.scrap.copy());
        this.maxProgress = 45;
    }
}
