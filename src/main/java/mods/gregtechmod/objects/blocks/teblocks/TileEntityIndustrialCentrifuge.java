package mods.gregtechmod.objects.blocks.teblocks;

import com.google.common.collect.Sets;
import ic2.core.block.state.Ic2BlockState;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.gui.GuiIndustrialCentrifuge;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityIndustrialCentrifugeBase;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerIndustrialCentrifuge;
import mods.gregtechmod.util.PropertyHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

public class TileEntityIndustrialCentrifuge extends TileEntityIndustrialCentrifugeBase {
    private static final Set<EnumFacing> ANIMATED_SIDES = Sets.newHashSet(EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST, EnumFacing.UP);

    public TileEntityIndustrialCentrifuge() {
        super("industrial_centrifuge", 32000, GtRecipes.industrialCentrifuge);
    }
    
    @Override
    protected Ic2BlockState.Ic2BlockStateInstance getExtendedState(Ic2BlockState.Ic2BlockStateInstance state) {
        Ic2BlockState.Ic2BlockStateInstance extendedState = super.getExtendedState(state);
        return getActive() ? extendedState.withProperty(PropertyHelper.ANIMATION_SPEED_PROPERTY, new PropertyHelper.AnimationSpeed(ANIMATED_SIDES, GregTechConfig.GENERAL.dynamicCentrifugeAnimationSpeed ? Math.min(getUpgradeCount(IC2UpgradeType.OVERCLOCKER) + 1, 3) : 3)) : extendedState;
    }

    @Override
    public int getBaseSinkTier() {
        return 1;
    }

    @Override
    public ContainerIndustrialCentrifuge getGuiContainer(EntityPlayer player) {
        return new ContainerIndustrialCentrifuge(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiIndustrialCentrifuge(getGuiContainer(player));
    }
}
