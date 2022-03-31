package mods.gregtechmod.objects.blocks.teblocks;

import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityRedstoneDisplayBase;
import mods.gregtechmod.util.ITextureMode;
import mods.gregtechmod.util.nbt.NBTPersistent;

import java.util.List;
import java.util.Locale;

public class TileEntityRedstoneDisplay extends TileEntityRedstoneDisplayBase {
    @NBTPersistent
    private DisplayMode mode = DisplayMode.BAR;

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
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("mode");
    }

    public enum DisplayMode implements ITextureMode {
        BAR,
        HEX,
        DEC,
        LARGE,
        COL,
        ENERGY;

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
