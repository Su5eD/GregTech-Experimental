package mods.gregtechmod.api;

import mods.gregtechmod.api.util.Reference;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import java.util.HashMap;
import java.util.Map;

public class GregTechTEBlocks {
    private static final Map<String, ItemStack> TEBlocks = new HashMap<>();

    public static ItemStack getTileEntity(String name) {
        ItemStack stack = TEBlocks.get(name);
        if (stack == null) throw new IllegalArgumentException("Invalid TileEntity name provided: "+name);
        return stack;
    }

    public static void setTileEntityMap(Map<String, ItemStack> map) {
        ModContainer activeModContainer = Loader.instance().activeModContainer();
        if (activeModContainer == null || !activeModContainer.getModId().equals(Reference.MODID)) throw new IllegalAccessError("This method may only be called by GregTech!");
        TEBlocks.putAll(map);
    }
}