package mods.gregtechmod.objects.blocks.teblocks.struct;

import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerSecondaryFluid;
import mods.gregtechmod.gui.GuiIndustrialGrinder;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerIndustrialGrinder;
import mods.gregtechmod.util.struct.StructureElement;
import mods.gregtechmod.util.struct.StructureElementGatherer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class TileEntityIndustrialGrinder extends TileEntityStructureFluid<Object, IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>, IGtRecipeManagerSecondaryFluid<IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>>> {

    public TileEntityIndustrialGrinder() {
        super(3, GtRecipes.industrialGrinder);
    }

    @Override
    public int getBaseSinkTier() {
        return 2;
    }

    @Override
    protected int getBaseEUCapacity() {
        return 10000;
    }

    @Override
    protected List<List<String>> getStructurePattern() {
        return Arrays.asList(
            Arrays.asList(
                "   ",
                "SSS",
                "SSS",
                "SSS"
            ),
            Arrays.asList(
                " X ",
                "RRR",
                "RWR",
                "RRR"
            ),
            Arrays.asList(
                "   ",
                "SSS",
                "SSS",
                "SSS"
            )
        );
    }

    @Override
    protected Map<Character, Collection<StructureElement>> getStructureElements() {
        return new StructureElementGatherer(this::getWorld)
            .block('S', BlockItems.Block.STANDARD_MACHINE_CASING.getBlockInstance())
            .block('R', BlockItems.Block.REINFORCED_MACHINE_CASING.getBlockInstance())
            .block('W', Blocks.WATER)
            .gather();
    }

    @Override
    public ContainerIndustrialGrinder getGuiContainer(EntityPlayer player) {
        return new ContainerIndustrialGrinder(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiIndustrialGrinder(getGuiContainer(player));
    }
}
