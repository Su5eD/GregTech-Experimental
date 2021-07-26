package mods.gregtechmod.objects.blocks.teblocks.struct;

import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IRecipeCellular;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerCellular;
import mods.gregtechmod.gui.GuiDistillationTower;
import mods.gregtechmod.inventory.invslot.GtConsumableCell;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerDistillationTower;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class TileEntityDistillationTower extends TileEntityStructureBase<Object, IRecipeCellular, IRecipeIngredient, ItemStack, IGtRecipeManagerCellular> {
    public GtConsumableCell cellSlot;
    
    public TileEntityDistillationTower() {
        super("distillation_tower", 4, GtRecipes.distillation);
        this.cellSlot = new GtConsumableCell(this, "cell_slot", 1);
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
                        " X "
                ),
                Arrays.asList(
                        "DDD",
                        "DAD",
                        "DDD",
                        "   "
                ),
                Arrays.asList(
                        "SSS",
                        "SAS",
                        "SSS",
                        "   "
                ),
                Arrays.asList(
                        "DDD",
                        "DAD",
                        "DDD",
                        "   "
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
    protected void getStructureElements(Map<Character, Predicate<BlockPos>> map) {
        map.put('S', pos -> GtUtil.findBlocks(world, pos, BlockItems.Block.STANDARD_MACHINE_CASING.getInstance()));
        map.put('D', pos -> GtUtil.findBlocks(world, pos, BlockItems.Block.ADVANCED_MACHINE_CASING.getInstance()));
        map.put('A', pos -> GtUtil.findBlocks(world, pos, Blocks.AIR));
    }

    @Override
    public void consumeInput(IRecipeCellular recipe, boolean consumeContainers) {
        IRecipeIngredient input = recipe.getInput();
        
        this.inputSlot.consume(input.getCount(), true);
        this.cellSlot.consume(recipe.getCells());
    }

    @Override
    public IRecipeCellular getRecipe() {
        return this.recipeManager.getRecipeFor(this.inputSlot.get(), this.cellSlot.get());
    }

    @Override
    public ContainerDistillationTower getGuiContainer(EntityPlayer player) {
        return new ContainerDistillationTower(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiDistillationTower(getGuiContainer(player));
    }
}
