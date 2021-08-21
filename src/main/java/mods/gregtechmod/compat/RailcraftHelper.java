package mods.gregtechmod.compat;

import mods.gregtechmod.core.GregTechMod;
import mods.railcraft.api.crafting.IGenRule;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;

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
                GregTechMod.logger.catching(e);
            }
        }

        RANDOM_CHANCE_GETTER = handleRandomChanceGetter;
        RANDOM_CHANCE_GEN_RULE_CLASS = classRandomChanceGenRule;
    }

    public static float getRandomChance(IGenRule genRule) {
        if (ModHandler.railcraft && RANDOM_CHANCE_GEN_RULE_CLASS.isInstance(genRule)) {
            try {
                return (float) RANDOM_CHANCE_GETTER.invoke(genRule);
            } catch (Throwable t) {
                GregTechMod.logger.catching(t);
            }
        }

        return 0;
    }
}
