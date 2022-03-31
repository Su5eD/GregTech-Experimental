package mods.gregtechmod.objects.blocks.teblocks;

import mods.gregtechmod.api.util.QuadFunction;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityMultiMode;
import mods.gregtechmod.util.BooleanCountdown;
import mods.gregtechmod.util.ITextureMode;
import mods.gregtechmod.util.JavaUtil;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import one.util.streamex.StreamEx;

import java.util.List;
import java.util.Locale;
import java.util.function.IntUnaryOperator;

public class TileEntityButtonPanel extends TileEntityMultiMode {
    @NBTPersistent
    private PanelMode mode = PanelMode.SMALL;
    private int emitted;
    private final BooleanCountdown update = createCountDown(3);

    @Override
    protected ITextureMode getMode() {
        return this.mode;
    }

    @Override
    protected void updateMode() {
        this.mode = this.mode.next();
        updateClientField("mode");
    }

    @Override
    protected int getTextureIndex() {
        return this.emitted;
    }

    @Override
    protected boolean onActivatedChecked(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (side == getFacing()) {
            if (!this.world.isRemote) {
                int strength = this.mode.getRedstoneStrength(side, hitX, hitY, hitZ);
                int emitted = this.mode.getEmittedIndex(strength);
                setActive(true);
                this.update.reset();
                updateRedstoneOutput(strength, emitted);
            }
            return true;
        }
        return super.onActivatedChecked(player, hand, side, hitX, hitY, hitZ);
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
        if (!this.update.get() && isAllowedToWork()) {
            updateRedstoneOutput(0, 0);
            setActive(false);
        }
    }

    private void updateRedstoneOutput(int strength, int emitted) {
        StreamEx.of(EnumFacing.VALUES)
            .without(getFacing())
            .forEach(side -> this.rsEmitter.setLevel(side, strength));
        this.emitted = emitted;
        updateClientField("emitted");
        rerender();
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("mode");
    }

    public enum PanelMode implements ITextureMode {
        SMALL((side, x, y, z) -> {
            switch (side) {
                case DOWN:
                case UP:
                    return (int) (x * 4) + 4 * (int) (z * 4);
                case NORTH:
                    return (int) (4 - x * 4) + 4 * (int) (4 - y * 4);
                case SOUTH:
                    return (int) (x * 4) + 4 * (int) (4 - y * 4);
                case WEST:
                    return (int) (z * 4) + 4 * (int) (4 - y * 4);
                case EAST:
                    return (int) (4 - z * 4) + 4 * (int) (4 - y * 4);
            }
            return 0;
        }, IntUnaryOperator.identity(), 16),
        LARGE((side, x, y, z) -> {
            switch (side) {
                case DOWN:
                case UP:
                    return 1 << (int) (x * 2) + 2 * (int) (z * 2);
                case NORTH:
                    return 1 << (int) (2 - x * 2) + 2 * (int) (2 - y * 2);
                case SOUTH:
                    return 1 << (int) (x * 2) + 2 * (int) (2 - y * 2);
                case WEST:
                    return 1 << (int) (z * 2) + 2 * (int) (2 - y * 2);
                case EAST:
                    return 1 << (int) (2 - z * 2) + 2 * (int) (2 - y * 2);
            }
            return 0;
        }, i -> 1 + JavaUtil.log2(i), 5),
        ROW((side, x, y, z) -> {
            switch (side) {
                case DOWN:
                case UP:
                    return 1 << (int) (z * 4);
                default:
                    return 1 << (int) (4 - y * 4);
            }
        }, LARGE.indexGetter, 5);

        private static final PanelMode[] VALUES = values();

        private final QuadFunction<EnumFacing, Float, Float, Float, Integer> strengthGetter;
        private final IntUnaryOperator indexGetter;
        private final int count;

        PanelMode(QuadFunction<EnumFacing, Float, Float, Float, Integer> strengthGetter, IntUnaryOperator indexGetter, int count) {
            this.strengthGetter = strengthGetter;
            this.indexGetter = indexGetter;
            this.count = count;
        }

        private int getRedstoneStrength(EnumFacing side, float x, float y, float z) {
            return this.strengthGetter.apply(side, x, y, z);
        }

        private int getEmittedIndex(int strength) {
            return this.indexGetter.applyAsInt(strength);
        }

        public PanelMode next() {
            return VALUES[(ordinal() + 1) % VALUES.length];
        }

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ROOT);
        }

        @Override
        public int getCount() {
            return this.count;
        }
    }
}
