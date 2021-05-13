package mods.gregtechmod.objects.blocks.tileentities.teblocks;

import ic2.core.ContainerBase;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerBasic;
import mods.gregtechmod.gui.GuiChemicalReactor;
import mods.gregtechmod.inventory.GtSlotProcessableItemStack;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.base.TileEntityGTMachine;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.container.ContainerChemicalReactor;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class TileEntityChemicalReactor extends TileEntityGTMachine<IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>, List<IRecipeIngredient>, List<ItemStack>, IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>>> {
    public final GtSlotProcessableItemStack<IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>>, List<ItemStack>> secondaryInputSlot;

    public TileEntityChemicalReactor() {
        super("chemical_reactor", 10000, 1, 1, GtRecipes.chemical);
        this.secondaryInputSlot = getInputSlot("secondary_input", false);
    }

    @Override
    public void consumeInput(IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>> recipe, boolean consumeContainers) {
        List<IRecipeIngredient> input = recipe.getInput();
        Stream.of(this.inputSlot, this.secondaryInputSlot)
                .forEach(slot -> input.forEach(ingredient -> {
                    if (ingredient.apply(slot.get())) slot.consume(ingredient.getCount(), true);
                }));
    }

    @Override
    public IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>> getRecipe() {
        IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>> recipe = this.recipeManager.getRecipeFor(Arrays.asList(this.inputSlot.get(), this.secondaryInputSlot.get()));
        return recipe;
    }

    @Override
    public ContainerBase<?> getGuiContainer(EntityPlayer player) {
        return new ContainerChemicalReactor(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiChemicalReactor(new ContainerChemicalReactor(player, this));
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {}
}
