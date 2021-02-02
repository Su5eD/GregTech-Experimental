package mods.gregtechmod.util;

import appeng.api.AEApi;
import appeng.api.features.IGrinderRecipe;
import appeng.api.features.IGrinderRegistry;
import cofh.thermalexpansion.util.managers.machine.PulverizerManager;
import cofh.thermalexpansion.util.managers.machine.SawmillManager;
import cofh.thermalexpansion.util.managers.machine.SmelterManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipe;
import ic2.core.recipe.BasicMachineRecipeManager;
import ic2.core.util.StackUtil;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ModHandler {
    public static ItemStack SLAG = ItemStack.EMPTY;
    public static ItemStack SLAG_RICH = ItemStack.EMPTY;

    public static void gatherModItems() {
        Item material = ForgeRegistries.ITEMS.getValue(new ResourceLocation("thermalfoundation", "material"));
        if (material != null) {
            SLAG = new ItemStack(material, 1, 864);
            SLAG_RICH = new ItemStack(material, 1, 865);
        }
    }

    public static ItemStack getModItem(String modid, String itemName) {
        return getModItem(modid, itemName, 0);
    }

    public static ItemStack getModItem(String modid, String itemName, int meta) {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, itemName));
        if (item == null) return ItemStack.EMPTY;
        return new ItemStack(item, 1, meta);
    }

    public static ItemStack getTEItem(String baseItem, int meta) {
        Item base = ForgeRegistries.ITEMS.getValue(new ResourceLocation("thermalexpansion", baseItem));
        if (base == null) return ItemStack.EMPTY;
        return new ItemStack(base, 1, meta);
    }

    public static ItemStack getTFItem(String baseItem, int meta) {
        Item base = ForgeRegistries.ITEMS.getValue(new ResourceLocation("thermalfoundation", baseItem));
        if (base == null) return ItemStack.EMPTY;
        return new ItemStack(base, 1, meta);
    }

    public static ItemStack getRCItem(String baseItem, int meta) {
        Item base = ForgeRegistries.ITEMS.getValue(new ResourceLocation("railcraft", baseItem));
        if (base == null) return ItemStack.EMPTY;
        return new ItemStack(base, 1, meta);
    }

    public static ItemStack getPRItem(String baseItem, int meta) {
        Item base = ForgeRegistries.ITEMS.getValue(new ResourceLocation("projectred-core", baseItem));
        if (base == null) return ItemStack.EMPTY;
        return new ItemStack(base, 1, meta);
    }

    public static ItemStack getFRItem(String baseItem) {
        Item base = ForgeRegistries.ITEMS.getValue(new ResourceLocation("forestry", baseItem));
        if (base == null) return ItemStack.EMPTY;
        return new ItemStack(base);
    }

    public static ItemStack getTCItem(String baseItem, int meta) {
        Item base = ForgeRegistries.ITEMS.getValue(new ResourceLocation("thaumcraft", baseItem));
        if (base == null) return ItemStack.EMPTY;
        return new ItemStack(base, 1, meta);
    }

    public static void addTESawmillRecipe(int energy, ItemStack input, ItemStack output, boolean overwrite) {
        addTESawmillRecipe(energy, input, output, ItemStack.EMPTY, 0, overwrite);
    }

    public static void addTESawmillRecipe(int energy, ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, int chance, boolean overwrite) {
        if (Loader.isModLoaded("thermalexpansion")) registerSawmillRecipe(energy, input, primaryOutput, secondaryOutput, chance, overwrite);
    }

    @Optional.Method(modid = "thermalexpansion")
    private static void registerSawmillRecipe(int energy, ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, int chance, boolean overwrite) {
        if (overwrite && SawmillManager.recipeExists(input)) SawmillManager.removeRecipe(input);
        SawmillManager.addRecipe(energy, input, primaryOutput, secondaryOutput, chance);
    }

    public static void addTEPulverizerRecipe(int energy, ItemStack input, ItemStack output, boolean overwrite) {
        addTEPulverizerRecipe(energy, input, output, ItemStack.EMPTY, 0, overwrite);
    }

    public static void addTEPulverizerRecipe(int energy, ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, int chance, boolean overwrite) {
        if (Loader.isModLoaded("thermalexpansion")) registerPulverizerRecipe(energy, input, primaryOutput, secondaryOutput, chance, overwrite);
    }

    @Optional.Method(modid = "thermalexpansion")
    private static void registerPulverizerRecipe(int energy, ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, int chance, boolean overwrite) {
        if (overwrite && PulverizerManager.recipeExists(input)) PulverizerManager.removeRecipe(input);
        PulverizerManager.addRecipe(energy, input, primaryOutput, secondaryOutput, chance);
    }

    public static void addInductionSmelterRecipe(ItemStack primaryInput, ItemStack secondaryInput, ItemStack primaryOutput, ItemStack secondaryOutput, int energy, int chance) {
        if (Loader.isModLoaded("thermalexpansion")) registerInductionsmelterRecipe(primaryInput, secondaryInput, primaryOutput, secondaryOutput, energy, chance);
    }

    @Optional.Method(modid = "thermalexpansion")
    private static void registerInductionsmelterRecipe(ItemStack primaryInput, ItemStack secondaryInput, ItemStack primaryOutput, ItemStack secondaryOutput, int energy, int chance) {
        SmelterManager.addRecipe(energy, primaryInput, secondaryInput, primaryOutput, secondaryOutput, chance);
    }

    public static void addSmelterOreToIngotsRecipe(String ore, Item output) {
        addSmelterOreToIngotsRecipe(ore, new ItemStack(output));
    }

    public static void addSmelterOreToIngotsRecipe(ItemStack input, Item output) {
        addSmelterOreToIngotsRecipe(input, new ItemStack(output));
    }

    public static void addSmelterOreToIngotsRecipe(List<String> ores, Item output) {
        ItemStack outputStack = new ItemStack(output);
        ores.forEach(ore -> addSmelterOreToIngotsRecipe(ore, outputStack));
    }

    public static void addSmelterOreToIngotsRecipe(String ore, ItemStack output) {
        OreDictionary.getOres(ore).forEach(stack -> addSmelterOreToIngotsRecipe(stack, output));
    }

    public static void addSmelterOreToIngotsRecipe(ItemStack input, ItemStack output) {
        ItemStack ore = StackUtil.copyWithSize(input, 1);
        ItemStack ingots2 = StackUtil.copyWithSize(output, 2);
        ItemStack ingots3 = StackUtil.copyWithSize(output, 3);

        addInductionSmelterRecipe(ore, new ItemStack(Blocks.SAND), ingots2, SLAG_RICH, 3200, 5);
        addInductionSmelterRecipe(ore, SLAG_RICH, ingots3, SLAG, 4000, 75);
        GameRegistry.addSmelting(input, output, 0);
    }

    public static void addRockCrusherRecipe(ItemStack input, boolean matchMeta, boolean matchNBT, Map<ItemStack, Float> outputs) {
        NBTTagCompound nbt = new NBTTagCompound();

        NBTTagCompound inputNBT = new NBTTagCompound();
        input.writeToNBT(inputNBT);
        nbt.setTag("input", inputNBT);

        nbt.setBoolean("matchMeta", matchMeta);
        nbt.setBoolean("matchNBT", matchNBT);

        int outCount = 0;
        for (Map.Entry<ItemStack, Float> output : outputs.entrySet()) {
            NBTTagCompound outputNBT = new NBTTagCompound();
            output.getKey().writeToNBT(outputNBT);
            outputNBT.setFloat("chance", output.getValue());
            nbt.setTag("output" + outCount++, outputNBT);
        }

        FMLInterModComms.sendMessage("Railcraft", "rock-crusher", nbt);
    }

    public static void addAEGrinderRecipe(ItemStack input, ItemStack output, int turns) {
        if (Loader.isModLoaded("appliedenergistics2")) registerAEGrinderRecipe(input, output, turns);
    }

    @Optional.Method(modid = "appliedenergistics2")
    private static void registerAEGrinderRecipe(ItemStack input, ItemStack output, int turns) {
        IGrinderRegistry registry = AEApi.instance().registries().grinder();
        IGrinderRecipe recipe = registry.builder()
                .withInput(input)
                .withOutput(output)
                .withTurns(turns)
                .build();
        registry.addRecipe(recipe);
    }

    public static void removeSimpleIC2MachineRecipe(ItemStack input, BasicMachineRecipeManager manager) {
        Iterator<? extends MachineRecipe<IRecipeInput, Collection<ItemStack>>> iterator = manager.getRecipes().iterator();
        while (iterator.hasNext()) {
            MachineRecipe<IRecipeInput, Collection<ItemStack>> recipe = iterator.next();
            if (recipe.getInput().matches(input)) {
                iterator.remove();
                return;
            }
        }
    }
}