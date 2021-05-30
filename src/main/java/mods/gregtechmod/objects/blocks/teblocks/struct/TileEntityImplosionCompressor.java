package mods.gregtechmod.objects.blocks.teblocks.struct;

import ic2.core.ContainerBase;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerBasic;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.gui.GuiImplosionCompressor;
import mods.gregtechmod.inventory.GtSlotProcessableImplosion;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerImplosionCompressor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;

import java.util.*;
import java.util.function.Predicate;

public class TileEntityImplosionCompressor extends TileEntityStructureBase<Object, IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>, List<IRecipeIngredient>, List<ItemStack>, IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>>> {
    public final GtSlotProcessableImplosion secondaryInput;
    
    public TileEntityImplosionCompressor() {
        super("implosion_compressor", 10000, 2, 1, GtRecipes.implosion);
        this.secondaryInput = new GtSlotProcessableImplosion(this, "itnt_input", 1);
    }
    
    @Override
    protected List<List<String>> getStructurePattern() {
        return Arrays.asList(
                Arrays.asList(
                        "SRS",
                        "RRR",
                        "SRS"
                ),
                Arrays.asList(
                        "RRR",
                        "RAR",
                        "RRR"
                ),
                Arrays.asList(
                        "SRS",
                        "RRR",
                        "SRS"
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
        map.put('S', state -> state.getBlock() == BlockItems.Block.STANDARD_MACHINE_CASING.getInstance());
        map.put('R', state -> state.getBlock() == BlockItems.Block.REINFORCED_MACHINE_CASING.getInstance());
        map.put('A', state -> state.getBlock() == Blocks.AIR);
    }

    @Override
    public void consumeInput(IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>> recipe, boolean consumeContainers) {
        List<IRecipeIngredient> input = recipe.getInput();
        this.inputSlot.consume(input.get(0).getCount(), true);
        this.secondaryInput.consume(input.get(1).getCount(), true);
    }

    @Override
    public IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>> getRecipe() {
        return this.recipeManager.getRecipeFor(Arrays.asList(this.inputSlot.get(), this.secondaryInput.get()));
    }

    @Override
    protected boolean processRecipe(IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>> recipe) {
        boolean ret = super.processRecipe(recipe);
        if (getActive() && tickCounter % 20 == 0) {
            this.world.playSound(null, this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4, (1 + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F);
        }
        return ret;
    }

    @Override
    public ContainerBase<?> getGuiContainer(EntityPlayer player) {
        return new ContainerImplosionCompressor(player, this);
    }

    @Override
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiImplosionCompressor(new ContainerImplosionCompressor(player, this));
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {}

    @Override
    public Set<IC2UpgradeType> getCompatibleIC2Upgrades() {
        return EnumSet.of(IC2UpgradeType.TRANSFORMER, IC2UpgradeType.BATTERY);
    }
}
