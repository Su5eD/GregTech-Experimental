package dev.su5ed.gtexperimental.blockentity;

import dev.su5ed.gtexperimental.api.util.FriendlyCompoundTag;
import dev.su5ed.gtexperimental.block.SimpleMachineBlock;
import dev.su5ed.gtexperimental.blockentity.base.MachineBlockEntity;
import dev.su5ed.gtexperimental.blockentity.component.RecipeHandler;
import dev.su5ed.gtexperimental.menu.SimpleMachineMenu;
import dev.su5ed.gtexperimental.network.Networked;
import dev.su5ed.gtexperimental.network.SynchronizedData;
import dev.su5ed.gtexperimental.object.GTBlockEntity;
import dev.su5ed.gtexperimental.object.ModMenus;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeManagers;
import dev.su5ed.gtexperimental.util.BlockEntityProvider;
import dev.su5ed.gtexperimental.util.GtUtil;
import dev.su5ed.gtexperimental.util.InvUtil;
import dev.su5ed.gtexperimental.util.KeyboardHandler;
import dev.su5ed.gtexperimental.util.OutputSide;
import dev.su5ed.gtexperimental.util.inventory.DischargingInventorySlot;
import dev.su5ed.gtexperimental.util.inventory.InventorySlot;
import dev.su5ed.gtexperimental.util.inventory.SlotDirection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class SimpleMachineBlockEntity extends MachineBlockEntity implements MenuProvider {
    private final ModMenus.BlockEntityMenuConstructor<SimpleMachineMenu> menuConstructor;
    public final InventorySlot queueInputSlot;
    public final InventorySlot inputSlot;
    public final InventorySlot queueOutputSlot;
    public final InventorySlot outputSlot;
    public final InventorySlot extraSlot;
    public final RecipeHandler<?, ?, ?, ?> recipeHandler;

    @Networked
    public boolean provideEnergy;
    @Networked
    public boolean autoOutput = true;

    private final SynchronizedData.Key provideEnergyKey = SynchronizedData.Key.field("provideEnergy");
    private final SynchronizedData.Key autoOutputKey = SynchronizedData.Key.field("autoOutput");

    public static SimpleMachineBlockEntity autoMacerator(BlockPos pos, BlockState state) {
        return new SimpleMachineBlockEntity(GTBlockEntity.AUTO_MACERATOR, pos, state, SimpleMachineMenu::autoMacerator, be -> RecipeHandler.SISO.create(be, ModRecipeManagers.MACERATOR));
    }

    public static SimpleMachineBlockEntity autoExtractor(BlockPos pos, BlockState state) {
        return new SimpleMachineBlockEntity(GTBlockEntity.AUTO_EXTRACTOR, pos, state, SimpleMachineMenu::autoExtractor, be -> RecipeHandler.SISO.create(be, ModRecipeManagers.EXTRACTOR));
    }

    public static SimpleMachineBlockEntity autoCompressor(BlockPos pos, BlockState state) {
        return new SimpleMachineBlockEntity(GTBlockEntity.AUTO_COMPRESSOR, pos, state, SimpleMachineMenu::autoCompressor, be -> RecipeHandler.SISO.create(be, ModRecipeManagers.COMPRESSOR));
    }

    public SimpleMachineBlockEntity(BlockEntityProvider provider, BlockPos pos, BlockState state, ModMenus.BlockEntityMenuConstructor<SimpleMachineMenu> menuConstructor, Function<SimpleMachineBlockEntity, RecipeHandler<?, ?, ?, ?>> recipeHandlerFactory) {
        super(provider, pos, state);
        this.menuConstructor = menuConstructor;
        // Slot registration order defines the order in which items are inserted/extracted
        this.inputSlot = this.inventoryHandler.addSlot("input", InventorySlot.Mode.INPUT, SlotDirection.SIDE, 1, this::hasRecipeForItem);
        this.queueInputSlot = this.inventoryHandler.addSlot("queueInput", InventorySlot.Mode.INPUT, SlotDirection.VERTICAL, 1, this::hasRecipeForItem);
        this.outputSlot = this.inventoryHandler.addSlot("output", InventorySlot.Mode.OUTPUT, SlotDirection.BOTTOM, 1);
        this.queueOutputSlot = this.inventoryHandler.addSlot("queueOutput", InventorySlot.Mode.OUTPUT, SlotDirection.BOTTOM, 1);
        this.extraSlot = createExtraSlot();
        this.recipeHandler = addComponent(recipeHandlerFactory.apply(this));
    }

    protected InventorySlot createExtraSlot() {
        DischargingInventorySlot slot = this.inventoryHandler.addSlot(handler -> new DischargingInventorySlot(handler, "battery", InventorySlot.Mode.BOTH, SlotDirection.NONE, 1));
        this.energy.addDischargingSlot(slot);
        return slot;
    }

    private boolean hasRecipeForItem(ItemStack stack) {
        return this.recipeHandler.accepts(stack);
    }

    public OutputSide getOutputSide() {
        return getBlockState().getValue(SimpleMachineBlock.OUTPUT_SIDE);
    }

    public void switchProvideEnergy() {
        this.provideEnergy = !this.provideEnergy;
        this.energy.refreshSides();
    }

    public void switchAutoOutput() {
        this.autoOutput = !this.autoOutput;
    }

    public void switchSplitInput() {
        this.machineController.setStrictInputSides(!this.machineController.isStrictInputSides());
    }

    @Override
    public void addSyncedData(Set<? super SynchronizedData.Key> keys) {
        super.addSyncedData(keys);
        keys.add(provideEnergyKey);
        keys.add(autoOutputKey);
    }

    @Override
    public boolean setFacingWrench(Direction side, Player player) {
        if (getOutputSide().direction != side) {
            if (KeyboardHandler.isPressed(player, KeyboardHandler.Key.LEFT_ALT)) {
                OutputSide outputSide = side == getFacing() ? OutputSide.NONE : OutputSide.fromDirection(side);
                this.level.setBlockAndUpdate(this.worldPosition, getBlockState().setValue(SimpleMachineBlock.OUTPUT_SIDE, outputSide));
                return true;
            }
            return super.setFacingWrench(side, player);
        }
        return false;
    }

    @Override
    protected boolean canPlaceCoverAtSide(Direction side) {
        return side != getFacing() && super.canPlaceCoverAtSide(side);
    }

    @Override
    public Collection<Direction> getSinkSides() {
        return GtUtil.allSidesWithout(getFacing(), getOutputSide().direction);
    }

    @Override
    public Collection<Direction> getSourceSides() {
        Direction outputSide = getOutputSide().direction;
        return this.provideEnergy && outputSide != null ? List.of(outputSide) : List.of();
    }

    @Override
    public int getBaseSinkTier() {
        return 1;
    }

    @Override
    public int getBaseSourceTier() {
        return 1;
    }

    @Override
    protected int getBaseEUCapacity() {
        return 2000;
    }

    @Override
    public double getMinimumStoredEnergy() {
        return 1000;
    }

    @Override
    protected void onInventoryChanged() {
        super.onInventoryChanged();
        this.recipeHandler.checkRecipe();
    }

    @Override
    public void tickServer() {
        super.tickServer();

        // Advance Queue
        moveStack(this.queueInputSlot, this.inputSlot);
        moveStack(this.queueOutputSlot, this.outputSlot);

        // Dump output
        if (getTicks() % 1200 == 0 || this.recipeHandler.isOutputBlocked()) {
            ejectOutput();
        }
    }

    public void ejectOutput() {
        OutputSide outputSide = getOutputSide();
        if (this.autoOutput && outputSide != OutputSide.NONE) {
            ItemStack output = this.outputSlot.get();
            if (!output.isEmpty() && this.energy.canUseEnergy(500)) {
                BlockEntity dest = this.level.getBlockEntity(this.worldPosition.relative(outputSide.direction));
                if (dest != null) {
                    double used = this.energy.useEnergy(InvUtil.moveItemStack(this, dest, outputSide.direction, 64));
                    if (used > 0 && !this.queueOutputSlot.isEmpty()) {
                        this.energy.useEnergy(InvUtil.moveItemStack(this, dest, outputSide.direction, 64));
                    }
                }
            }
        }
    }

    @Override
    protected void saveAdditional(FriendlyCompoundTag tag) {
        super.saveAdditional(tag);

        tag.putBoolean("provideEnergy", this.provideEnergy);
        tag.putBoolean("autoOutput", this.autoOutput);
    }

    @Override
    protected void load(FriendlyCompoundTag tag) {
        super.load(tag);

        this.provideEnergy = tag.getBoolean("provideEnergy");
        this.autoOutput = tag.getBoolean("autoOutput");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return this.menuConstructor.create(containerId, this.worldPosition, playerInventory, player);
    }

    public void moveStack(InventorySlot src, InventorySlot dest) {
        ItemStack srcItem = src.get();
        ItemStack destItem = dest.get();
        if (!srcItem.isEmpty() && destItem.isEmpty()) {
            src.setItem(0, ItemStack.EMPTY);
            dest.setItem(0, srcItem);
        }
        else if (ItemHandlerHelper.canItemStacksStack(srcItem, destItem)) {
            int toMove = Math.min(destItem.getMaxStackSize() - destItem.getCount(), srcItem.getCount());
            srcItem.shrink(toMove);
            destItem.grow(toMove);
        }
    }
}
