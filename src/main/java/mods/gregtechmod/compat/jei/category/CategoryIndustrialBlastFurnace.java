package mods.gregtechmod.compat.jei.category;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mods.gregtechmod.api.GregTechObjectAPI;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.jei.factory.RecipeWrapperFactory;
import mods.gregtechmod.compat.jei.wrapper.WrapperIndustrialBlastFurnace;
import mods.gregtechmod.compat.jei.wrapper.WrapperMultiInput;
import mods.gregtechmod.gui.GuiIndustrialBlastFurnace;
import mods.gregtechmod.recipe.RecipeBlastFurnace;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class CategoryIndustrialBlastFurnace implements IRecipeCategory<WrapperMultiInput> {
    public static final String UID = Reference.MODID + ".industrial_blast_furnace";
    private static final ResourceLocation GUI_PATH = new ResourceLocation(Reference.MODID, "textures/gui/industrial_blast_furnace.png");
    
    private final IDrawable background;
    private final IDrawable gauge;
    
    public CategoryIndustrialBlastFurnace(IGuiHelper guiHelper) {
        background = guiHelper.drawableBuilder(GUI_PATH, 33, 15, 88, 39)
                        .addPadding(7, 43, 43, 43)
                        .build();
        
        IDrawableStatic gaugeStatic = guiHelper.createDrawable(GuiIndustrialBlastFurnace.TEXTURE, 176, 0, 20, 11);
        gauge = guiHelper.createAnimatedDrawable(gaugeStatic, 200, IDrawableAnimated.StartDirection.LEFT, false);
    }
    
    public void init(IModRegistry registry) {
        registry.handleRecipes(RecipeBlastFurnace.class, WrapperIndustrialBlastFurnace::new, CategoryIndustrialBlastFurnace.UID);
    
        registry.addRecipes(RecipeWrapperFactory.getMultiRecipes(GtRecipes.blastFurnace, WrapperIndustrialBlastFurnace::new), CategoryIndustrialBlastFurnace.UID);
    
        registry.addRecipeCatalyst(GregTechObjectAPI.getTileEntity("industrial_blast_furnace"), CategoryIndustrialBlastFurnace.UID);
    
        registry.addRecipeClickArea(GuiIndustrialBlastFurnace.class, 58, 28, 20, 11, CategoryIndustrialBlastFurnace.UID);
    }

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getTitle() {
        return GtUtil.translate("teblock.industrial_blast_furnace");
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
    public void setRecipe(IRecipeLayout recipeLayout, WrapperMultiInput recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        
        guiItemStacks.init(0, true, 43, 7);
        guiItemStacks.init(1, true, 43, 25);
        guiItemStacks.init(2, false, 95, 16);
        guiItemStacks.init(3, false, 113, 16);
        
        guiItemStacks.set(ingredients);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        gauge.draw(minecraft, 68, 20);
    }
}
