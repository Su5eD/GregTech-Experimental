package mods.gregtechmod.objects.blocks.teblocks.inv;

import ic2.core.IHasGui;
import ic2.core.block.invslot.InvSlot;
import ic2.core.util.StackUtil;
import ic2.core.util.Util;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.gui.GuiElectricCraftingTable;
import mods.gregtechmod.inventory.invslot.GtSlot;
import mods.gregtechmod.inventory.invslot.GtSlotCraftingGrid;
import mods.gregtechmod.inventory.tank.GtFluidTank;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityUpgradable;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerElectricCraftingTable;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.OptionalItemStack;
import mods.gregtechmod.util.OreDictUnificator;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.ItemHandlerHelper;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class TileEntityElectricCraftingTable extends TileEntityUpgradable implements IHasGui {
    public final GtFluidTank tank;
    @NBTPersistent
    private ThroughPutMode throughPutMode = ThroughPutMode.BOTH;
    @NBTPersistent
    private CraftingMode craftingMode = CraftingMode.PATTERN;

    private int ticksUntilNextUpdate = 20;
    private boolean lastCraftSuccessful;
    private int currentSlot;
    private SlotPos currentSlotPos;
    private final List<SlotPos> slotPositions;

    public final GtSlot input;
    public final GtSlot crafting;
    public final GtSlotCraftingGrid craftingGrid;
    public final GtSlot output;
    public final GtSlot buffer;

    public TileEntityElectricCraftingTable() {
        this.tank = new GtFluidTank(this, "content", Util.allFacings, Util.allFacings, fluid -> true, 16000);
        this.fluids.addTank(this.tank);

        this.input = new GtSlot(this, "input", InvSlot.Access.I, 9);
        this.crafting = new GtSlot(this, "crafting", InvSlot.Access.NONE, 1);
        this.craftingGrid = new GtSlotCraftingGrid(this, "craftingGrid", 9);
        this.output = new GtSlot(this, "output", InvSlot.Access.O, 1);
        this.buffer = new GtSlot(this, "buffer", InvSlot.Access.O, 9);

        this.slotPositions = StreamEx.of(this.input, this.buffer)
            .cross(slot -> IntStreamEx.range(slot.size()).boxed())
            .mapKeyValue(SlotPos::new)
            .toList();
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
        return 10000;
    }

    @Override
    protected int getMinimumStoredEU() {
        return 3000;
    }

    @Override
    public int getSteamCapacity() {
        return getEUCapacity();
    }

    @Override
    public long getMjCapacity() {
        return getEUCapacity();
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();

        if (isAllowedToWork() && canUseEnergy(this.craftingMode.energyCost) && (workJustHasBeenEnabled() || this.ticksUntilNextUpdate-- < 1)) {
            this.ticksUntilNextUpdate = 32;

            if (this.output.isEmpty()) {
                processTank();

                ItemStack lastBuffer = this.buffer.get(8);
                if (!lastBuffer.isEmpty() && this.throughPutMode.items && this.craftingMode != CraftingMode.PATTERN) {
                    this.output.put(lastBuffer);
                    this.buffer.clear(8);
                    return;
                }

                Pair<ItemStack[], ItemStack> pair = getRecipe();
                ItemStack[] recipe = pair.getLeft();

                OptionalItemStack.of(pair.getRight())
                    .switchIfEmpty(() -> recipe != null ? ModHandler.getCraftingResult(recipe) : OptionalItemStack.EMPTY)
                    .ifEmpty(() -> this.lastCraftSuccessful = false) // TODO Clear crafting slot
                    .ifPresent(output -> {
                        if (this.craftingMode == CraftingMode.PATTERN) this.crafting.put(output);

                        ItemStack unified = OreDictUnificator.get(output);
                        this.crafting.put(unified);

                        List<ItemStack> list = recipeContent(recipe);
                        List<ItemStack> content = benchContent();

                        if (!list.isEmpty() && !content.isEmpty()) {
                            boolean canCraft = (this.craftingMode == CraftingMode.DUST || this.craftingMode == CraftingMode.NUGGET || this.buffer.isEmpty(8))
                                && StreamEx.of(list)
                                .allMatch(listStack -> StreamEx.of(content)
                                    .anyMatch(contentStack -> GtUtil.stackEquals(listStack, contentStack) && listStack.getCount() <= contentStack.getCount()));

                            this.lastCraftSuccessful = canCraft;
                            if (canCraft) {
                                processRecipe(recipe, output);
                            } else {
                                SlotPos lastPos = this.craftingMode == CraftingMode.PATTERN ? this.slotPositions.get(8) : this.slotPositions.get(17);
                                ItemStack lastStack = lastPos.getStack();
                                if (!lastStack.isEmpty() && this.output.isEmpty() && this.throughPutMode.items) {
                                    addStackToOutput(lastPos, lastStack);
                                }
                            }
                        }

                        moveRemainingItemsToOutput();
                    });
            }

            if (this.throughPutMode.items) {
                int cost = GtUtil.moveItemStack(this, getNeighborTE(getOppositeFacing()), getOppositeFacing(), getFacing(), 64, 1) * 10;
                useEnergy(cost);
            }
        }
    }
    
    private Pair<ItemStack[], ItemStack> getRecipe() {
        if (!this.lastCraftSuccessful) {
            nextSlot();

            IntStreamEx.ofIndices(this.slotPositions)
                .takeWhile(i -> this.slotPositions.get(this.currentSlot).isEmpty())
                .forEach(i -> nextSlot());

            this.currentSlotPos = this.slotPositions.get(this.currentSlot);
        }
        
        return !this.craftingMode.predicate.test(this) ? this.craftingMode.recipe.apply(this) : Pair.of(null, ItemStack.EMPTY);
    }

    private void processTank() {
        if (this.output.isEmpty()) {
            inputBufferStream()
                .findFirst(stack -> {
                    if (!this.tank.isEmpty()) {
                        Pair<ItemStack, FluidStack> result = GtUtil.fillContainer(stack, this.tank, Integer.MAX_VALUE);
                        ItemStack resultStack = result.getLeft();
                        FluidStack resultFluid = result.getRight();

                        return !resultStack.isEmpty() && IntStreamEx.range(this.input.size()).boxed()
                            .mapToEntry(this.input::get)
                            .anyMatch((i, slotStack) -> {
                                if ((slotStack.isEmpty() || GtUtil.stackEquals(resultStack, slotStack)) && GtUtil.canGrowStack(slotStack, resultStack)) {
                                    this.tank.drainInternal(resultFluid, true);
                                    stack.shrink(1);

                                    this.input.add(i, resultStack);
                                    return true;
                                }
                                return false;
                            });
                    }
                    return true;
                });
        }
    }
    
    private void processRecipe(ItemStack[] recipe, ItemStack output) {
        StreamEx.ofReversed(recipe)
            .mapPartial(stack -> StreamEx.ofReversed(this.slotPositions)
                .map(SlotPos::getStack)
                .findFirst(invStack -> GtUtil.stackEquals(stack, invStack)))
            .forEach(invStack -> {
                Item item = invStack.getItem();
                if (item.hasContainerItem(invStack)) {
                    ItemStack container = item.getContainerItem(invStack);
                    invStack.shrink(1);
                    if (!container.isEmpty() && (!container.isItemEnchantable() || container.getItemDamage() < container.getMaxDamage())) {
                        this.buffer.add(container);
                    }
                } else invStack.shrink(1);
            });
        
        this.output.put(output.copy());
        useEnergy(this.craftingMode.energyCost);
        this.ticksUntilNextUpdate = 1;
    }

    private void moveRemainingItemsToOutput() {
        if (this.output.isEmpty() && this.throughPutMode.items) {
            IntStreamEx.range(8).boxed()
                .mapToEntry(this.input::get)
                .forKeyValue((i, stack) -> IntStreamEx.range(i, 9)
                    .mapToObj(this.slotPositions::get)
                    .mapToEntry(SlotPos::getStack)
                    .filterValues(slotStack -> GtUtil.stackEquals(stack, slotStack) && stack.getMaxStackSize() > 8)
                    .findFirst()
                    .ifPresent(entry -> addStackToOutput(entry.getKey(), entry.getValue()))
                );
        }
    }
    
    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("throughPutMode");
        list.add("craftingMode");
        list.add("tank");
    }

    @Override
    public boolean placeCoverAtSide(ICover cover, EntityPlayer player, EnumFacing side, boolean simulate) {
        return side != getFacing() && side != getOppositeFacing() && super.placeCoverAtSide(cover, player, side, simulate);
    }

    @Override
    public Set<IC2UpgradeType> getCompatibleIC2Upgrades() {
        return EnumSet.of(IC2UpgradeType.TRANSFORMER, IC2UpgradeType.BATTERY);
    }

    @Override
    protected Collection<EnumFacing> getSinkSides() {
        return GtUtil.allSidesExcept(getOppositeFacing());
    }

    @Override
    protected Collection<EnumFacing> getSourceSides() {
        return this.throughPutMode.energy ? Collections.singleton(getOppositeFacing()) : Util.noFacings;
    }

    @Override
    public ContainerElectricCraftingTable getGuiContainer(EntityPlayer player) {
        return new ContainerElectricCraftingTable(player, this);
    }

    @Override
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiElectricCraftingTable(getGuiContainer(player));
    }

    @Override
    public void onGuiClosed(EntityPlayer player) {}

    public ThroughPutMode getThroughPutMode() {
        return this.throughPutMode;
    }

    public CraftingMode getCraftingMode() {
        return this.craftingMode;
    }

    public void nextCraftingMode() {
        this.craftingMode = this.craftingMode.switchType(1);
    }

    public void previousCraftingMode() {
        this.craftingMode = this.craftingMode.switchType(-1);
    }

    public void nextThroughPutMode() {
        this.throughPutMode = this.throughPutMode.next();
        this.energy.refreshSides();
    }
    
    private void nextSlot() {
        this.currentSlot = (this.currentSlot + 1) % this.slotPositions.size();
    }

    private boolean isItemInCraftingGrid(ItemStack stack) {
        return stack.isEmpty() || StreamEx.of(this.craftingGrid.iterator())
            .remove(ItemStack::isEmpty)
            .anyMatch(slotStack -> GtUtil.stackEquals(slotStack, stack) || GtUtil.stackEquals(GtUtil.getEmptyFluidContainer(slotStack), stack));
    }
    
    private StreamEx<ItemStack> inputBufferStream() {
        return StreamEx.of(StreamEx.of(this.input.iterator()))
            .append(StreamEx.of(this.buffer.iterator()));
    }

    private List<ItemStack> benchContent() {
        return inputBufferStream()
            .remove(ItemStack::isEmpty)
            .map(ItemStack::copy)
            .collapse(GtUtil::stackEquals, (first, second) -> StackUtil.incSize(first, second.getCount()))
            .toList();
    }

    private List<ItemStack> recipeContent(ItemStack[] recipe) {
        return StreamEx.of(recipe)
            .remove(ItemStack::isEmpty)
            .map(stack -> ItemHandlerHelper.copyStackWithSize(stack, 1))
            .collapse(GtUtil::stackEquals, (first, second) -> StackUtil.incSize(first))
            .toList();
    }
    
    private void addStackToOutput(SlotPos slot, ItemStack stack) {
        this.output.put(stack);
        slot.clear();
        this.ticksUntilNextUpdate = 1;
    }

    public enum ThroughPutMode {
        BOTH(true, true),
        ITEMS(false, true),
        ENERGY(true, false),
        NOTHING(false, false);

        private static final ThroughPutMode[] VALUES = values();

        public final boolean energy;
        public final boolean items;

        ThroughPutMode(boolean energy, boolean items) {
            this.energy = energy;
            this.items = items;
        }

        public ThroughPutMode next() {
            return VALUES[(ordinal() + 1) % VALUES.length];
        }
    }

    public enum CraftingMode {
        PATTERN(te -> {
            ItemStack stack = te.currentSlotPos.getStack();
            if (!te.isItemInCraftingGrid(stack)) {
                if (te.output.isEmpty() && te.throughPutMode.items && te.currentSlot < 8) {
                    te.addStackToOutput(te.currentSlotPos, stack);
                }
                return true;
            }
            return false;
        }, recipe(te -> StreamEx.of(te.craftingGrid.iterator())
            .map(stack -> stack.isEmpty() ? stack : ItemHandlerHelper.copyStackWithSize(stack, 1))
            .toArray(ItemStack[]::new))),
        DYNAMIC(recipe((te, recipe) -> {
            ItemStack stack = ItemHandlerHelper.copyStackWithSize(te.currentSlotPos.getStack(), 1);
            // Try crafting 1x1 first
            recipe[0] = stack;
            if (ModHandler.getCraftingResult(recipe).isEmpty()) {
                // Try 2x2 now
                recipe[1] = stack;
                recipe[3] = stack;
                recipe[4] = stack;
                if (ModHandler.getCraftingResult(recipe).isEmpty()) {
                    // Finally, try 3x3
                    recipe[2] = stack;
                    recipe[5] = stack;
                    recipe[6] = stack;
                    recipe[7] = stack;
                    recipe[8] = stack;
                    
                    addFallbackOutput(te, recipe);
                }
            }
        })),
        SINGLE(2048),
        SMALL(2048),
        LARGE(2048),
        OREDICT(128),
        DUST(128),
        NUGGET(128),
        REPAIR(2048),
        MIXER(2048);

        private static final CraftingMode[] VALUES = values();

        private final Predicate<TileEntityElectricCraftingTable> predicate;
        private final Function<TileEntityElectricCraftingTable, Pair<ItemStack[], ItemStack>> recipe;
        private final int energyCost;

        CraftingMode(int energyCost) {
            this(te -> false, te -> Pair.of(null, ItemStack.EMPTY), energyCost); // TODO Remove this and array null checks
        }
        
        CraftingMode(Function<TileEntityElectricCraftingTable, Pair<ItemStack[], ItemStack>> recipe) {
            this(recipe, 2048);
        }

        CraftingMode(Function<TileEntityElectricCraftingTable, Pair<ItemStack[], ItemStack>> recipe, int energyCost) {
            this(CraftingMode::checkStack, recipe, energyCost);
        }
        
        CraftingMode(Predicate<TileEntityElectricCraftingTable> predicate, Function<TileEntityElectricCraftingTable, Pair<ItemStack[], ItemStack>> recipe) {
            this(predicate, recipe, 2048);
        }

        CraftingMode(Predicate<TileEntityElectricCraftingTable> predicate, Function<TileEntityElectricCraftingTable, Pair<ItemStack[], ItemStack>> recipe, int energyCost) {
            this.predicate = predicate;
            this.recipe = recipe;
            this.energyCost = energyCost;
        }

        public CraftingMode switchType(int step) {
            return VALUES[(VALUES.length + ordinal() + step) % VALUES.length];
        }

        private static Function<TileEntityElectricCraftingTable, Pair<ItemStack[], ItemStack>> recipe(Function<TileEntityElectricCraftingTable, ItemStack[]> recipe) {
            return te -> Pair.of(recipe.apply(te), ItemStack.EMPTY);
        }
        
        private static Function<TileEntityElectricCraftingTable, Pair<ItemStack[], ItemStack>> recipe(BiConsumer<TileEntityElectricCraftingTable, ItemStack[]> recipe) {
            return te -> {
                ItemStack[] stacks = GtUtil.emptyStackArray(9);
                recipe.accept(te, stacks);
                return Pair.of(stacks, ItemStack.EMPTY);
            };
        }

        private static boolean checkStack(TileEntityElectricCraftingTable te) {
            ItemStack stack = te.currentSlotPos.getStack();
            if (te.isItemInCraftingGrid(stack)) {
                if (te.output.isEmpty() && te.throughPutMode.items) {
                    te.addStackToOutput(te.currentSlotPos, stack);
                }
                return true;
            }
            return false;
        }
        
        private static void addFallbackOutput(TileEntityElectricCraftingTable te, ItemStack[] recipe) {
            if (ModHandler.getCraftingResult(recipe).isEmpty() && te.output.isEmpty()) {
                te.output.put(te.currentSlotPos.getStack());
                te.currentSlotPos.clear();
                te.ticksUntilNextUpdate = 1;
            }
        }
    }

    private static class SlotPos {
        private final GtSlot slot;
        private final int index;

        public SlotPos(GtSlot slot, int index) {
            this.slot = slot;
            this.index = index;
        }
        
        public boolean isEmpty() {
            return this.slot.isEmpty(this.index);
        }

        public ItemStack getStack() {
            return this.slot.get(this.index);
        }
        
        public void clear() {
            this.slot.clear(this.index);
        }
    }
}
