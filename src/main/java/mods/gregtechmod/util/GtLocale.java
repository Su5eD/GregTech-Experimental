package mods.gregtechmod.util;

import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityAutoNBT;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import one.util.streamex.StreamEx;

public final class GtLocale {

    private GtLocale() {}

    public static String translateTeBlock(TileEntityAutoNBT te, String key, Object... parameters) {
        return translateTeBlock(te.getTeBlock().getName(), key, parameters);
    }

    public static String translateTeBlock(String teBlockName, String key, Object... parameters) {
        return I18n.format(buildKey("teblock", teBlockName, key), parameters);
    }

    public static String translateTeBlockName(TileEntityAutoNBT te) {
        return I18n.format(buildKeyTeBlock(te));
    }

    public static ITextComponent translateScan(String key, Object... args) {
        return new TextComponentTranslation(GtLocale.buildKeyScan(key), args);
    }

    public static String translateTeBlockDescription(String key) {
        return I18n.format(buildKey("teblock", key, "description"));
    }

    public static String translateInfo(String key, Object... parameters) {
        return I18n.format(buildKey("info", key), parameters);
    }

    public static String translateGenericDescription(String key, Object... parameters) {
        return I18n.format(buildKey("generic", key, "description"), parameters);
    }

    public static String translateGeneric(String key, Object... parameters) {
        return I18n.format(buildKey("generic", key), parameters);
    }

    public static String translateItemDescription(String key, Object... parameters) {
        return I18n.format(buildKeyItem(key, "description"), parameters);
    }

    public static String translateItem(String key, Object... parameters) {
        return I18n.format(buildKeyItem(key), parameters);
    }

    public static String translateKey(String... paths) {
        return I18n.format(buildKey(paths));
    }

    public static String translate(String key, Object... parameters) {
        return I18n.format(Reference.MODID + "." + key, parameters);
    }

    public static String buildKeyScan(String key, String... paths) {
        return buildKey(paths, "scan", key);
    }

    public static String buildKeyTeBlock(TileEntityAutoNBT teBlock, String... paths) {
        return buildKey(paths, "teblock", teBlock.getTeBlock().getName());
    }

    public static String buildKeyInfo(String... paths) {
        return buildKey(paths, "info");
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
}
