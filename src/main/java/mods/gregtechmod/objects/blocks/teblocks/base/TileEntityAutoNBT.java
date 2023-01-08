package mods.gregtechmod.objects.blocks.teblocks.base;

import ic2.core.IC2;
import ic2.core.block.TileEntityInventory;
import ic2.core.gui.dynamic.IGuiValueProvider;
import mods.gregtechmod.util.BooleanCountdown;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.nbt.NBTSaveHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.Constants.BlockFlags;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;
import java.util.function.DoubleSupplier;

public abstract class TileEntityAutoNBT extends TileEntityInventory implements IGuiValueProvider {
    protected final String descriptionKey;
    private final Map<String, DoubleSupplier> guiValues = new HashMap<>();
    private final Collection<BooleanCountdown> countdowns = new HashSet<>();

    private final BooleanCountdown inventoryModified = createSingleCountDown();

    protected int tickCounter;

    protected TileEntityAutoNBT() {
        String key = getDescriptionKey();
        this.descriptionKey = FMLCommonHandler.instance().getSide() == Side.CLIENT && I18n.hasKey(key) ? key : null;
    }

    protected String getDescriptionKey() {
        return GtLocale.buildKeyTeBlock(this, "description");
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();

        this.tickCounter++;
        this.countdowns.forEach(BooleanCountdown::countDown);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        if (this.descriptionKey != null) tooltip.add(I18n.format(this.descriptionKey));
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

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        markDirty();
    }

    protected boolean isRedstonePowered() {
        return this.world != null && this.world.getRedstonePowerFromNeighbors(this.pos) > 0;
    }

    public EnumFacing getOppositeFacing() {
        return getFacing().getOpposite();
    }

    public void updateRender() {
        IBlockState state = getBlockState();
        this.world.notifyBlockUpdate(this.pos, state, state, BlockFlags.SEND_TO_CLIENTS);
    }

    public void updateRenderNeighbors() {
        IBlockState state = getBlockState();
        this.world.notifyBlockUpdate(this.pos, state, state, BlockFlags.DEFAULT);
    }
    
    public void updateAndNotifyNeighbors() {
        IBlockState state = getBlockState();
        this.world.notifyBlockUpdate(this.pos, state, state, BlockFlags.DEFAULT);
        this.world.notifyNeighborsOfStateChange(this.pos, this.blockType, true);
    }

    public TileEntity getNeighborTE(EnumFacing side) {
        return this.world.getTileEntity(this.pos.offset(side));
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, EnumFacing side) {
        return isInputSide(side) && super.canInsertItem(index, stack, side);
    }

    public boolean isInputSide(EnumFacing side) {
        return true;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing side) {
        return isOutputSide(side) && super.canExtractItem(index, stack, side);
    }

    public boolean isOutputSide(EnumFacing side) {
        return true;
    }

    public void onInventoryChanged() {
        this.inventoryModified.reset();
    }

    public boolean hasInventoryBeenModified() {
        return this.inventoryModified.get();
    }

    public String getTeBlockName() {
        return this.teBlock.getName();
    }
}
