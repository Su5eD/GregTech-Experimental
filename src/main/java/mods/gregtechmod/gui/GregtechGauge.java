package mods.gregtechmod.gui;

import ic2.core.gui.Gauge;
import mods.gregtechmod.core.GregTechMod;
import net.minecraft.util.ResourceLocation;

public enum GregtechGauge implements Gauge.IGaugeStyle {
    SMALL_ARROW_RIGHT(new Gauge.GaugePropertyBuilder(10, 0, 10, 10, Gauge.GaugePropertyBuilder.GaugeOrientation.Right).withTexture(GregTechMod.COMMON_TEXTURE).withSmooth(false).build()),
    SMALL_ARROW_UP(new Gauge.GaugePropertyBuilder(20, 0, 10, 10, Gauge.GaugePropertyBuilder.GaugeOrientation.Up).withTexture(GregTechMod.COMMON_TEXTURE).withSmooth(false).build()),
    SMALL_ARROW_LEFT(new Gauge.GaugePropertyBuilder(30, 0, 10, 10, Gauge.GaugePropertyBuilder.GaugeOrientation.Left).withTexture(GregTechMod.COMMON_TEXTURE).withSmooth(false).build()),
    SMALL_ARROW_DOWN(new Gauge.GaugePropertyBuilder(0, 0, 10, 10, Gauge.GaugePropertyBuilder.GaugeOrientation.Down).withTexture(GregTechMod.COMMON_TEXTURE).withSmooth(false).build()),
    MACERATING(buildGaugeFromGui(GuiAutoMacerator.TEXTURE)),
    EXTRACTING(buildGaugeFromGui(GuiAutoExtractor.TEXTURE)),
    COMPRESSING(buildGaugeFromGui(GuiAutoCompressor.TEXTURE)),
    RECYCLING(buildGaugeFromGui(GuiAutoRecycler.TEXTURE)),
    SMELTING(buildGaugeFromGui(GuiAutoElectricFurnace.TEXTURE)),
    EXTRUDING(buildGaugeFromGui(GuiWiremill.TEXTURE)),
    CANNING(buildGaugeFromGui(GuiAutoCanner.TEXTURE)),
    BENDING(buildGaugeFromGui(GuiBender.TEXTURE)),
    ASSEMBLING(buildGaugeFromGui(GuiAssembler.TEXTURE)),
    TURNING(buildGaugeFromGui(GuiLathe.TEXTURE)),
    BLASTING(buildGaugeFromGui(GuiIndustrialBlastFurnace.TEXTURE, 11)),
    GRINDER_WATER_LEVEL(buildGaugeFromGui(GuiIndustrialGrinder.TEXTURE)),
    IMPLODING(buildGaugeFromGui(GuiImplosionCompressor.TEXTURE, 11)),
    FREEZING(buildGaugeFromGui(GuiVacuumFreezer.TEXTURE, 11)),
    DISTILLING(new Gauge.GaugePropertyBuilder(176, 0, 16, 72, Gauge.GaugePropertyBuilder.GaugeOrientation.Up).withTexture(GuiDistillationTower.TEXTURE).withSmooth(false).build());

    public final Gauge.GaugeProperties properties;

    GregtechGauge(Gauge.GaugeProperties properties) {
        this.properties = properties;
    }

    @Override
    public Gauge.GaugeProperties getProperties() {
        return this.properties;
    }
    
    private static Gauge.GaugeProperties buildGaugeFromGui(ResourceLocation texture) {
        return buildGaugeFromGui(texture, 18);
    }
    
    private static Gauge.GaugeProperties buildGaugeFromGui(ResourceLocation texture, int height) {
        return new Gauge.GaugePropertyBuilder(176, 0, 20, height, Gauge.GaugePropertyBuilder.GaugeOrientation.Right)
                .withTexture(texture)
                .withSmooth(false)
                .build();
    }
}
