package mods.gregtechmod.compat.jei.category;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mods.gregtechmod.api.GregTechObjectAPI;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerBasic;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.jei.RecipeMaker;
import mods.gregtechmod.compat.jei.wrapper.WrapperBasicMachine;
import mods.gregtechmod.gui.GuiBasicMachine;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class CategoryBasicMachine implements IRecipeCategory<WrapperBasicMachine> {
    private final String name;
    private final Class<? extends IMachineRecipe<IRecipeIngredient, List<ItemStack>>> recipeClass;
    private final Class<? extends GuiBasicMachine<?>> guiClass;
    private final IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, List<ItemStack>>> recipeManager;
    private final String uid;
    private final IDrawable background;
    private final IDrawable gauge;

    public CategoryBasicMachine(String name, Class<? extends IMachineRecipe<IRecipeIngredient, List<ItemStack>>> recipeClass, Class<? extends GuiBasicMachine<?>> guiClass,
                                IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, List<ItemStack>>> recipeManager, IGuiHelper guiHelper) {
        this.name = name;
        this.recipeClass = recipeClass;
        this.guiClass = guiClass;
        this.recipeManager = recipeManager;
        this.uid = Reference.MODID+"."+name;
        ResourceLocation guiTexture = new ResourceLocation(Reference.MODID, "textures/gui/jei/" + name + ".png");
        this.background = guiHelper.drawableBuilder(guiTexture, 52, 24, 72, 18)
                .addPadding(24, 44, 52, 52)
                .build();
        IDrawableStatic gaugeStatic = guiHelper.createDrawable(guiTexture, 176, 0, 20, 18);
        this.gauge = guiHelper.createAnimatedDrawable(gaugeStatic, 200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    public void init(IModRegistry registry) {
        registry.handleRecipes(this.recipeClass, WrapperBasicMachine::new, this.uid);

        registry.addRecipes(RecipeMaker.getBasicMachineRecipes(this.recipeManager), this.uid);

        registry.addRecipeCatalyst(GregTechObjectAPI.getTileEntity(this.name), this.uid);

        registry.addRecipeClickArea(this.guiClass, 78, 24, 18, 18, this.uid);
    }

    @Override
    public String getUid() {
        return this.uid;
    }

    @Override
    public String getTitle() {
        return GtUtil.translate("teblock."+this.name);
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
    public void setRecipe(IRecipeLayout recipeLayout, WrapperBasicMachine recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(0, true, 52, 24);
        guiItemStacks.init(1, false, 106, 24);

        guiItemStacks.set(ingredients);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        this.gauge.draw(minecraft, 78, 24);
    }
}
