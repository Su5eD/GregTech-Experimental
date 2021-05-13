package mods.gregtechmod.inventory;

import ic2.core.block.invslot.InvSlot;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.EnumHelper;

public class GtInvSide {
    public static final InvSlot.InvSide VERTICAL = EnumHelper.addEnum(InvSlot.InvSide.class, "VERTICAL", new Class[] { EnumFacing[].class }, (Object) new EnumFacing[] { EnumFacing.UP, EnumFacing.DOWN });
}
