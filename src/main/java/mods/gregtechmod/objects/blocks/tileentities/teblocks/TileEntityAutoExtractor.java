package mods.gregtechmod.objects.blocks.tileentities.teblocks;

import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.gui.GuiAutoExtractor;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.base.TileEntityBasicMachineSingleInput;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.container.ContainerBasicMachine;
import mods.gregtechmod.recipe.compat.ModRecipes;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TileEntityAutoExtractor extends TileEntityBasicMachineSingleInput<IMachineRecipe<IRecipeIngredient, List<ItemStack>>> {

    public TileEntityAutoExtractor() {
        super("auto_extractor", ModRecipes.EXTRACTOR);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiAutoExtractor(new ContainerBasicMachine<>(player, this));
    }
}
