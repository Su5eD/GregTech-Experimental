package mods.gregtechmod.objects.blocks.tileentities.teblocks;

import ic2.core.block.invslot.InvSlot;
import ic2.core.util.StackUtil;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerBasic;
import mods.gregtechmod.gui.GuiAutoElectricFurnace;
import mods.gregtechmod.inventory.GtSlotProcessableItemStack;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.base.TileEntityBasicMachineSingleInput;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.container.ContainerBasicMachine;
import mods.gregtechmod.recipe.compat.ModRecipes;
import mods.gregtechmod.util.MachineSafety;
import mods.gregtechmod.util.OreDictUnificator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

import javax.annotation.Nullable;
import java.util.List;

public class TileEntityMicrowave extends TileEntityBasicMachineSingleInput<IMachineRecipe<IRecipeIngredient, List<ItemStack>>> {
    private static final ItemStack NETHERRACK = new ItemStack(Blocks.NETHERRACK);

    public TileEntityMicrowave() {
        super("microwave", ModRecipes.FURNACE);
    }

    @Override
    public GtSlotProcessableItemStack<IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, List<ItemStack>>>, ItemStack> getInputSlot(int count, InvSlot.InvSide side) {
        return new GtSlotProcessableItemStack<>(this, "input", InvSlot.Access.I, count, side, null);
    }

    @Override
    public IMachineRecipe<IRecipeIngredient, List<ItemStack>> getRecipe() {
        IMachineRecipe<IRecipeIngredient, List<ItemStack>> recipe = super.getRecipe();
        if (recipe != null && shouldExplode(recipe.getOutput().get(0))) return null;

        return recipe;
    }

    @Override
    protected ItemStack getInput() {
        ItemStack input = super.getInput();
        if (shouldExplode(input)) return ItemStack.EMPTY;
        return input;
    }

    private boolean shouldExplode(ItemStack stack) {
        if (OreDictUnificator.isItemInstanceOf(stack, "ingot", true) || OreDictUnificator.isItemInstanceOf(stack, "dustGunpowder", false) ||
                stack.isItemEqual(NETHERRACK) || StackUtil.checkItemEquality(stack, Items.EGG)) {
            explodeMachine(4);
            return true;
        } else if (TileEntityFurnace.getItemBurnTime(stack) > 0) {
            MachineSafety.setBlockOnFire(this.world, this.pos);
            return true;
        }

        return false;
    }

    @Override
    protected void prepareRecipeForProcessing(IMachineRecipe<IRecipeIngredient, List<ItemStack>> recipe) {
        recipe.getOutput().stream()
                .map(ItemStack::copy)
                .forEach(this.pendingRecipe::add);
        this.maxProgress = 25;
        consumeInput(recipe);
    }

    @Override
    protected void updateEnergyConsume(@Nullable IMachineRecipe<IRecipeIngredient, List<ItemStack>> recipe) {
        if (recipe != null) {
            this.energyConsume = this.baseEnergyConsume = 4;
            overclockEnergyConsume();
        }
    }

    @Override
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiAutoElectricFurnace(new ContainerBasicMachine<>(player, this));
    }
}
