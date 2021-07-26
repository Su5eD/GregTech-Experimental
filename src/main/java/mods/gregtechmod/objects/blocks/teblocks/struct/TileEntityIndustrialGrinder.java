package mods.gregtechmod.objects.blocks.teblocks.struct;

import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerSecondaryFluid;
import mods.gregtechmod.gui.GuiIndustrialGrinder;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerIndustrialGrinder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class TileEntityIndustrialGrinder extends TileEntityStructureFluid<Object, IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>, IGtRecipeManagerSecondaryFluid<IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>>> {
    
    public TileEntityIndustrialGrinder() {
        super("industrial_grinder", 3, GtRecipes.industrialGrinder);
    }

    @Override
    protected int getBaseSinkTier() {
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
                        "SSS",
                        "SSS",
                        "SSS",
                        "   "
                ),
                Arrays.asList(
                        "RRR",
                        "RWR",
                        "RRR",
                        " X "
                ),
                Arrays.asList(
                        "SSS",
                        "SSS",
                        "SSS",
                        "   "
                )
        );
    }

    @Override
    protected void getStructureElements(Map<Character, Predicate<IBlockState>> map) {
        map.put('S', state -> state.getBlock() == BlockItems.Block.STANDARD_MACHINE_CASING.getInstance());
        map.put('R', state -> state.getBlock() == BlockItems.Block.REINFORCED_MACHINE_CASING.getInstance());
        map.put('W', state -> state.getBlock() == Blocks.WATER);
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
