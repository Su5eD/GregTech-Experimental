package mods.gregtechmod.compat.crafttweaker;

import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.init.OreDictHandler;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ModOnly(Reference.MODID)
@ZenClass("mods.gregtechmod.OreDict")
@ZenRegister
public class OreDictHelper {

    @ZenMethod
    public static void addOreAlias(String ore, String alias) {
        OreDictHandler.addOreAlias(ore, alias);
    }

    @ZenMethod
    public static void removeOreAlias(String ore) {
        OreDictHandler.removeOreAlias(ore);
    }

    @ZenMethod
    public static void addValuableOre(String ore, int value) {
        OreDictHandler.addValuableOre(ore, value);
    }

    @ZenMethod
    public static void removeValuableOre(String ore) {
        OreDictHandler.removeValuableOre(ore);
    }
    
    @ZenMethod
    public static void addIgnoredName(String ore) {
        OreDictHandler.addIgnoredName(ore);
    }
    
    @ZenMethod
    public static void removeIgnoredName(String ore) {
        OreDictHandler.removeIgnoredName(ore);
    }
}
