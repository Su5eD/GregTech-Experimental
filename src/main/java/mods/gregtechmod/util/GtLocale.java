package mods.gregtechmod.util;

import ic2.core.block.ITeBlock;
import mods.gregtechmod.api.util.Reference;
import net.minecraft.client.resources.I18n;

public final class GtLocale {
    
    private GtLocale() {}

    public static String translateTeBlock(ITeBlock te, String key, Object... parameters) {
        return translateTeBlock(te.getName(), key, parameters);
    }

    public static String translateTeBlock(String teBlockName, String key, Object... parameters) {
        return translate("teblock." + teBlockName + "." + key, parameters);
    }

    public static String translateTeBlockName(ITeBlock te) {
        return translate("teblock." + te.getName());
    }

    public static String translateScan(String key, Object... parameters) {
        return translate("scan." + key, parameters);
    }

    public static String translateTeBlockDescription(String key) {
        return translate("teblock." + key + ".description");
    }

    public static String translateInfo(String key, Object... parameters) {
        return translate("info." + key, parameters);
    }

    public static String translateGenericDescription(String key, Object... parameters) {
        return translateGeneric(key + ".description", parameters);
    }

    public static String translateGeneric(String key, Object... parameters) {
        return translate("generic." + key, parameters);
    }

    public static String translateItemDescription(String key, Object... parameters) {
        return translateItem(key + ".description", parameters);
    }

    public static String translateItem(String key, Object... parameters) {
        return translate("item." + key, parameters);
    }

    public static String translate(String key, Object... parameters) {
        return I18n.format(Reference.MODID + "." + key, parameters);
    }
    
    public static boolean hasKey(String key) {
        return I18n.hasKey(Reference.MODID + "." + key);
    }
}
