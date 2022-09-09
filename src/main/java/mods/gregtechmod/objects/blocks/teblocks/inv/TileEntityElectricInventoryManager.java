package mods.gregtechmod.objects.blocks.teblocks.inv;

import ic2.core.IHasGui;
import ic2.core.block.IInventorySlotHolder;
import ic2.core.block.invslot.InvSlot;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.gui.GuiElectricInventoryManager;
import mods.gregtechmod.inventory.invslot.GtSlot;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityUpgradable;
import mods.gregtechmod.objects.blocks.teblocks.component.GtComponentBase;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerElectricInventoryManager;
import mods.gregtechmod.util.BooleanCountdown;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.nbt.*;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TileEntityElectricInventoryManager extends TileEntityUpgradable implements IHasGui {
    public final InvSlot buffer;
    public final SlotRangeManager manager;

    private final BooleanCountdown workedLastTick = createSingleCountDown();

    static {
        NBTHandlerRegistry.addSpecialDeserializer(SlotRangeListDeserializer::new);

        NBTSaveHandler.initClass(SlotRangeSetting.class);
        NBTSaveHandler.initClass(SlotRange.class);
    }

    public TileEntityElectricInventoryManager() {
        this.buffer = new InvSlot(this, "buffer", InvSlot.Access.IO, 3);
        this.manager = addComponent(new SlotRangeManager(this));
    }

    @Override
    public Set<IC2UpgradeType> getCompatibleIC2Upgrades() {
        return EnumSet.of(IC2UpgradeType.TRANSFORMER, IC2UpgradeType.BATTERY);
    }

    @Override
    protected Collection<EnumFacing> getSinkSides() {
        return GtUtil.allSidesWithout(getSourceSides());
    }

    @Override
    protected Collection<EnumFacing> getSourceSides() {
        return StreamEx.of(this.manager.ranges)
            .filter(slotRange -> slotRange.outputEnergy)
            .map(slotRange -> slotRange.facing)
            .toImmutableSet();
    }

    @Override
    public int getBaseSinkTier() {
        return 2;
    }

    @Override
    public int getBaseSourceTier() {
        return 1;
    }

    @Override
    public int getBaseSourcePackets() {
        return 4;
    }

    @Override
    protected int getBaseEUCapacity() {
        return 100000;
    }

    @Override
    protected int getMinimumStoredEU() {
        return 50000;
    }

    @Override
    public long getMjCapacity() {
        return getBaseEUCapacity();
    }

    @Override
    public int getSteamCapacity() {
        return getBaseEUCapacity();
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();

        if (isAllowedToWork() && canUseEnergy(5000) && (
            workJustHasBeenEnabled()
                || this.tickCounter % 100 == 0
                || this.workedLastTick.get()
                || hasInventoryBeenModified()
        )) {
            int cost = StreamEx.of(this.manager.ranges)
                .cross(slotRange -> StreamEx.ofNullable(getNeighborTE(slotRange.facing)))
                .flatMapKeyValue((slotRange, tileEntity) -> StreamEx.of(slotRange.rangeSettings)
                    .mapToEntry(setting -> setting.filter.get())
                    .mapKeyValue((setting, filter) -> 5 * GtUtil.moveItemStack(
                        setting.input ? tileEntity : this,
                        setting.input ? this : tileEntity,
                        setting.input ? setting.targetSide : slotRange.facing.getOpposite(),
                        setting.input ? slotRange.facing.getOpposite() : setting.targetSide,
                        filter.isEmpty() ? 64 : filter.getCount(), 1,
                        stack -> filter.isEmpty() || GtUtil.stackItemEquals(filter, stack)
                    ))
                )
                .mapToInt(i -> i)
                .sum();

            if (cost > 0) {
                this.workedLastTick.reset();
                useEnergy(cost);
            }
        }
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("manager");
    }

    @Override
    public ContainerElectricInventoryManager getGuiContainer(EntityPlayer player) {
        return new ContainerElectricInventoryManager(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiElectricInventoryManager(getGuiContainer(player));
    }

    @Override
    public void onGuiClosed(EntityPlayer player) {}

    public static class SlotRangeManager extends GtComponentBase {
        @NBTPersistent(deserializer = SlotRangeListDeserializer.class)
        public final List<SlotRange> ranges;

        public SlotRangeManager(TileEntityElectricInventoryManager parent) {
            super(parent);

            this.ranges = IntStreamEx.range(4)
                .mapToObj(i -> new SlotRange(parent, i))
                .toImmutableList();
        }
    }

    private static class SlotRangeListDeserializer extends Serializers.ListDeserializer {
        public SlotRangeListDeserializer() {
            super((instance, i) -> {
                TileEntityElectricInventoryManager parent = (TileEntityElectricInventoryManager) ((GtComponentBase) instance).getParent();
                return new SlotRange(parent, i);
            });
        }
    }

    public static class SlotRange {
        private final TileEntityElectricInventoryManager parent;

        @NBTPersistent
        @ModifyExisting
        public final List<SlotRangeSetting> rangeSettings;
        @NBTPersistent
        public EnumFacing facing = EnumFacing.DOWN;
        @NBTPersistent
        public boolean outputEnergy;

        public SlotRange(TileEntityElectricInventoryManager parent, int index) {
            this.parent = parent;
            this.rangeSettings = IntStream.range(0, 3)
                .mapToObj(i -> new SlotRangeSetting(parent, index, i))
                .collect(Collectors.toList());
        }

        public void switchFacing() {
            this.facing = GtUtil.getNextFacing(this.facing);
        }

        public void switchOutputEnergy() {
            this.outputEnergy = !this.outputEnergy;
            this.parent.energy.refreshSides();
        }
    }

    public static class SlotRangeSetting {
        public final InvSlot filter;
        @NBTPersistent
        public EnumFacing targetSide = EnumFacing.DOWN;
        @NBTPersistent
        public boolean input;

        public SlotRangeSetting(IInventorySlotHolder<?> parent, int parentIndex, int index) {
            this.filter = new GtSlot(parent, "slot_range_" + parentIndex + "_setting_" + index + "_filter", InvSlot.Access.NONE, 1);
        }

        public void switchTargetSide() {
            this.targetSide = GtUtil.getNextFacing(this.targetSide);
        }

        public void switchInput() {
            this.input = !this.input;
        }
    }
}
