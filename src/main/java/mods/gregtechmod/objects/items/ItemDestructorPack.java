package mods.gregtechmod.objects.items;

import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import ic2.core.item.IHandHeldInventory;
import ic2.core.item.tool.HandHeldInventory;
import mods.gregtechmod.gui.GuiDestructorPack;
import mods.gregtechmod.objects.items.base.ItemBase;
import mods.gregtechmod.objects.items.containers.ContainerDestructorpack;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class ItemDestructorPack extends ItemBase implements IHandHeldInventory {

    public ItemDestructorPack(String name, @Nullable String description) {
        super(name, description);
        setMaxStackSize(1);
        setNoRepair();
    }

    @Override
    public IHasGui getInventory(EntityPlayer entityPlayer, ItemStack itemStack) {
        return new HandHeldDestructorPack(entityPlayer, itemStack, 1);
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
