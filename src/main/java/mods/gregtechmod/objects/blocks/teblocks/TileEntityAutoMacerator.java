package mods.gregtechmod.objects.blocks.teblocks;

import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.gui.GuiAutoMacerator;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityBasicMachineSingleInput;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerBasicMachine;
import mods.gregtechmod.recipe.compat.ModRecipes;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TileEntityAutoMacerator extends TileEntityBasicMachineSingleInput<IMachineRecipe<IRecipeIngredient, List<ItemStack>>> {

    public TileEntityAutoMacerator() {
        super("auto_macerator", ModRecipes.MACERATOR);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiAutoMacerator(new ContainerBasicMachine<>(player, this));
    }
}
