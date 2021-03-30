package mods.gregtechmod.init;

import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.fluids.FluidGas;
import mods.gregtechmod.objects.fluids.FluidLiquid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class FluidLoader {
    public static final List<IFluidProvider> FLUIDS = new ArrayList<>();

    public static void init() {
        GregTechMod.logger.info("Initializing fluids");
        FLUIDS.addAll(Arrays.asList(Liquid.values()));
        FLUIDS.addAll(Arrays.asList(Gas.values()));
    }

    public interface IFluidProvider {
        String getName();

        Fluid getFluid();

        String getDescription();

        ResourceLocation getTexture();

        boolean isFallbackFluid();
    }

    public enum Liquid implements IFluidProvider {
        BERYLIUM("Be", 1690),
        CALCIUM("Ca", 1378),
        CALCIUM_CARBONATE("CaCO3", 2711),
        CHLORITE("Cl", 15625),
        DIESEL(860),
        GLYCERYL("C3H5N3O9", 1261),
        HELIUM_PLASMA("He"),
        LITHIUM("Li", 512),
        MERCURY("Hg", 13534),
        NITRO_COALFUEL,
        NITRO_DIESEL(832),
        OIL(800, true),
        POTASSIUM("K", 828),
        SEED_OIL(null, "seed.oil", 918, true),
        SILICON("Si", 2570),
        SODIUM("Na", 927),
        SODIUM_PERSULFATE("Na2S2O8", 1120),
        WOLFRAMIUM("W", 17600);

        public final String name;
        public final String description;
        public final int density;
        public final ResourceLocation texture;
        public final boolean fallback;
        private Fluid instance;

        Liquid() {
            this(1000);
        }

        Liquid(int density) {
            this(null, density);
        }

        Liquid(String description) {
            this(description, 1000);
        }

        Liquid(String description, int density) {
            this(description, density, false);
        }

        Liquid(int density, boolean fallback) {
            this(null, density, fallback);
        }

        Liquid(String description, int density, boolean fallback) {
            this.name = name().toLowerCase(Locale.ROOT);
            this.description = description;
            this.density = density;
            this.texture = getTexturePath();
            this.fallback = fallback;
        }

        Liquid(String description, String name, int density, boolean fallback) {
            this.name = name;
            this.description = description;
            this.density = density;
            this.texture = getTexturePath();
            this.fallback = fallback;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public Fluid getFluid() {
            if (this.instance == null) {
                if (this.fallback && FluidRegistry.isFluidRegistered(this.name)) this.instance = FluidRegistry.getFluid(this.name);
                else this.instance = new FluidLiquid(this.name, this.texture, this.texture)
                        .setUnlocalizedName(this.name)
                        .setDensity(this.density);
            }

            return this.instance;
        }

        @Override
        public String getDescription() {
            return this.description;
        }

        @Override
        public ResourceLocation getTexture() {
            return this.texture;
        }

        @Override
        public boolean isFallbackFluid() {
            return this.fallback;
        }

        private ResourceLocation getTexturePath() {
            return new ResourceLocation(Reference.MODID, "fluids/liquids/"+this.name);
        }
    }

    public enum Gas implements IFluidProvider {
        HYDROGEN("H"),
        DEUTERIUM("H-2"),
        TRITIUM("H-3"),
        HELIUM("He"),
        HELIUM3("He-3"),
        METHANE("CH4"),
        NITROGEN("N"),
        NITROGEN_DIOXIDE("NO2");

        public final String description;
        public final ResourceLocation texture;
        private Fluid instance;

        Gas(String description) {
            this.description = description;
            this.texture = getTextureLocation();
        }

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ROOT);
        }

        @Override
        public Fluid getFluid() {
            if (this.instance == null) {
                String name = this.name().toLowerCase(Locale.ROOT);
                this.instance = new FluidGas(name, this.texture, this.texture)
                        .setUnlocalizedName(name);
            }

            return this.instance;
        }

        @Override
        public String getDescription() {
            return this.description;
        }

        @Override
        public ResourceLocation getTexture() {
            return this.texture;
        }

        @Override
        public boolean isFallbackFluid() {
            return false;
        }

        private ResourceLocation getTextureLocation() {
            return new ResourceLocation(Reference.MODID, "fluids/gases/"+this.name().toLowerCase(Locale.ROOT));
        }
    }
}
