package mods.gregtechmod.objects.blocks.teblocks.computercube;

import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import ic2.core.block.state.Ic2BlockState.Ic2BlockStateInstance;
import ic2.core.util.Util;
import mods.gregtechmod.api.cover.CoverType;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityEnergy;
import mods.gregtechmod.util.PropertyHelper;
import mods.gregtechmod.util.PropertyHelper.TextureOverride;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.Locale;
import java.util.function.Supplier;

public class TileEntityComputerCube extends TileEntityEnergy implements IHasGui {
    @NBTPersistent
    private IComputerCubeModule activeModule = ComputerCubeMain.INSTANCE;

    public TileEntityComputerCube() {
        super(null);
        
        this.coverBlacklist.addAll(CoverType.VALUES);
    }
    
    public enum Module {
        MAIN(ComputerCubeMain::new);
        
        public final ResourceLocation name;
        private final Supplier<IComputerCubeModule> supplier;
        
        Module(Supplier<IComputerCubeModule> supplier) {
            this.name = new ResourceLocation(Reference.MODID, name().toLowerCase(Locale.ROOT));
            this.supplier = supplier;
        }
        
        public static void registerModules() {
            for (Module module : values()) {
                ComputerCubeModules.registerModule(module.name, module.supplier);
            }
        }
    }
    
    public void switchModule() {
        this.activeModule = ComputerCubeModules.getNextModule(this.activeModule.getName());
    }
    
    @Override
    protected Ic2BlockStateInstance getExtendedState(Ic2BlockStateInstance state) {
        Ic2BlockStateInstance ret = super.getExtendedState(state);
        ResourceLocation texture = this.activeModule.getTexture();
        return texture != null ? ret.withProperty(PropertyHelper.TEXTURE_OVERRIDE_PROPERTY, new TextureOverride(this.activeModule.getTexture())) : ret;
    }

    @Override
    protected Collection<EnumFacing> getSinkSides() {
        return Util.allFacings;
    }

    @Override
    public int getSinkTier() {
        return 1;
    }

    @Override
    public int getEUCapacity() {
        return 10000;
    }

    @Override
    public ContainerBase<?> getGuiContainer(EntityPlayer player) {
        return this.activeModule.getGuiContainer(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return this.activeModule.getGui(player, isAdmin, this);
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {}
}
