package mods.gregtechmod.objects.blocks.teblocks.inv;

import ic2.api.recipe.Recipes;
import ic2.core.block.invslot.InvSlot;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.gui.GuiScrapboxinator;
import mods.gregtechmod.inventory.invslot.GtSlotFiltered;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerScrapboxinator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityScrapboxinator extends TileEntityElectricBuffer {
    public final InvSlot scrapSlot;

    public TileEntityScrapboxinator() {
        super(1);
        
        this.scrapSlot = new GtSlotFiltered(this, "scrap", InvSlot.Access.I, 1, stack -> stack.isItemEqual(ModHandler.scrapbox) || stack.isItemEqual(ModHandler.scrap)); 
    }

    @Override
    protected InvSlot.Access getBufferSlotAccess() {
        return InvSlot.Access.O;
    }

    @Override
    protected int getBaseEUCapacity() {
        return 10000;
    }

    @Override
    protected int getMinimumStoredEU() {
        return 2000;
    }

    @Override
    protected void work() {
        if (canUseEnergy(500) && (this.tickCounter % 200 == 0 || this.success > 0 && this.tickCounter % 5 == 0 || this.success >= 20) 
                && this.buffer.isEmpty() && !this.scrapSlot.isEmpty()
        ) {
            ItemStack scrap = this.scrapSlot.get();
            boolean isScrapbox = scrap.isItemEqual(ModHandler.scrapbox);
            if (isScrapbox || scrap.getCount() > 8) {
                ItemStack drop = Recipes.scrapboxDrops.getDrop(ModHandler.scrapbox, false);
                if (drop != null) {
                    this.buffer.put(drop);
                    scrap.shrink(1);
                    useEnergy(isScrapbox ? 100 : 200);
                    this.success = 30;
                }
            }
        }
        
        super.work();
    }

    @Override
    public ContainerScrapboxinator getGuiContainer(EntityPlayer player) {
        return new ContainerScrapboxinator(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiScrapboxinator(getGuiContainer(player));
    }
}
