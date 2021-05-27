package mods.gregtechmod.objects.blocks.teblocks.struct;

import ic2.core.ContainerBase;
import ic2.core.IC2;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IRecipeBlastFurnace;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerBasic;
import mods.gregtechmod.gui.GuiIndustrialBlastFurnace;
import mods.gregtechmod.inventory.GtSlotProcessableItemStack;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.blocks.teblocks.component.CoilHandler;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerBlastFurnace;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.struct.Structure;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class TileEntityIndustrialBlastFurnace extends TileEntityStructureBase<TileEntityIndustrialBlastFurnace.BlastFurnaceStructure, IRecipeBlastFurnace, List<IRecipeIngredient>, List<ItemStack>, IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IRecipeBlastFurnace>> {
    public final GtSlotProcessableItemStack<IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IRecipeBlastFurnace>, List<ItemStack>> secondaryInput;
    private final CoilHandler coilHandler;
    
    public TileEntityIndustrialBlastFurnace() {
        super("industrial_blast_furnace", 10000, 2, 2, GtRecipes.industrialBlastFurnace);
        this.secondaryInput = getInputSlot("secondary_input", false);
        
        this.coilHandler = addComponent(new CoilHandler(this, 4, () -> IC2.network.get(true).updateTileEntityField(this, "coilHandler")));
    }
    
    // ------ Structure definition ------

    @Override
    protected List<List<String>> getStructurePattern() {
        return Arrays.asList(
                Arrays.asList(
                        "CCC",
                        "CCC",
                        "CCC",
                        " X "
                ),
                Arrays.asList(
                        "CCC",
                        "CLC",
                        "CCC",
                        "   "
                ),
                Arrays.asList(
                        "CCC",
                        "CLC",
                        "CCC",
                        "   "
                ),
                Arrays.asList(
                        "CCC",
                        "CCC",
                        "CCC",
                        "   "
                )
        );
    }

    @Override
    protected void getStructureElements(Map<Character, Predicate<IBlockState>> map) {
        map.put('C', state -> {
            Block block = state.getBlock();
            return block == BlockItems.Block.STANDARD_MACHINE_CASING.getInstance() ||
                    block == BlockItems.Block.REINFORCED_MACHINE_CASING.getInstance() ||
                    block == BlockItems.Block.ADVANCED_MACHINE_CASING.getInstance();
        });
        map.put('L', state -> {
            Block block = state.getBlock();
            return block == Blocks.AIR || state == Blocks.LAVA.getDefaultState();
        });
    }

    @Override
    protected BlastFurnaceStructure createStructureInstance(Collection<IBlockState> states) {
        return new BlastFurnaceStructure(states);
    }
    
    // ------ Coil Handling ------
    
    @Override
    protected boolean onActivated(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (this.coilHandler.onActivated(player)) return true;
            
        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
    }

    @Override
    protected List<ItemStack> getAuxDrops(int fortune) {
        return this.coilHandler.addDrops(super.getAuxDrops(fortune));
    }
    
    // ---------------------------

    @Override
    protected boolean canOperate(IRecipeBlastFurnace recipe) {
        boolean ret = super.canOperate(recipe);
        if (ret) {
            Structure<BlastFurnaceStructure>.WorldStructure struct = checkWorldStructure();
            if (struct.instance != null) {
                int heatCapacity = struct.instance.heatCapacity + this.coilHandler.heatingCoilTier * 500;
                return heatCapacity >= recipe.getHeat();
            }
            return true;
        }
        
        return false;
    }

    @Override
    public void consumeInput(IRecipeBlastFurnace recipe, boolean consumeContainers) {
        GtUtil.consumeMultiInput(recipe.getInput(), this.inputSlot, this.secondaryInput);
    }

    @Override
    public IRecipeBlastFurnace getRecipe() {
        return this.recipeManager.getRecipeFor(Arrays.asList(this.inputSlot.get(), this.secondaryInput.get()));
    }

    @Override
    public ContainerBase<?> getGuiContainer(EntityPlayer player) {
        return new ContainerBlastFurnace(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiIndustrialBlastFurnace(new ContainerBlastFurnace(player, this));
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {}
    
    public static class BlastFurnaceStructure {
        private int heatCapacity;
        
        public BlastFurnaceStructure(Collection<IBlockState> states) {
            states.stream()
                    .map(IBlockState::getBlock)
                    .forEach(block -> {
                        if (block == BlockItems.Block.STANDARD_MACHINE_CASING.getInstance()) {
                            heatCapacity += 30;
                        }
                        else if (block == BlockItems.Block.REINFORCED_MACHINE_CASING.getInstance()) {
                            heatCapacity += 50;
                        } else if (block == BlockItems.Block.ADVANCED_MACHINE_CASING.getInstance()) {
                            heatCapacity += 70;
                        } else if (block == Blocks.LAVA) {
                            heatCapacity += 250;
                        }
                    });
        }
    }
    
    public int getHeatCapacity() {
        BlastFurnaceStructure struct = checkWorldStructure().instance;
        return this.coilHandler.heatingCoilTier * 500 + (struct == null ? 0 : struct.heatCapacity);
    }
}
