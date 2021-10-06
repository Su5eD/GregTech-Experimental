package mods.gregtechmod.objects.blocks.teblocks;

import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipe;
import ic2.api.recipe.Recipes;
import ic2.core.IHasGui;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.profile.NotExperimental;
import ic2.core.util.Util;
import mods.gregtechmod.api.machine.IPanelInfoProvider;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.gui.GuiMatterFabricator;
import mods.gregtechmod.inventory.invslot.GtSlotFiltered;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityEnergy;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerMatterFabricator;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.Iterator;

@NotExperimental
public class TileEntityMatterFabricator extends TileEntityEnergy implements IHasGui, IPanelInfoProvider {
    @NBTPersistent
    private int progress;
    private int progressLast;
    private double euLast;
    @NBTPersistent
    private int amplifier;
    private int amplifierLast;
    
    public final InvSlot amplifierSlot;
    public final InvSlotOutput output;
    
    public TileEntityMatterFabricator() {
        super(null);
        
        this.amplifierSlot = new GtSlotFiltered(this, "amplifiers", InvSlot.Access.I, 8, stack -> Recipes.matterAmplifier.apply(stack, true) != null);
        this.output = new InvSlotOutput(this, "output", 1);
        
        addGuiValue("progress", () -> Math.max(0, Math.min(30000, this.progress / Math.max(1, getMaxProgress() / 100))));
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
        int maxProgress = getMaxProgress();
        
        setActive(this.euLast != getStoredEU() || this.amplifierLast != this.amplifier || this.progress != this.progressLast);
        this.euLast = getStoredEU();
        this.amplifierLast = this.amplifier;
        this.progressLast = this.progress;
        
        if (this.euLast > 0 && this.world.getRedstonePowerFromNeighbors(this.pos) <= 0) {
            Iterator<? extends MachineRecipe<IRecipeInput, Integer>> it = Recipes.matterAmplifier.getRecipes().iterator();
            
            while (this.amplifier < 100000 && it.hasNext()) {
                MachineRecipe<IRecipeInput, Integer> amp = it.next();
                IRecipeInput input = amp.getInput();
                int value = amp.getOutput() * (maxProgress / 166666);
                if (value > 0) {
                    for (ItemStack stack : this.amplifierSlot) {
                        if (this.amplifier >= 100000) break;
                        
                        if (input.matches(stack)) {
                            this.amplifier += value;
                            stack.shrink(1);
                        }
                    }
                }
            }
            
            double used = Math.min(GregTechConfig.DISABLED_RECIPES.massFabricator ? 8192 : getEUCapacity(), Math.min(getStoredEU(), this.amplifier));
            if (used > 0) {
                this.progress += used;
                this.amplifier -= used;
                useEnergy(used, false);
            }
            
            while (this.progress > maxProgress && this.output.canAdd(ModHandler.uuMatter)) {
                this.progress -= maxProgress;
                this.output.add(ModHandler.uuMatter.copy());
            } 
        }
    }
    
    private int getMaxProgress() {
        return GregTechConfig.FEATURES.matterFabricationRate;
    }

    @Override
    protected Collection<EnumFacing> getSinkSides() {
        return Util.allFacings;
    }

    @Override
    public int getSinkTier() {
        return 5;
    }

    @Override
    public int getEUCapacity() {
        return 1000000;
    }

    @Override
    public ContainerMatterFabricator getGuiContainer(EntityPlayer player) {
        return new ContainerMatterFabricator(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiMatterFabricator(getGuiContainer(player));
    }

    @Override
    public void onGuiClosed(EntityPlayer player) {}

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String getMainInfo() {
        return GtUtil.translateInfo("progress");
    }

    @Override
    public String getSecondaryInfo() {
        return Math.max(0, this.progress / Math.max(1, getMaxProgress() / 100)) + "%";
    }

    @Override
    public String getTertiaryInfo() {
        return GtUtil.translateInfo("energy", getStoredEU());
    }
}
