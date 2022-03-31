package mods.gregtechmod.objects.blocks.teblocks;

import ic2.core.block.state.Ic2BlockState;
import ic2.core.block.state.UnlistedIntegerProperty;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.List;

public class TileEntityCompartment extends TileEntityFileCabinet {
    public static final IUnlistedProperty<Integer> FACE_TEXTURE_INDEX_PROPERTY = new UnlistedIntegerProperty("faceIndex");
    
    @NBTPersistent
    private int faceIndex;

    @Override
    protected Ic2BlockState.Ic2BlockStateInstance getExtendedState(Ic2BlockState.Ic2BlockStateInstance state) {
        return super.getExtendedState(state)
            .withProperty(FACE_TEXTURE_INDEX_PROPERTY, this.faceIndex);
    }

    @Override
    protected boolean onScrewdriverActivated(ItemStack stack, EnumFacing side, EntityPlayer player, float hitX, float hitY, float hitZ) {
        if (!this.world.isRemote && side == getFacing()) {
            this.faceIndex = (this.faceIndex + 1) % 16;
            markDirty();
            updateClientField("faceIndex");
            return true;
        }
        return super.onScrewdriverActivated(stack, side, player, hitX, hitY, hitZ);
    }

    @Override
    protected boolean onActivatedChecked(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        return side == getFacing() && player.inventory.getCurrentItem().getItem() == BlockItems.Tool.SCREWDRIVER.getInstance() || super.onActivatedChecked(player, hand, side, hitX, hitY, hitZ);
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("faceIndex");
    }

    @Override
    public void onNetworkUpdate(String field) {
        super.onNetworkUpdate(field);
        if (field.equals("faceIndex")) rerender();
    }
}
