package mods.gregtechmod.objects.blocks.teblocks.struct;

import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IRecipeUniversal;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerSecondaryFluid;
import mods.gregtechmod.gui.GuiIndustrialSawmill;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerIndustrialSawmill;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class TileEntityIndustrialSawmill extends TileEntityStructureFluid<Object, IRecipeUniversal<List<IRecipeIngredient>>, IGtRecipeManagerSecondaryFluid<IRecipeUniversal<List<IRecipeIngredient>>>> {
    
    public TileEntityIndustrialSawmill() {
        super("industrial_sawmill", 2, GtRecipes.industrialSawmill);
    }

    @Override
    protected List<List<String>> getStructurePattern() {
        return Arrays.asList(
                Arrays.asList(
                        "SSS",
                        "SDS",
                        "SSS"
                ),
                Arrays.asList(
                        "   ",
                        " X ",
                        "   "
                )
        );
    }
    
    @Override
    protected void getStructureElements(Map<Character, Predicate<BlockPos>> map) {
        map.put('S', pos -> GtUtil.findBlocks(world, pos, BlockItems.Block.STANDARD_MACHINE_CASING.getInstance()));
        map.put('D', pos -> GtUtil.findBlocks(world, pos, BlockItems.Block.REINFORCED_MACHINE_CASING.getInstance()));
    }

    @Override
    public ContainerIndustrialSawmill getGuiContainer(EntityPlayer player) {
        return new ContainerIndustrialSawmill(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiIndustrialSawmill(getGuiContainer(player));
    }

    @Override
    protected int getBaseSinkTier() {
        return 2;
    }

    @Override
    protected int getBaseEUCapacity() {
        return 10000;
    }
}
