package mods.gregtechmod.objects.items;

import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.item.IHandHeldInventory;
import ic2.core.item.tool.HandHeldInventory;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.gui.GuiDestructorPack;
import mods.gregtechmod.objects.items.base.ItemBase;
import mods.gregtechmod.objects.items.containers.ContainerDestructorpack;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDestructorPack extends ItemBase implements IHandHeldInventory {

    public ItemDestructorPack() {
        super("destructorpack");
        setFolder("tool");
        setRegistryName("destructorpack");
        setTranslationKey("destructorpack");
        setCreativeTab(GregTechMod.GREGTECH_TAB);
        setMaxStackSize(1);
        setNoRepair();
    }

    @Override
    public IHasGui getInventory(EntityPlayer entityPlayer, ItemStack itemStack) {
        return new HandHeldDestructorPack(entityPlayer, itemStack, 1);
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!player.world.isRemote) IC2.platform.launchGui(player, getInventory(player, stack));
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    public static class HandHeldDestructorPack extends HandHeldInventory {

        public HandHeldDestructorPack(EntityPlayer player, ItemStack containerStack, int inventorySize) {
            super(player, containerStack, inventorySize);
        }

        @Override
        public ContainerBase<HandHeldDestructorPack> getGuiContainer(EntityPlayer entityPlayer) {
            return new ContainerDestructorpack(entityPlayer, this);
        }

        @Override
        @SideOnly(Side.CLIENT)
        public GuiScreen getGui(EntityPlayer entityPlayer, boolean b) {
            return new GuiDestructorPack(new ContainerDestructorpack(entityPlayer, this));
        }

        @Override
        public String getName() {
            return "destructorpack";
        }

        @Override
        public boolean hasCustomName() {
            return false;
        }
    }
}
