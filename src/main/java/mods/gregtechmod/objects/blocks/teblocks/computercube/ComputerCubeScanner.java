package mods.gregtechmod.objects.blocks.teblocks.computercube;

import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotOutput;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.gui.GuiComputerCubeScanner;
import mods.gregtechmod.inventory.invslot.GtSlotFiltered;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerComputerCubeScanner;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class ComputerCubeScanner implements IComputerCubeModule {
    private static final ResourceLocation NAME = new ResourceLocation(Reference.MODID, "scanner");
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "blocks/machines/computer_cube/computer_cube_scanner");
    private static final Predicate<ItemStack> INPUT_PREDICATE = stack -> stack.getItem() == ModHandler.cropSeedBag;
    
    private final TileEntityComputerCube parent;
    public final InvSlot inputSlot;
    public final InvSlot outputSlot;
    @NBTPersistent
    private int progress;
    
    public ComputerCubeScanner(TileEntityComputerCube parent) {
        this.parent = parent;
        
        this.inputSlot = new GtSlotFiltered(parent, "input", InvSlot.Access.I, 2, INPUT_PREDICATE);
        this.outputSlot = new InvSlotOutput(parent, "output", 2);
    }

    public int getProgress() {
        return this.progress;
    }

    @Override
    public ResourceLocation getName() {
        return NAME;
    }

    @Override
    public boolean updateServer() {
        moveStack(this.inputSlot, 0, this.inputSlot, 1);
        moveStack(this.outputSlot, 0, this.outputSlot, 1);

        NBTTagCompound tag = this.inputSlot.get(1).getTagCompound();
        if (tag != null) {
            byte scan = tag.getByte("scan");
            if (scan < 4) {
                if (this.progress >= 100) {
                    tag.setByte("scan", (byte) 4);
                    this.progress = 0;
                    return true;
                } else if (this.parent.useEnergy(100, false) >= 100) {
                    ++this.progress;
                    return true;
                }
            } else return stop();
        }
        
        return stop();
    }
    
    private boolean stop() {
        boolean updateNetwork = this.progress != 0;
        this.progress = 0;
        moveStack(this.inputSlot, 1, this.outputSlot, 0);
        return updateNetwork;
    }
    
    public static void moveStack(InvSlot src, int srcIndex, InvSlot dest, int destIndex) {
        ItemStack srcStack = src.get(srcIndex);
        if (!srcStack.isEmpty()) {
            ItemStack destStack = dest.get(destIndex);
            if (destStack.isEmpty()) {
                dest.put(destIndex, srcStack);
                src.clear(srcIndex);
            }
        }
    }

    @Override
    public ContainerComputerCubeScanner getGuiContainer(EntityPlayer player, TileEntityComputerCube base) {
        return new ContainerComputerCubeScanner(player, base);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin, TileEntityComputerCube base) {
        return new GuiComputerCubeScanner(getGuiContainer(player, base));
    }

    @Nullable
    @Override
    public ResourceLocation getTexture() {
        return TEXTURE;
    }
}
