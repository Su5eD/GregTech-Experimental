package mods.gregtechmod.util;

import ic2.core.block.wiring.TileEntityCable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtil {
    private static final Class<?> CABLE_FOAM_CLASS;

    static {
        Class<?> classCableFoam;
        try {
            classCableFoam = Class.forName("ic2.core.block.wiring.CableFoam");
        } catch(ClassNotFoundException ignored) {
            classCableFoam = null;
        }
        CABLE_FOAM_CLASS = classCableFoam;
    }

    public static void hardenCableFoam(Object tileEntityCable)
    {
        try {
            Object cableFoamHardened = CABLE_FOAM_CLASS.getEnumConstants()[2];
            Method methodChangeFoam = TileEntityCable.class.getDeclaredMethod("changeFoam", CABLE_FOAM_CLASS, boolean.class);
            methodChangeFoam.setAccessible(true);
            methodChangeFoam.invoke(tileEntityCable, cableFoamHardened, false);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
