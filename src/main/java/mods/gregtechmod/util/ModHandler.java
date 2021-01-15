package mods.gregtechmod.util;

import appeng.api.AEApi;
import appeng.api.features.IGrinderRecipe;
import appeng.api.features.IGrinderRegistry;
import cofh.thermalexpansion.util.managers.machine.PulverizerManager;
import cofh.thermalexpansion.util.managers.machine.SawmillManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipe;
import ic2.core.recipe.BasicMachineRecipeManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class ModHandler {

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

    public static ItemStack getRCItem(String baseItem, int meta, ItemStack alternative) {
        ItemStack stack = getRCItem(baseItem, meta);
        if (stack.isEmpty()) return alternative;
        return stack;
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

    public static void addTESawmillRecipe(int energy, ItemStack input, ItemStack output) {
        addTESawmillRecipe(energy, input, output, ItemStack.EMPTY, 0);
    }

    public static void addTESawmillRecipe(int energy, ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, int chance) {
        if (Loader.isModLoaded("thermalexpansion")) registerSawmillRecipe(energy, input, primaryOutput, secondaryOutput, chance);
    }

    @Optional.Method(modid = "thermalexpansion")
    private static void registerSawmillRecipe(int energy, ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, int chance) {
        SawmillManager.addRecipe(energy, input, primaryOutput, secondaryOutput, chance);
    }

    public static void addTEPulverizerRecipe(int energy, ItemStack input, ItemStack output) {
        addTEPulverizerRecipe(energy, input, output, ItemStack.EMPTY, 0);
    }

    public static void addTEPulverizerRecipe(int energy, ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, int chance) {
        if (Loader.isModLoaded("thermalexpansion")) registerPulverizerRecipe(energy, input, primaryOutput, secondaryOutput, chance);
    }

    @Optional.Method(modid = "thermalexpansion")
    private static void registerPulverizerRecipe(int energy, ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, int chance) {
        PulverizerManager.addRecipe(energy, input, primaryOutput, secondaryOutput, chance);
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

    public static void addCraftingRecipe(ItemStack stack, Object... args) {

    }
}