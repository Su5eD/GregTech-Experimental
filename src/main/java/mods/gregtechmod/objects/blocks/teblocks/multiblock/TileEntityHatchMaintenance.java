package mods.gregtechmod.objects.blocks.teblocks.multiblock;

import ic2.core.IHasGui;
import ic2.core.block.state.Ic2BlockState;
import mods.gregtechmod.api.item.ISolderingTool;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.gui.GuiHatchMaintenance;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityCoverBehavior;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerHatchMaintenance;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.OreDictUnificator;
import mods.gregtechmod.util.PropertyHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class TileEntityHatchMaintenance extends TileEntityCoverBehavior implements IHasGui {
    private static final PropertyHelper.TextureOverride DUCT_TAPE_TEXTURE_OVERRIDE;
    
    static {
        Map<EnumFacing, ResourceLocation> textures = Collections.singletonMap(EnumFacing.NORTH, new ResourceLocation(Reference.MODID, "blocks/machines/hatch_maintenance/hatch_maintenance_front_ducttape"));
        DUCT_TAPE_TEXTURE_OVERRIDE = new PropertyHelper.TextureOverride(textures);
    }

    public boolean ductTape;
    public boolean wrench;
    public boolean screwdriver;
    public boolean softHammer;
    public boolean hardHammer;
    public boolean solderingTool;
    public boolean crowbar;
    
    private int runtime;

    public TileEntityHatchMaintenance() {
        super("hatch_maintenance");
    }

    @Override
    protected boolean onActivatedChecked(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (side != getFacing()) return true;
        return super.onActivatedChecked(player, hand, side, hitX, hitY, hitZ);
    }
    
    public void onToolClick(ItemStack stack, EntityPlayer player) {
        if (checkTool(stack, GtUtil::isWrench, player)) this.wrench = true;
        else if (checkTool(stack, GtUtil::isScrewdriver, player)) this.screwdriver = true;
        else if (checkTool(stack, GtUtil::isSoftHammer, player)) this.softHammer = true;
        else if (checkTool(stack, GtUtil::isHardHammer, player)) this.hardHammer = true;
        else if (checkTool(stack, GtUtil::isCrowbar, player)) this.crowbar = true;
        else {
            Item item = stack.getItem();
            if (item instanceof ISolderingTool && ((ISolderingTool) item).solder(stack, player, false)) this.solderingTool = true;
            else if (OreDictUnificator.isItemInstanceOf(stack, "craftingDuctTape", false)) {
                this.ductTape = this.wrench = this.screwdriver = this.softHammer = this.hardHammer = this.solderingTool = this.crowbar = true;
                stack.shrink(1);
                rerender();
            }
        }
    }
    
    private boolean checkTool(ItemStack stack, Predicate<ItemStack> predicate, EntityPlayer player) {
        return predicate.test(stack) && GtUtil.damageStack(player, stack, 1);
    }

    public void doRandomMaintenanceDamage() {
        /* TODO Implement this in the multiblock base class
        if (!isCorrectMachinePart(this.mInventory[1]) || getRepairStatus() == 0) {
              stopMachine();
              return false;
            } 
         */
        
        if (this.runtime++ > 1000) {
            this.runtime = 0;
            if (this.world.rand.nextInt(6000) == 0) {
                switch (this.world.rand.nextInt(6)) {
                    case 0:
                        this.wrench = false;
                        break;
                    case 1:
                        this.screwdriver = false;
                        break;
                    case 2:
                        this.softHammer = false;
                        break;
                    case 3:
                        this.hardHammer = false;
                        break;
                    case 4:
                        this.solderingTool = false;
                        break;
                    case 5:
                        this.crowbar = false;
                        break;
                }
            }
        }
        
        /* TODO: Implement this in the multiblock base class
        if (this.mInventory[1] != null && getBaseMetaTileEntity().getRandomNumber(2) == 0) {
                this.mInventory[1].setItemDamage(this.mInventory[1].getItemDamage() + getDamageToComponent(this.mInventory[1]));
                if (this.mInventory[1].getItemDamage() >= this.mInventory[1].getMaxDamage()) {
                  if (explodesOnComponentBreak(this.mInventory[1])) {
                    this.mInventory[1] = null;
                    getBaseMetaTileEntity().doExplosion(1000000);
                  } else {
                    this.mInventory[1] = null;
                  } 
                  return false;
                }
         */
    }

    @Override
    protected Ic2BlockState.Ic2BlockStateInstance getExtendedState(Ic2BlockState.Ic2BlockStateInstance state) {
        if (this.ductTape) {
            return state.withProperty(PropertyHelper.TEXTURE_OVERRIDE_PROPERTY, DUCT_TAPE_TEXTURE_OVERRIDE);
        }
        return super.getExtendedState(state);
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("ductTape");
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.ductTape = nbt.getBoolean("ductTape");
        this.wrench = nbt.getBoolean("wrench");
        this.screwdriver = nbt.getBoolean("screwdriver");
        this.softHammer = nbt.getBoolean("softHammer");
        this.hardHammer = nbt.getBoolean("hardHammer");
        this.solderingTool = nbt.getBoolean("solderingTool");
        this.crowbar = nbt.getBoolean("crowbar");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("ductTape", this.ductTape);
        nbt.setBoolean("wrench", this.wrench);
        nbt.setBoolean("screwdriver", this.screwdriver);
        nbt.setBoolean("softHammer", this.softHammer);
        nbt.setBoolean("hardHammer", this.hardHammer);
        nbt.setBoolean("solderingTool", this.solderingTool);
        nbt.setBoolean("crowbar", this.crowbar);
        
        return super.writeToNBT(nbt);
    }

    @Override
    public ContainerHatchMaintenance getGuiContainer(EntityPlayer player) {
        return new ContainerHatchMaintenance(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiHatchMaintenance(getGuiContainer(player));
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {}
}
