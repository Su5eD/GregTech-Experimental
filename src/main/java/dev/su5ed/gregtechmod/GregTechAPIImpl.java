package dev.su5ed.gregtechmod;

import com.fasterxml.jackson.databind.JsonNode;
import dev.su5ed.gregtechmod.api.GregTechAPI;
import dev.su5ed.gregtechmod.api.IGregTechAPI;
import dev.su5ed.gregtechmod.api.item.TurbineRotor;
import dev.su5ed.gregtechmod.api.util.SonictronSound;
import dev.su5ed.gregtechmod.util.JavaUtil;
import ic2.core.util.StackUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import one.util.streamex.StreamEx;

import java.util.*;
import java.util.function.Predicate;

final class GregTechAPIImpl implements IGregTechAPI {
    private final List<SonictronSound> sonictronSounds = new ArrayList<>();
    private final Map<String, Predicate<JsonNode>> conditions = new HashMap<>();

    private final Map<Item, TurbineRotor> turbineRotors = new HashMap<>();

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
    public String getSoundFor(ItemStack stack) {
        return StreamEx.of(this.sonictronSounds)
            .filter(sound -> StackUtil.checkItemEquality(stack, sound.getItem()))
            .map(SonictronSound::getName)
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
    public <T extends Item & TurbineRotor> void registerTurbineRotor(T rotor) {
        this.turbineRotors.put(rotor, rotor);
    }

    @Override
    public Optional<TurbineRotor> getTurbineRotor(Item item) {
        return Optional.ofNullable(this.turbineRotors.get(item));
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
