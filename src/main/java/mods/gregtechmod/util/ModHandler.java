package mods.gregtechmod.util;

import ic2.api.item.IC2Items;
import ic2.api.recipe.IBasicMachineRecipeManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipe;
import ic2.core.recipe.BasicMachineRecipeManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Collection;

public class ModHandler {

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

    public static ItemStack getICItem(String baseItem, String type, int count) {
        ItemStack stack = IC2Items.getItem(baseItem, type);
        stack.setCount(count);
        return stack;
    }

    /*public static boolean addPulverisationRecipe(ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, int chance, boolean overwrite) {
        primaryOutput = OreDictUnificator.get(primaryOutput);
        secondaryOutput = OreDictUnificator.get(secondaryOutput);

        removeSimpleIC2MachineRecipe(input, ItemStack.EMPTY, Recipes.macerator);
        if (!input.getItem().hasContainerItem(input)) {
            if (input.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
                for (int i = 0; i < 16; i++) {
                    ItemStack metaInput = input.copy();
                    metaInput.setItemDamage(i);
                    removeSimpleIC2MachineRecipe(metaInput, ItemStack.EMPTY, Recipes.macerator);
                    Recipes.macerator.addRecipe(Recipes.inputFactory.forStack(metaInput), null, true, primaryOutput);
                }
            } else {
                Recipes.macerator.addRecipe(Recipes.inputFactory.forStack(input), null, true, primaryOutput);
            }
        }
        try {
            sPulverizerRecipes.put(Integer.valueOf(GT_Utility.stackToInt(input)), new GT_PulverizerRecipe(input, primaryOutput, secondaryOutput, (chance <= 0) ? 10 : chance));
        } catch (Throwable e) {}
        if (!GT_OreDictUnificator.isItemStackInstanceOf(primaryOutput, "dustWood", false) && !GT_OreDictUnificator.isItemStackInstanceOf(primaryOutput, "dustSmallWood", false)) {
            try {
                if (!input.getItem().hasContainerItem() &&
                        GT_OreDictUnificator.isItemStackInstanceOf(input, "ingot", true))
                    Util.getGrinderRecipeManage().addRecipe(input, primaryOutput, 5);
            } catch (Throwable e) {}
            try {
                if (!input.getItem().hasContainerItem() &&
                        input.itemID != Block.obsidian.blockID) {
                    IRockCrusherRecipe tRecipe = RailcraftCraftingManager.rockCrusher.createNewRecipe(input.copy().splitStack(1), (input.getItemDamage() != 32767), false);
                    tRecipe.addOutput(primaryOutput.copy(), 1.0F / input.stackSize);
                    tRecipe.addOutput(secondaryOutput.copy(), 0.01F * ((chance <= 0) ? 10 : chance) / input.stackSize);
                }
            } catch (Throwable e) {}
            try {
                if (!input.getItem().hasContainerItem())
                    if (secondaryOutput == null) {
                        CraftingManagers.pulverizerManager.addRecipe(400, input.copy(), primaryOutput.copy(), overwrite);
                    } else {
                        CraftingManagers.pulverizerManager.addRecipe(400, input.copy(), primaryOutput.copy(), secondaryOutput.copy(), (chance <= 0) ? 10 : chance, overwrite);
                    }
            } catch (Throwable e) {}
        } else {
            try {
                if (!input.getItem().hasContainerItem())
                    if (secondaryOutput == null) {
                        CraftingManagers.sawmillManager.addRecipe(80, input.copy(), primaryOutput.copy(), overwrite);
                    } else {
                        CraftingManagers.sawmillManager.addRecipe(80, input.copy(), primaryOutput.copy(), secondaryOutput.copy(), (chance <= 0) ? 10 : chance, overwrite);
                    }
            } catch (Throwable e) {}
        }
        return true;
    }*/

    public static boolean removeSimpleIC2MachineRecipe(ItemStack input, ItemStack output, IBasicMachineRecipeManager manager) {
        if ((input.isEmpty() && output.isEmpty()) || manager == null) return false;

        for (MachineRecipe<IRecipeInput, Collection<ItemStack>> recipe : manager.getRecipes()) {
            ItemStack recipeInput = recipe.getInput().getInputs().get(0);
            Collection<ItemStack> recipeOutput = recipe.getOutput();
            if ((input.isEmpty() || recipeInput.isItemEqual(input)) && (output.isEmpty() || recipeOutput.toArray(new ItemStack[0])[0].isItemEqual(output))) {
                ((BasicMachineRecipeManager) manager).removeRecipe(recipeInput, recipeOutput);
                removeSimpleIC2MachineRecipe(input, output, manager);
                return true;
            }
        }
        return false;
    }
}