package mods.gregtechmod.objects.blocks.teblocks.computercube;

import ic2.api.item.IC2Items;
import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
import ic2.core.block.invslot.InvSlot;
import ic2.core.init.MainConfig;
import ic2.core.util.ConfigUtil;
import mods.gregtechmod.api.util.IDataOrbSerializable;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.gui.GuiComputerCubeReactor;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerComputerCubeReactor;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ComputerCubeReactor implements IComputerCubeModule, IReactor, IDataOrbSerializable {
    private static final ResourceLocation NAME = new ResourceLocation(Reference.MODID, "reactor");
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "blocks/machines/computer_cube/computer_cube_reactor");

    private static final Collection<ItemStack> DEPLETED_ITEMS;
    public static final List<ItemStack> COMPONENTS;
    
    static {
        List<ItemStack> depletedItems = Stream.of("depleted_uranium", "depleted_dual_uranium", "depleted_quad_uranium", "depleted_mox", "depleted_dual_mox", "depleted_quad_mox")
                .map(s -> IC2Items.getItem("nuclear", s))
                .collect(Collectors.toList());
        if (GregTechMod.classic) depletedItems.add(IC2Items.getItem("depleted_isotope_fuel_rod"));
        DEPLETED_ITEMS = Collections.unmodifiableList(depletedItems);
        
        List<Item> components = new ArrayList<>();
        Stream.of(
                Stream.of("uranium_fuel_rod", "dual_uranium_fuel_rod", "quad_uranium_fuel_rod", "mox_fuel_rod", "dual_mox_fuel_rod", "quad_mox_fuel_rod")
                        .map(ModHandler.ic2ItemApi::getItem),
                Arrays.stream(BlockItems.NuclearFuelRod.values())
                        .map(BlockItems.NuclearFuelRod::getInstance), 
                Stream.of("neutron_reflector", "thick_neutron_reflector", "iridium_reflector",
                                "heat_storage", "tri_heat_storage", "hex_heat_storage")
                        .map(ModHandler.ic2ItemApi::getItem),
                Arrays.stream(BlockItems.NuclearCoolantPack.values())
                        .map(BlockItems.NuclearCoolantPack::getInstance),
                Stream.of("rsh_condensator", "lzh_condensator", 
                                "plating", "heat_plating", "containment_plating", 
                                "heat_vent", "reactor_heat_vent", "overclocked_heat_vent", "component_heat_vent", "advanced_heat_vent", 
                                "heat_exchanger", "reactor_heat_exchanger", "component_heat_exchanger", "advanced_heat_exchanger")
                        .map(ModHandler.ic2ItemApi::getItem),
                ForgeRegistries.ITEMS.getValuesCollection().stream()
                        .filter(item -> {
                            if (item instanceof IReactorComponent && !components.contains(item)) {
                                return item != ModHandler.lithiumFuelRod && (GregTechMod.classic || item != ModHandler.depletedIsotopeFuelRod && item != ModHandler.heatpack);
                            }
                            return false;
                        })
        )
                .flatMap(Function.identity())
                .forEach(components::add);
        
        COMPONENTS = components.stream()
                .map(ItemStack::new)
                .collect(Collectors.toList());
    }

    private final TileEntityComputerCube parent;
    public final InvSlot selection;
    public final InvSlot content;
    private final InvSlot contentCopy;

    @NBTPersistent
    private boolean started;
    @NBTPersistent
    private int heat;
    @NBTPersistent
    private int eu;
    @NBTPersistent
    private int euOut;
    @NBTPersistent
    private int maxHeat = 10000;
    @NBTPersistent
    private float hem = 1;
    @NBTPersistent
    private float explosionStrength;
    private int euTimer;
    private int euLast1;
    private int euLast2;
    private int euLast3;
    private int euLast4;

    public ComputerCubeReactor(TileEntityComputerCube parent) {
        this.parent = parent;
        this.selection = new InvSlot(parent, "selection", InvSlot.Access.NONE, 1);
        this.content = new InvSlot(parent, "content", InvSlot.Access.NONE, 54);
        this.contentCopy = new InvSlot(parent, "contentCopy", InvSlot.Access.NONE, 54);
    }

    @Override
    public ResourceLocation getName() {
        return NAME;
    }

    @Override
    public boolean updateServer() {
        if (this.started && this.parent.useEnergy(32) >= 32) {
            for (int i = 0; i < 25 && this.started; i++) {
                this.euOut = 0;
                this.maxHeat = 10000;
                this.hem = 1;
                this.explosionStrength = 10;
                float multiplier = 1;
                
                for (int y = 0; y < 6; y++) {
                    for (int x = 0; x < 9; x++) {
                        ItemStack stack = this.content.get(x + y * 9);
                        if (!stack.isEmpty()) {
                            if (stack.getItem() instanceof IReactorComponent) {
                                IReactorComponent component = (IReactorComponent) stack.getItem();
                                component.processChamber(stack, this, x, y, true);
                                component.processChamber(stack, this, x, y, false);
                                float influence = ((IReactorComponent) stack.getItem()).influenceExplosion(stack, this);
                                
                                if (influence > 0 && influence < 1) multiplier *= influence;
                                else this.explosionStrength += influence;
                            } else if (GtUtil.containsStack(stack, DEPLETED_ITEMS)) {
                                stopNuclearReactor();
                            } else {
                                this.content.put(x + y * 9, ItemStack.EMPTY);
                            }
                        }
                    }
                }
                this.euOut *= 5;
                if (this.euOut == 0 && this.euTimer++ > 20 || this.heat >= this.maxHeat) stopNuclearReactor();
                if (this.euOut != 0) this.euTimer = 0;
                this.explosionStrength *= this.hem * multiplier;
                this.eu += this.euOut * 20;
                int eu = this.euLast1;
                this.euLast1 = this.euLast2;
                this.euLast2 = this.euLast3;
                this.euLast3 = this.euLast4;
                this.euLast4 = this.euOut;
                this.euOut = (this.euOut + this.euLast1 + this.euLast2 + this.euLast3 + eu) / 5;
            }
            return true;
        }
        return false;
    }

    public void switchNuclearReactor() {
        if (this.started) stopNuclearReactor();
        else startNuclearReactor();
    }

    public void startNuclearReactor() {
        this.started = true;
        this.heat = 0;
        this.eu = 0;
    }

    public void stopNuclearReactor() {
        this.started = false;
    }
    
    public void saveNuclearReactor() {
        for (int i = 0; i < this.content.size(); i++) {
            this.contentCopy.put(i, this.content.get(i).copy());
        }
    }
    
    public void loadNuclearReactor() {
        for (int i = 0; i < this.contentCopy.size(); i++) {
            this.content.put(i, this.contentCopy.get(i).copy());
        }
    }

    @Override
    public TileEntity getCoreTe() {
        return this.parent;
    }

    @Override
    public int getHeat() {
        return this.heat;
    }

    @Override
    public void setHeat(int heat) {
        this.heat = heat;
    }

    @Override
    public int addHeat(int amount) {
        this.heat += amount;
        return this.heat;
    }

    @Override
    public int getMaxHeat() {
        return this.maxHeat;
    }

    @Override
    public void setMaxHeat(int newMaxHeat) {
        this.maxHeat = newMaxHeat;
    }

    @Override
    public void addEmitHeat(int heat) {}

    @Override
    public float getHeatEffectModifier() {
        return this.hem;
    }

    @Override
    public void setHeatEffectModifier(float newHEM) {
        this.hem = newHEM;
    }

    @Override
    public float getReactorEnergyOutput() {
        return this.euOut;
    }

    @Override
    public double getReactorEUEnergyOutput() {
        return this.euOut * ConfigUtil.getFloat(MainConfig.get(), "balance/energy/generator/nuclear");
    }

    @Override
    public float addOutput(float energy) {
        this.euOut += energy;
        return this.euOut;
    }

    @Override
    public ItemStack getItemAt(int x, int y) {
        if (x < 0 || x > 8 || y < 0 || y > 5) return ItemStack.EMPTY; 
        return this.content.get(x + y * 9);
    }

    @Override
    public void setItemAt(int x, int y, ItemStack stack) {
        this.content.put(x + y * 9, stack);
    }

    @Override
    public void explode() {
        stopNuclearReactor();
    }

    @Override
    public int getTickRate() {
        return 1;
    }

    @Override
    public boolean produceEnergy() {
        return true;
    }

    @Override
    public boolean isFluidCooled() {
        return false;
    }

    @Override
    public BlockPos getPosition() {
        return this.parent.getPos();
    }

    @Override
    public World getWorldObj() {
        return this.parent.getWorld();
    }

    @Override
    public ContainerComputerCubeReactor getGuiContainer(EntityPlayer player, TileEntityComputerCube base) {
        return new ContainerComputerCubeReactor(this.parent);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin, TileEntityComputerCube base) {
        return new GuiComputerCubeReactor(getGuiContainer(player, base));
    }

    @Nullable
    @Override
    public ResourceLocation getTexture() {
        return TEXTURE;
    }

    @Override
    public String getDataName() {
        return "Reactorplan-Data";
    }

    @Nullable
    @Override
    public NBTTagCompound saveDataToOrb() {
        if (!this.contentCopy.isEmpty()) {
            NBTTagCompound nbt = new NBTTagCompound();
            this.contentCopy.writeToNbt(nbt);
            return nbt;
        }
        return null;
    }

    @Override
    public void loadDataFromOrb(NBTTagCompound nbt) {
        this.content.readFromNbt(nbt);
    }

    public int getEu() {
        return this.eu;
    }

    public int getEuOut() {
        return this.euOut;
    }

    public float getExplosionStrength() {
        return this.explosionStrength;
    }
}
