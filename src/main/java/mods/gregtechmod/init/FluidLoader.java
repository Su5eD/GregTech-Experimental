package mods.gregtechmod.init;

import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.fluids.FluidGas;
import mods.gregtechmod.objects.fluids.FluidLiquid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FluidLoader {
    public static final List<Fluid> FLUIDS = new ArrayList<>();

    public static void init() {
        GregTechAPI.logger.info("Initializing fluids");
        for (Liquid type : Liquid.values()) {
            FLUIDS.add(type.getInstance());
        }
        for (Gas type : Gas.values()) {
            FLUIDS.add(type.getInstance());
        }
    }

    public enum Liquid {
        BERYLIUM(1690),
        CALCIUM(1378),
        CALCIUM_CARBONATE(2711),
        CHLORITE(15625),
        GLYCERYL(1261),
        LITHIUM(512),
        MERCURY(13534),
        NITRO_COALFUEL,
        NITRO_DIESEL(832),
        PLASMA,
        POTASSIUM(828),
        SEED_OIL("seed.oil", 918),
        SILICON(2570),
        SODIUM(927),
        SODIUM_PERSULFATE(1120),
        WOLFRAMIUM(17600);

        public final String name;
        public final int density;
        public final ResourceLocation texture;
        private Fluid instance;

        Liquid() {
            this(1000);
        }

        Liquid(int density) {
            this.name = name().toLowerCase(Locale.ROOT);
            this.density = density;
            this.texture = getTexture();
        }

        Liquid(String name, int density) {
            this.name = name;
            this.density = density;
            this.texture = getTexture();
        }

        private ResourceLocation getTexture() {
            return new ResourceLocation(Reference.MODID, "fluids/liquids/"+this.name);
        }

        public Fluid getInstance() {
            if (this.instance == null) {
                this.instance = new FluidLiquid(this.name, this.texture, this.texture)
                                    .setUnlocalizedName(this.name)
                                    .setDensity(this.density);
            }

            return this.instance;
        }
    }

    public enum Gas {
        HYDROGEN,
        DEUTERIUM,
        TRITIUM,
        HELIUM,
        HELIUM3,
        METHANE,
        NITROGEN,
        NITROGEN_DIOXIDE;

        public final ResourceLocation texture;
        private Fluid instance;

        Gas() {
            this.texture = getTexture();
        }

        private ResourceLocation getTexture() {
            return new ResourceLocation(Reference.MODID, "fluids/gases/"+this.name().toLowerCase(Locale.ROOT));
        }

        public Fluid getInstance() {
            if (this.instance == null) {
                String name = this.name().toLowerCase(Locale.ROOT);
                this.instance = new FluidGas(name, this.texture, this.texture)
                                    .setUnlocalizedName(name);
            }

            return this.instance;
        }
    }
}
