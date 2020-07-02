package mods.gregtechmod.common.objects.items.tools;

import ic2.core.IC2;
import ic2.core.item.tool.HarvestLevel;
import ic2.core.item.tool.ItemElectricTool;
import ic2.core.item.tool.ToolClass;
import mods.gregtechmod.common.core.GregtechMod;
import mods.gregtechmod.common.init.ItemInit;
import mods.gregtechmod.common.util.IHasModel;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.EnumSet;
import java.util.IdentityHashMap;
import java.util.Map;

public class ItemDrillAdvanced extends ItemElectricTool implements IHasModel {

    public ItemDrillAdvanced() {
        super(null, 800, HarvestLevel.Iridium, EnumSet.of(ToolClass.Pickaxe, ToolClass.Shovel));
        setRegistryName("drill_advanced");
        this.maxCharge = 300000;
        this.transferLimit = 1000;
        this.tier = 3;
        this.efficiency = 24.0F;
        ItemInit.ITEMS.put("drill_advanced", this);
    }

    @Override
    public String getTranslationKey() {
        return "item.drill_advanced";
    }

    protected ItemStack getItemStack(double charge) {
        ItemStack ret = super.getItemStack(charge);
        Map<Enchantment, Integer> enchantmentMap = new IdentityHashMap<>();
        enchantmentMap.put(Enchantments.FORTUNE, Integer.valueOf(3));
        EnchantmentHelper.setEnchantments(enchantmentMap, ret);
        return ret;
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if (!world.isRemote && GregtechMod.ic2modekey.isKeyDown()) {
            Map<Enchantment, Integer> enchantmentMap = new IdentityHashMap<>();
            enchantmentMap.put(Enchantments.FORTUNE, Integer.valueOf(3));
            ItemStack stack = player.getHeldItem(hand);
            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) == 0) {
                enchantmentMap.put(Enchantments.SILK_TOUCH, Integer.valueOf(1));
                IC2.platform.messagePlayer(player, "ic2.tooltip.mode", "ic2.tooltip.mode.silkTouch");
            } else {
                IC2.platform.messagePlayer(player, "ic2.tooltip.mode", "ic2.tooltip.mode.normal");
            }
            EnchantmentHelper.setEnchantments(enchantmentMap, stack);
        }
        return super.onItemRightClick(world, player, hand);
    }

    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float xOffset, float yOffset, float zOffset) {
        if (GregtechMod.ic2modekey.isKeyDown())
            return EnumActionResult.PASS;
        return super.onItemUse(player, world, pos, hand, side, xOffset, yOffset, zOffset);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        float speed = super.getDestroySpeed(stack, state);
        EntityPlayer player = getPlayerHoldingItem(stack);
        if (player != null) {
            if (player.isInsideOfMaterial(Material.WATER) && !EnchantmentHelper.getAquaAffinityModifier((EntityLivingBase)player))
                speed *= 5.0F;
            if (!player.onGround)
                speed *= 5.0F;
        }
        return speed;
    }

    private static EntityPlayer getPlayerHoldingItem(ItemStack stack) {
        for (EntityPlayer player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
            if (player.inventory.getCurrentItem() == stack)
                return player;
        }
        return null;
    }

    @Override
    public void registerModels() {
        GregtechMod.proxy.registerModel(this, 0);
    }
}
