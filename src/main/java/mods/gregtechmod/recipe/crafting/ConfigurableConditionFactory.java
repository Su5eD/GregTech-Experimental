package mods.gregtechmod.recipe.crafting;

import com.google.gson.JsonObject;
import mods.gregtechmod.api.GregTechAPI;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

import java.util.function.BooleanSupplier;

public class ConfigurableConditionFactory implements IConditionFactory {
    @Override
    public BooleanSupplier parse(JsonContext context, JsonObject json) {
        JsonObject config = JsonUtils.getJsonObject(json, "config");
        final String category = JsonUtils.getString(config, "category");
        final String name = JsonUtils.getString(config, "name");
        final boolean value = JsonUtils.getBoolean(config, "value");

        return () -> GregTechAPI.getDynamicConfig(category, name, value);
    }
}
