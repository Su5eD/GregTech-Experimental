package mods.gregtechmod.objects.blocks.teblocks.multiblock;

import ic2.core.IHasGui;
import ic2.core.block.state.Ic2BlockState.Ic2BlockStateInstance;
import mods.gregtechmod.api.item.ISolderingTool;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.gui.GuiHatchMaintenance;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityCoverBehavior;
import mods.gregtechmod.objects.blocks.teblocks.component.Maintenance;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerHatchMaintenance;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.OreDictUnificator;
import mods.gregtechmod.util.PropertyHelper;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class TileEntityHatchMaintenance extends TileEntityCoverBehavior implements IHasGui {
    private static final PropertyHelper.TextureOverride DUCT_TAPE_TEXTURE_OVERRIDE = new PropertyHelper.TextureOverride(EnumFacing.NORTH, new ResourceLocation(Reference.MODID, "blocks/machines/hatch_maintenance/hatch_maintenance_front_ducttape"));

    @NBTPersistent
    public boolean ductTape;
    private final Maintenance maintenance;

    public TileEntityHatchMaintenance() {
        this.maintenance = addComponent(new Maintenance(this));
    }

    @Override
    protected boolean onActivatedChecked(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (side != getFacing()) return true;
        return super.onActivatedChecked(player, hand, side, hitX, hitY, hitZ);
    }

    public void onToolClick(ItemStack stack, EntityPlayer player) {
        checkTool(player, stack, GtUtil::isWrench, this.maintenance::setWrench);
        checkTool(player, stack, GtUtil::isScrewdriver, this.maintenance::setScrewdriver);
        checkTool(player, stack, GtUtil::isSoftHammer, this.maintenance::setSoftHammer);
        checkTool(player, stack, GtUtil::isHardHammer, this.maintenance::setHardHammer);
        checkTool(player, stack, GtUtil::isCrowbar, this.maintenance::setCrowbar);
        checkTool(player, stack, s -> {
            Item item = s.getItem();
            return item instanceof ISolderingTool && ((ISolderingTool) item).solder(stack, player, false);
        }, this.maintenance::setSolderingTool);

        if (!stack.isEmpty() && OreDictUnificator.isItemInstanceOf(stack, "craftingDuctTape", false)) {
            this.ductTape = true;
            this.maintenance.setAll(true);
            stack.shrink(1);
            updateRender();
        }
    }

    private void checkTool(EntityPlayer player, ItemStack stack, Predicate<ItemStack> predicate, Consumer<Boolean> setter) {
        if (predicate.test(stack) && GtUtil.damageStack(player, stack, 1)) setter.accept(true);
    }

    public Maintenance getMaintenance() {
        return this.maintenance;
    }

    @Override
    protected Ic2BlockStateInstance getExtendedState(Ic2BlockStateInstance state) {
        Ic2BlockStateInstance extendedState = super.getExtendedState(state);
        if (this.ductTape) {
            return extendedState.withProperty(PropertyHelper.TEXTURE_OVERRIDE_PROPERTY, DUCT_TAPE_TEXTURE_OVERRIDE);
        }
        return extendedState;
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("ductTape");
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
