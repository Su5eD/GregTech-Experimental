package mods.gregtechmod.objects.blocks.teblocks;

import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IRecipeUniversal;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.gui.GuiAlloySmelter;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityElectricFurnaceBase;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;

public class TileEntityAlloySmelter extends TileEntityElectricFurnaceBase<List<IRecipeIngredient>, List<ItemStack>, IRecipeUniversal<List<IRecipeIngredient>>> {

    public TileEntityAlloySmelter() {
        super(GtRecipes.alloySmelter);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiAlloySmelter(getGuiContainer(player));
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
    protected void consumeInput(IRecipeUniversal<List<IRecipeIngredient>> recipe, boolean consumeContainers) {
        GtUtil.consumeMultiInput(recipe.getInput(), this.inputSlot, this.queueInputSlot);
    }
}
