package mods.gregtechmod.objects.blocks.teblocks;

import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerBasic;
import mods.gregtechmod.gui.GuiChemicalReactor;
import mods.gregtechmod.inventory.invslot.GtSlotProcessableItemStack;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityGTMachine;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerChemicalReactor;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;

public class TileEntityChemicalReactor extends TileEntityGTMachine<IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>, List<IRecipeIngredient>, List<ItemStack>, IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>>> {
    public final GtSlotProcessableItemStack<IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>>, List<ItemStack>> secondaryInputSlot;

    public TileEntityChemicalReactor() {
        super(1, GtRecipes.chemical);
        this.secondaryInputSlot = getInputSlot("secondary_input", false);
    }

    @Override
    public int getBaseSinkTier() {
        return 1;
    }

    @Override
    protected int getBaseEUCapacity() {
        return 10000;
    }

    @Override
    public void consumeInput(IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>> recipe, boolean consumeContainers) {
        GtUtil.consumeMultiInput(recipe.getInput(), this.inputSlot, this.secondaryInputSlot);
    }

    @Override
    public IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>> getRecipe() {
        return this.recipeManager.getRecipeFor(Arrays.asList(this.inputSlot.get(), this.secondaryInputSlot.get()));
    }

    @Override
    public ContainerChemicalReactor getGuiContainer(EntityPlayer player) {
        return new ContainerChemicalReactor(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiChemicalReactor(getGuiContainer(player));
    }
}
