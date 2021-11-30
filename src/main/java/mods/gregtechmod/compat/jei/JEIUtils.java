package mods.gregtechmod.compat.jei;

import ic2.core.gui.Gauge;
import ic2.core.util.StackUtil;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.gui.GuiColors;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.JavaUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class JEIUtils {
    
    public static void drawInfo(Minecraft minecraft, IMachineRecipe<?, ?> recipe, boolean showEnergyCost) {
        drawInfo(minecraft, recipe, 0, showEnergyCost);
    }
    public static void drawInfo(Minecraft minecraft, IMachineRecipe<?, ?> recipe, int yOffset, boolean showEnergyCost) {
        int duration = recipe.getDuration();
        double energyCost = recipe.getEnergyCost();
        minecraft.fontRenderer.drawString(GtLocale.translate("jei.energy", JavaUtil.formatNumber(duration * energyCost)), 2, yOffset + 60, GuiColors.BLACK, false);
        minecraft.fontRenderer.drawString(GtLocale.translate("jei.time", JavaUtil.formatNumber(duration / 20)), 2,yOffset + 70, GuiColors.BLACK, false);
        if (showEnergyCost) minecraft.fontRenderer.drawString(GtLocale.translate("jei.max_energy", JavaUtil.formatNumber(energyCost)), 2,yOffset + 80, GuiColors.BLACK, false);
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
    
    public static IDrawable gaugeToDrawable(IGuiHelper guiHelper, Gauge.IGaugeStyle gauge) {
        Gauge.GaugeProperties props = gauge.getProperties();
        IDrawableStatic gaugeStatic = guiHelper.createDrawable(props.texture, props.uInner, props.vInner, props.innerWidth, props.innerHeight);
        IDrawableAnimated.StartDirection direction = getDirection(props.vertical, props.reverse);
        
        return guiHelper.createAnimatedDrawable(gaugeStatic, 200, direction, false);
    }
    
    private static IDrawableAnimated.StartDirection getDirection(boolean vertical, boolean reverse) {
        if (vertical) {
            return reverse ? IDrawableAnimated.StartDirection.BOTTOM : IDrawableAnimated.StartDirection.TOP;
        }
        
        return reverse ? IDrawableAnimated.StartDirection.RIGHT : IDrawableAnimated.StartDirection.LEFT;
    }
}
