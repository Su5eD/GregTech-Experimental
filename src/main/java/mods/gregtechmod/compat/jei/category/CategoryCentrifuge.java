package mods.gregtechmod.compat.jei.category;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mods.gregtechmod.api.GregTechObjectAPI;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.jei.RecipeMaker;
import mods.gregtechmod.compat.jei.wrapper.WrapperCentrifuge;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.gui.GuiIndustrialCentrifuge;
import mods.gregtechmod.recipe.RecipeCentrifuge;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class CategoryCentrifuge implements IRecipeCategory<WrapperCentrifuge> {
    public static final String UID = Reference.MODID+"_industrial_centrifuge";
    private static final ResourceLocation GUI_PATH = new ResourceLocation(Reference.MODID, "textures/gui/industrial_centrifuge.png");
    private final IDrawable background;
    private final IDrawable gaugeUp;
    private final IDrawable gaugeDown;
    private final IDrawable gaugeRight;
    private final IDrawable gaugeLeft;
    private final IDrawable tank;

    public CategoryCentrifuge(IGuiHelper guiHelper) {
        background = guiHelper.drawableBuilder(GUI_PATH, 48, 4, 79, 79)
                .addPadding(0, 0, 48, 48)
                .build();

        IDrawableStatic gaugeUpStatic = guiHelper.createDrawable(GregTechMod.COMMON_TEXTURE, 20, 0, 10, 10);
        gaugeUp = guiHelper.createAnimatedDrawable(gaugeUpStatic, 200, IDrawableAnimated.StartDirection.BOTTOM, false);
        IDrawableStatic gaugeDownStatic = guiHelper.createDrawable(GregTechMod.COMMON_TEXTURE, 0, 0, 10, 10);
        gaugeDown = guiHelper.createAnimatedDrawable(gaugeDownStatic, 200, IDrawableAnimated.StartDirection.TOP, false);
        IDrawableStatic gaugeRightStatic = guiHelper.createDrawable(GregTechMod.COMMON_TEXTURE, 10, 0, 10, 10);
        gaugeRight = guiHelper.createAnimatedDrawable(gaugeRightStatic, 200, IDrawableAnimated.StartDirection.LEFT, false);
        IDrawableStatic gaugeLeftStatic = guiHelper.createDrawable(GregTechMod.COMMON_TEXTURE, 30, 0, 10, 10);
        gaugeLeft = guiHelper.createAnimatedDrawable(gaugeLeftStatic, 200, IDrawableAnimated.StartDirection.RIGHT, false);

        tank = guiHelper.createDrawable(GregTechMod.COMMON_TEXTURE, 40, 0, 18, 18);
    }

    public static void init(IModRegistry registry) {
        registry.handleRecipes(RecipeCentrifuge.class, WrapperCentrifuge::new, CategoryCentrifuge.UID);

        registry.addRecipes(RecipeMaker.getCentrifugeRecipes(), CategoryCentrifuge.UID);

        registry.addRecipeCatalyst(GregTechObjectAPI.getTileEntity("industrial_centrifuge"), CategoryCentrifuge.UID);

        registry.addRecipeClickArea(GuiIndustrialCentrifuge.class, 98, 38, 10, 10, CategoryCentrifuge.UID);
        registry.addRecipeClickArea(GuiIndustrialCentrifuge.class, 83, 23, 10, 10, CategoryCentrifuge.UID);
        registry.addRecipeClickArea(GuiIndustrialCentrifuge.class, 68, 38, 10, 10, CategoryCentrifuge.UID);
        registry.addRecipeClickArea(GuiIndustrialCentrifuge.class, 83, 53, 10, 10, CategoryCentrifuge.UID);
    }

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getTitle() {
        return "Industrial Centrifuge"; //TODO: Localization
    }

    @Override
    public String getModName() {
        return Reference.NAME;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, WrapperCentrifuge recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

        guiItemStacks.init(0, true, 49, 0);
        guiItemStacks.init(1, true, 79, 30);
        guiItemStacks.init(2, false, 79, 0);
        guiItemStacks.init(3, false, 109, 30);
        guiItemStacks.init(4, false, 79, 60);
        guiItemStacks.init(5, false, 49, 30);

        guiFluidStacks.init(6, true, 110, 60, 16, 16, 1, false, null);

        guiItemStacks.set(ingredients);
        guiFluidStacks.set(ingredients);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        gaugeUp.draw(minecraft, 83, 19);
        gaugeDown.draw(minecraft, 83, 49);
        gaugeRight.draw(minecraft, 98, 34);
        gaugeLeft.draw(minecraft, 68, 34);

        tank.draw(minecraft, 109, 59);
    }
}
