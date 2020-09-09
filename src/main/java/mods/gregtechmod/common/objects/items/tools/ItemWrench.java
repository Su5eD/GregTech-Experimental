package mods.gregtechmod.common.objects.items.tools;

import buildcraft.api.tools.IToolWrench;
import ic2.api.tile.IWrenchable;
import ic2.core.IC2;
import ic2.core.audio.PositionSpec;
import ic2.core.item.tool.ItemToolWrench;
import ic2.core.util.RotationUtil;
import mods.gregtechmod.common.core.GregtechMod;
import mods.gregtechmod.common.util.IModelInfoProvider;
import mods.gregtechmod.common.util.ModelInformation;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nullable;
import java.util.List;

public class ItemWrench extends ItemToolWrench implements IModelInfoProvider, IToolWrench {
    public final String name;
    protected final int durability;
    protected int rotateDamage = 1;
    protected int removeDamage = 10;

    public ItemWrench(String type, int durability) {
        super(null);
        this.name = "wrench_"+type;
        this.durability = durability;
        setRegistryName(name);
        setMaxDamage(durability);
    }

    @Override
    public String getTranslationKey() {
        return "item."+name;
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
    public boolean canWrench(EntityPlayer player, EnumHand hand, ItemStack wrench, RayTraceResult rayTrace) {
        return true;
    }

    @Override
    public void wrenchUsed(EntityPlayer player, EnumHand hand, ItemStack wrench, RayTraceResult rayTrace) {
        wrench.damageItem(1, player);
        IC2.audioManager.playOnce(player, "Tools/wrench.ogg");
    }

    @Override
    public ModelInformation getModelInformation() {
        return new ModelInformation(GregtechMod.getModelResourceLocation(this.name, "tool"));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (Loader.isModLoaded("buildcraft")) tooltip.add("Works as a BuildCraft wrench, too");
        tooltip.add("To dismantle and rotate blocks of most mods");
        tooltip.add("Rotation of target depends on where exactly you click");
    }
}
