package mods.gregtechmod.objects.blocks.teblocks.inv;

import ic2.core.IHasGui;
import ic2.core.block.invslot.InvSlot;
import ic2.core.util.Util;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.gui.GuiElectricTranslocator;
import mods.gregtechmod.inventory.invslot.GtSlot;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityUpgradable;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerElectricTranslocator;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.JavaUtil;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class TileEntityElectricTranslocator extends TileEntityUpgradable implements IHasGui {
    public final InvSlot filter;

    @NBTPersistent
    public boolean outputEnergy = true;
    @NBTPersistent
    public boolean invertFilter;
    private final int minimumWorkEnergy;
    private int success;

    public TileEntityElectricTranslocator() {
        this(1000);
    }

    public TileEntityElectricTranslocator(int minimumWorkEnergy) {
        this.minimumWorkEnergy = minimumWorkEnergy;

        this.filter = new GtSlot(this, "filter", InvSlot.Access.NONE, 9);
    }

    public void switchOutputEnergy() {
        this.outputEnergy = !this.outputEnergy;
        this.energy.refreshSides();
    }

    public void switchInvertFilter() {
        this.invertFilter = !this.invertFilter;
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
        return 2000;
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();

        if (isAllowedToWork() && canUseEnergy(this.minimumWorkEnergy) && (
            workJustHasBeenEnabled()
                || this.tickCounter % 200 == 0
                || this.tickCounter % 5 == 0 && this.success > 0
                || this.success >= 20
        )) {
            this.success--;

            List<ItemStack> stacks = StreamSupport.stream(this.filter.spliterator(), false).collect(Collectors.toList());
            boolean empty = stacks.stream().allMatch(ItemStack::isEmpty);
            Predicate<ItemStack> filter = stack -> stacks.stream().anyMatch(s -> GtUtil.stackItemEquals(s, stack));

            int multiplier = empty ? 1 : 2;
            int cost = GtUtil.moveItemStack(
                getNeighborTE(getFacing()),
                getNeighborTE(getOppositeFacing()),
                transferFromSide(), transferToSide(),
                64, 1,
                empty ? JavaUtil.alwaysTrue() : this.invertFilter ? filter.negate() : filter
            ) * multiplier;

            if (cost > 0) {
                useEnergy(cost);
                this.success = 30;
            }
        }
    }

    protected EnumFacing transferFromSide() {
        return getOppositeFacing();
    }

    protected EnumFacing transferToSide() {
        return getFacing();
    }

    @Override
    public int getSteamCapacity() {
        return getBaseEUCapacity();
    }

    @Override
    public long getMjCapacity() {
        return getBaseEUCapacity();
    }

    @Override
    protected Collection<EnumFacing> getSinkSides() {
        return GtUtil.allSidesWithout(getOppositeFacing());
    }

    @Override
    protected Collection<EnumFacing> getSourceSides() {
        return this.outputEnergy ? Collections.singleton(getOppositeFacing()) : Util.noFacings;
    }

    @Override
    public boolean isInputSide(EnumFacing side) {
        return !isOutputSide(side);
    }

    @Override
    public boolean isOutputSide(EnumFacing side) {
        return side == getFacing() || side == getOppositeFacing();
    }

    @Override
    public Set<IC2UpgradeType> getCompatibleIC2Upgrades() {
        return EnumSet.of(IC2UpgradeType.TRANSFORMER, IC2UpgradeType.BATTERY);
    }

    @Override
    public boolean placeCoverAtSide(ICover cover, EntityPlayer player, EnumFacing side, boolean simulate) {
        return side != getFacing() && side != getOppositeFacing() && super.placeCoverAtSide(cover, player, side, simulate);
    }

    @Override
    public ContainerElectricTranslocator<?> getGuiContainer(EntityPlayer player) {
        return new ContainerElectricTranslocator<>(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiElectricTranslocator<>(getGuiContainer(player));
    }

    @Override
    public void onGuiClosed(EntityPlayer player) {}
}
