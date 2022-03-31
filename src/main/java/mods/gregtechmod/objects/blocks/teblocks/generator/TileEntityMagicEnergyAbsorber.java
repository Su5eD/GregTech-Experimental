package mods.gregtechmod.objects.blocks.teblocks.generator;

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
import mods.gregtechmod.util.nbt.NBTPersistent;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.common.entities.monster.EntityWisp;

import java.util.ArrayList;
import java.util.List;

public class TileEntityMagicEnergyAbsorber extends TileEntityGenerator implements IHasGui {
    private static final List<EntityEnderCrystal> USED_ENDER_CRYSTALS = new ArrayList<>();

    public final InvSlotConsumable inputSlot;
    public final InvSlotOutput outputSlot;
    @NBTPersistent
    public boolean drainCrystalEnergy;
    @NBTPersistent
    public boolean drainAura;
    private EntityEnderCrystal targetedCrystal;

    public TileEntityMagicEnergyAbsorber() {
        this.inputSlot = new GtSlotConsumable(this, "input", 1);
        this.outputSlot = new InvSlotOutput(this, "output", 1);
    }

    public void switchDrainAura() {
        this.drainAura = !this.drainAura;
    }

    public void switchDrainCrystalEnergy() {
        this.drainCrystalEnergy = !this.drainCrystalEnergy;
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
                            if (enchantment != null) addEnergy(1000000 * (level / (double) (enchantment.getMaxLevel() * enchantment.getRarity().getWeight())));
                        }
                        input.getTagCompound().removeTag("ench");

                        if (StackUtil.checkItemEquality(input, Items.ENCHANTED_BOOK)) this.outputSlot.add(new ItemStack(Items.BOOK));
                        else this.outputSlot.add(input);

                        this.inputSlot.consume(1);
                    }
                }
            }

            if (GregTechConfig.MACHINES.MAGIC_ENERGY_ABSORBER.energyPerEnderCrystal > 0 && this.drainCrystalEnergy) {
                if (this.targetedCrystal == null) {
                    AxisAlignedBB range = new AxisAlignedBB(this.pos.add(-64, -64, -64), this.pos.add(64, 64, 64));
                    List<EntityEnderCrystal> list = this.world.getEntitiesWithinAABB(EntityEnderCrystal.class, range);

                    list.removeAll(USED_ENDER_CRYSTALS);
                    if (!list.isEmpty()) {
                        this.targetedCrystal = list.get(0);
                        USED_ENDER_CRYSTALS.add(this.targetedCrystal);
                    }
                }
                else if (this.targetedCrystal.isEntityAlive()) {
                    addEnergy(GregTechConfig.MACHINES.MAGIC_ENERGY_ABSORBER.energyPerEnderCrystal);
                }
                else {
                    USED_ENDER_CRYSTALS.remove(this.targetedCrystal);
                    this.targetedCrystal = null;
                }
            }

            if (ModHandler.thaumcraft) {
                if (this.drainAura && GregTechConfig.MACHINES.MAGIC_ENERGY_ABSORBER.energyFromVis > 0 && getStoredEU() < GregTechConfig.MACHINES.MAGIC_ENERGY_ABSORBER.energyFromVis) {
                    drainAura();
                }

                if (this.shouldExplode) {
                    int minFlux = (int) (GregTechConfig.MACHINES.MAGIC_ENERGY_ABSORBER.energyFromVis / 3200D);
                    int maxFlux = (int) (GregTechConfig.MACHINES.MAGIC_ENERGY_ABSORBER.energyFromVis / 1600D);
                    ModHandler.polluteAura(this.world, this.pos, minFlux + this.world.rand.nextInt(maxFlux), true);
                    polluteAura();
                }
            }

            setActive(getStoredEU() >= getMaxOutputEUt() + getMinimumStoredEU());
        }
    }

    @Optional.Method(modid = "thaumcraft")
    private void drainAura() {
        if (AuraHelper.drainVis(this.world, this.pos, 1, false) >= 1) {
            addEnergy(GregTechConfig.MACHINES.MAGIC_ENERGY_ABSORBER.energyFromVis);

            AxisAlignedBB range = new AxisAlignedBB(this.pos.add(-8, -8, -8), this.pos.add(8, 8, 8));
            List<EntityWisp> wisps = this.world.getEntitiesWithinAABB(EntityWisp.class, range);

            if (!wisps.isEmpty()) markForExplosion();
        }
    }

    @Optional.Method(modid = "thaumcraft")
    private void polluteAura() {
        AuraHelper.polluteAura(this.world, this.pos, 50 + this.world.rand.nextInt(50), true);
    }

    @Override
    public int getBaseSourceTier() {
        return 2;
    }

    @Override
    protected int getBaseEUCapacity() {
        return (int) Math.max(1000000, Math.max(GregTechConfig.MACHINES.MAGIC_ENERGY_ABSORBER.energyFromVis, GregTechConfig.MACHINES.MAGIC_ENERGY_ABSORBER.energyPerEnderCrystal / 10D));
    }

    @Override
    public double getMaxOutputEUp() {
        return Math.max(128, GregTechConfig.MACHINES.MAGIC_ENERGY_ABSORBER.energyPerEnderCrystal / 10D);
    }

    @Override
    public ContainerMagicEnergyAbsorber getGuiContainer(EntityPlayer player) {
        return new ContainerMagicEnergyAbsorber(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiMagicEnergyAbsorber(getGuiContainer(player));
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {}
}
