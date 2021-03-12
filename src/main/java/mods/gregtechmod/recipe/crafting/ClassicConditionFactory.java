package mods.gregtechmod.recipe.crafting;

import com.google.gson.JsonObject;
import ic2.core.IC2;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

import java.util.function.BooleanSupplier;

public class ClassicConditionFactory implements IConditionFactory {
    @Override
    public BooleanSupplier parse(JsonContext context, JsonObject json) {
        return () -> !IC2.version.isExperimental();
    }
}
