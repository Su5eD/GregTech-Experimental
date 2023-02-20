package dev.su5ed.gtexperimental.blockentity.component;

import dev.su5ed.gtexperimental.api.util.FriendlyCompoundTag;
import dev.su5ed.gtexperimental.blockentity.base.BaseBlockEntity;
import dev.su5ed.gtexperimental.util.inventory.InventorySlot;
import dev.su5ed.gtexperimental.util.inventory.SlotAwareItemHandler;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;

import static dev.su5ed.gtexperimental.util.GtUtil.location;

public class InventoryHandler extends GtComponentBase<BaseBlockEntity> {
    public static final ResourceLocation NAME = location("inventory_handler");
    
    private final SlotAwareItemHandler itemHandler;
    private final LazyOptional<IItemHandler> optional;
    
    public InventoryHandler(BaseBlockEntity parent, Runnable onChanged) {
        super(parent);
        
        this.itemHandler = new SlotAwareItemHandler(onChanged);
        this.optional = LazyOptional.of(() -> this.itemHandler);
    }
        
    public InventorySlot addSlot(String name, InventorySlot.Mode mode, int size, Predicate<ItemStack> filter, Consumer<ItemStack> onChanged) {
        return this.itemHandler.addSlot(name, mode, size, filter, onChanged);
    }

    @Override
    public ResourceLocation getName() {
        return NAME;
    }

    @Override
    public void onFieldUpdate(String name) {

    }

    @Override
    public <U> LazyOptional<U> getCapability(@NotNull Capability<U> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.optional.cast();
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
