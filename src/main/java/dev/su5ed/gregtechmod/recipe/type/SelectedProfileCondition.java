package dev.su5ed.gregtechmod.recipe.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.su5ed.gregtechmod.GregTechMod;
import dev.su5ed.gregtechmod.util.ProfileManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import one.util.streamex.EntryStream;

import java.util.Map;

import static dev.su5ed.gregtechmod.api.Reference.location;

public class SelectedProfileCondition implements ICondition {
    public static final SelectedProfileCondition CLASSIC = new SelectedProfileCondition(ProfileManager.LAYOUT, ProfileManager.CLASSIC_LAYOUT);
    public static final SelectedProfileCondition EXPERIMENTAL = new SelectedProfileCondition(ProfileManager.LAYOUT, ProfileManager.EXPERIMENTAL_LAYOUT);
    public static final SelectedProfileCondition REFINED_IRON = new SelectedProfileCondition(ProfileManager.IRON_VARIANT, ProfileManager.REFINED_IRON_VARIANT);
    public static final SelectedProfileCondition IRON = new SelectedProfileCondition(ProfileManager.IRON_VARIANT, ProfileManager.REGULAR_IRON_VARIANT);
    
    private static final ResourceLocation NAME = location("selected_profile");

    private final Map<String, String> properties;

    public SelectedProfileCondition(String key, String value) {
        this(Map.of(key, value));
    }

    public SelectedProfileCondition(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public ResourceLocation getID() {
        return NAME;
    }

    @Override
    public boolean test(IContext context) {
        return EntryStream.of(this.properties)
            .allMatch(GregTechMod.PROFILE_MANAGER.getProfile()::checkProperty);
    }

    public static class Serializer implements IConditionSerializer<SelectedProfileCondition> {
        public static final SelectedProfileCondition.Serializer INSTANCE = new SelectedProfileCondition.Serializer();

        @Override
        public void write(JsonObject json, SelectedProfileCondition value) {
            JsonObject map = new JsonObject();
            value.properties.forEach(map::addProperty);
            json.add("properties", map);
        }

        @Override
        public SelectedProfileCondition read(JsonObject json) {
            JsonObject jsonProperties = GsonHelper.getAsJsonObject(json, "properties");
            Map<String, String> properties = EntryStream.of(jsonProperties.entrySet().iterator())
                .mapValues(JsonElement::getAsString)
                .toMap();
            return new SelectedProfileCondition(properties);
        }

        @Override
        public ResourceLocation getID() {
            return SelectedProfileCondition.NAME;
        }
    }
}
