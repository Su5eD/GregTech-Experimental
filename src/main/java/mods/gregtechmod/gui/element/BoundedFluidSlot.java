package mods.gregtechmod.gui.element;

import ic2.core.GuiIC2;
import mods.gregtechmod.util.JavaUtil;
import net.minecraftforge.fluids.IFluidTank;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;

public class BoundedFluidSlot extends CustomFluidSlot {
    private final int[][] bounds;

    public BoundedFluidSlot(GuiIC2<?> gui, int x, int y, IFluidTank tank, int[][] bounds, boolean verboseTooltip) {
        super(gui, x, y, tank, verboseTooltip);

        this.bounds = bounds;
    }

    @Override
    protected boolean suppressTooltip(int mouseX, int mouseY) {
        return !isInside(mouseX, mouseY);
    }

    public boolean isInside(int x, int y) {
        return IntStreamEx.range(0, bounds.length).boxed()
            .zipWith(StreamEx.iterate(this.bounds.length - 1, JavaUtil.alwaysTrue(), j -> (j + 1) % this.bounds.length))
            .anyMatch((i, j) -> {
                int xi = this.bounds[i][0], yi = this.bounds[i][1];
                int xj = this.bounds[j][0], yj = this.bounds[j][1];
                return yi > y != yj > y && x < (xj - xi) * (y - yi) / (yj - yi) + xi;
            });
    }
}
