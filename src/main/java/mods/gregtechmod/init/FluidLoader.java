package mods.gregtechmod.init;

import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.fluids.FluidGas;
import mods.gregtechmod.objects.fluids.FluidLiquid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

import java.util.ArrayList;
import java.util.List;

public class FluidLoader {
    public static final List<Fluid> FLUIDS = new ArrayList<>();

    public static void init() {
        GregTechMod.logger.info("Initializing fluids");
        for (Liquids type : Liquids.values()) {
            Fluid fluid = new FluidLiquid(type.name(), type.texture, type.texture)
                    .setUnlocalizedName(type.name())
                    .setDensity(type.density);
            FLUIDS.add(fluid);
            type.setInstance(fluid);
        }
        for (Gases type : Gases.values()) {
            Fluid fluid = new FluidGas(type.name(), type.texture, type.texture)
                    .setUnlocalizedName(type.name());
            FLUIDS.add(fluid);
            type.setInstance(fluid);
        }
    }

    //TODO: Descriptions
    public enum Liquids {
        berylium(1690),
        calcium(1378),
        calcium_carbonate(2711),
        chlorite(15625),
        glyceryl(1261),
        lithium(512),
        mercury(13534),
        nitro_coalfuel,
        nitro_diesel(832),
        plasma,
        potassium(828),
        seed_oil(918),
        silicon(2570),
        sodium(927),
        sodium_persulfate(1120),
        wolframium(17600);

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
            return new ResourceLocation(Reference.MODID, "fluids/liquids/"+this.name());
        }

        /**
         * <b>Only GregTech may call this!!</b>
         */
        private void setInstance(Fluid item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+this.name());
            this.instance = item;
        }

        public Fluid getInstance() {
            return this.instance;
        }
    }

    public enum Gases {
        hydrogen,
        deuterium,
        tritium,
        helium,
        helium3,
        methane,
        nitrogen,
        nitrogen_dioxide;

        public final ResourceLocation texture;
        private Fluid instance;

        Gases() {
            this.texture = getTexture();
        }

        private ResourceLocation getTexture() {
            return new ResourceLocation(Reference.MODID, "fluids/gases/"+this.name());
        }

        /**
         * <b>Only GregTech may call this!!</b>
         */
        private void setInstance(Fluid item) {
            if (this.instance != null) throw new RuntimeException("The instance has been already set for "+this.name());
            this.instance = item;
        }

        public Fluid getInstance() {
            return this.instance;
        }
    }
}
