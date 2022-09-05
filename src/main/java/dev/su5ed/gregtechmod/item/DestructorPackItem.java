package dev.su5ed.gregtechmod.item;

import dev.su5ed.gregtechmod.container.DestructorPackContainer;
import dev.su5ed.gregtechmod.object.Tool;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class DestructorPackItem extends ResourceItem {

    public DestructorPackItem() {
        super(new ExtendedItemProperties<>()
            .stacksTo(1)
            .setNoRepair()
            .setNoEnchant()
            .autoDescription());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (player instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openGui(serverPlayer, new DestructorPackMenuProvider(hand), buf -> buf.writeEnum(hand));
        }
        return super.use(level, player, hand);
    }

    private record DestructorPackMenuProvider(InteractionHand hand) implements MenuProvider {
        @Override
        public Component getDisplayName() {
            return Tool.DESTRUCTORPACK.getItem().getDescription();
        }

        @Nullable
        @Override
        public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
            return new DestructorPackContainer(containerId, this.hand, playerInventory);
        }
    }
}
