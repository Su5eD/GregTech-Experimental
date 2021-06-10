package mods.gregtechmod.inventory;

import mods.gregtechmod.gui.GuiThermalGenerator;
import mods.gregtechmod.inventory.slot.CustomFluidSlot;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.IFluidTank;

public class ThermalGeneratorFluidSlot extends CustomFluidSlot {
        
    public ThermalGeneratorFluidSlot(GuiThermalGenerator gui, int x, int y, IFluidTank tank, ResourceLocation backgroundTexture, double textureX, double textureY, boolean displayFluidNameOnly) {
        super(gui, x, y, tank, backgroundTexture, textureX, textureY, displayFluidNameOnly);
    }

    @Override
    public void drawBackground(int mouseX, int mouseY) {
        super.drawBackground(mouseX, mouseY);
        
        if (this.tank.getFluid() == null && ((GuiThermalGenerator) this.gui).getContainer().base.getSolidFuelEnergy() > 0) {
            TextureAtlasSprite sprite = getBlockTextureMap().getAtlasSprite("minecraft:blocks/fire_layer_0");
            bindBlockTexture();
            this.gui.drawSprite(this.x + 1, this.y + 1, 16, 16, sprite, -1, 1, false, false);
        }
    }
}
