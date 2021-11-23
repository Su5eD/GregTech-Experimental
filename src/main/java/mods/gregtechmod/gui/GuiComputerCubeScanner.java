package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.computercube.ComputerCubeScanner;
import mods.gregtechmod.objects.blocks.teblocks.computercube.IComputerCubeModule;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerComputerCubeScanner;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiComputerCubeScanner extends GuiInventory<ContainerComputerCubeScanner> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("computer_cube_scanner");
    
    private ComputerCubeScanner module;
    
    public GuiComputerCubeScanner(ContainerComputerCubeScanner container) {
        super(container);
    }

    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);
        
        IComputerCubeModule newModule = this.container.base.getActiveModule();
        if (newModule instanceof ComputerCubeScanner) this.module = (ComputerCubeScanner) newModule;
        
        if (this.module != null) {
            int progress = this.module.getProgress();
            
            drawString(51, 7, GtUtil.translate("computercube.scanner.title"), GuiColors.WHITE, false);
            if (progress == 0) {
                drawString(51, 24, GtUtil.translate("computercube.scanner.usage"), GuiColors.WHITE, false);
                drawString(51, 32, GtUtil.translate("computercube.scanner.usage.2"), GuiColors.WHITE, false);
            } else {
                drawString(51, 24, GtUtil.translateInfo("progress"), GuiColors.WHITE, false);
                drawString(51, 32, progress + "%", GuiColors.WHITE, false);
            }
        }
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