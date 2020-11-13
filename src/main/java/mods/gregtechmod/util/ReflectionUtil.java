package mods.gregtechmod.util;

import ic2.core.block.wiring.TileEntityCable;
import mods.gregtechmod.api.BlockItems;
import mods.gregtechmod.api.GregTechAPI;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtil {
    private static final Class<?> ITEM_SENSOR_KIT_CLASS;
    private static final Class<?> ITEM_SENSOR_CARD_CLASS;
    private static final Class<?> CABLE_FOAM_CLASS;
    private static final Method REGISTER_KIT_METHOD;
    private static final Method REGISTER_CARD_METHOD;

    static {
        Class<?> classItemSensorKit;
        Class<?> classItemSensorCard;
        Class<?> classCableFoam;
        Method methodRegisterKit;
        Method methodRegisterCard;
        try {
            classItemSensorKit = Class.forName("mods.gregtechmod.objects.items.ItemSensorKit");
            classItemSensorCard = Class.forName("mods.gregtechmod.objects.items.ItemSensorCard");
            classCableFoam = Class.forName("ic2.core.block.wiring.CableFoam");
            Class<?> classEnergyControlRegister = Class.forName("com.zuxelus.energycontrol.api.EnergyContolRegister");
            Class<?> classIItemKit = Class.forName("com.zuxelus.energycontrol.api.IItemKit");
            Class<?> classIItemCard = Class.forName("com.zuxelus.energycontrol.api.IItemCard");
            methodRegisterKit = classEnergyControlRegister.getDeclaredMethod("registerKit", classIItemKit);
            methodRegisterCard = classEnergyControlRegister.getDeclaredMethod("registerCard", classIItemCard);
        } catch(ClassNotFoundException | NoSuchMethodException ignored) {
            classItemSensorKit = classItemSensorCard = classCableFoam = null;
            methodRegisterKit = methodRegisterCard = null;
        }
        ITEM_SENSOR_KIT_CLASS = classItemSensorKit;
        ITEM_SENSOR_CARD_CLASS = classItemSensorCard;
        CABLE_FOAM_CLASS = classCableFoam;
        REGISTER_KIT_METHOD = methodRegisterKit;
        REGISTER_CARD_METHOD = methodRegisterCard;
    }

    public static void registerEnergyControlItems() {
        try {
            Object itemKitInstance = ITEM_SENSOR_KIT_CLASS.getConstructors()[0].newInstance();
            BlockItems.class.getDeclaredField("sensor_kit").set(null, itemKitInstance);
            Object itemCardInstance = ITEM_SENSOR_CARD_CLASS.getConstructors()[0].newInstance();
            BlockItems.class.getDeclaredField("sensor_card").set(null, itemCardInstance);
            REGISTER_KIT_METHOD.invoke(null, itemKitInstance);
            REGISTER_CARD_METHOD.invoke(null, itemCardInstance);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchFieldException e) {
            GregTechAPI.logger.error("Failed to register Energy Control items");
            e.printStackTrace();
        }
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
