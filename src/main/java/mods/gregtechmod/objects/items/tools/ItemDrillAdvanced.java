package mods.gregtechmod.objects.items.tools;

import ic2.core.IC2;
import ic2.core.item.tool.HarvestLevel;
import ic2.core.item.tool.ItemElectricTool;
import ic2.core.item.tool.ToolClass;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.util.IModelInfoProvider;
import mods.gregtechmod.util.ModelInformation;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
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

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class ItemDrillAdvanced extends ItemElectricTool implements IModelInfoProvider {

    public ItemDrillAdvanced() {
        super(null, 800, HarvestLevel.Iridium, EnumSet.of(ToolClass.Pickaxe, ToolClass.Shovel));
        setRegistryName("drill_advanced");
        setCreativeTab(GregTechMod.GREGTECH_TAB);
        this.maxCharge = 400000;
        this.transferLimit = 1000;
        this.tier = 4;
        this.efficiency = 26.0F;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add("For quickly making holes");
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public String getTranslationKey() {
        return Reference.MODID+".item.drill_advanced";
    }

    @Override
    public int getTier(ItemStack stack) {
        return 2;
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if (!world.isRemote && IC2.keyboard.isModeSwitchKeyDown(player)) {
            Map<Enchantment, Integer> enchantmentMap = new IdentityHashMap<>();
            ItemStack stack = player.getHeldItem(hand);
            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) == 0) {
                enchantmentMap.put(Enchantments.SILK_TOUCH, 1);
                IC2.platform.messagePlayer(player, "ic2.tooltip.mode", "ic2.tooltip.mode.silkTouch");
            } else {
                IC2.platform.messagePlayer(player, "ic2.tooltip.mode", "ic2.tooltip.mode.normal");
            }

            EnchantmentHelper.setEnchantments(enchantmentMap, stack);
        }
        return super.onItemRightClick(world, player, hand);
    }

    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float xOffset, float yOffset, float zOffset) {
        if (IC2.keyboard.isModeSwitchKeyDown(player)) return EnumActionResult.PASS;
        return super.onItemUse(player, world, pos, hand, side, xOffset, yOffset, zOffset);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        float speed = super.getDestroySpeed(stack, state);
        EntityPlayer player = getPlayerHoldingItem(stack);
        if (player != null) {
            if (player.isInsideOfMaterial(Material.WATER) && !EnchantmentHelper.getAquaAffinityModifier(player))
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
    public ModelInformation getModelInformation() {
        return new ModelInformation(GregTechMod.getModelResourceLocation("drill_advanced", "tool"));
    }
}
