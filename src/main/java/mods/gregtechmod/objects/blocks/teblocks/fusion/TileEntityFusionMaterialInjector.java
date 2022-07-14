package mods.gregtechmod.objects.blocks.teblocks.fusion;

import ic2.core.IHasGui;
import ic2.core.block.comp.Fluids;
import ic2.core.block.invslot.InvSlotConsumableLiquid;
import ic2.core.util.Util;
import mods.gregtechmod.api.cover.CoverType;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.gui.GuiBasicTank;
import mods.gregtechmod.inventory.tank.GtFluidTankProcessable;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityCoverBehavior;
import mods.gregtechmod.objects.blocks.teblocks.component.BasicTank;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerBasicTank;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityFusionMaterialInjector extends TileEntityCoverBehavior implements IHasGui {
    public final BasicTank tank;
    
    public TileEntityFusionMaterialInjector() {
        Fluids fluids = addComponent(new Fluids(this));
        this.tank = addComponent(new BasicTank(this, fluids, new GtFluidTankProcessable(this, "content", GtRecipes.fusionFluid, Util.allFacings, Util.allFacings, 10000), InvSlotConsumableLiquid.OpType.Both, true));

        this.coverBlacklist.add(CoverType.ENERGY);
    }

    @Override
    public ContainerBasicTank<TileEntityFusionMaterialInjector> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerBasicTank<>(entityPlayer, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiBasicTank<>(getGuiContainer(player), this.tank.content);
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {}
}
