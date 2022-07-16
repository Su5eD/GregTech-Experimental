package dev.su5ed.gregtechmod.cover;

import dev.su5ed.gregtechmod.api.cover.CoverCategory;
import dev.su5ed.gregtechmod.api.cover.CoverType;
import dev.su5ed.gregtechmod.api.cover.Coverable;
import dev.su5ed.gregtechmod.util.GtUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

public class CraftingCover extends BaseCover {
    public static final ResourceLocation TEXTURE = GtUtil.getCoverTexture("crafting");
    private static final Component CONTAINER_TITLE = new TranslatableComponent("container.crafting");

    public CraftingCover(CoverType type, Coverable be, Direction side, Item item) {
        super(type, be, side, item);
    }

    @Override
    public boolean onCoverRightClick(Player player, InteractionHand hand, Direction side, float hitX, float hitY, float hitZ) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.nextContainerCounter();
            Level level = ((BlockEntity) this.be).getLevel();
            BlockPos pos = ((BlockEntity) this.be).getBlockPos();
            serverPlayer.openMenu(getMenuProvider(level, pos));
        }
        player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
        return true;
    }

    public MenuProvider getMenuProvider(Level level, BlockPos pos) {
        return new SimpleMenuProvider((id, inventory, player) -> new CoverCraftingMenu(id, inventory, ContainerLevelAccess.create(level, pos)), CONTAINER_TITLE);
    }

    @Override
    public ResourceLocation getIcon() {
        return TEXTURE;
    }

    @Override
    public CoverCategory getCategory() {
        return CoverCategory.UTIL;
    }

    private class CoverCraftingMenu extends CraftingMenu {

        public CoverCraftingMenu(int id, Inventory inventory, ContainerLevelAccess access) {
            super(id, inventory, access);
        }

        @Override
        public boolean stillValid(Player player) {
            BlockEntity be = (BlockEntity) CraftingCover.this.be;
            Block block = be.getBlockState().getBlock();
            return be.getLevel().getBlockState(be.getBlockPos()).getBlock().equals(block);
        }
    }
}
