package dev.su5ed.gregtechmod.util.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.fml.ModList;

import static dev.su5ed.gregtechmod.api.Reference.location;

public class ModLoadedCondition implements LootItemCondition {
    public static final ResourceLocation NAME = location("mod_loaded");
    public static final LootItemConditionType TYPE = new LootItemConditionType(new Serializer());

    private final String modid;

    private ModLoadedCondition(String modid) {
        this.modid = modid;
    }
    
    public static LootItemCondition.Builder modLoaded(String modid) {
        return () -> new ModLoadedCondition(modid);
    }

    @Override
    public boolean test(LootContext context) {
        return ModList.get().isLoaded(this.modid);
    }

    @Override
    public LootItemConditionType getType() {
        return TYPE;
    }

    private static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<ModLoadedCondition> {

        @Override
        public void serialize(JsonObject json, ModLoadedCondition value, JsonSerializationContext context) {
            json.addProperty("modid", value.modid);
        }

        @Override
        public ModLoadedCondition deserialize(JsonObject json, JsonDeserializationContext context) {
            String modid = GsonHelper.getAsString(json, "modid");
            return new ModLoadedCondition(modid);
        }
    }
}
