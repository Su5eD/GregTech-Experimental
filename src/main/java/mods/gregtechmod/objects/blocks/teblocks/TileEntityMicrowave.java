package mods.gregtechmod.objects.blocks.teblocks;

import ic2.core.util.StackUtil;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.gui.GuiAutoElectricFurnace;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityBasicMachineSingleInput;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerBasicMachine;
import mods.gregtechmod.recipe.compat.ModRecipes;
import mods.gregtechmod.recipe.compat.RecipeFurnace;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientItemStack;
import mods.gregtechmod.util.MachineSafety;
import mods.gregtechmod.util.OreDictUnificator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class TileEntityMicrowave extends TileEntityBasicMachineSingleInput<IMachineRecipe<IRecipeIngredient, List<ItemStack>>> {
    private static final ItemStack NETHERRACK = new ItemStack(Blocks.NETHERRACK);

    public TileEntityMicrowave() {
        super("microwave", ModRecipes.FURNACE, true);
    }

    @Override
    public IMachineRecipe<IRecipeIngredient, List<ItemStack>> getRecipe() {
        IMachineRecipe<IRecipeIngredient, List<ItemStack>> recipe =  super.getRecipe();
        if (recipe == null) { // workaround for the egg recipe
            ItemStack input = getInput();
            if (input.getItem() == Items.EGG) return RecipeFurnace.create(RecipeIngredientItemStack.create(Items.EGG), new ItemStack(Items.EGG)); // We don't care what the output is, the microwave will explode before we get to adding it
        }
        return recipe;
    }

    @Override
    protected void prepareRecipeForProcessing(IMachineRecipe<IRecipeIngredient, List<ItemStack>> recipe) {
        recipe.getInput().getMatchingInputs().forEach(this::checkStack);
        recipe.getOutput().forEach(this::checkStack);

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
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiAutoElectricFurnace(new ContainerBasicMachine<>(player, this));
    }

    private boolean checkStack(ItemStack stack) {
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
}
