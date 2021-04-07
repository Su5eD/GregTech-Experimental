package mods.gregtechmod.objects.blocks.tileentities.teblocks;

import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.gui.GuiAutoElectricFurnace;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.base.TileEntityBasicMachine;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.container.ContainerBasicMachine;
import mods.gregtechmod.recipe.compat.ModRecipes;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TileEntityAutoElectricFurnace extends TileEntityBasicMachine {
    private int heatingCoilTier;

    public TileEntityAutoElectricFurnace() {
        super("auto_electric_furnace", ModRecipes.FURNACE);
    }

    @Override
    protected boolean onActivated(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            ItemStack stack = player.inventory.getCurrentItem();
            Item item = stack.getItem();
            boolean temp = false;

            if (this.heatingCoilTier <= 0 && item == BlockItems.Component.COIL_KANTHAL.getInstance()) {
                temp = true;
                this.heatingCoilTier = 1;
            }
            if (this.heatingCoilTier == 1 && item == BlockItems.Component.COIL_NICHROME.getInstance()) {
                temp = true;
                this.heatingCoilTier = 2;
            }

            if (temp) {
                if (!player.capabilities.isCreativeMode) stack.shrink(1);
                return true;
            }
        }
        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
    }

    @Override
    protected void prepareRecipeForProcessing(IMachineRecipe<IRecipeIngredient, List<ItemStack>> recipe) {
        super.prepareRecipeForProcessing(recipe);
        if (this.heatingCoilTier > 0) this.maxProgress /= this.heatingCoilTier;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagCompound ret = super.writeToNBT(nbt);
        ret.setInteger("heatingCoilTier", this.heatingCoilTier);
        return ret;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.heatingCoilTier = nbt.getInteger("heatingCoilTier");
    }

    @Override
    protected List<ItemStack> getAuxDrops(int fortune) {
        List<ItemStack> ret = super.getAuxDrops(fortune);
        if (this.heatingCoilTier > 0) ret.add(new ItemStack(BlockItems.Component.COIL_KANTHAL.getInstance()));
        if (this.heatingCoilTier > 1) ret.add(new ItemStack(BlockItems.Component.COIL_NICHROME.getInstance()));
        return ret;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiAutoElectricFurnace(new ContainerBasicMachine<>(player, this));
    }
}
