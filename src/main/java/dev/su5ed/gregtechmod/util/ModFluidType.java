package dev.su5ed.gregtechmod.util;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class ModFluidType extends FluidType {
    private final ResourceLocation stillTexture;
    private final ResourceLocation flowingTexture;
    private final ResourceLocation overlayTexture;
    private final ResourceLocation renderOverlayTexture;
    private final int tintColor;

    public ModFluidType(FluidProperties properties) {
        super(properties.properties);

        this.stillTexture = properties.stillTexture;
        this.flowingTexture = properties.flowingTexture;
        this.overlayTexture = properties.overlayTexture;
        this.renderOverlayTexture = properties.renderOverlayTexture;
        this.tintColor = properties.tintColor;
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return stillTexture;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return flowingTexture;
            }

            @Nullable
            @Override
            public ResourceLocation getOverlayTexture() {
                return overlayTexture;
            }

            @Nullable
            @Override
            public ResourceLocation getRenderOverlayTexture(Minecraft mc) {
                return renderOverlayTexture;
            }

            @Override
            public int getTintColor() {
                return tintColor;
            }
        });
    }

    public static class FluidProperties {
        private final Properties properties;

        private ResourceLocation stillTexture;
        private ResourceLocation flowingTexture;
        private ResourceLocation overlayTexture;
        private ResourceLocation renderOverlayTexture;
        private int tintColor = 0xFFFFFFFF;

        public FluidProperties() {
            this(Properties.create()
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY));
        }

        public FluidProperties(Properties properties) {
            this.properties = properties;
        }

        public FluidProperties density(int density) {
            this.properties.density(density);
            return this;
        }

        public FluidProperties stillTexture(ResourceLocation stillTexture) {
            this.stillTexture = stillTexture;
            return this;
        }

        public FluidProperties flowingTexture(ResourceLocation flowingTexture) {
            this.flowingTexture = flowingTexture;
            return this;
        }

        public FluidProperties overlayTexture(ResourceLocation overlayTexture) {
            this.overlayTexture = overlayTexture;
            return this;
        }

        public FluidProperties renderOverlayTexture(ResourceLocation renderOverlayTexture) {
            this.renderOverlayTexture = renderOverlayTexture;
            return this;
        }

        public FluidProperties tintColor(int tintColor) {
            this.tintColor = tintColor;
            return this;
        }
    }
}
