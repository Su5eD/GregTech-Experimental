package dev.su5ed.gtexperimental;

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
        public final ForgeConfigSpec.BooleanValue enhancedWrenchOverlay;
        
        public final ForgeConfigSpec.IntValue upgradeStackSize;
        public final ForgeConfigSpec.BooleanValue teslaStaffDestroysArmor;
        
        public final ForgeConfigSpec.BooleanValue solarPanelCoverOvervoltageProtection;
        public final ForgeConfigSpec.IntValue explosionPowerMultiplier;
        
        public final ForgeConfigSpec.BooleanValue machineWireFire;

        private Common(ForgeConfigSpec.Builder builder) {
            builder.push("General config");
            this.hiddenOres = builder.define("hiddenOres", true);
            this.enhancedWrenchOverlay = builder.define("enhancedWrenchOverlay", true);
            builder.pop();
            
            builder.push("Features");
            this.upgradeStackSize = builder.defineInRange("upgradeStackSize", 4, 1, 64);
            this.teslaStaffDestroysArmor = builder.define("teslaStaffDestroysArmor", false);
            builder.pop();
            
            builder.push("Balance");
            this.solarPanelCoverOvervoltageProtection = builder
                .comment("Prevent MV and HV solar panel covers from overloading (and exploding) your machines")
                .define("solarPanelCoverOvervoltageProtection", false);
            this.explosionPowerMultiplier = builder
                .defineInRange("explosionPowerMultiplier", 2, 0, 100);
            builder.pop();
            
            builder.push("Machines");
            this.machineWireFire = builder
                .comment("Wirefire on explosion")
                .define("machineWireFire", true);
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
