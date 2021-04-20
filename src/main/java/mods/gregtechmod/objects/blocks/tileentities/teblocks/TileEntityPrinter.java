package mods.gregtechmod.objects.blocks.tileentities.teblocks;

import ic2.core.block.invslot.InvSlot;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IRecipePrinter;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerBasic;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.gui.GuiPrinter;
import mods.gregtechmod.inventory.slot.InvSlotCopy;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.base.TileEntityBasicMachine;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.container.ContainerBasicMachine;
import mods.gregtechmod.recipe.RecipePrinter;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientItemStack;
import mods.gregtechmod.util.OreDictUnificator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TileEntityPrinter extends TileEntityBasicMachine<IRecipePrinter, List<IRecipeIngredient>, List<ItemStack>, IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IRecipePrinter>> {

    public TileEntityPrinter() {
        super("printer", GtRecipes.printer, true);
    }

    @Override
    protected List<ItemStack> getInput() {
        return !this.queueInputSlot.isEmpty() && !this.inputSlot.isEmpty() ? Arrays.asList(this.queueInputSlot.get(), this.inputSlot.get(), this.extraSlot.get()) : Collections.singletonList(this.queueInputSlot.get());
    }

    @Override
    protected InvSlot getExtraSlot() {
        return new InvSlotCopy(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiPrinter(new ContainerBasicMachine<>(player, this));
    }

    @Override
    public void consumeInput(IRecipePrinter recipe, boolean consumeContainers) {
        List<IRecipeIngredient> input = recipe.getInput();
        this.queueInputSlot.consume(input.get(0).getCount());
        if (input.size() > 1) {
            ItemStack oldStack = this.inputSlot.consume(input.get(1).getCount());
            ItemStack newStack = this.inputSlot.get();
            if (!(oldStack.getItem() == newStack.getItem())) {
                this.queueOutputSlot.put(this.inputSlot.consume(1));
            }
        }
    }

    @Override
    public IRecipePrinter getRecipe() {
        IRecipePrinter recipe = super.getRecipe();
        ItemStack primaryInput = this.queueInputSlot.get();
        if (recipe == null) recipe = this.recipeManager.getRecipeFor(Collections.singletonList(this.queueInputSlot.get()));
        if (recipe == null) {
            if (!this.queueOutputSlot.isEmpty()) this.outputBlocked = true;

            ItemStack secondaryInput = this.inputSlot.get();
            if (OreDictUnificator.isItemInstanceOf(secondaryInput, "dye", true)) {
                ItemStack result = ModHandler.getCraftingResult(primaryInput, secondaryInput);
                if (!result.isEmpty())
                    return addLazyRecipe(primaryInput, secondaryInput, null, result, 200, 2);
            }

            ItemStack extra = this.extraSlot.get();
            if (!extra.isEmpty() && OreDictUnificator.isItemInstanceOf(secondaryInput, "dyeBlack", false)) {
                Item extraItem = extra.getItem();
                Item primaryItem = primaryInput.getItem();
                if (extraItem == Items.WRITTEN_BOOK && primaryItem == Items.BOOK) {
                    ItemStack copy = extra.copy();
                    return addLazyRecipe(primaryInput, secondaryInput, copy, copy, 200, 1);
                }
                else if (extraItem == Items.FILLED_MAP && primaryItem == Items.MAP) {
                    ItemStack copy = extra.copy();
                    return addLazyRecipe(primaryInput, secondaryInput, copy, copy, 100, 1);
                }
            }
        }

        return recipe;
    }

    private IRecipePrinter addLazyRecipe(ItemStack primaryInput, ItemStack secondaryInput, ItemStack copy, ItemStack output, int duration, double energyCost) {
        IRecipePrinter newRecipe = RecipePrinter.create(Arrays.asList(RecipeIngredientItemStack.create(primaryInput), RecipeIngredientItemStack.create(secondaryInput)), copy != null ? RecipeIngredientItemStack.create(copy) : null, output, duration, energyCost);
        GtRecipes.printer.addRecipe(newRecipe);
        return newRecipe;
    }

    @Override
    public void addOutput(List<ItemStack> output) {
        this.outputSlot.add(output);

        dumpOutput();
    }
}
