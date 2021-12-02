package mods.gregtechmod.compat.jei.wrapper;

import mods.gregtechmod.api.recipe.IRecipeBlastFurnace;
import mods.gregtechmod.compat.jei.JEIUtils;
import mods.gregtechmod.gui.GuiColors;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.JavaUtil;
import net.minecraft.client.Minecraft;

public class WrapperIndustrialBlastFurnace extends WrapperMultiInput<IRecipeBlastFurnace> {

    public WrapperIndustrialBlastFurnace(IRecipeBlastFurnace recipe) {
        super(recipe);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        JEIUtils.drawInfo(minecraft, recipe, -10, true);
        minecraft.fontRenderer.drawString(GtLocale.translateInfo("heat_capacity", JavaUtil.formatNumber(recipe.getHeat())), 2,80, GuiColors.BLACK, false);
    }
}
