package mods.gregtechmod.objects.blocks.teblocks.inv;

import ic2.core.block.invslot.InvSlot;
import ic2.core.util.Util;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.gui.GuiElectricRockBreaker;
import mods.gregtechmod.inventory.invslot.GtSlotFiltered;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerElectricRockBreaker;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

public class TileEntityElectricRockBreaker extends TileEntityElectricBufferSingle {
    public final InvSlot redstoneSlot;

    public TileEntityElectricRockBreaker() {
        super(1);
        
        this.redstoneSlot = new GtSlotFiltered(this, "redstone", InvSlot.Access.I, 1, stack -> stack.getItem() == Items.REDSTONE);
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
        return 1000 + getOverclockerMultiplier() * 100;
    }

    @Override
    protected void work() {
        if (this.tickCounter % (40 / (int) Math.pow(2, getUpgradeCount(IC2UpgradeType.OVERCLOCKER))) == 0 && findNearbyBlock(Blocks.WATER)) {
            boolean obsidian = !this.redstoneSlot.isEmpty();
            ItemStack output;
            if (this.world.getBlockState(this.pos.offset(EnumFacing.UP)).getBlock() == Blocks.LAVA)
                output = new ItemStack(obsidian ? Blocks.OBSIDIAN : Blocks.STONE);
            else if (findNearbyBlock(Blocks.LAVA))
                output = new ItemStack(obsidian ? Blocks.OBSIDIAN : Blocks.COBBLESTONE);
            else output = ItemStack.EMPTY;

            if (this.buffer.canAdd(output)) {
                int base = obsidian ? 500 : 100;
                if (tryUseEnergy(base * getOverclockerMultiplier())) {
                    if (obsidian) this.redstoneSlot.get().shrink(1);
                    this.buffer.add(output);
                }
            }
        }
        super.work();
    }

    private boolean findNearbyBlock(Block block) {
        return Util.horizontalFacings.stream()
                .map(this.pos::offset)
                .map(this.world::getBlockState)
                .anyMatch(state -> state.getBlock() == block);
    }

    @Override
    public Set<IC2UpgradeType> getCompatibleIC2Upgrades() {
        return IC2UpgradeType.DEFAULT;
    }

    @Override
    public ContainerElectricRockBreaker getGuiContainer(EntityPlayer player) {
        return new ContainerElectricRockBreaker(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiElectricRockBreaker(getGuiContainer(player));
    }
}
