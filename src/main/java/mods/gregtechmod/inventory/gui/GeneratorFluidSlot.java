package mods.gregtechmod.inventory.gui;

import mods.gregtechmod.gui.GuiFluidGenerator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.IFluidTank;

public class GeneratorFluidSlot extends CustomFluidSlot {
        
    public GeneratorFluidSlot(GuiFluidGenerator gui, int x, int y, IFluidTank tank, ResourceLocation backgroundTexture, double textureX, double textureY, boolean displayFluidNameOnly) {
        super(gui, x, y, tank, backgroundTexture, textureX, textureY, displayFluidNameOnly);
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
