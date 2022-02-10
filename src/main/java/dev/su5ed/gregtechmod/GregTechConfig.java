package dev.su5ed.gregtechmod;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public final class GregTechConfig {
    public static final ForgeConfigSpec CLIENT_SPEC;

    public static final Client CLIENT;
    
    static {
        Pair<Client, ForgeConfigSpec> configBuilder = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT = configBuilder.getKey();
        CLIENT_SPEC = configBuilder.getRight();
    }
    
    private GregTechConfig() {}

    public static final class Client {
        public final ForgeConfigSpec.BooleanValue connectedTextures;
        
        private Client(ForgeConfigSpec.Builder builder) {
            builder.push("Client config");
            
            this.connectedTextures = builder.define("connected_textures", true);
            
            builder.pop();
        }
    }
}
