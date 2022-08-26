package dev.su5ed.gregtechmod.blockentity.component;

import dev.su5ed.gregtechmod.blockentity.base.BaseBlockEntity;
import dev.su5ed.gregtechmod.network.GregTechNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public abstract class GtComponentBase<T extends BaseBlockEntity> implements BlockEntityComponent {
    protected final T parent;

    protected GtComponentBase(T parent) {
        this.parent = parent;
    }

    @Override
    public T getParent() {
        return this.parent;
    }
    
    public void updateClient() {
        GregTechNetwork.updateClientComponent(this.parent, this);
    }
    
    public boolean isServerSide() {
        return !this.parent.getLevel().isClientSide;
    }

    public void onPlaced(ItemStack stack, LivingEntity placer, Direction facing) {}

    public boolean onActivated(Player player, InteractionHand hand, Direction side, float hitX, float hitY, float hitZ) {
        return false;
    }

    public void getScanInfo(List<Component> scan, Player player, BlockPos pos, int scanLevel) {}
}
