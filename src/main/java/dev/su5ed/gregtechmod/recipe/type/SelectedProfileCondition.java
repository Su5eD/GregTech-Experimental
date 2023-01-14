package dev.su5ed.gregtechmod.recipe.type;

import com.google.gson.JsonObject;
import dev.su5ed.gregtechmod.GregTechMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

import static dev.su5ed.gregtechmod.api.Reference.location;

public class SelectedProfileCondition implements ICondition {
    public static final SelectedProfileCondition CLASSIC = new SelectedProfileCondition("classic");
    public static final SelectedProfileCondition EXPERIMENTAL = new SelectedProfileCondition("experimental");
    
    private static final ResourceLocation NAME = location("selected_profile");

    private final String profile;

    public SelectedProfileCondition(String profile) {
        this.profile = profile;
    }

    @Override
    public ResourceLocation getID() {
        return NAME;
    }

    @Override
    public boolean test(IContext context) {
        return this.profile.equals("classic") == GregTechMod.isClassic;
    }

    public static class Serializer implements IConditionSerializer<SelectedProfileCondition> {
        public static final SelectedProfileCondition.Serializer INSTANCE = new SelectedProfileCondition.Serializer();

        @Override
        public void write(JsonObject json, SelectedProfileCondition value) {
            json.addProperty("profile", value.profile);
        }

        @Override
        public SelectedProfileCondition read(JsonObject json) {
            return new SelectedProfileCondition(GsonHelper.getAsString(json, "profile"));
        }

        @Override
        public ResourceLocation getID() {
            return SelectedProfileCondition.NAME;
        }
    }
}
