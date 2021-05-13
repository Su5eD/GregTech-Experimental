package mods.gregtechmod.compat.jei;

import ic2.core.util.StackUtil;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class JEIUtils {
    
    public static void drawInfo(Minecraft minecraft, IMachineRecipe<?, ?> recipe, boolean showEnergyCost) {
        int duration = recipe.getDuration();
        double energyCost = recipe.getEnergyCost();
        minecraft.fontRenderer.drawString(GtUtil.translate("jei.energy", GtUtil.formatNumber(duration * energyCost)), 2, 60, -16777216, false);
        minecraft.fontRenderer.drawString(GtUtil.translate("jei.time", GtUtil.formatNumber(duration / 20)), 2,70, -16777216, false);
        if (showEnergyCost) minecraft.fontRenderer.drawString(GtUtil.translate("jei.max_energy", GtUtil.formatNumber(energyCost)), 2,80, -16777216, false);
    }
    
    public static List<List<ItemStack>> getMultiInputs(IMachineRecipe<List<IRecipeIngredient>, ?> recipe) {
        return recipe.getInput().stream()
                .map(input -> {
                    int count = input.getCount();
                    return input.getMatchingInputs().stream()
                            .map(stack -> StackUtil.copyWithSize(stack, count))
                            .collect(Collectors.toList());
                })
                .collect(Collectors.toList());
    }
}
