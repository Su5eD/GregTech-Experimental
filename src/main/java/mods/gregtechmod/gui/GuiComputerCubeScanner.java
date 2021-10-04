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
            
            drawString(51, 7, GtUtil.translate("computercube.scanner.title"), 16448255, false);
            if (progress == 0) {
                drawString(51, 24, GtUtil.translate("computercube.scanner.usage"), 16448255, false);
                drawString(51, 32, GtUtil.translate("computercube.scanner.usage.2"), 16448255, false);
            } else {
                drawString(51, 24, GtUtil.translate("computercube.scanner.progress"), 16448255, false);
                drawString(51, 32, progress + "%", 16448255, false);
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
