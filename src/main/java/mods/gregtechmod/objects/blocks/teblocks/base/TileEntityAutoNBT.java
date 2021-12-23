package mods.gregtechmod.objects.blocks.teblocks.base;

import ic2.core.IC2;
import ic2.core.block.TileEntityInventory;
import ic2.core.gui.dynamic.IGuiValueProvider;
import mods.gregtechmod.util.BooleanCountdown;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.nbt.NBTSaveHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.*;
import java.util.function.DoubleSupplier;

public abstract class TileEntityAutoNBT extends TileEntityInventory implements IGuiValueProvider {
    protected final String descriptionKey;
    private final Map<String, DoubleSupplier> guiValues = new HashMap<>();
    private final Collection<BooleanCountdown> countdowns = new HashSet<>();
    
    protected int tickCounter;
    
    protected TileEntityAutoNBT() {
        String key = getDescriptionKey();
        this.descriptionKey = FMLCommonHandler.instance().getSide() == Side.CLIENT && GtLocale.hasKey(key) ? key : null;
    }
    
    protected String getDescriptionKey() {
        return "teblock." + this.teBlock.getName() + ".description";
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
        
        this.tickCounter++;
        this.countdowns.forEach(BooleanCountdown::countDown);
    }
    
    protected BooleanCountdown createSingleCountDown() {
        return createCountDown(2);
    }
    
    protected BooleanCountdown createCountDown(int count) {
        BooleanCountdown countdown = new BooleanCountdown(count);
        this.countdowns.add(countdown);
        return countdown;
    }

    /**
     * Runs on the SERVER side, updates CLIENT-side fields
     */
    public void updateClientField(String name) {
        if (!this.world.isRemote) {
            IC2.network.get(true).updateTileEntityField(this, name);
        }
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        NBTSaveHandler.readClassFromNBT(this, nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTSaveHandler.writeClassToNBT(this, nbt);
        return super.writeToNBT(nbt);
    }
    
    @Override
    public final List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        getNetworkedFields(ret);
        return ret;
    }
    
    public void getNetworkedFields(List<? super String> list) {}
    
    public void addGuiValue(String name, DoubleSupplier supplier) {
        if (this.guiValues.containsKey(name)) throw new IllegalArgumentException("Duplicate Gui value " + name);
        
        this.guiValues.put(name, supplier);
    }

    @Override
    public final double getGuiValue(String name) {
        DoubleSupplier supplier = this.guiValues.get(name);
        if (supplier != null) return supplier.getAsDouble();
        
        throw new IllegalArgumentException("Cannot get value for " + name);
    }
    
    protected boolean isRedstonePowered() {
        return this.world != null && this.world.getRedstonePowerFromNeighbors(this.pos) > 0;
    }
    
    public EnumFacing getOppositeFacing() {
        return getFacing().getOpposite();
    }
    
    public void updateRender() {
        rerender();
    }
}
