package dev.su5ed.gtexperimental;

import com.fasterxml.jackson.databind.JsonNode;
import dev.su5ed.gtexperimental.api.GregTechAPI;
import dev.su5ed.gtexperimental.api.IGregTechAPI;
import dev.su5ed.gtexperimental.api.util.SonictronSound;
import dev.su5ed.gtexperimental.util.JavaUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import one.util.streamex.StreamEx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

final class GregTechAPIImpl implements IGregTechAPI {
    private final List<SonictronSound> sonictronSounds = new ArrayList<>();
    private final Map<String, Predicate<JsonNode>> conditions = new HashMap<>();

    @Override
    public void registerSonictronSound(SonictronSound sound) {
        this.sonictronSounds.add(sound);
    }

    @Override
    public void registerSonictronSounds(Collection<SonictronSound> sounds) {
        this.sonictronSounds.addAll(sounds);
    }

    @Override
    public List<SonictronSound> getSonictronSounds() {
        return Collections.unmodifiableList(this.sonictronSounds);
    }

    @Override
    public String getSoundFor(Item item) {
        return StreamEx.of(this.sonictronSounds)
            .filter(sound -> item == sound.item().asItem())
            .map(SonictronSound::name)
            .findFirst()
            .orElse("block.note.harp");
    }

    @Override
    public void registerCondition(String type, Predicate<JsonNode> predicate) {
        this.conditions.put(type, predicate);
    }

    @Override
    public boolean testCondition(String type, JsonNode node) {
        Predicate<JsonNode> condition = this.conditions.get(type);
        if (condition == null) throw new IllegalArgumentException("Unknown condition type: " + type);

        return condition.test(node);
    }

    @Override
    public void addComputerCubeGuidePage(String translationKey, int length, List<ItemStack> stacks) {
//        ComputerCubeGuide.addPage(translationKey, length, stacks);
        throw new UnsupportedOperationException();
    }

    static void createAndInject() {
        IGregTechAPI api = new GregTechAPIImpl();
        JavaUtil.setStaticValue(GregTechAPI.class, "impl", api);
    }
}
