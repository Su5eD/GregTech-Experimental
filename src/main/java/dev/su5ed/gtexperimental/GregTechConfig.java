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

        public final ForgeConfigSpec.BooleanValue woodNeedsSawForCrafting;
        public final ForgeConfigSpec.BooleanValue toolPlateCrafting;
        public final ForgeConfigSpec.BooleanValue armorPlateCrafting;
        public final ForgeConfigSpec.BooleanValue storageBlockCrafting;
        public final ForgeConfigSpec.BooleanValue storageBlockDecrafting;

        public final ForgeConfigSpec.IntValue upgradeStackSize;
        public final ForgeConfigSpec.BooleanValue teslaStaffDestroysArmor;

        public final ForgeConfigSpec.BooleanValue solarPanelCoverOvervoltageProtection;
        public final ForgeConfigSpec.IntValue explosionPowerMultiplier;

        public final ForgeConfigSpec.BooleanValue machineWireFire;
        public final ForgeConfigSpec.BooleanValue machineChainExplosions;

        private Common(ForgeConfigSpec.Builder builder) {
            builder.push("General config");
            this.hiddenOres = builder.define("hiddenOres", true);
            this.enhancedWrenchOverlay = builder.define("enhancedWrenchOverlay", true);
            builder.pop();

            builder.push("Recipes");
            this.woodNeedsSawForCrafting = builder.define("woodNeedsSawForCrafting", true);
            this.toolPlateCrafting = builder.define("toolPlateCrafting", true);
            this.armorPlateCrafting = builder.define("armorPlateCrafting", true);
            this.storageBlockCrafting = builder.define("storageBlockCrafting", false);
            this.storageBlockDecrafting = builder.define("storageBlockDecrafting", false);
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
            this.machineChainExplosions = builder
                .comment("Nearby explosions cause machines to explode, too")
                .define("machineChainExplosions", true);
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
