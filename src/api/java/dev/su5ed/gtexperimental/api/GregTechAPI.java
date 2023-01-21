package dev.su5ed.gtexperimental.api;

@SuppressWarnings("unused")
public final class GregTechAPI {
    private static IGregTechAPI impl;

    private GregTechAPI() {}

    public static IGregTechAPI instance() {
        return impl;
    }
}
