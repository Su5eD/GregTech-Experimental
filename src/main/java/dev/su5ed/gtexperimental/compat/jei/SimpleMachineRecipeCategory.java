package dev.su5ed.gtexperimental.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.su5ed.gtexperimental.api.recipe.BaseRecipe;
import dev.su5ed.gtexperimental.api.recipe.ListRecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.MIMORecipe;
import dev.su5ed.gtexperimental.recipe.MISORecipe;
import dev.su5ed.gtexperimental.recipe.SIMORecipe;
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

import java.util.List;
import java.util.function.BiConsumer;

import static dev.su5ed.gtexperimental.util.GtUtil.location;

public class SimpleMachineRecipeCategory<T extends BaseRecipe<?, ?, ?, ?, ?>> extends BaseRecipeCategory<T> {
    private final ResourceLocation backgroundTexture;
    private final int yOffset;
    private final IDrawable gauge;
    private final BiConsumer<IRecipeLayoutBuilder, T> inputSetter;
    private final BiConsumer<IRecipeLayoutBuilder, T> outputSetter;

    public static <U extends BlockEntityProvider & ItemProvider> SimpleMachineRecipeCategory<SISORecipe<ItemStack, ItemStack>> createSISO(U provider, String name, RecipeType<SISORecipe<ItemStack, ItemStack>> recipeType, IGuiHelper guiHelper, RecipeProgressBar progressBar, int yOffset, boolean customTexture) {
        return new SimpleMachineRecipeCategory<>(provider, name, recipeType, guiHelper, progressBar, yOffset, customTexture, SimpleMachineRecipeCategory::setSingleInput, SimpleMachineRecipeCategory::setSingleOutput);
    }

    public static <U extends BlockEntityProvider & ItemProvider> SimpleMachineRecipeCategory<SIMORecipe<ItemStack, List<ItemStack>>> createSIMO(U provider, String name, RecipeType<SIMORecipe<ItemStack, List<ItemStack>>> recipeType, IGuiHelper guiHelper, RecipeProgressBar progressBar, int yOffset, boolean customTexture) {
        return new SimpleMachineRecipeCategory<>(provider, name, recipeType, guiHelper, progressBar, yOffset, customTexture, SimpleMachineRecipeCategory::setSingleInput, SimpleMachineRecipeCategory::setMultiOutput);
    }

    public static <U extends BlockEntityProvider & ItemProvider> SimpleMachineRecipeCategory<MIMORecipe> createMIMO(U provider, String name, RecipeType<MIMORecipe> recipeType, IGuiHelper guiHelper, RecipeProgressBar progressBar, int yOffset, boolean customTexture) {
        return new SimpleMachineRecipeCategory<>(provider, name, recipeType, guiHelper, progressBar, yOffset, customTexture, SimpleMachineRecipeCategory::setMultiInput, SimpleMachineRecipeCategory::setMultiOutput);
    }

    public static <U extends BlockEntityProvider & ItemProvider> SimpleMachineRecipeCategory<MISORecipe<ItemStack, ItemStack>> createMISO(U provider, String name, RecipeType<MISORecipe<ItemStack, ItemStack>> recipeType, IGuiHelper guiHelper, RecipeProgressBar progressBar, int yOffset, boolean customTexture) {
        return new SimpleMachineRecipeCategory<>(provider, name, recipeType, guiHelper, progressBar, yOffset, customTexture, SimpleMachineRecipeCategory::setMultiInput, SimpleMachineRecipeCategory::setSingleOutput);
    }

    public <U extends BlockEntityProvider & ItemProvider> SimpleMachineRecipeCategory(U provider, String name, RecipeType<T> recipeType, IGuiHelper guiHelper, RecipeProgressBar progressBar, int yOffset, boolean customTexture, BiConsumer<IRecipeLayoutBuilder, T> inputSetter, BiConsumer<IRecipeLayoutBuilder, T> outputSetter) {
        super(provider, guiHelper, recipeType);

        this.backgroundTexture = location("textures/gui%s/%s.png".formatted(customTexture ? "/jei" : "", name));
        this.yOffset = yOffset;
        this.gauge = JEIUtil.gaugeToDrawable(guiHelper, progressBar);
        this.inputSetter = inputSetter;
        this.outputSetter = outputSetter;
    }

    @Override
    protected IDrawable drawBackground(IGuiHelper guiHelper) {
        return guiHelper.drawableBuilder(this.backgroundTexture, 34, 24, 108, 18)
            .addPadding(24, 44, 34, 34)
            .build();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, T recipe, IFocusGroup focuses) {
        this.inputSetter.accept(builder, recipe);
        this.outputSetter.accept(builder, recipe);
    }

    @Override
    public void draw(T recipe, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY) {
        this.gauge.draw(poseStack, 78, 24);

        JEIUtil.drawInfo(Minecraft.getInstance(), poseStack, recipe, this.yOffset, true);
    }

    private static void setSingleInput(IRecipeLayoutBuilder builder, BaseRecipe<?, RecipeIngredient<ItemStack>, ?, ?, ?> recipe) {
        builder.addSlot(RecipeIngredientRole.INPUT, 53, 25)
            .addItemStacks(recipe.getInput().getItemStacks());
    }

    private static void setSingleOutput(IRecipeLayoutBuilder builder, BaseRecipe<?, ?, ?, ItemStack, ?> recipe) {
        builder.addSlot(RecipeIngredientRole.OUTPUT, 107, 25)
            .addItemStack(recipe.getOutput());
    }

    private static void setMultiInput(IRecipeLayoutBuilder builder, BaseRecipe<?, ListRecipeIngredient<ItemStack>, ?, ?, ?> recipe) {
        ListRecipeIngredient<ItemStack> input = recipe.getInput();
        for (int i = 0; i < input.size(); i++) {
            RecipeIngredient<ItemStack> ingredient = input.get(i);
            builder.addSlot(RecipeIngredientRole.INPUT, 35 + i * 18, 25)
                .addItemStacks(ingredient.getItemStacks());
        }
    }

    private static void setMultiOutput(IRecipeLayoutBuilder builder, BaseRecipe<?, ?, ?, List<ItemStack>, ?> recipe) {
        List<ItemStack> output = recipe.getOutput();
        for (int i = 0; i < output.size(); i++) {
            ItemStack stack = output.get(i);
            builder.addSlot(RecipeIngredientRole.OUTPUT, 107 + i * 18, 25)
                .addItemStack(stack);
        }
    }
}
