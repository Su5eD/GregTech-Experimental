package mods.gregtechmod.compat;

import mods.gregtechmod.api.BlockItems;
import mods.gregtechmod.api.GregTechAPI;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ModuleEnergyControl {
    private static final Class<?> ITEM_SENSOR_KIT_CLASS;
    private static final Class<?> ITEM_SENSOR_CARD_CLASS;
    private static final Method REGISTER_KIT_METHOD;
    private static final Method REGISTER_CARD_METHOD;

    static {
        Class<?> classItemSensorKit;
        Class<?> classItemSensorCard;
        Method methodRegisterKit;
        Method methodRegisterCard;
        try {
            classItemSensorKit = Class.forName("mods.gregtechmod.objects.items.ItemSensorKit");
            classItemSensorCard = Class.forName("mods.gregtechmod.objects.items.ItemSensorCard");
            Class<?> classEnergyControlRegister = Class.forName("com.zuxelus.energycontrol.api.EnergyContolRegister");
            Class<?> classIItemKit = Class.forName("com.zuxelus.energycontrol.api.IItemKit");
            Class<?> classIItemCard = Class.forName("com.zuxelus.energycontrol.api.IItemCard");
            methodRegisterKit = classEnergyControlRegister.getDeclaredMethod("registerKit", classIItemKit);
            methodRegisterCard = classEnergyControlRegister.getDeclaredMethod("registerCard", classIItemCard);
        } catch(ClassNotFoundException | NoSuchMethodException ignored) {
            classItemSensorKit = classItemSensorCard = null;
            methodRegisterKit = methodRegisterCard = null;
        }
        ITEM_SENSOR_KIT_CLASS = classItemSensorKit;
        ITEM_SENSOR_CARD_CLASS = classItemSensorCard;
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
}
