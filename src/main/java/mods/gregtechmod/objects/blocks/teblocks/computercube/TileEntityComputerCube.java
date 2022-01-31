package mods.gregtechmod.objects.blocks.teblocks.computercube;

import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import ic2.core.block.state.Ic2BlockState.Ic2BlockStateInstance;
import ic2.core.util.Util;
import mods.gregtechmod.api.cover.CoverType;
import mods.gregtechmod.api.upgrade.GtUpgradeType;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.api.util.IDataOrbSerializable;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityUpgradable;
import mods.gregtechmod.objects.blocks.teblocks.component.GtComponentBase;
import mods.gregtechmod.util.PropertyHelper;
import mods.gregtechmod.util.PropertyHelper.TextureOverride;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.*;

public class TileEntityComputerCube extends TileEntityUpgradable implements IHasGui, IDataOrbSerializable {
    private final ComputerCubeModuleComponent module;
    public final Map<ResourceLocation, IComputerCubeModule> moduleCache = new HashMap<>();

    public TileEntityComputerCube() {
        this.module = addComponent(new ComputerCubeModuleComponent(this));
        this.coverBlacklist.addAll(CoverType.VALUES);
        
        setPrivate(true);
    }
    
    public IComputerCubeModule getActiveModule() {
        return this.module.activeModule;
    }
    
    public void switchModule() {
        this.module.activeModule = getNextModule(this.module.activeModule.getName());
        markDirty();
        updateRender();
    }

    public IComputerCubeModule getNextModule(ResourceLocation current) {
        List<ResourceLocation> list = new ArrayList<>(ComputerCubeModules.MODULES.keySet());
        int size = list.size();

        for (int i = 0; i < size; i++) {
            ResourceLocation loc = list.get(i);
            if (ComputerCubeModules.MODULES.get(loc).getLeft().getAsBoolean() && current.equals(loc)) {
                ResourceLocation next = list.get((i + 1) % size);

                IComputerCubeModule cached = this.moduleCache.get(next);
                if (cached != null) return cached;

                IComputerCubeModule module = ComputerCubeModules.getModule(next, this);
                this.moduleCache.put(next, module);
                return module;
            }
        }

        throw new IllegalArgumentException("Module " + current + " not found");
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
        if (getActiveModule().updateServer()) updateClientField("module");
    }

    @Override
    protected Ic2BlockStateInstance getExtendedState(Ic2BlockStateInstance state) {
        Ic2BlockStateInstance ret = super.getExtendedState(state);
        ResourceLocation texture = getActiveModule().getTexture();
        return texture != null ? ret.withProperty(PropertyHelper.TEXTURE_OVERRIDE_PROPERTY, new TextureOverride(texture)) : ret;
    }

    @Override
    protected Collection<EnumFacing> getSinkSides() {
        return Util.allFacings;
    }
    
    @Override
    public int getBaseSinkTier() {
        return 1;
    }
    
    @Override
    protected int getBaseEUCapacity() {
        return 10000;
    }

    @Override
    public Set<GtUpgradeType> getCompatibleGtUpgrades() {
        return Collections.emptySet();
    }

    @Override
    public Set<IC2UpgradeType> getCompatibleIC2Upgrades() {
        return Collections.emptySet();
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("module");
    }

    @Override
    public String getDataName() {
        IComputerCubeModule module = getActiveModule();
        if (module instanceof IDataOrbSerializable) return ((IDataOrbSerializable) module).getDataName();
        return null;
    }

    @Nullable
    @Override
    public NBTTagCompound saveDataToOrb() {
        IComputerCubeModule module = getActiveModule();
        if (module instanceof IDataOrbSerializable) return ((IDataOrbSerializable) module).saveDataToOrb();
        return null;
    }

    @Override
    public void loadDataFromOrb(NBTTagCompound nbt) {
        IComputerCubeModule module = getActiveModule();
        if (module instanceof IDataOrbSerializable) ((IDataOrbSerializable) module).loadDataFromOrb(nbt);
    }

    @Override
    public ContainerBase<?> getGuiContainer(EntityPlayer player) {
        return getActiveModule().getGuiContainer(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return getActiveModule().getGui(player, isAdmin, this);
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {}
    
    public static class ComputerCubeModuleComponent extends GtComponentBase {
        @NBTPersistent
        private IComputerCubeModule activeModule = ComputerCubeMain.INSTANCE;
        
        public ComputerCubeModuleComponent(TileEntityComputerCube parent) {
            super(parent);
        }

        @Override
        public void onLoaded() {
            ((TileEntityComputerCube) this.parent).moduleCache.put(this.activeModule.getName(), this.activeModule);
        }
    }
}
