package dev.su5ed.gregtechmod.compat;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import one.util.streamex.StreamEx;

import java.util.Map;

public final class ThermalHandler {
    private static final Map<DyeColor, Block> COLOR_ROCKWOOLS;
    
    static {
        COLOR_ROCKWOOLS = StreamEx.of(DyeColor.values())
            .mapToEntry(color -> new ResourceLocation(ModHandler.THERMAL_MODID, color.getName() + "_rockwool"))
            .mapValues(ForgeRegistries.BLOCKS::getValue)
            .toImmutableMap();
    }
    
    public static boolean canColorRockwool(Block block, DyeColor color) {
        return COLOR_ROCKWOOLS.containsValue(block) && COLOR_ROCKWOOLS.get(color) != block; 
    }
    
    public static Block getRockWool(DyeColor color) {
        return COLOR_ROCKWOOLS.get(color);
    }
}
