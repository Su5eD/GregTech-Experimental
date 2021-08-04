package mods.gregtechmod.objects.blocks.teblocks.struct;

import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerBasic;
import mods.gregtechmod.gui.GuiVacuumFreezer;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerVacuumFreezer;
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

public class TileEntityVacuumFreezer extends TileEntityStructureBase<Object, IMachineRecipe<IRecipeIngredient, List<ItemStack>>, IRecipeIngredient, ItemStack, IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, List<ItemStack>>>> {
    
    public TileEntityVacuumFreezer() {
        super("vacuum_freezer", 1, GtRecipes.vacuumFreezer);
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
                        "RRR",
                        "RDR",
                        "RRR"
                ),
                Arrays.asList(
                        "RDR",
                        "DAD",
                        "RDR"
                ),
                Arrays.asList(
                        "RRR",
                        "RDR",
                        "RRR"
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
                .block('R', BlockItems.Block.REINFORCED_MACHINE_CASING.getInstance())
                .block('D', BlockItems.Block.ADVANCED_MACHINE_CASING.getInstance())
                .block('A', Blocks.AIR)
                .gather();
    }

    @Override
    public void consumeInput(IMachineRecipe<IRecipeIngredient, List<ItemStack>> recipe, boolean consumeContainers) {
        IRecipeIngredient input = recipe.getInput();
        this.inputSlot.consume(input.getCount(), true);
    }

    @Override
    public IMachineRecipe<IRecipeIngredient, List<ItemStack>> getRecipe() {
        return this.recipeManager.getRecipeFor(this.inputSlot.get());
    }

    @Override
    public ContainerVacuumFreezer getGuiContainer(EntityPlayer player) {
        return new ContainerVacuumFreezer(player, this); 
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiVacuumFreezer(getGuiContainer(player));
    }
}
