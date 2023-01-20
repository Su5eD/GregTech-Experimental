package dev.su5ed.gtexperimental.block;

import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;

public class LightSourceBlock extends AirBlock {

    public LightSourceBlock() {
        super(Properties.of(Material.AIR)
            .lightLevel(state -> 15)
            .randomTicks());
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }
}
