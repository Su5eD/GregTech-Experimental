package mods.gregtechmod.objects.blocks.teblocks.struct;

import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IRecipeUniversal;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerSecondaryFluid;
import mods.gregtechmod.gui.GuiIndustrialSawmill;
import mods.gregtechmod.inventory.invslot.GtSlotProcessableSecondary;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerIndustrialSawmill;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.struct.StructureElement;
import mods.gregtechmod.util.struct.StructureElementGatherer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class TileEntityIndustrialSawmill extends TileEntityStructureFluid<Object, IRecipeUniversal<List<IRecipeIngredient>>, IGtRecipeManagerSecondaryFluid<IRecipeUniversal<List<IRecipeIngredient>>>> {

    public TileEntityIndustrialSawmill() {
        super(2, GtRecipes.industrialSawmill);
    }

    @Override
    protected GtSlotProcessableSecondary<IGtRecipeManagerSecondaryFluid<IRecipeUniversal<List<IRecipeIngredient>>>, List<ItemStack>> getSecondaryInputSlot(String name) {
        return new GtSlotProcessableSecondary<>(this, name, 1, GtUtil.INV_SIDE_NS, this.recipeManager);
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
    protected Map<Character, Collection<StructureElement>> getStructureElements() {
        return new StructureElementGatherer(this::getWorld)
            .block('S', BlockItems.Block.STANDARD_MACHINE_CASING.getBlockInstance())
            .block('D', BlockItems.Block.REINFORCED_MACHINE_CASING.getBlockInstance())
            .gather();
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
    public int getBaseSinkTier() {
        return 2;
    }

    @Override
    protected int getBaseEUCapacity() {
        return 10000;
    }
}
