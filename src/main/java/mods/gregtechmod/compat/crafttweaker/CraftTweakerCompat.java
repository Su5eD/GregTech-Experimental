package mods.gregtechmod.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.runtime.ScriptLoader;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechMod;

public final class CraftTweakerCompat {

    public static void loadScripts() {
        ScriptLoader loader = CraftTweakerAPI.tweaker.getOrCreateLoader(Reference.MODID);
        CraftTweakerAPI.tweaker.loadScript(false, loader);

        if (loader.getLoaderStage() != ScriptLoader.LoaderStage.LOADED_SUCCESSFUL) {
            GregTechMod.LOGGER.error("Failed to load CT scripts");
        }
    }

    private CraftTweakerCompat() {}
}
