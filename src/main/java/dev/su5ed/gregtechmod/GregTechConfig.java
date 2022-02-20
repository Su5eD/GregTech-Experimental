package dev.su5ed.gregtechmod;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public final class GregTechConfig {
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final ForgeConfigSpec CLIENT_SPEC;

    public static final Common COMMON;
    public static final Client CLIENT;

    static {
        Pair<Common, ForgeConfigSpec> commonPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON = commonPair.getLeft();
        COMMON_SPEC = commonPair.getRight();

        Pair<Client, ForgeConfigSpec> clientPair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT = clientPair.getLeft();
        CLIENT_SPEC = clientPair.getRight();
    }

    private GregTechConfig() {}

    public static final class Common {
        public final ForgeConfigSpec.BooleanValue hiddenOres;
        public final ForgeConfigSpec.BooleanValue solarPanelCoverOvervoltageProtection;

        private Common(ForgeConfigSpec.Builder builder) {
            builder.push("General config");
            this.hiddenOres = builder.define("hiddenOres", true);
            builder.pop();
            
            builder.push("Balance");
            this.solarPanelCoverOvervoltageProtection = builder
                .comment("Prevent MV and HV solar panel covers from overloading (and exploding) your machines")
                .define("solarPanelCoverOvervoltageProtection", false);
            builder.pop();
        }
    }

    public static final class Client {
        public final ForgeConfigSpec.BooleanValue connectedTextures;

        private Client(ForgeConfigSpec.Builder builder) {
            builder.push("Client config");

            this.connectedTextures = builder.define("connected_textures", true);

            builder.pop();
        }
    }
}
