package dev.su5ed.gregtechmod.block;

import dev.su5ed.gregtechmod.util.GtLocale;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ResourceBlock extends Block {
    
    public ResourceBlock(float strength, float resistance) {
        super(BlockBehaviour.Properties.of(Material.METAL)
            .strength(strength, resistance)
            .requiresCorrectToolForDrops()
            .isValidSpawn((state, world, pos, type) -> false)
        );
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
        
        pTooltip.add(GtLocale.key("info", "no_mob_spawn").toComponent().withStyle(ChatFormatting.GRAY));
    }
}
