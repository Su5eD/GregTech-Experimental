package mods.gregtechmod.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.runtime.ScriptLoader;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechMod;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.function.Predicate;

public final class CraftTweakerCompat {

    public static void loadScripts() {
        ScriptLoader loader = CraftTweakerAPI.tweaker.getOrCreateLoader(Reference.MODID);
        CraftTweakerAPI.tweaker.loadScript(false, loader);

        if (loader.getLoaderStage() != ScriptLoader.LoaderStage.LOADED_SUCCESSFUL) {
            GregTechMod.LOGGER.error("Failed to load CT scripts");
        }
    }

    public static Predicate<List<ItemStack>> compareOutputs(List<ItemStack> outputs) {
        return recipeOutput -> {
            if (recipeOutput.size() == outputs.size()) {
                for (int i = 0; i < recipeOutput.size(); i++) {
                    if (!recipeOutput.get(i).isItemEqual(outputs.get(i))) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        };
    }

    private CraftTweakerCompat() {}
}
