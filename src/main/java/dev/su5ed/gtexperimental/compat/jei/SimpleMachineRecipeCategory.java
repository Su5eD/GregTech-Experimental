package dev.su5ed.gtexperimental.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.su5ed.gtexperimental.api.recipe.BaseRecipe;
import dev.su5ed.gtexperimental.api.recipe.ListRecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.MISORecipe;
import dev.su5ed.gtexperimental.recipe.SISORecipe;
import dev.su5ed.gtexperimental.screen.RecipeProgressBar;
import dev.su5ed.gtexperimental.util.BlockEntityProvider;
import dev.su5ed.gtexperimental.util.ItemProvider;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import static dev.su5ed.gtexperimental.util.GtUtil.location;

public abstract class SimpleMachineRecipeCategory<T extends BaseRecipe<?, ?, ?, ?, ?>> extends BaseRecipeCategory<T> {
    private final ResourceLocation backgroundTexture;
    private final int yOffset;
    private final IDrawable gauge;

    public <U extends BlockEntityProvider & ItemProvider> SimpleMachineRecipeCategory(U provider, String name, RecipeType<T> recipeType, IGuiHelper guiHelper, RecipeProgressBar progressBar, int yOffset, boolean customTexture) {
        super(provider, guiHelper, recipeType);

        this.backgroundTexture = location("textures/gui%s/%s.png".formatted(customTexture ? "/jei" : "", name));
        this.yOffset = yOffset;
        this.gauge = JEIUtil.gaugeToDrawable(guiHelper, progressBar);
    }

    @Override
    protected IDrawable drawBackground(IGuiHelper guiHelper) {
        return guiHelper.drawableBuilder(this.backgroundTexture, 34, 24, 108, 18)
            .addPadding(24, 44, 34, 34)
            .build();
    }

    @Override
    public void draw(T recipe, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY) {
        this.gauge.draw(poseStack, 78, 24);

        JEIUtil.drawInfo(Minecraft.getInstance(), poseStack, recipe, this.yOffset, true);
    }

    public static class SISO extends SimpleMachineRecipeCategory<SISORecipe<ItemStack, ItemStack>> {
        public <U extends BlockEntityProvider & ItemProvider> SISO(U provider, String name, RecipeType<SISORecipe<ItemStack, ItemStack>> recipeType, IGuiHelper guiHelper, RecipeProgressBar progressBar, int yOffset, boolean customTexture) {
            super(provider, name, recipeType, guiHelper, progressBar, yOffset, customTexture);
        }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, SISORecipe<ItemStack, ItemStack> recipe, IFocusGroup focuses) {
            builder.addSlot(RecipeIngredientRole.INPUT, 53, 25)
                .addIngredients(recipe.getInput().asIngredient());
            builder.addSlot(RecipeIngredientRole.OUTPUT, 107, 25)
                .addItemStack(recipe.getOutput());
        }
    }

    public static class MISO extends SimpleMachineRecipeCategory<MISORecipe<ItemStack, ItemStack>> {
        public <U extends BlockEntityProvider & ItemProvider> MISO(U provider, String name, RecipeType<MISORecipe<ItemStack, ItemStack>> recipeType, IGuiHelper guiHelper, RecipeProgressBar progressBar, int yOffset, boolean customTexture) {
            super(provider, name, recipeType, guiHelper, progressBar, yOffset, customTexture);
        }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, MISORecipe<ItemStack, ItemStack> recipe, IFocusGroup focuses) {
            ListRecipeIngredient<ItemStack> input = recipe.getInput();
            for (int i = 0; i < input.size(); i++) {
                RecipeIngredient<ItemStack> ingredient = input.get(i);
                builder.addSlot(RecipeIngredientRole.INPUT, 35 + i * 18, 25)
                    .addIngredients(ingredient.asIngredient());
            }
            builder.addSlot(RecipeIngredientRole.OUTPUT, 107, 25)
                .addItemStack(recipe.getOutput());
        }
    }
}
