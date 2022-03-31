package mods.gregtechmod.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;

public class NormalStateMapper extends StateMapperBase {
    public static final NormalStateMapper INSTANCE = new NormalStateMapper();

    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        String name = Block.REGISTRY.getNameForObject(state.getBlock()).toString();

        return new ModelResourceLocation(name, "normal");
    }
}
