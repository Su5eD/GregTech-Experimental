package mods.gregtechmod.objects.items.tools;

import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.items.base.ItemToolBase;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.JavaUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneComparator;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemScrewdriver extends ItemToolBase {

    public ItemScrewdriver() {
        super("screwdriver", JavaUtil.NULL_SUPPLIER, 256, 4);
        setRegistryName("screwdriver");
        setTranslationKey("screwdriver");
        setCreativeTab(GregTechMod.GREGTECH_TAB);
        GregTechAPI.instance().registerScrewdriver(this);
        this.effectiveAganist.add("minecraft:spider");
        this.effectiveAganist.add("minecraft:cave_spider");
        this.effectiveAganist.add("twilightforest:hedge_spider");
        this.effectiveAganist.add("twilightforest:king_spider");
        this.effectiveAganist.add("twilightforest:swarm_spider");
        this.effectiveAganist.add("twilightforest:tower_broodling");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(GtLocale.translateItem("screwdriver.description"));
        tooltip.add(GtLocale.translateItem("screwdriver.description_2"));
        tooltip.add(GtLocale.translateItem("screwdriver.description_3"));
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (player.isSneaking()) return EnumActionResult.PASS;

        Block block = world.getBlockState(pos).getBlock();
        ItemStack stack = player.inventory.getCurrentItem();
        if (block instanceof BlockRedstoneRepeater || block instanceof BlockRedstoneComparator) {
            block.rotateBlock(world, pos, EnumFacing.fromAngle(90));
            stack.damageItem(1, player);
            return EnumActionResult.SUCCESS;
        }

        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }
}
