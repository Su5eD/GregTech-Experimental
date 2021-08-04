package mods.gregtechmod.inventory.gui;

import ic2.core.GuiIC2;
import ic2.core.gui.GuiElement;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

import java.util.List;

public class CustomFluidSlot extends GuiElement<CustomFluidSlot> {
    protected final IFluidTank tank;
    private final ResourceLocation texture;
    private final double textureX;
    private final double textureY;
    private final boolean fluidNameOnly;

    public CustomFluidSlot(GuiIC2<?> gui, int x, int y, IFluidTank tank, ResourceLocation backgroundTexture, double textureX, double textureY, boolean displayFluidNameOnly) {
        super(gui, x, y, 18, 18);
        if (tank == null) throw new NullPointerException("FluidTank is null");
        else this.tank = tank;
        
        this.texture = backgroundTexture;
        this.textureX = textureX;
        this.textureY = textureY;
        this.fluidNameOnly = displayFluidNameOnly;
    }

    @Override
    public void drawBackground(int mouseX, int mouseY) {
        if (this.texture != null) {
            bindTexture(this.texture);
            this.gui.drawTexturedRect(this.x, this.y, this.width, this.height, this.textureX, this.textureY);
        }
        
        FluidStack fs = this.tank.getFluid();
        if (fs != null && fs.amount > 0) {
            int fluidX = this.x + 1;
            int fluidY = this.y + 1;
            Fluid fluid = fs.getFluid();
            TextureAtlasSprite sprite = fluid != null ? getBlockTextureMap().getAtlasSprite(fluid.getStill(fs).toString()) : null;
            bindBlockTexture();
            this.gui.drawSprite(fluidX, fluidY, 16, 16, sprite, fluid != null ? fluid.getColor(fs) : -1, 1, false, false);
        }
    }

    @Override
    protected List<String> getToolTip() {
        List<String> ret = super.getToolTip();
        FluidStack stack = this.tank.getFluid();
        if (stack != null && stack.amount > 0) {
            Fluid fluid = stack.getFluid();
            if (fluid != null) {
                ret.add(fluid.getLocalizedName(stack));
                if (fluidNameOnly) return ret;

                ret.add("Amount: " + stack.amount + " mB");
                ret.add("Type: " + (stack.getFluid().isGaseous() ? "Gas" : "Liquid"));
            } else if (!fluidNameOnly) ret.add("<Invalid>");
        }
        return ret;
    }
}
