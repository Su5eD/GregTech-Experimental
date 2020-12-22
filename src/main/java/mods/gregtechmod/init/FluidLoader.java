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
        for (Liquids type : Liquids.values()) {
            Fluid fluid = new FluidLiquid(type.name().toLowerCase(Locale.ROOT), type.texture, type.texture)
                    .setUnlocalizedName(type.name().toLowerCase(Locale.ROOT))
                    .setDensity(type.density);
            FLUIDS.add(fluid);
            type.setInstance(fluid);
        }
        for (Gases type : Gases.values()) {
            Fluid fluid = new FluidGas(type.name().toLowerCase(Locale.ROOT), type.texture, type.texture)
                    .setUnlocalizedName(type.name().toLowerCase(Locale.ROOT));
            FLUIDS.add(fluid);
            type.setInstance(fluid);
        }
    }

    //TODO: Descriptions
    public enum Liquids {
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
        SEED_OIL(918),
        SILICON(2570),
        SODIUM(927),
        SODIUM_PERSULFATE(1120),
        WOLFRAMIUM(17600);

        public final int density;
        public final ResourceLocation texture;
        private Fluid instance;

        Liquids() {
            this(1000);
        }

        Liquids(int density) {
            this.density = density;
            this.texture = getTexture();
        }

        private ResourceLocation getTexture() {
            return new ResourceLocation(Reference.MODID, "fluids/liquids/"+this.name().toLowerCase(Locale.ROOT));
        }

        /**
         * <b>Only GregTech may call this!!</b>
         */
        private void setInstance(Fluid item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+this.name().toLowerCase(Locale.ROOT));
            this.instance = item;
        }

        public Fluid getInstance() {
            return this.instance;
        }
    }

    public enum Gases {
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

        Gases() {
            this.texture = getTexture();
        }

        private ResourceLocation getTexture() {
            return new ResourceLocation(Reference.MODID, "fluids/gases/"+this.name().toLowerCase(Locale.ROOT));
        }

        /**
         * <b>Only GregTech may call this!!</b>
         */
        private void setInstance(Fluid item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+this.name().toLowerCase(Locale.ROOT));
            this.instance = item;
        }

        public Fluid getInstance() {
            return this.instance;
        }
    }
}
