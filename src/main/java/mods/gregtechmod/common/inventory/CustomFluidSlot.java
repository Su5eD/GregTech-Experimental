package mods.gregtechmod.common.inventory;

import ic2.core.GuiIC2;
import ic2.core.gui.GuiElement;
import ic2.core.init.Localization;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

import javax.annotation.Nullable;
import java.util.List;

public class CustomFluidSlot extends GuiElement<CustomFluidSlot> {
    private final IFluidTank tank;
    private final ResourceLocation texture;
    private final double textureX;
    private final double textureY;
    private final boolean fluidNameOnly;

    public static CustomFluidSlot createFluidSlot(GuiIC2<?> gui, int x, int y, IFluidTank tank, ResourceLocation backgroundTexture, double textureX, double textureY, boolean displayFluidNameOnly) {
        return new CustomFluidSlot(gui, x, y, 18, 18, tank, backgroundTexture, textureX, textureY, displayFluidNameOnly);
    }

    protected CustomFluidSlot(GuiIC2<?> gui, int x, int y, int width, int height, IFluidTank tank, @Nullable ResourceLocation backgroundTexture, double textureX, double textureY, boolean displayFluidNameOnly) {
        super(gui, x, y, width, height);
        if (tank == null) {
            throw new NullPointerException("Null FluidTank instance.");
        } else {
            this.tank = tank;
        }
        this.texture = backgroundTexture;
        this.textureX = textureX;
        this.textureY = textureY;
        this.fluidNameOnly = displayFluidNameOnly;
    }

    public void drawBackground(int mouseX, int mouseY) {
        if (this.texture != null) {
            bindTexture(this.texture);
            this.gui.drawTexturedRect(this.x, this.y, this.width, this.height, this.textureX, this.textureY);
        }
        FluidStack fs = this.tank.getFluid();
        if (fs != null && fs.amount > 0) {
            int fluidX = this.x + 1;
            int fluidY = this.y + 1;
            int fluidWidth = 16;
            int fluidHeight = 16;
            Fluid fluid = fs.getFluid();
            TextureAtlasSprite sprite = fluid != null ? getBlockTextureMap().getAtlasSprite(fluid.getStill(fs).toString()) : null;
            int color = fluid != null ? fluid.getColor(fs) : -1;
            bindBlockTexture();
            this.gui.drawSprite(fluidX, fluidY, fluidWidth, fluidHeight, sprite, color, 1.0D, false, false);
        }

    }

    protected List<String> getToolTip() {
        List<String> ret = super.getToolTip();
        FluidStack fs = this.tank.getFluid();
        if (fs != null && fs.amount > 0) {
            Fluid fluid = fs.getFluid();
            if (fluid != null) {
                ret.add(fluid.getLocalizedName(fs));
                if (fluidNameOnly) return ret;
                ret.add("Amount: " + fs.amount + " " + Localization.translate("ic2.generic.text.mb"));
                String state = fs.getFluid().isGaseous() ? "Gas" : "Liquid";
                ret.add("Type: " + state);
            } else {
                if (!fluidNameOnly) ret.add("Invalid FluidStack instance.");
            }
        } else if (!fluidNameOnly) {
            ret.add("No Fluid");
            ret.add("Amount: 0 " + Localization.translate("ic2.generic.text.mb"));
            ret.add("Type: Not Available");
        }
        return ret;
    }
}
