package mods.gregtechmod.objects.items.base;

import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.cover.ICoverProvider;
import mods.gregtechmod.api.cover.ICoverable;
import mods.gregtechmod.util.GtLocale;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

public class ItemCover extends ItemBase {
    private final ICoverProvider coverProvider;

    public ItemCover(String name, ICoverProvider coverProvider, String descriptionKey) {
        super(name, () -> GtLocale.translateItemDescription(descriptionKey));
        this.coverProvider = coverProvider;
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (!player.isSneaking()) {
            TileEntity block = world.getTileEntity(pos);
            ItemStack stack = player.inventory.getCurrentItem();

            if (block instanceof ICoverable) {
                ICover cover = this.coverProvider.constructCover(side, (ICoverable) block, ItemHandlerHelper.copyStackWithSize(stack, 1));
                if (((ICoverable) block).placeCoverAtSide(cover, player, side, false)) {
                    if (!player.capabilities.isCreativeMode) stack.shrink(1);
                    return EnumActionResult.SUCCESS;
                }
            }
        }
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }
}
