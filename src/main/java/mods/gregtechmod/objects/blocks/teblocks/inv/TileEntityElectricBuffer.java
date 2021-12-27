package mods.gregtechmod.objects.blocks.teblocks.inv;

import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.core.IHasGui;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.state.Ic2BlockState;
import ic2.core.block.state.UnlistedBooleanProperty;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.inventory.invslot.GtSlotElectricBuffer;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityUpgradable;
import mods.gregtechmod.objects.blocks.teblocks.component.GtRedstoneEmitter;
import mods.gregtechmod.util.BooleanCountdown;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.InvUtil;
import mods.gregtechmod.util.PropertyHelper;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.*;

public abstract class TileEntityElectricBuffer extends TileEntityUpgradable implements IHasGui, INetworkClientTileEntityEventListener {
    public static final IUnlistedProperty<Boolean> REDSTONE_TEXTURE_PROPERTY = new UnlistedBooleanProperty("redstoneTextures");

    public final BooleanCountdown inventoryModified = createSingleCountDown();
    public final GtSlotElectricBuffer buffer;
    private final GtRedstoneEmitter emitter;

    @NBTPersistent
    public boolean outputEnergy = true;
    @NBTPersistent
    public boolean redstoneIfFull;
    @NBTPersistent
    public boolean invertRedstone;
    @NBTPersistent
    protected int targetStackSize;
    protected int success;

    public TileEntityElectricBuffer(int invSize) {
        this.buffer = new GtSlotElectricBuffer(this, "buffer", getBufferSlotAccess(), invSize);
        this.emitter = addComponent(new GtRedstoneEmitter(this, () -> updateClientField("emitter")));
    }
    
    protected InvSlot.Access getBufferSlotAccess() {
        return InvSlot.Access.IO;
    }
    
    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
        
        if (isAllowedToWork()) work();
    }
    
    protected void work() {
        boolean hasItem = !this.buffer.isEmpty();
        if (canUseEnergy(500) && shouldUpdate(hasItem)) {
            this.success--;
            if (hasItem) moveItem();

            if (this.redstoneIfFull) {
                this.emitter.setLevel(this.invertRedstone ? 0 : 15);
                for (InvSlot slot : InvUtil.getInvSlots(this)) {
                    if (slot.isEmpty()) {
                        this.emitter.setLevel(this.invertRedstone ? 15 : 0);
                        useEnergy(1);
                        break;
                    }
                }
            } else this.emitter.setLevel(this.invertRedstone ? 15 : 0);
        }
    }
    
    protected boolean shouldUpdate(boolean hasItem) {
        int invSize = getSizeInventory();
        return workJustHasBeenEnabled() 
                || this.tickCounter % 200 == 0
                || this.tickCounter % 5 == 0 && (this.success > 0 || hasItem && this.tickCounter % 10 == 0 && invSize <= 1)
                || this.success >= 20
                || this.inventoryModified.get();
    }
    
    protected void moveItem() {
        int cost = GtUtil.moveItemStack(this, this.world.getTileEntity(this.pos.offset(getOppositeFacing())), getOppositeFacing(), getFacing(), this.targetStackSize != 0 ? this.targetStackSize : 64, this.targetStackSize != 0 ? this.targetStackSize : 1, 64, getSizeInventory() > 10 ? 2 : 1);
        if (cost > 0) {
            this.success = 20;
            useEnergy(cost);
        }
    }
    
    protected int getOverclockerMultiplier() {
        return (int) Math.pow(4, getUpgradeCount(IC2UpgradeType.OVERCLOCKER));
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("emitter");
        list.add("outputEnergy");
        list.add("redstoneIfFull");
        list.add("invertRedstone");
    }

    @Override
    protected boolean onScrewdriverActivated(ItemStack stack, EnumFacing side, EntityPlayer player, float hitX, float hitY, float hitZ) {
        if (!this.world.isRemote && side == getOppositeFacing()) {
            this.targetStackSize = (this.targetStackSize + 1) % 64;
            if (this.targetStackSize == 0)
                GtUtil.sendMessage(player, Reference.MODID + ".teblock.electric_buffer_small.no_regulate");
            else
                GtUtil.sendMessage(player, Reference.MODID + ".teblock.electric_buffer_small.regulate", this.targetStackSize);
            return true;
        }
        return super.onScrewdriverActivated(stack, side, player, hitX, hitY, hitZ);
    }

    @Override
    protected Ic2BlockState.Ic2BlockStateInstance getExtendedState(Ic2BlockState.Ic2BlockStateInstance state) {
        return super.getExtendedState(state)
                .withProperty(REDSTONE_TEXTURE_PROPERTY, this.emitter.getLevel() > 0);
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, EnumFacing side) {
        return side != getOppositeFacing() && super.canInsertItem(index, stack, side);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing side) {
        return side == getOppositeFacing() && super.canExtractItem(index, stack, side);
    }

    @Override
    protected Collection<EnumFacing> getSinkSides() {
        return GtUtil.allSidesExcept(getOppositeFacing());
    }

    @Override
    protected Collection<EnumFacing> getSourceSides() {
        return this.outputEnergy ? Collections.singleton(getOppositeFacing()) : Collections.emptySet();
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
    protected int getMinimumStoredEU() {
        return 1000;
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
    protected PropertyHelper.VerticalRotation getVerticalRotation() {
        return PropertyHelper.VerticalRotation.ROTATE_X;
    }

    @Override
    public Set<IC2UpgradeType> getCompatibleIC2Upgrades() {
        return EnumSet.of(IC2UpgradeType.TRANSFORMER, IC2UpgradeType.BATTERY);
    }

    @Override
    public boolean placeCoverAtSide(ICover cover, EntityPlayer player, EnumFacing side, boolean simulate) {
        return side != getOppositeFacing() && super.placeCoverAtSide(cover, player, side, simulate);
    }

    @Override
    protected int getWeakPower(EnumFacing side) {
        return this.emitter.getLevel();
    }

    @Override
    public void onNetworkEvent(EntityPlayer player, int event) {
        boolean value = event % 2 != 0;
        switch (event) {
            case 0:
            case 1:
                this.outputEnergy = value;
                this.energy.refreshSides();
                break;
            case 2:
            case 3:
                this.redstoneIfFull = value;
                break;
            case 4:
            case 5:
                this.invertRedstone = value;
                break;
        }
    }

    @Override
    public void onGuiClosed(EntityPlayer player) {}
}
