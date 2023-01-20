package dev.su5ed.gtexperimental.util;

import dev.su5ed.gtexperimental.compat.ModHandler;

import java.util.Map;

public class ProfileManager {
    public static final String LAYOUT = "layout";
    public static final String CLASSIC_LAYOUT = "classic";
    public static final String EXPERIMENTAL_LAYOUT = "experimental";
    
    public static final String IRON_VARIANT = "iron_variant";
    public static final String REFINED_IRON_VARIANT = "refined_iron";
    public static final String REGULAR_IRON_VARIANT = "iron";
    
    private ModProfile profile = ModProfile.EXPERIMENTAL;
    private boolean isClassic;

    public ModProfile getProfile() {
        return this.profile;
    }
    
    public boolean isClassic() {
        return this.isClassic;
    }

    public void init() {
        boolean classicIc2 = false; // TODO PLACEHOLDER until ic2 profiles are ported
        if (ModHandler.ic2Loaded && classicIc2) {
            this.profile = ModProfile.CLASSIC;
        }
        else if (ModHandler.ftbicLoaded) {
            this.profile = ModProfile.FTBIC;
        }
        
        this.isClassic = this.profile.checkProperty(LAYOUT, CLASSIC_LAYOUT);
    }

    public enum ModProfile {
        CLASSIC(CLASSIC_LAYOUT, REFINED_IRON_VARIANT),
        EXPERIMENTAL(EXPERIMENTAL_LAYOUT, IRON_VARIANT),
        FTBIC(CLASSIC_LAYOUT, IRON_VARIANT);

        private final Map<String, String> properties;

        ModProfile(String layout, String ironVariant) {
            this.properties = Map.of(
                "layout", layout,
                "iron_variant", ironVariant
            );
        }
        
        public boolean checkProperty(String name, String value) {
            String prop = this.properties.get(name);
            return prop != null && prop.equals(value);
        }
    }
}
