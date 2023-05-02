package mods.gregtechmod.objects.items.tools;

import buildcraft.api.tools.IToolWrench;
import com.google.common.collect.Multimap;
import ic2.api.item.IEnhancedOverlayProvider;
import ic2.api.tile.IWrenchable;
import ic2.api.transport.IPipe;
import ic2.core.IC2;
import ic2.core.audio.PositionSpec;
import ic2.core.item.tool.ItemToolWrench;
import ic2.core.util.RotationUtil;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.util.*;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

@Optional.Interface(modid = "buildcraftlib", iface = "buildcraft.api.tools.IToolWrench")
public class ItemWrench extends ItemToolWrench implements ICustomItemModel, IToolWrench, IEnhancedOverlayProvider, IRepairableCraftingItem {
    public final String name;
    protected final int entityDamage;
    protected int rotateDamage = 1;
    protected int removeDamage = 10;
    protected List<String> effectiveEntities = Arrays.asList("minecraft:villager_golem", "twilightforest:tower_golem", "thaumcraft:golem");
    protected boolean showDurability = true;

    public ItemWrench(String name, int entityDamage) {
        this(name, 28, entityDamage);
    }

    public ItemWrench(String name, int durability, int entityDamage) {
        super(null);
        this.name = name;
        this.entityDamage = entityDamage;
        setMaxDamage(durability - 1);
        setNoRepair();
        GregTechAPI.instance().registerWrench(this);
    }

    @Override
    public boolean isCraftingRepairable() {
        return true;
    }

    @Override
    public boolean providesEnhancedOverlay(World world, BlockPos pos, EnumFacing facing, EntityPlayer player, ItemStack stack) {
        if (GregTechConfig.GENERAL.enhancedWrenchOverlay) {
            Block block = world.getBlockState(pos).getBlock();
            return block instanceof IWrenchable && (world.getTileEntity(pos) instanceof IPipe || Arrays.stream(EnumFacing.VALUES).anyMatch(side -> ((IWrenchable) block).canSetFacing(world, pos, side, player)));
        }
        return false;
    }

    @Override
    public String getTranslationKey() {
        return GtLocale.buildKeyItem(this.name);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        Block block = world.getBlockState(pos).getBlock();
        ItemStack stack = player.getHeldItem(hand);

        if (this.canTakeDamage(stack, this.rotateDamage)) {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof IPipe) {
                EnumFacing rotated = RotationUtil.rotateByHit(side, hitX, hitY, hitZ);
                ((IPipe) te).flipConnection(rotated);

                TileEntity neighborPipe = world.getTileEntity(pos.offset(rotated));
                if (neighborPipe instanceof IPipe) {
                    EnumFacing rotatedOpposite = rotated.getOpposite();
                    if (((IPipe) te).isConnected(rotated) != ((IPipe) neighborPipe).isConnected(rotatedOpposite)) {
                        ((IPipe) neighborPipe).flipConnection(rotatedOpposite);
                    }
                }

                playWrenchSound(world, player);
                return EnumActionResult.SUCCESS;
            }
            else if (block instanceof IWrenchable) {
                EnumFacing face = ((IWrenchable) block).getFacing(world, pos);
                EnumFacing rotated = RotationUtil.rotateByHit(side, hitX, hitY, hitZ);
                if (rotated == face && !canTakeDamage(stack, this.removeDamage)) return EnumActionResult.FAIL;

                String result = ((Object) ItemToolWrench.wrenchBlock(world, pos, rotated, player, true)).toString();
                int damage = result.equals("Removed") ? this.removeDamage : result.equals("Rotated") ? this.rotateDamage : 0;
                GtUtil.damageStack(player, stack, damage);

                playWrenchSound(world, player);
                return EnumActionResult.SUCCESS;
            }
        }

        return EnumActionResult.PASS;
    }

    private void playWrenchSound(World world, EntityPlayer player) {
        if (world.isRemote) IC2.audioManager.playOnce(player, PositionSpec.Hand, "Tools/wrench.ogg", true, IC2.audioManager.getDefaultVolume());
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(3, attacker);
        ResourceLocation entityName = EntityList.getKey(target);
        if (entityName != null && this.effectiveEntities.contains(entityName.toString())) GtUtil.damageEntity(target, attacker, this.entityDamage);
        return true;
    }

    @Override
    public ResourceLocation getItemModel() {
        return GtUtil.getModelResourceLocation(this.name, "tool");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (this.showDurability) tooltip.add((stack.getMaxDamage() - stack.getItemDamage() + 1) + " / " + (stack.getMaxDamage() + 1));
        if (ModHandler.buildcraftCore) tooltip.add(GtLocale.translateItem("wrench.description_bc"));
        tooltip.add(GtLocale.translateItem("wrench.description"));
        tooltip.add(GtLocale.translateItem("wrench.description_2"));
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
        if (slot == EntityEquipmentSlot.MAINHAND) multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", this.entityDamage - 1, 0));
        return multimap;
    }

    @Override
    public boolean canWrench(EntityPlayer player, EnumHand hand, ItemStack wrench, RayTraceResult rayTrace) {
        return true;
    }

    @Override
    public void wrenchUsed(EntityPlayer player, EnumHand hand, ItemStack wrench, RayTraceResult rayTrace) {
        wrench.damageItem(1, player);
        playWrenchSound(player.world, player);
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        ItemStack copy = stack.copy();
        return copy.attemptDamageItem(8, JavaUtil.RANDOM, null) ? ItemStack.EMPTY : copy;
    }
}
