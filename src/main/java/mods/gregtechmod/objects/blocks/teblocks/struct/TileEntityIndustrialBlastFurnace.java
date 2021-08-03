package mods.gregtechmod.objects.blocks.teblocks.struct;

import ic2.core.IC2;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IRecipeBlastFurnace;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerBasic;
import mods.gregtechmod.gui.GuiIndustrialBlastFurnace;
import mods.gregtechmod.inventory.invslot.GtSlotProcessableItemStack;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.blocks.teblocks.component.CoilHandler;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerBlastFurnace;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.struct.Structure;
import mods.gregtechmod.util.struct.StructureElement;
import mods.gregtechmod.util.struct.StructureElementGatherer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class TileEntityIndustrialBlastFurnace extends TileEntityStructureBase<TileEntityIndustrialBlastFurnace.BlastFurnaceStructure, IRecipeBlastFurnace, List<IRecipeIngredient>, List<ItemStack>, IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IRecipeBlastFurnace>> {
    public final GtSlotProcessableItemStack<IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IRecipeBlastFurnace>, List<ItemStack>> secondaryInput;
    private final CoilHandler coilHandler;
    
    public TileEntityIndustrialBlastFurnace() {
        super("industrial_blast_furnace", 2, GtRecipes.industrialBlastFurnace);
        this.secondaryInput = getInputSlot("secondary_input", false);
        
        this.coilHandler = addComponent(new CoilHandler(this, 4, () -> IC2.network.get(true).updateTileEntityField(this, "coilHandler")));
    }

    @Override
    protected int getBaseSinkTier() {
        return 2;
    }

    @Override
    protected int getBaseEUCapacity() {
        return 10000;
    }

    // ------ Structure definition ------
    @Override
    protected List<List<String>> getStructurePattern() {
        return Arrays.asList(
                Arrays.asList(
                        " X ",
                        "CCC",
                        "CCC",
                        "CCC"
                ),
                Arrays.asList(
                        "   ",
                        "CCC",
                        "CLC",
                        "CCC"
                ),
                Arrays.asList(
                        "   ",
                        "CCC",
                        "CLC",
                        "CCC"
                ),
                Arrays.asList(
                        "   ",
                        "CCC",
                        "CCC",
                        "CCC"
                )
        );
    }

    @Override
    protected Map<Character, Collection<StructureElement>> getStructureElements() {
        return new StructureElementGatherer(this::getWorld)
                .block('C', BlockItems.Block.STANDARD_MACHINE_CASING.getInstance(), BlockItems.Block.REINFORCED_MACHINE_CASING.getInstance(), BlockItems.Block.ADVANCED_MACHINE_CASING.getInstance())
                .predicate('L', pos -> {
                    IBlockState state = world.getBlockState(pos);
                    Block block = state.getBlock();
                    return block == Blocks.AIR || state == Blocks.LAVA.getDefaultState();
                })
                .gather();
    }

    @Override
    protected BlastFurnaceStructure createStructureInstance(EnumFacing facing, Map<Character, Collection<BlockPos>> elements) {
        return new BlastFurnaceStructure(this.world, elements);
    }
    
    // ------ Coil Handling ------
    
    @Override
    protected boolean onActivatedChecked(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (this.coilHandler.onActivated(player)) return true;
        
        return super.onActivatedChecked(player, hand, side, hitX, hitY, hitZ);
    }

    @Override
    protected List<ItemStack> getAuxDrops(int fortune) {
        return this.coilHandler.addDrops(super.getAuxDrops(fortune));
    }
    
    // ---------------------------

    @Override
    protected boolean canOperate(IRecipeBlastFurnace recipe) {
        boolean ret = super.canOperate(recipe);
        if (recipe != null && ret) {
            return this.structure.getWorldStructure()
                    .map(Structure.WorldStructure::getInstance)
                    .map(instance -> {
                        int heatCapacity = instance.heatCapacity + this.coilHandler.heatingCoilTier * 500;
                        return heatCapacity >= recipe.getHeat();
                    })
                    .orElse(true);
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
    public ContainerBlastFurnace getGuiContainer(EntityPlayer player) {
        return new ContainerBlastFurnace(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiIndustrialBlastFurnace(getGuiContainer(player));
    }
    
    public static class BlastFurnaceStructure {
        private int heatCapacity;
        
        public BlastFurnaceStructure(World world, Map<Character, Collection<BlockPos>> elements) {
            elements.get('C').stream()
                    .map(world::getBlockState)
                    .map(IBlockState::getBlock)
                    .forEach(block -> {
                        if (block == BlockItems.Block.STANDARD_MACHINE_CASING.getInstance()) {
                            heatCapacity += 30;
                        } else if (block == BlockItems.Block.REINFORCED_MACHINE_CASING.getInstance()) {
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
        int heatCapacity = this.structure.getWorldStructure()
                .map(Structure.WorldStructure::getInstance)
                .map(instance -> instance.heatCapacity)
                .orElse(0);
        return this.coilHandler.heatingCoilTier * 500 + heatCapacity;
    }
}
