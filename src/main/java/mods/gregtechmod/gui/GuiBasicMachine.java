package mods.gregtechmod.gui;

import ic2.core.ContainerBase;
import ic2.core.gui.Gauge;
import ic2.core.gui.LinkedGauge;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityBasicMachine;
import net.minecraft.client.resources.I18n;

public abstract class GuiBasicMachine<T extends ContainerBase<? extends TileEntityBasicMachine<?, ?, ?, ?>>> extends GuiSimple<T> {
    
    public GuiBasicMachine(T container, Gauge.IGaugeStyle gaugeStyle) {
        super(container);
        
        addVerticalIconCycle(7, 62, 58, () -> container.base.provideEnergy);
        addVerticalIconCycle(25, 62, 76, () -> container.base.autoOutput);
        addVerticalIconCycle(43, 62, 94, () -> container.base.splitInput);

        addElement(new LinkedGauge(this, 78, 24, container.base, "progress", gaugeStyle));
    }

    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);
        drawString(8, 4, I18n.format(this.container.base.getName()), GuiColors.DARK_GRAY, false);
    }
}
