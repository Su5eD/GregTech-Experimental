package mods.gregtechmod.common.util;

import net.minecraft.util.ResourceLocation;

public class ModelInformation {
    public final ResourceLocation path;
    public final int metadata;

    public ModelInformation(ResourceLocation path) {
        this(path, 0);
    }

    public ModelInformation(ResourceLocation path, int metadata) {
        this.path = path;
        this.metadata = metadata;
    }
}