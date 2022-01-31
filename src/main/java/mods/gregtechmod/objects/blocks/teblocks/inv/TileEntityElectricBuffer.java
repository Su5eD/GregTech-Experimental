package mods.gregtechmod.objects.blocks.teblocks.inv;

import ic2.core.IHasGui;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.state.Ic2BlockState;
import ic2.core.block.state.UnlistedBooleanProperty;
import ic2.core.util.Util;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityUpgradable;
import mods.gregtechmod.objects.blocks.teblocks.component.GtRedstoneEmitter;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.InvUtil;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.*;

public abstract class TileEntityElectricBuffer extends TileEntityUpgradable implements IHasGui {
    public static final IUnlistedProperty<Boolean> REDSTONE_TEXTURE_PROPERTY = new UnlistedBooleanProperty("redstoneTextures");
    
    protected final GtRedstoneEmitter emitter;

    @NBTPersistent
    public boolean outputEnergy = true;
    @NBTPersistent
    public boolean redstoneIfFull;
    @NBTPersistent
    public boolean invertRedstone;
    @NBTPersistent
    protected int targetStackSize;
    protected int success;

    public TileEntityElectricBuffer() {
        this.emitter = addComponent(new GtRedstoneEmitter(this, () -> updateClientField("emitter")));
    }
    
    protected abstract boolean hasItem();
    
    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
        
        if (isAllowedToWork()) work();
    }
    
    protected void work() {
        if (hasItem() && canWork()) {
            this.success--;
            int cost = moveItem();
            if (cost > 0) {
                this.success = getMaxSuccess();
                useEnergy(cost);
            }
            
            updateRedstone();
        }
    }
    
    protected void updateRedstone() {
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
    
    protected boolean canWork() {
        int invSize = getSizeInventory();
        return canUseEnergy(500) && (
                workJustHasBeenEnabled() 
                || this.tickCounter % 200 == 0
                || this.tickCounter % 5 == 0 && (this.success > 0 || this.tickCounter % 10 == 0 && invSize <= 1)
                || this.success >= 20
                || hasInventoryBeenModified()
            );
    }
    
    protected int moveItem() {
        return moveItemStack(getNeighborTE(getOppositeFacing()), getOppositeFacing()) * getMoveCostMultiplier();
    }
    
    protected int moveItemStack(TileEntity to, EnumFacing fromSide) {
        return GtUtil.moveItemStack(
                this, to,
                fromSide, fromSide.getOpposite(),
                this.targetStackSize != 0 ? this.targetStackSize : 64, this.targetStackSize != 0 ? this.targetStackSize : 1
        );
    }
    
    protected int getMoveCostMultiplier() {
        return getSizeInventory() >= 10 ? 2 : 1;
    }
    
    protected int getMaxSuccess() {
        return 20;
    }
    
    protected int getOverclockerMultiplier() {
        return (int) Math.pow(4, getUpgradeCount(IC2UpgradeType.OVERCLOCKER));
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("emitter");
        list.add("outputEnergy");
    }

    @Override
    protected boolean onScrewdriverActivated(ItemStack stack, EnumFacing side, EntityPlayer player, float hitX, float hitY, float hitZ) {
        if (!this.world.isRemote && side == getOppositeFacing()) {
            updateTargetStackSize(player);
            return true;
        }
        return super.onScrewdriverActivated(stack, side, player, hitX, hitY, hitZ);
    }
    
    protected void updateTargetStackSize(EntityPlayer player) {
        this.targetStackSize = (this.targetStackSize + 1) % 64;
        if (this.targetStackSize == 0)
            GtUtil.sendMessage(player, GtLocale.buildKey("teblock", "electric_buffer_small", "no_regulate"));
        else
            GtUtil.sendMessage(player, GtLocale.buildKey("teblock", "electric_buffer_small", "regulate"), this.targetStackSize);
    }

    @Override
    protected Ic2BlockState.Ic2BlockStateInstance getExtendedState(Ic2BlockState.Ic2BlockStateInstance state) {
        return super.getExtendedState(state)
                .withProperty(REDSTONE_TEXTURE_PROPERTY, this.emitter.emitsRedstone());
    }

    @Override
    public boolean isInputSide(EnumFacing side) {
        return side != getOppositeFacing();
    }

    @Override
    public boolean isOutputSide(EnumFacing side) {
        return side == getOppositeFacing();
    }

    @Override
    protected Collection<EnumFacing> getSinkSides() {
        return GtUtil.allSidesExcept(getOppositeFacing());
    }

    @Override
    protected Collection<EnumFacing> getSourceSides() {
        return this.outputEnergy ? Collections.singleton(getOppositeFacing()) : Util.noFacings;
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

    public void switchOutputEnergy() {
        this.outputEnergy = !this.outputEnergy;
        this.energy.refreshSides();
    }

    public void switchRedstoneIfFull() {
        this.redstoneIfFull = !this.redstoneIfFull;
    }

    public void switchInvertRedstone() {
        this.invertRedstone = !this.invertRedstone;
    }

    @Override
    public void onGuiClosed(EntityPlayer player) {}
}
