package mods.gregtechmod.gui;

import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerComputerCubeMain;
import net.minecraft.util.ResourceLocation;

public class GuiComputerCubeMain extends GuiInventory<ContainerComputerCubeMain> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/computer_cube_main.png");
    
    public GuiComputerCubeMain(ContainerComputerCubeMain container) {
        super(container);
    }

    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);
        
        drawString(64, 61, "G.L.A.D.-OS", 16448255, false);
    }

    @Override
    protected void drawTitle() {
        // noop
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
