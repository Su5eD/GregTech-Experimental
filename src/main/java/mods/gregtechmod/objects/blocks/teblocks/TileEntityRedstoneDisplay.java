package mods.gregtechmod.objects.blocks.teblocks;

import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityMultiMode;
import mods.gregtechmod.util.ITextureMode;
import mods.gregtechmod.util.nbt.NBTPersistent;

import java.util.List;
import java.util.Locale;

public class TileEntityRedstoneDisplay extends TileEntityMultiMode {
    @NBTPersistent
    private DisplayMode mode = DisplayMode.BAR;
    private int lastRedstone;

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
        return this.world.getRedstonePowerFromNeighbors(this.pos); // Get the strength client-side so that it can be rendered immediately as the block is placed
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();

        if (isAllowedToWork()) {
            int strength = this.world.getRedstonePowerFromNeighbors(this.pos);
            if (this.lastRedstone != strength) {
                this.lastRedstone = strength;
                rerender();
            }
        }
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("mode");
    }

    public enum DisplayMode implements ITextureMode {
        BAR,
        HEX,
        DEC,
        LARGE;

        private static final DisplayMode[] VALUES = values();

        public DisplayMode next() {
            return VALUES[(ordinal() + 1) % VALUES.length];
        }

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ROOT);
        }

        @Override
        public int getCount() {
            return 16;
        }
    }
}
