package mods.gregtechmod.gui.element;

import ic2.core.GuiIC2;
import ic2.core.gui.GuiElement;
import mods.gregtechmod.util.GtLocale;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

import javax.annotation.Nullable;
import java.util.List;

public class CustomFluidSlot extends GuiElement<CustomFluidSlot> {
    protected final IFluidTank tank;
    private final ResourceLocation texture;
    private final double textureX;
    private final double textureY;
    private final boolean verboseTooltip;
    
    public CustomFluidSlot(GuiIC2<?> gui, int x, int y, IFluidTank tank, boolean verboseTooltip) {
        this(gui, x, y, tank, null, 0, 0, verboseTooltip);
    }

    public CustomFluidSlot(GuiIC2<?> gui, int x, int y, IFluidTank tank, @Nullable ResourceLocation texture, double textureX, double textureY, boolean verboseTooltip) {
        super(gui, x, y, 18, 18);
        
        this.tank = tank;
        this.texture = texture;
        this.textureX = textureX;
        this.textureY = textureY;
        this.verboseTooltip = verboseTooltip;
    }

    @Override
    public void drawBackground(int mouseX, int mouseY) {
        if (this.texture != null) {
            bindTexture(this.texture);
            this.gui.drawTexturedRect(this.x, this.y, this.width, this.height, this.textureX, this.textureY);
        }
        
        FluidStack fs = this.tank.getFluid();
        if (fs != null && fs.amount > 0) {
            Fluid fluid = fs.getFluid();
            TextureAtlasSprite sprite = fluid != null ? getBlockTextureMap().getAtlasSprite(fluid.getStill(fs).toString()) : null;
            bindBlockTexture();
            this.gui.drawSprite(this.x + 1, this.y + 1, 16, 16, sprite, fluid != null ? fluid.getColor(fs) : -1, 1, false, false);
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
                if (!this.verboseTooltip) return ret;
                
                ret.add(GtLocale.translateInfo("fluid.amount", stack.amount));
                String type = stack.getFluid().isGaseous() ? "gas" : "liquid";
                ret.add(GtLocale.translateInfo("fluid.type", GtLocale.translateInfo("fluid.type." + type)));
            }
            else if (this.verboseTooltip) ret.add(GtLocale.translateInfo("fluid.invalid"));
        }
        return ret;
    }
}
