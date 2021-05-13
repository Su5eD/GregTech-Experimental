package mods.gregtechmod.compat;

import mods.railcraft.api.crafting.IGenRule;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;

/**
 * Helper? More like hacker...
 */
public class RailcraftHelper {
    private static final MethodHandle RANDOM_CHANCE_GETTER;
    private static final Class<?> RANDOM_CHANCE_GEN_RULE_CLASS;

    static {
        MethodHandle handleRandomChanceGetter = null;
        Class<?> classRandomChanceGenRule = null;
        if (ModHandler.railcraft) {
            try {
                classRandomChanceGenRule = Class.forName("mods.railcraft.common.util.crafting.RockCrusherCrafter$RandomChanceGenRule");
                Field fieldRandomChance = classRandomChanceGenRule.getDeclaredField("randomChance");
                fieldRandomChance.setAccessible(true);
                handleRandomChanceGetter = MethodHandles.lookup().unreflectGetter(fieldRandomChance);
            } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        RANDOM_CHANCE_GETTER = handleRandomChanceGetter;
        RANDOM_CHANCE_GEN_RULE_CLASS = classRandomChanceGenRule;
    }

    public static float getRandomChance(IGenRule genRule) {
        if (ModHandler.railcraft) {
            if (RANDOM_CHANCE_GEN_RULE_CLASS.isInstance(genRule)) {
                try {
                    return (float) RANDOM_CHANCE_GETTER.invoke(genRule);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }

        return 0;
    }
}
