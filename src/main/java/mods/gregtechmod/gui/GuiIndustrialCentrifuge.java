package mods.gregtechmod.gui;

import ic2.core.GuiIC2;
import ic2.core.gui.GuiElement;
import ic2.core.gui.LinkedGauge;
import ic2.core.init.Localization;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.inventory.CustomFluidSlot;
import mods.gregtechmod.objects.blocks.tileentities.machines.container.ContainerIndustrialCentrifuge;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiIndustrialCentrifuge extends GuiIC2<ContainerIndustrialCentrifuge> {

    public GuiIndustrialCentrifuge(ContainerIndustrialCentrifuge container) {
        super(container, 166);
        addElement(CustomFluidSlot.createFluidSlot(this, 109, 64, container.base.lavaTank, GregTechMod.COMMON_TEXTURE, 40, 0, false));

        addElement(new LinkedGauge(this, 98, 38, container.base, "progress", GregtechGauge.ArrowRight));
        addElement(new LinkedGauge(this, 83, 23, container.base, "progress", GregtechGauge.ArrowUp));
        addElement(new LinkedGauge(this, 68, 38, container.base, "progress", GregtechGauge.ArrowLeft));
        addElement(new LinkedGauge(this, 83, 53, container.base, "progress", GregtechGauge.ArrowDown));
    }

    @Override
    protected void drawForegroundLayer(int mouseX, int mouseY) {

        for (GuiElement<?> guiElement : this.elements) {
            if (guiElement.isEnabled()) {
                guiElement.drawForeground(mouseX, mouseY);
            }
        }
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mouseX -= this.guiLeft;
        mouseY -= this.guiTop;
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        bindTexture();
        drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        String[] name = Localization.translate((this.container).base.getName()).split(" ");
        drawString(110, 4, name[0], 4210752, false);
        drawString(110, 12, name[1], 4210752, false);

        for (GuiElement<?> element : this.elements) {
            if (element.isEnabled())
                element.drawBackground(mouseX, mouseY);
        }
    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Reference.MODID, "textures/gui/industrial_centrifuge.png");
    }
}
