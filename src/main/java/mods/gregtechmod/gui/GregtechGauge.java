package mods.gregtechmod.gui;

import ic2.core.gui.Gauge;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public enum GregtechGauge implements Gauge.IGaugeStyle {
    SMALL_ARROW_RIGHT(buildGauge(GtUtil.COMMON_TEXTURE, 10, 10, 10, Gauge.GaugePropertyBuilder.GaugeOrientation.Right)),
    SMALL_ARROW_UP(buildGauge(GtUtil.COMMON_TEXTURE, 20, 10, 10, Gauge.GaugePropertyBuilder.GaugeOrientation.Up)),
    SMALL_ARROW_LEFT(buildGauge(GtUtil.COMMON_TEXTURE, 30, 10, 10, Gauge.GaugePropertyBuilder.GaugeOrientation.Left)),
    SMALL_ARROW_DOWN(buildGauge(GtUtil.COMMON_TEXTURE, 0, 10, 10, Gauge.GaugePropertyBuilder.GaugeOrientation.Down)),
    MACERATING(buildGauge(GuiAutoMacerator.TEXTURE)),
    EXTRACTING(buildGauge(GuiAutoExtractor.TEXTURE)),
    COMPRESSING(buildGauge(GuiAutoCompressor.TEXTURE)),
    RECYCLING(buildGauge(GuiAutoRecycler.TEXTURE)),
    SMELTING(buildGauge(GuiAutoElectricFurnace.TEXTURE)),
    EXTRUDING(buildGauge(GuiWiremill.TEXTURE)),
    CANNING(buildGauge(GuiAutoCanner.TEXTURE)),
    BENDING(buildGauge(GuiBender.TEXTURE)),
    ASSEMBLING(buildGauge(GuiAssembler.TEXTURE)),
    TURNING(buildGauge(GuiLathe.TEXTURE)),
    BLASTING(buildGauge(GuiIndustrialBlastFurnace.TEXTURE, 11)),
    IMPLODING(buildGauge(GuiImplosionCompressor.TEXTURE, 11)),
    FREEZING(buildGauge(GuiVacuumFreezer.TEXTURE, 11)),
    DISTILLING(buildGauge(GuiDistillationTower.TEXTURE, 176, 16, 72, Gauge.GaugePropertyBuilder.GaugeOrientation.Up)),
    SAWING(buildGauge(GuiIndustrialSawmill.TEXTURE, 176, 20, 11, Gauge.GaugePropertyBuilder.GaugeOrientation.Right)),
    FUSION(buildGauge(GtUtil.getGuiTexture("jei/fusion_reactor"), 176, 25, 17, Gauge.GaugePropertyBuilder.GaugeOrientation.Right));

    public final Gauge.GaugeProperties properties;

    GregtechGauge(Gauge.GaugeProperties properties) {
        this.properties = properties;
    }

    @Override
    public Gauge.GaugeProperties getProperties() {
        return this.properties;
    }

    private static Gauge.GaugeProperties buildGauge(ResourceLocation texture) {
        return buildGauge(texture, 18);
    }

    private static Gauge.GaugeProperties buildGauge(ResourceLocation texture, int height) {
        return buildGauge(texture, 176, 20, height, Gauge.GaugePropertyBuilder.GaugeOrientation.Right);
    }

    private static Gauge.GaugeProperties buildGauge(ResourceLocation texture, int x, int width, int height, Gauge.GaugePropertyBuilder.GaugeOrientation orientation) {
        return buildGauge(texture, x, 0, width, height, orientation);
    }

    private static Gauge.GaugeProperties buildGauge(ResourceLocation texture, int x, int y, int width, int height, Gauge.GaugePropertyBuilder.GaugeOrientation orientation) {
        return new Gauge.GaugePropertyBuilder(x, y, width, height, orientation)
            .withTexture(texture)
            .withSmooth(false)
            .build();
    }
}
