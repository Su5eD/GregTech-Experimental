package mods.gregtechmod.compat.jei.wrapper;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mods.gregtechmod.api.recipe.fuel.IFuel;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.gui.GuiColors;
import mods.gregtechmod.util.JavaUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import one.util.streamex.StreamEx;
import org.lwjgl.opengl.GL11;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class WrapperFuel<T extends IFuel<IRecipeIngredient>> implements IRecipeWrapper {
    private final T fuel;

    public WrapperFuel(T fuel) {
        this.fuel = fuel;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        IRecipeIngredient input = this.fuel.getInput();
        ingredients.setInputLists(VanillaTypes.ITEM, Collections.singletonList(input.getMatchingInputs()));
        if (input instanceof IRecipeIngredientFluid) {
            List<FluidStack> fluids = ((IRecipeIngredientFluid) input).getMatchingFluids().stream()
                .map(fluid -> new FluidStack(fluid, Fluid.BUCKET_VOLUME))
                .collect(Collectors.toList());
            ingredients.setInputLists(VanillaTypes.FLUID, Collections.singletonList(fluids));
        }
        ingredients.setOutputLists(VanillaTypes.ITEM, Collections.singletonList(this.fuel.getOutput()));
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        minecraft.fontRenderer.drawString("EU: " + JavaUtil.formatNumber(this.fuel.getEnergy() * 1000) + "EU", 2, 50, GuiColors.BLACK);

        List<ItemStack> output = this.fuel.getOutput();
        if (output.size() > 0 && StreamEx.of(output).noneMatch(ItemStack::isEmpty)) {
            minecraft.renderEngine.bindTexture(new ResourceLocation("textures/gui/container/generic_54.png"));
            GlStateManager.disableDepth();
            drawTexturedModalRect(98, 20, 7, 17, 18, 18);
        }
    }

    public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
        double maxWidth = 256.0;
        int zLevel = 0;
        double minU = textureX / maxWidth;
        double minV = textureY / maxWidth;
        double maxU = (textureX + width) / maxWidth;
        double maxV = (textureY + height) / maxWidth;
        double maxX = x + width;
        double maxY = y + height;

        GlStateManager.color(1, 1, 1, 1);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y, zLevel).tex(minU, minV).endVertex();
        worldrenderer.pos(x, maxY, zLevel).tex(minU, maxV).endVertex();
        worldrenderer.pos(maxX, maxY, zLevel).tex(maxU, maxV).endVertex();
        worldrenderer.pos(maxX, y, zLevel).tex(maxU, minV).endVertex();
        tessellator.draw();
    }
}
