package mods.gregtechmod.objects.covers;

import mods.gregtechmod.api.cover.CoverType;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.machine.IGregTechMachine;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Locale;

public class CoverMachineController extends CoverGeneric {
    public static final ResourceLocation TEXTURE = GtUtil.getCoverTexture("machine_controller");
    
    @NBTPersistent
    protected ControllerMode mode = ControllerMode.NORMAL;

    public CoverMachineController(ResourceLocation name, ICoverable te, EnumFacing side, ItemStack stack) {
        super(name, te, side, stack);
    }

    @Override
    public void doCoverThings() {
        if (!(te instanceof IGregTechMachine)) return;

        World world = ((TileEntity)te).getWorld();
        BlockPos offset = ((TileEntity)te).getPos().offset(side);
        boolean isPowered = world.isBlockPowered(offset) || world.isSidePowered(offset, side);
        ((IGregTechMachine)te).setAllowedToWork(isPowered == (mode == ControllerMode.NORMAL) && mode != ControllerMode.DISABLED);
    }

    @Override
    public CoverType getType() {
        return CoverType.CONTROLLER;
    }

    @Override
    public ResourceLocation getIcon() {
        return TEXTURE;
    }

    @Override
    public boolean onScrewdriverClick(EntityPlayer player) {
        mode = mode.next();
        GtUtil.sendMessage(player, mode.getMessageKey());
        return true;
    }

    private enum ControllerMode {
        NORMAL,
        INVERTED,
        DISABLED;

        private static final ControllerMode[] VALUES = values();

        public ControllerMode next() {
            return VALUES[(this.ordinal() + 1) % VALUES.length];
        }

        public String getMessageKey() {
            return Reference.MODID+".cover.mode."+this.name().toLowerCase(Locale.ROOT);
        }
    }

    @Override
    public void onCoverRemove() {
        if (te instanceof IGregTechMachine) ((IGregTechMachine)te).setAllowedToWork(true);
    }

    @Override
    public int getTickRate() {
        return 1;
    }

    @Override
    public boolean allowEnergyTransfer() {
        return true;
    }

    @Override
    public boolean letsLiquidsIn() {
        return true;
    }

    @Override
    public boolean letsLiquidsOut() {
        return true;
    }

    @Override
    public boolean letsItemsIn() {
        return true;
    }

    @Override
    public boolean letsItemsOut() {
        return true;
    }

    @Override
    public boolean acceptsRedstone() {
        return true;
    }
}
