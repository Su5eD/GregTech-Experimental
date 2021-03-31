package mods.gregtechmod.cover.type;

import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.api.util.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class CoverCrafting extends CoverGeneric {

    public CoverCrafting(ICoverable te, EnumFacing side, ItemStack stack) {
        super(te, side, stack);
    }

    @Override
    public boolean onCoverRightClick(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (player instanceof EntityPlayerMP) {
            ((EntityPlayerMP) player).getNextWindowId();
            ((EntityPlayerMP) player).connection.sendPacket(new SPacketOpenWindow(((EntityPlayerMP) player).currentWindowId, "minecraft:crafting_table", new TextComponentTranslation(Blocks.CRAFTING_TABLE.getTranslationKey() + ".name")));
            player.openContainer = new CoverContainerWorkbench(player.inventory, ((TileEntity)te).getWorld(), ((TileEntity)te).getPos());
            player.openContainer.windowId = ((EntityPlayerMP) player).currentWindowId;
            player.openContainer.addListener((EntityPlayerMP) player);
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.player.PlayerContainerEvent.Open(player, player.openContainer));
        }
        player.addStat(StatList.CRAFTING_TABLE_INTERACTION);
        return true;
    }

    @Override
    public ResourceLocation getIcon() {
        return new ResourceLocation(Reference.MODID, "blocks/covers/crafting");
    }

    private class CoverContainerWorkbench extends ContainerWorkbench {

        public CoverContainerWorkbench(InventoryPlayer playerInventory, World worldIn, BlockPos posIn) {
            super(playerInventory, worldIn, posIn);
        }

        @Override
        public boolean canInteractWith(EntityPlayer playerIn) {
            return ((TileEntity)te).getWorld().getBlockState(((TileEntity)te).getPos()).getBlock().equals(((TileEntity)te).getBlockType());
        }
    }
}
