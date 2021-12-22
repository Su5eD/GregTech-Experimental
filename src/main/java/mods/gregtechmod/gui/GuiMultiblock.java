package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.component.Maintenance;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerMultiblock;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import java.util.function.Supplier;

public class GuiMultiblock extends GuiSimple<ContainerMultiblock> {
    private static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("multiblock");

    public GuiMultiblock(ContainerMultiblock container) {
        super(container);
    }

    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);
        
        String name = I18n.format(this.container.base.getName());
        this.drawString(10, 8, name, GuiColors.WHITE, false);
        
        this.container.base.structure.getWorldStructure()
                .ifPresent(struct -> { 
                    if (!struct.isValid()) displayStatus("error.structure", 64);
                    
                    Maintenance maintenance = container.base.maintenance;
                    boolean wrench = checkMaintenanceStatus(maintenance::getWrench, "wrench", 16);
                    boolean screwdriver = checkMaintenanceStatus(maintenance::getScrewdriver, "screwdriver", 24);
                    boolean softHammer = checkMaintenanceStatus(maintenance::getSoftHammer, "soft_hammer", 32);
                    boolean hardHammer = checkMaintenanceStatus(maintenance::getHardHammer, "hard_hammer", 40);
                    boolean solderingTool = checkMaintenanceStatus(maintenance::getSolderingTool, "soldering_tool", 48);
                    boolean crowbar = checkMaintenanceStatus(maintenance::getCrowbar, "crowbar", 56);
                    if (wrench && screwdriver && softHammer && hardHammer && solderingTool && crowbar) {
                        if (!container.base.getActive()) {
                            displayStatus("start_1", 16);
                            displayStatus("start_2", 24);
                            displayStatus("start_3", 32);
                        } else {
                            displayStatus("running", 16);
                        }
                    }
                });
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }

    private boolean checkMaintenanceStatus(Supplier<Boolean> supplier, String name, int y) {
        if (!supplier.get()) {
            displayStatus("error." + name, y);
            return false;
        }
        return true;
    }
    
    private void displayStatus(String name, int y) {
        drawString(10, y, GtLocale.translate("multiblock." + name), GuiColors.WHITE, false);
    }
}
