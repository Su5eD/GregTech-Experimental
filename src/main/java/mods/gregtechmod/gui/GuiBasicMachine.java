package mods.gregtechmod.gui;

import ic2.core.ContainerBase;
import ic2.core.gui.Gauge;
import ic2.core.gui.LinkedGauge;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityBasicMachine;
import net.minecraft.client.resources.I18n;

public abstract class GuiBasicMachine<T extends ContainerBase<? extends TileEntityBasicMachine<?, ?, ?, ?>>> extends GuiSimple<T> {
    
    public GuiBasicMachine(T container, Gauge.IGaugeStyle gaugeStyle) {
        super(container);
        
        addCycleButton(0, 58, 0, 76, 18, 7, 62, () -> GuiBasicMachine.this.container.base.provideEnergy ? 1 : 0);
        addCycleButton(2, 76, 0, 94, 18, 25, 62, () -> GuiBasicMachine.this.container.base.autoOutput ? 1 : 0);
        addCycleButton(4, 94, 0, 112, 18, 43, 62, () -> GuiBasicMachine.this.container.base.splitInput ? 1 : 0);

        addElement(new LinkedGauge(this, 78, 24, container.base, "progress", gaugeStyle));
    }

    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);
        drawString(8, 4, I18n.format(this.container.base.getName()), GuiColors.DARK_GRAY, false);
    }
}
