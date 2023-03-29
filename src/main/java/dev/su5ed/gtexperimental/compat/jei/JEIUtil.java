package dev.su5ed.gtexperimental.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.su5ed.gtexperimental.api.recipe.BaseRecipe;
import dev.su5ed.gtexperimental.recipe.type.ModRecipeProperty;
import dev.su5ed.gtexperimental.screen.RecipeProgressBar;
import dev.su5ed.gtexperimental.screen.ScreenColors;
import dev.su5ed.gtexperimental.util.GtLocale;
import dev.su5ed.gtexperimental.util.JavaUtil;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.client.Minecraft;

public final class JEIUtil {
    public static void drawInfo(Minecraft minecraft, PoseStack poseStack, BaseRecipe<?, ?, ?, ?> recipe, int yOffset, boolean showEnergyCost) {
        int duration = recipe.getProperty(ModRecipeProperty.DURATION);
        double energyCost = recipe.getProperty(ModRecipeProperty.ENERGY_COST);

        minecraft.font.draw(poseStack, GtLocale.key("jei", "energy").toComponent(JavaUtil.formatNumber(duration * energyCost)), 2, yOffset + 60, ScreenColors.BLACK);
        minecraft.font.draw(poseStack, GtLocale.key("jei", "time").toComponent(JavaUtil.formatNumber(duration / 20)), 2, yOffset + 70, ScreenColors.BLACK);
        if (showEnergyCost) {
            minecraft.font.draw(poseStack, GtLocale.key("jei", "max_energy").toComponent(JavaUtil.formatNumber(energyCost)), 2, yOffset + 80, ScreenColors.BLACK);
        }
    }

    public static IDrawable gaugeToDrawable(IGuiHelper guiHelper, RecipeProgressBar progressBar) {
        IDrawableStatic gaugeStatic = guiHelper.createDrawable(progressBar.texture, progressBar.x, progressBar.y, progressBar.width, progressBar.height);
        IDrawableAnimated.StartDirection direction = switch (progressBar.direction) {
            case DOWN -> IDrawableAnimated.StartDirection.TOP;
            case UP -> IDrawableAnimated.StartDirection.BOTTOM;
            case LEFT -> IDrawableAnimated.StartDirection.RIGHT;
            case RIGHT -> IDrawableAnimated.StartDirection.LEFT;
        };

        return guiHelper.createAnimatedDrawable(gaugeStatic, 200, direction, false);
    }

    private JEIUtil() {}
}
