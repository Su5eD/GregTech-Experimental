package mods.gregtechmod.objects.blocks.teblocks.struct;

import ic2.core.ContainerBase;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerBasic;
import mods.gregtechmod.gui.GuiVacuumFreezer;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerVacuumFreezer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class TileEntityVacuumFreezer extends TileEntityStructureBase<Object, IMachineRecipe<IRecipeIngredient, List<ItemStack>>, IRecipeIngredient, ItemStack, IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, List<ItemStack>>>> {
    
    public TileEntityVacuumFreezer() {
        super("vacuum_freezer", 10000, 1, 2, GtRecipes.vacuumFreezer);
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
    protected void getStructureElements(Map<Character, Predicate<IBlockState>> map) {
        map.put('R', state -> state.getBlock() == BlockItems.Block.REINFORCED_MACHINE_CASING.getInstance());
        map.put('D', state -> state.getBlock() == BlockItems.Block.ADVANCED_MACHINE_CASING.getInstance());
        map.put('A', state -> state.getBlock() == Blocks.AIR);
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
    public ContainerBase<?> getGuiContainer(EntityPlayer player) {
        return new ContainerVacuumFreezer(player, this); 
    }

    @Override
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiVacuumFreezer(new ContainerVacuumFreezer(player, this));
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {}
}
