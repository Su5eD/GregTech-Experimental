package mods.gregtechmod.compat;

import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.items.tools.ItemWrench;
import mods.gregtechmod.objects.items.tools.ItemWrenchAdvanced;

import java.lang.reflect.InvocationTargetException;

public class ModuleBuildcraft {
    private static final Class<?> ITEM_WRENCH_BC_CLASS;
    private static final Class<?> ITEM_WRENCH_ADVANCED_BC_CLASS;

    static {
        Class<?> classItemWrenchBC;
        Class<?> classItemWrenchAdvancedBC;
        try {
            classItemWrenchBC = Class.forName("mods.gregtechmod.objects.items.tools.ItemWrenchBC");
            classItemWrenchAdvancedBC = Class.forName("mods.gregtechmod.objects.items.tools.ItemWrenchAdvancedBC");
        } catch (ClassNotFoundException ignored) {
            classItemWrenchBC = classItemWrenchAdvancedBC = null;
        }
        ITEM_WRENCH_BC_CLASS = classItemWrenchBC;
        ITEM_WRENCH_ADVANCED_BC_CLASS = classItemWrenchAdvancedBC;
    }

    public static ItemWrench constructBuildcraftWrench(String name, int durability) {
        try {
            Object itemWrenchBC = ITEM_WRENCH_BC_CLASS.getConstructors()[0].newInstance(name, durability);
            return (ItemWrench) itemWrenchBC;
        } catch(IllegalAccessException | InvocationTargetException | InstantiationException e) {
            GregTechMod.logger.error("Failed to construct Buildcraft Wrench, defaulting to the normal one");
            e.printStackTrace();
            return new ItemWrench(name, durability);
        }
    }

    public static ItemWrenchAdvanced constructBuildcraftAdvancedWrench() {
        try {
            Object itemWrenchAdvancedBC = ITEM_WRENCH_ADVANCED_BC_CLASS.getConstructors()[0].newInstance();
            return (ItemWrenchAdvanced) itemWrenchAdvancedBC;
        } catch(IllegalAccessException | InvocationTargetException | InstantiationException e) {
            GregTechMod.logger.error("Failed to construct Buildcraft Advanced Wrench, defaulting to the normal one");
            e.printStackTrace();
            return new ItemWrenchAdvanced();
        }
    }
}
