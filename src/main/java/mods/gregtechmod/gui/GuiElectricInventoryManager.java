package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerElectricInventoryManager;
import mods.gregtechmod.objects.blocks.teblocks.inv.TileEntityElectricInventoryManager.SlotRangeSetting;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import java.util.function.IntSupplier;
import java.util.stream.IntStream;

public class GuiElectricInventoryManager extends GuiSimple<ContainerElectricInventoryManager> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("electric_inventory_manager");
    
    public GuiElectricInventoryManager(ContainerElectricInventoryManager container) {
        super(container);
        
        addSlotRange(0, 4);
        addSlotRange(1, 60);
        addSlotRange(2, 79);
        addSlotRange(3, 135);
    }
    
    private void addSlotRange(int index, int xOffset) {
        boolean right = index % 2 == 0;
        int settingX = xOffset + (right ? 19 : -19);
        IntSupplier facingSupplier = () -> this.container.base.manager.ranges.get(index).facing.ordinal();
        
        addIconCycle(xOffset, 4, TEXTURE, 0, 202, 18, 53, false, facingSupplier);
        IntStream.range(0, 3).forEach(i -> {
            SlotRangeSetting setting = this.container.base.manager.ranges.get(index).rangeSettings.get(i);
            addIconCycleGrid(settingX, 4 + i * 18, TEXTURE, 0, 166, 18, false, () -> setting.targetSide.ordinal(), () -> setting.input);
        });
        addIconCycle(settingX, 59, TEXTURE, 126, 166, 18, false, facingSupplier);
        addVerticalIconCycle(xOffset, 59, 58, () -> {
            EnumFacing facing = this.container.base.manager.ranges.get(index).facing;
            return this.container.base.manager.ranges.stream().anyMatch(slotRange -> slotRange.facing == facing && slotRange.outputEnergy);
        });
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
