package mods.gregtechmod.gui;

import ic2.core.gui.Gauge;
import mods.gregtechmod.core.GregTechMod;

import java.util.HashMap;
import java.util.Map;

public enum GregtechGauge implements Gauge.IGaugeStyle {
    ARROW_RIGHT((new Gauge.GaugePropertyBuilder(10, 0, 10, 10, Gauge.GaugePropertyBuilder.GaugeOrientation.Right)).withTexture(GregTechMod.COMMON_TEXTURE).withSmooth(false).build()),
    ARROW_UP((new Gauge.GaugePropertyBuilder(20, 0, 10, 10, Gauge.GaugePropertyBuilder.GaugeOrientation.Up)).withTexture(GregTechMod.COMMON_TEXTURE).withSmooth(false).build()),
    ARROW_LEFT((new Gauge.GaugePropertyBuilder(30, 0, 10, 10, Gauge.GaugePropertyBuilder.GaugeOrientation.Left)).withTexture(GregTechMod.COMMON_TEXTURE).withSmooth(false).build()),
    ARROW_DOWN((new Gauge.GaugePropertyBuilder(0, 0, 10, 10, Gauge.GaugePropertyBuilder.GaugeOrientation.Down)).withTexture(GregTechMod.COMMON_TEXTURE).withSmooth(false).build());

    private static final Map<String, Gauge.IGaugeStyle> VALUES = getValues();

    public final Gauge.GaugeProperties properties;

    GregtechGauge(Gauge.GaugeProperties properties) {
        this.properties = properties;
    }

    public Gauge.GaugeProperties getProperties() {
        return this.properties;
    }

    public static Gauge.IGaugeStyle get(String name) {
        return VALUES.get(name);
    }

    private static Map<String, Gauge.IGaugeStyle> getValues() {
        Gauge.IGaugeStyle[] values = values();
        Map<String, Gauge.IGaugeStyle> ret = new HashMap<>(values.length);
        for (Gauge.IGaugeStyle style : values) ret.put(style.getClass().getName(), style);
        return ret;
    }
}
