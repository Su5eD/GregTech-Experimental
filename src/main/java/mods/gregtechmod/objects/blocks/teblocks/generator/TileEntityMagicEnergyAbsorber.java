package mods.gregtechmod.objects.blocks.teblocks.generator;

import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.core.IHasGui;
import ic2.core.block.invslot.InvSlotConsumable;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.util.StackUtil;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.gui.GuiMagicEnergyAbsorber;
import mods.gregtechmod.inventory.invslot.GtSlotConsumable;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityGenerator;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerMagicEnergyAbsorber;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.Optional;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.common.entities.monster.EntityWisp;

import java.util.ArrayList;
import java.util.List;

public class TileEntityMagicEnergyAbsorber extends TileEntityGenerator implements IHasGui, INetworkClientTileEntityEventListener {
    private static final List<EntityEnderCrystal> usedDragonCrystalList = new ArrayList<>();
    private EntityEnderCrystal targetedCrystal;
    
    public boolean drainCrystalEnergy;
    public boolean drainAura;
    
    public final InvSlotConsumable inputSlot;
    public final InvSlotOutput outputSlot;

    public TileEntityMagicEnergyAbsorber() {
        super("magic_energy_absorber");
        
        this.inputSlot = new GtSlotConsumable(this, "input", 1);
        this.outputSlot = new InvSlotOutput(this, "output", 1);
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();

        if (isAllowedToWork() && this.tickCounter % 10 == 0) {
            if (canAddEnergy()) {
                ItemStack input = this.inputSlot.get();
                if (!input.isEmpty() && input.isItemEnchanted() && input.getItem().getItemEnchantability() > 0) {
                    NBTTagList enchantments = input.getEnchantmentTagList();
                    if (!enchantments.isEmpty()) {
                        for (int i = 0; i < enchantments.tagCount(); i++) {
                            NBTTagCompound compound = enchantments.getCompoundTagAt(i);
                            short id = compound.getShort("id");
                            short level = compound.getShort("lvl");

                            Enchantment enchantment = Enchantment.getEnchantmentByID(id);
                            if (enchantment != null)
                                addEnergy(1000000 * (level / (double) (enchantment.getMaxLevel() * enchantment.getRarity().getWeight())));
                        }
                        input.getTagCompound().removeTag("ench");
                        
                        if (StackUtil.checkItemEquality(input, Items.ENCHANTED_BOOK)) this.outputSlot.add(new ItemStack(Items.BOOK));
                        else this.outputSlot.add(input);
                        
                        this.inputSlot.consume(1);
                    }
                }
            }
            
            if (GregTechConfig.MACHINES.magicEnergyAbsorber.energyPerEnderCrystal > 0 && this.drainCrystalEnergy) {
                if (this.targetedCrystal == null) {
                    int x = this.pos.getX();
                    int y = this.pos.getY();
                    int z = this.pos.getZ();
                    List<EntityEnderCrystal> list = this.world.getEntitiesWithinAABB(EntityEnderCrystal.class, new AxisAlignedBB(x - 64, y - 64, z - 64, x + 64, y + 64, z + 64));
                    list.removeAll(usedDragonCrystalList);
                    if (!list.isEmpty()) {
                        this.targetedCrystal = list.get(0);
                        if (this.targetedCrystal != null) usedDragonCrystalList.add(this.targetedCrystal);
                    } 
                } else if (this.targetedCrystal.isEntityAlive()) {
                    addEnergy(GregTechConfig.MACHINES.magicEnergyAbsorber.energyPerEnderCrystal);
                } else {
                    usedDragonCrystalList.remove(this.targetedCrystal);
                    this.targetedCrystal = null;
                }  
            }
            
            if (ModHandler.thaumcraft) {
                if (this.drainAura && GregTechConfig.MACHINES.magicEnergyAbsorber.energyFromVis > 0 && getStoredEU() < GregTechConfig.MACHINES.magicEnergyAbsorber.energyFromVis) {
                    drainAura();
                }
                
                if (shouldExplode) {
                    int minFlux = (int) (GregTechConfig.MACHINES.magicEnergyAbsorber.energyFromVis / 3200D);
                    int maxFlux = (int) (GregTechConfig.MACHINES.magicEnergyAbsorber.energyFromVis / 1600D);
                    ModHandler.polluteAura(this.world, this.pos, minFlux + this.world.rand.nextInt(maxFlux), true);
                    polluteAura();
                }
            }
            
            setActive(getStoredEU() >= getMaxOutputEUt() + getMinimumStoredEnergy());
        }
    }
    
    @Optional.Method(modid = "thaumcraft")
    private void drainAura() {
        if (AuraHelper.drainVis(this.world, this.pos, 1, false) >= 1) {
            addEnergy(GregTechConfig.MACHINES.magicEnergyAbsorber.energyFromVis);
            
            int x = this.pos.getX();
            int y = this.pos.getY();
            int z = this.pos.getZ();
            List<EntityWisp> wisps = this.world.getEntitiesWithinAABB(EntityWisp.class, new AxisAlignedBB(x - 8, y - 8, z - 8, x + 8, y + 8, z + 8));
            if (!wisps.isEmpty()) {
                markForExplosion();
            }
        }
    }
    
    @Optional.Method(modid = "thaumcraft")
    private void polluteAura() {
        AuraHelper.polluteAura(this.world, this.pos, 50 + this.world.rand.nextInt(50), true);
    }

    @Override
    public int getSourceTier() {
        return 2;
    }

    @Override
    protected int getBaseEUCapacity() {
        return (int) Math.max(1000000, Math.max(GregTechConfig.MACHINES.magicEnergyAbsorber.energyFromVis, GregTechConfig.MACHINES.magicEnergyAbsorber.energyPerEnderCrystal / 10D));
    }

    @Override
    protected double getMaxOutputEUp() {
        return Math.max(128, GregTechConfig.MACHINES.magicEnergyAbsorber.energyPerEnderCrystal / 10D);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("drainCrystalEnergy", this.drainCrystalEnergy);
        nbt.setBoolean("drainAura", this.drainAura);
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.drainCrystalEnergy = nbt.getBoolean("drainCrystalEnergy");
        this.drainAura = nbt.getBoolean("drainAura");
    }
    
    @Override
    public void onNetworkEvent(EntityPlayer player, int event) {
        boolean value = event % 2 != 0;
        switch (event) {
            case 0:
            case 1:
                this.drainAura = value;
                break;
            case 2:
            case 3:    
                this.drainCrystalEnergy = value;
                break;
        }
    }

    @Override
    public ContainerMagicEnergyAbsorber getGuiContainer(EntityPlayer player) {
        return new ContainerMagicEnergyAbsorber(player, this);
    }

    @Override
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiMagicEnergyAbsorber(getGuiContainer(player));
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {}
}
