package dev.su5ed.gtexperimental.util;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

import static dev.su5ed.gtexperimental.util.GtUtil.location;

public enum FluidTextureType {
    LIQUID(GtUtil.LIQUID_STILL, GtUtil.LIQUID_FLOW),
    DENSE_LIQUID(GtUtil.LIQUID_DENSE_STILL, GtUtil.LIQUID_FLOW),
    GAS(GtUtil.GAS, GtUtil.GAS),
    CUSTOM(name -> location("fluid/" + name));

    private final Function<String, ResourceLocation> stillFactory;
    private final Function<String, ResourceLocation> flowFactory;

    FluidTextureType(ResourceLocation still, ResourceLocation flow) {
        this(name -> still, name -> flow);
    }

    FluidTextureType(Function<String, ResourceLocation> factory) {
        this(factory, factory);
    }

    FluidTextureType(Function<String, ResourceLocation> stillFactory, Function<String, ResourceLocation> flowFactory) {
        this.stillFactory = stillFactory;
        this.flowFactory = flowFactory;
    }

    public ResourceLocation getStillTexture(String name) {
        return this.stillFactory.apply(name);
    }

    public ResourceLocation getFlowTexture(String name) {
        return this.flowFactory.apply(name);
    }
}
