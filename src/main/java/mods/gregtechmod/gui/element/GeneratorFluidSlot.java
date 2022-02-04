package mods.gregtechmod.gui.element;

import ic2.core.GuiIC2;
import mods.gregtechmod.gui.GuiFluidGenerator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fluids.IFluidTank;

public class GeneratorFluidSlot extends CustomFluidSlot {

    public GeneratorFluidSlot(GuiIC2<?> gui, int x, int y, IFluidTank tank) {
        super(gui, x, y, tank, false);
    }

    @Override
    public void drawBackground(int mouseX, int mouseY) {
        super.drawBackground(mouseX, mouseY);

        if (this.tank.getFluid() == null && ((GuiFluidGenerator) this.gui).getContainer().base.getSolidFuelEnergy() > 0) {
            TextureAtlasSprite sprite = getBlockTextureMap().getAtlasSprite("minecraft:blocks/fire_layer_0");
            bindBlockTexture();
            this.gui.drawSprite(this.x + 1, this.y + 1, 16, 16, sprite, -1, 1, false, false);
        }
    }
}
