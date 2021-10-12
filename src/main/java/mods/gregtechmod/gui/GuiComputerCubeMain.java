package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerComputerCubeMain;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiComputerCubeMain extends GuiInventory<ContainerComputerCubeMain> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("computer_cube_main");
    
    public GuiComputerCubeMain(ContainerComputerCubeMain container) {
        super(container);
    }

    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);
        
        drawString(64, 61, "G.L.A.D.-OS", GuiColors.WHITE, false);
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
