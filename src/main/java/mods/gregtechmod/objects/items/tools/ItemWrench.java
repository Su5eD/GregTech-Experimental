package mods.gregtechmod.objects.items.tools;

import ic2.api.tile.IWrenchable;
import ic2.core.IC2;
import ic2.core.audio.PositionSpec;
import ic2.core.item.tool.ItemToolWrench;
import ic2.core.util.RotationUtil;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.util.IModelInfoProvider;
import mods.gregtechmod.util.ModelInformation;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemWrench extends ItemToolWrench implements IModelInfoProvider {
    public final String name;
    protected final int durability;
    protected int rotateDamage = 1;
    protected int removeDamage = 10;

    public ItemWrench(String name, int durability) {
        super(null);
        this.name = name;
        this.durability = durability;
        setMaxDamage(durability);
    }

    @Override
    public String getTranslationKey() {
        return Reference.MODID+".item."+name;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        Block block = world.getBlockState(pos).getBlock();
        ItemStack currentStack = player.getHeldItem(hand);

        if (block instanceof IWrenchable && this.canTakeDamage(currentStack, rotateDamage)) {
            EnumFacing face = ((IWrenchable) block).getFacing(world, pos);
            EnumFacing rotated = RotationUtil.rotateByHit(side, hitX, hitY, hitZ);
            if (rotated == face && !canTakeDamage(currentStack, removeDamage)) return EnumActionResult.FAIL;
            ItemToolWrench.wrenchBlock(world, pos, rotated, player, true);
            this.damage(currentStack, world.getBlockState(pos).getBlock() == Blocks.AIR ? removeDamage : rotateDamage, player);

            if (world.isRemote) IC2.audioManager.playOnce(player, PositionSpec.Hand, "Tools/wrench.ogg", true, IC2.audioManager.getDefaultVolume());
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }

    @Override
    public ModelInformation getModelInformation() {
        return new ModelInformation(GregTechMod.getModelResourceLocation(this.name, "tool"));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add("To dismantle and rotate blocks of most mods");
        tooltip.add("Rotation of target depends on where exactly you click");
    }
}
