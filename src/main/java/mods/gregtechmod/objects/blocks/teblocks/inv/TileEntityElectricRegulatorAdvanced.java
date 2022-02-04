package mods.gregtechmod.objects.blocks.teblocks.inv;

import ic2.core.block.invslot.InvSlot;
import mods.gregtechmod.gui.GuiElectricRegulatorAdvanced;
import mods.gregtechmod.inventory.invslot.GtSlot;
import mods.gregtechmod.inventory.invslot.GtSlotFiltered;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerElectricRegulatorAdvanced;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class TileEntityElectricRegulatorAdvanced extends TileEntityElectricBuffer {
    @NBTPersistent
    public final int[] slotIndices = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    public final List<GtSlotFiltered> bufferSlots = new ArrayList<>();
    public final InvSlot filter;

    public TileEntityElectricRegulatorAdvanced() {
        this.filter = new GtSlot(this, "filter", InvSlot.Access.NONE, 9);

        IntStream.range(0, 9).forEach(i -> this.bufferSlots.add(new GtSlotFiltered(this, "buffer" + i, InvSlot.Access.IO, 1, stack -> !this.filter.isEmpty(i) && GtUtil.stackItemEquals(this.filter.get(i), stack))));
    }

    @Override
    protected void work() {
        if (hasItem() && canWork()) {
            this.success--;

            for (int i = 0; i < this.bufferSlots.size(); i++) {
                GtSlotFiltered slot = this.bufferSlots.get(i);
                if (!slot.isEmpty()) {
                    int targetSlot = this.slotIndices[i];

                    int cost = moveItem(targetSlot);
                    if (cost > 0) {
                        useEnergy(cost);
                        break;
                    }
                }
            }
        }
    }

    protected int moveItem(int targetSlot) {
        return GtUtil.moveItemStackIntoSlot(
            this, getNeighborTE(getOppositeFacing()),
            getOppositeFacing(), getFacing(),
            targetSlot,
            this.targetStackSize != 0 ? this.targetStackSize : 64, this.targetStackSize != 0 ? this.targetStackSize : 1
        ) * getMoveCostMultiplier();
    }

    @Override
    protected boolean hasItem() {
        return this.bufferSlots.stream()
            .anyMatch(slot -> !slot.isEmpty());
    }

    @Override
    protected boolean canWork() {
        return canUseEnergy(500) && (workJustHasBeenEnabled() || this.tickCounter % 10 == 0);
    }

    @Override
    protected int getMoveCostMultiplier() {
        return 3;
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("slotIndices");
    }

    @Override
    public ContainerElectricRegulatorAdvanced getGuiContainer(EntityPlayer player) {
        return new ContainerElectricRegulatorAdvanced(player, this);
    }

    @Override
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiElectricRegulatorAdvanced(getGuiContainer(player));
    }

    @Override
    protected int getBaseEUCapacity() {
        return 10000;
    }

    @Override
    public int getBaseSinkTier() {
        return 2;
    }

    @Override
    public int getBaseSourcePackets() {
        return 4;
    }
}
