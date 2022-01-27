package mods.gregtechmod.util;

import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityAutoNBT;
import net.minecraft.client.resources.I18n;
import one.util.streamex.StreamEx;

public final class GtLocale {
    
    private GtLocale() {}

    public static String translateTeBlock(TileEntityAutoNBT te, String key, Object... parameters) {
        return translateTeBlock(te.getTeBlock().getName(), key, parameters);
    }

    public static String translateTeBlock(String teBlockName, String key, Object... parameters) {
        return translate("teblock." + teBlockName + "." + key, parameters);
    }

    public static String translateTeBlockName(TileEntityAutoNBT te) {
        return translate("teblock." + te.getTeBlock().getName());
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
    
    public static String translateKey(String... paths) {
        return translate(buildKey(paths));
    }

    public static String translate(String key, Object... parameters) {
        return I18n.format(Reference.MODID + "." + key, parameters);
    }
    
    public static String buildKeyTeBlock(TileEntityAutoNBT teBlock, String... paths) {
        return buildKey(paths, "teblock", teBlock.getTeBlock().getName());
    }
    
    public static String buildKeyInfo(String... paths) {
        return buildKey(paths, "info");
    }
    
    public static String buildKeyItem(INamedItem item, String... paths) {
        return buildKeyItem(item.getName(), paths);
    }
    
    public static String buildKeyItem(String item, String... paths) {
        return buildKey(paths, "item", item);
    }
    
    public static String buildKey(String... paths) {
        return StreamEx.of(Reference.MODID)
            .append(paths)
            .joining(".");
    }
    
    private static String buildKey(String[] args, String... paths) {
        return StreamEx.of(Reference.MODID)
            .append(paths)
            .append(args)
            .joining(".");
    }
    
    public static boolean hasKey(String key) {
        return I18n.hasKey(Reference.MODID + "." + key);
    }
}
