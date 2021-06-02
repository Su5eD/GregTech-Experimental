package mods.gregtechmod.gui;

import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityBasicMachineSingleInput;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerBasicMachine;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class GuiAutoMacerator extends GuiBasicMachine<ContainerBasicMachine<? extends TileEntityBasicMachineSingleInput<? extends IMachineRecipe<IRecipeIngredient, List<ItemStack>>>>> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/auto_macerator.png");

    public GuiAutoMacerator(ContainerBasicMachine<? extends TileEntityBasicMachineSingleInput<? extends IMachineRecipe<IRecipeIngredient, List<ItemStack>>>> container) {
        super(TEXTURE, container, GregtechGauge.MACERATING);
    }
}
