package mods.gregtechmod.gui;

import ic2.core.gui.Gauge;
import mods.gregtechmod.core.GregTechMod;

public enum GregtechGauge implements Gauge.IGaugeStyle {
    SMALL_ARROW_RIGHT(new Gauge.GaugePropertyBuilder(10, 0, 10, 10, Gauge.GaugePropertyBuilder.GaugeOrientation.Right).withTexture(GregTechMod.COMMON_TEXTURE).withSmooth(false).build()),
    SMALL_ARROW_UP(new Gauge.GaugePropertyBuilder(20, 0, 10, 10, Gauge.GaugePropertyBuilder.GaugeOrientation.Up).withTexture(GregTechMod.COMMON_TEXTURE).withSmooth(false).build()),
    SMALL_ARROW_LEFT(new Gauge.GaugePropertyBuilder(30, 0, 10, 10, Gauge.GaugePropertyBuilder.GaugeOrientation.Left).withTexture(GregTechMod.COMMON_TEXTURE).withSmooth(false).build()),
    SMALL_ARROW_DOWN(new Gauge.GaugePropertyBuilder(0, 0, 10, 10, Gauge.GaugePropertyBuilder.GaugeOrientation.Down).withTexture(GregTechMod.COMMON_TEXTURE).withSmooth(false).build()),
    MACERATING(new Gauge.GaugePropertyBuilder(176, 0, 20, 18, Gauge.GaugePropertyBuilder.GaugeOrientation.Right).withTexture(GuiAutoMacerator.TEXTURE).withSmooth(false).build()),
    EXTRACTING(new Gauge.GaugePropertyBuilder(176, 0, 20, 18, Gauge.GaugePropertyBuilder.GaugeOrientation.Right).withTexture(GuiAutoExtractor.TEXTURE).withSmooth(false).build()),
    COMPRESSING(new Gauge.GaugePropertyBuilder(176, 0, 20, 18, Gauge.GaugePropertyBuilder.GaugeOrientation.Right).withTexture(GuiAutoCompressor.TEXTURE).withSmooth(false).build()),
    RECYCLING(new Gauge.GaugePropertyBuilder(176, 0, 20, 18, Gauge.GaugePropertyBuilder.GaugeOrientation.Right).withTexture(GuiAutoRecycler.TEXTURE).withSmooth(false).build()),
    SMELTING(new Gauge.GaugePropertyBuilder(176, 0, 20, 18, Gauge.GaugePropertyBuilder.GaugeOrientation.Right).withTexture(GuiAutoElectricFurnace.TEXTURE).withSmooth(false).build()),
    EXTRUDING(new Gauge.GaugePropertyBuilder(176, 0, 20, 18, Gauge.GaugePropertyBuilder.GaugeOrientation.Right).withTexture(GuiWiremill.TEXTURE).withSmooth(false).build()),
    CANNING(new Gauge.GaugePropertyBuilder(176, 0, 20, 18, Gauge.GaugePropertyBuilder.GaugeOrientation.Right).withTexture(GuiAutoCanner.TEXTURE).withSmooth(false).build()),
    BENDING(new Gauge.GaugePropertyBuilder(176, 0, 20, 18, Gauge.GaugePropertyBuilder.GaugeOrientation.Right).withTexture(GuiBender.TEXTURE).withSmooth(false).build()),
    ASSEMBLING(new Gauge.GaugePropertyBuilder(176, 0, 20, 18, Gauge.GaugePropertyBuilder.GaugeOrientation.Right).withTexture(GuiAssembler.TEXTURE).withSmooth(false).build()),
    TURNING(new Gauge.GaugePropertyBuilder(176, 0, 20, 18, Gauge.GaugePropertyBuilder.GaugeOrientation.Right).withTexture(GuiLathe.TEXTURE).withSmooth(false).build()),
    BLASTING(new Gauge.GaugePropertyBuilder(176, 0, 20, 11, Gauge.GaugePropertyBuilder.GaugeOrientation.Right).withTexture(GuiIndustrialBlastFurnace.TEXTURE).withSmooth(false).build()),
    GRINDER_WATER_LEVEL(new Gauge.GaugePropertyBuilder(176, 0, 20, 18, Gauge.GaugePropertyBuilder.GaugeOrientation.Up).withTexture(GuiIndustrialGrinder.TEXTURE).withSmooth(false).build());

    public final Gauge.GaugeProperties properties;

    GregtechGauge(Gauge.GaugeProperties properties) {
        this.properties = properties;
    }

    @Override
    public Gauge.GaugeProperties getProperties() {
        return this.properties;
    }
}
