package mods.gregtechmod.util;

import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot;
import mods.gregtechmod.core.GregTechMod;

import javax.annotation.Nullable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class InvUtil {
    private static final MethodHandle GET_INVENTORY_SLOT;
    private static final MethodHandle INVENTORY_SLOTS;

    static {
        MethodHandle handleGetInventorySlot;
        MethodHandle handleInvSlots;

        try {
            Method methodGetInventorySlot = TileEntityInventory.class.getDeclaredMethod("getInventorySlot", int.class);
            methodGetInventorySlot.setAccessible(true);
            Field fieldInvSlots = TileEntityInventory.class.getDeclaredField("invSlots");
            fieldInvSlots.setAccessible(true);
            MethodHandles.Lookup lookup = MethodHandles.lookup();

            handleGetInventorySlot = lookup.unreflect(methodGetInventorySlot);
            handleInvSlots = lookup.unreflectGetter(fieldInvSlots);
        } catch (NoSuchMethodException | IllegalAccessException | NoSuchFieldException e) {
            GregTechMod.LOGGER.catching(e);
            handleGetInventorySlot = handleInvSlots = null;
        }

        GET_INVENTORY_SLOT = handleGetInventorySlot;
        INVENTORY_SLOTS = handleInvSlots;
    }

    @Nullable
    public static InvSlot getInventorySlot(TileEntityInventory instance, int index) {
        try {
            return (InvSlot) GET_INVENTORY_SLOT.invokeExact(instance, index);
        } catch (Throwable t) {
            GregTechMod.LOGGER.catching(t);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static List<InvSlot> getInvSlots(TileEntityInventory instance) {
        try {
            return (List<InvSlot>) INVENTORY_SLOTS.invokeExact(instance);
        } catch (Throwable t) {
            GregTechMod.LOGGER.catching(t);
            return Collections.emptyList();
        }
    }
}
