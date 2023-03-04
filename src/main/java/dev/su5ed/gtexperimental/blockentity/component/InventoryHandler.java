package dev.su5ed.gtexperimental.blockentity.component;

import dev.su5ed.gtexperimental.api.util.FriendlyCompoundTag;
import dev.su5ed.gtexperimental.blockentity.base.BaseBlockEntity;
import dev.su5ed.gtexperimental.util.inventory.InventorySlot;
import dev.su5ed.gtexperimental.util.inventory.SidedSlotHandlerWrapper;
import dev.su5ed.gtexperimental.util.inventory.SlotAwareItemHandler;
import dev.su5ed.gtexperimental.util.inventory.SlotDirection;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import one.util.streamex.StreamEx;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static dev.su5ed.gtexperimental.util.GtUtil.location;

public class InventoryHandler extends GtComponentBase<BaseBlockEntity> {
    public static final ResourceLocation NAME = location("inventory_handler");

    private final SlotAwareItemHandler itemHandler;
    private final Map<Direction, LazyOptional<IItemHandler>> sidedCapability;

    public InventoryHandler(BaseBlockEntity parent, SlotAwareItemHandler itemHandler) {
        super(parent);

        this.itemHandler = itemHandler;
        this.sidedCapability = StreamEx.of(Direction.values())
            .append((Direction) null)
            .<LazyOptional<IItemHandler>>mapToEntry(side -> LazyOptional.of(() -> new SidedSlotHandlerWrapper(this.itemHandler, side)))
            .toImmutableMap();
    }

    public InventorySlot addSlot(String name, InventorySlot.Mode mode, SlotDirection side, int size) {
        return addSlot(name, mode, side, size, stack -> true);
    }

    public InventorySlot addSlot(String name, InventorySlot.Mode mode, SlotDirection side, int size, Predicate<ItemStack> filter) {
        return addSlot(name, mode, side, size, filter, stack -> {});
    }
    
    public InventorySlot addSlot(String name, InventorySlot.Mode mode, SlotDirection side, int size, Predicate<ItemStack> filter, Consumer<ItemStack> onChanged) {
        return this.itemHandler.addSlot(name, mode, side, size, filter, onChanged);
    }
    
    public <T extends InventorySlot> T addSlot(Function<SlotAwareItemHandler, T> factory) {
        return this.itemHandler.addSlot(factory.apply(this.itemHandler));
    }

    public Collection<ItemStack> getAllItems() {
        return this.itemHandler.getAllItems();
    }

    @Override
    public ResourceLocation getName() {
        return NAME;
    }

    @Override
    public void onFieldUpdate(String name) {}

    @Override
    public <U> LazyOptional<U> getCapability(@NotNull Capability<U> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.sidedCapability.get(side).cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void save(FriendlyCompoundTag tag) {
        super.save(tag);

        tag.put("items", this.itemHandler.serializeNBT());
    }

    @Override
    public void load(FriendlyCompoundTag tag) {
        super.load(tag);

        this.itemHandler.deserializeNBT(tag.getCompound("items"));
    }
}
