package mods.gregtechmod.core;

import com.fasterxml.jackson.databind.JsonNode;
import ic2.core.util.StackUtil;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.IGregTechAPI;
import mods.gregtechmod.api.util.SonictronSound;
import mods.gregtechmod.api.util.TurbineRotor;
import mods.gregtechmod.objects.blocks.teblocks.computercube.ComputerCubeGuide;
import mods.gregtechmod.util.JavaUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;
import java.util.function.Predicate;

final class GregTechAPIImpl implements IGregTechAPI {
    private final List<SonictronSound> sonictronSounds = new ArrayList<>();
    private final Map<String, Predicate<JsonNode>> conditions = new HashMap<>();

    private final Set<ItemStack> jackHammerMinableBlocks = new HashSet<>();
    private final Set<TurbineRotor> turbineRotors = new HashSet<>();

    private final Set<ItemStack> wrenches = new HashSet<>();
    private final Set<ItemStack> screwdrivers = new HashSet<>();
    private final Set<ItemStack> softHammers = new HashSet<>();
    private final Set<ItemStack> hardHammers = new HashSet<>();
    private final Set<ItemStack> crowbars = new HashSet<>();

    @Override
    public void addJackHammerMinableBlock(ItemStack stack) {
        jackHammerMinableBlocks.add(stack);
    }

    @Override
    public void addJackHammerMinableBlocks(Collection<ItemStack> stacks) {
        jackHammerMinableBlocks.addAll(stacks);
    }

    @Override
    public Collection<ItemStack> getJackHammerMinableBlocks() {
        return Collections.unmodifiableSet(jackHammerMinableBlocks);
    }

    @Override
    public void registerSonictronSound(SonictronSound sound) {
        sonictronSounds.add(sound);
    }

    @Override
    public void registerSonictronSounds(Collection<SonictronSound> sounds) {
        sonictronSounds.addAll(sounds);
    }

    @Override
    public List<SonictronSound> getSonictronSounds() {
        return Collections.unmodifiableList(sonictronSounds);
    }

    @Override
    public void registerCondition(String type, Predicate<JsonNode> predicate) {
        conditions.put(type, predicate);
    }

    @Override
    public boolean testCondition(String type, JsonNode node) {
        Predicate<JsonNode> condition = conditions.get(type);
        if (condition == null) throw new IllegalArgumentException("Unknown condition type: " + type);

        return condition.test(node);
    }

    @Override
    public void registerTurbineRotor(ItemStack stack, int efficiency, int efficiencyMultiplier, int damageToComponent) {
        TurbineRotor rotor = new TurbineRotor(stack, efficiency, efficiencyMultiplier, damageToComponent);
        turbineRotors.add(rotor);
    }

    @Override
    public Optional<TurbineRotor> getTurbineRotor(ItemStack stack) {
        return turbineRotors.stream()
            .filter(rotor -> StackUtil.checkItemEquality(stack, rotor.item.getItem()))
            .findFirst();
    }

    @Override
    public void addComputerCubeGuidePage(String translationKey, int length, List<ItemStack> stacks) {
        ComputerCubeGuide.addPage(translationKey, length, stacks);
    }

    @Override
    public void registerWrench(Item item) {
        registerTool(item, wrenches);
    }

    @Override
    public void registerWrench(ItemStack stack) {
        registerTool(stack, wrenches);
    }

    @Override
    public Collection<ItemStack> getWrenches() {
        return Collections.unmodifiableSet(wrenches);
    }

    @Override
    public void registerScrewdriver(Item item) {
        registerTool(item, screwdrivers);
    }

    @Override
    public void registerScrewdriver(ItemStack stack) {
        registerTool(stack, screwdrivers);
    }

    @Override
    public Collection<ItemStack> getScrewdrivers() {
        return Collections.unmodifiableSet(screwdrivers);
    }

    @Override
    public void registerSoftHammer(Item item) {
        registerTool(item, softHammers);
    }

    @Override
    public void registerSoftHammer(ItemStack stack) {
        registerTool(stack, softHammers);
    }

    @Override
    public Collection<ItemStack> getSoftHammers() {
        return Collections.unmodifiableSet(softHammers);
    }

    @Override
    public void registerHardHammer(Item item) {
        registerTool(item, hardHammers);
    }

    @Override
    public void registerHardHammer(ItemStack stack) {
        registerTool(stack, hardHammers);
    }

    @Override
    public Collection<ItemStack> getHardHammers() {
        return Collections.unmodifiableSet(hardHammers);
    }

    @Override
    public void registerCrowbar(Item item) {
        registerTool(item, crowbars);
    }

    @Override
    public void registerCrowbar(ItemStack stack) {
        registerTool(stack, crowbars);
    }

    @Override
    public Collection<ItemStack> getCrowbars() {
        return Collections.unmodifiableSet(crowbars);
    }

    private static void registerTool(Item item, Set<ItemStack> registry) {
        registerTool(new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE), registry);
    }

    private static void registerTool(ItemStack stack, Set<ItemStack> registry) {
        if (!stack.isEmpty()) registry.add(stack);
    }

    static void createAndInject() {
        IGregTechAPI api = new GregTechAPIImpl();
        JavaUtil.setStaticValue(GregTechAPI.class, "impl", api);
    }
}
